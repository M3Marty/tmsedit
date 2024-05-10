package org.m3m.tmsedit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.m3m.tmsedit.editors.*;
import org.m3m.tmsedit.history.*;
import org.m3m.tmsedit.logging.TextAreaLogger;
import org.m3m.tmsedit.documentation.Suite;
import org.m3m.tmsedit.parser.Parser;
import org.m3m.tmsedit.parsers.XmlQaseParser;
import org.m3m.tmsedit.source.DataSource;
import org.m3m.tmsedit.source.FileSource;

import java.io.*;
import java.util.Optional;

import static java.lang.System.Logger.Level.*;

public final class TMSEditController {

	public static TextAreaLogger logger;

	private HistoryStack history;

	private DataSource dataSource;
	private Suite suite;

	private Parser parser;

	private Object objectBuffer;
	private String stringBuffer;

	private EditorMode editorMode;

	@FXML
	public TextArea logField;
	@FXML
	public Label status, source, mode, historyId, currentFocus, driver;
	@FXML
	public Parent content, projectViewPane;
	public SplitPane sideSplit, mainSplit;
	@FXML
	private TreeView<Suite> projectView;

	@FXML
	public void initialize() {
		logger = new TextAreaLogger(logField);
		toggleLogView(null);

		history = new HistoryStack(historyId);

		setDriverXmlQase();
		setModePlain();
	}

	@Undo(Undo.Type.SKIPPED)
	private void setEditorMode(EditorMode mode) {
		history.happen(new SkippedHistoryEvent<EditorMode>(
				this.editorMode, mode, "Choose mode " + mode.getClass().getSimpleName(),
				() -> {
					this.editorMode = mode;
					this.mode.setText(this.editorMode.getClass().getSimpleName());
				},
				previousMode -> {
					this.editorMode = previousMode;
					this.mode.setText(Optional.ofNullable(editorMode)
							.map(m -> m.getClass().getSimpleName()).orElse("None"));
				},
				editorMode -> {
					logger.log(INFO, "Choose mode %s", Optional.ofNullable(editorMode)
							.map(m -> m.getClass().getSimpleName()).orElse("None"));
				}
		));
	}

	@Undo(Undo.Type.SKIPPED)
	private void setDriver(Parser parser) {
		history.happen(new SkippedHistoryEvent<Parser>(
				this.parser, parser, "Choose driver " + parser.getClass().getSimpleName(),
				() -> {
					this.parser = parser;
					driver.setText(parser.getClass().getSimpleName());
				},
				previousParser -> {
					this.parser = previousParser;
					this.driver.setText(Optional.ofNullable(this.parser)
							.map(v -> v.getClass().getSimpleName()).orElse("None"));
				},
				value -> {
					logger.log(INFO, "Choose driver %s", Optional.ofNullable(value)
							.map(v -> v.getClass().getSimpleName()).orElse("None"));
				}
		));
	}

	private Window getWindow() {
		return status.getScene().getWindow();
	}

	private void addToSuiteNodeChildren(TreeItem<Suite> node) {
		for (var suite : node.getValue().getSuites()) {
			var subNode = new TreeItem<>(suite);
			addToSuiteNodeChildren(subNode);
			node.getChildren().add(subNode);
		}

		for (var test : node.getValue().getCases()) {
			var subNode = new TreeItem<Suite>(test);
			node.getChildren().add(subNode);
		}

		node.setExpanded(true);
	}

	private void setWorkingSuite(Suite suite) {
		if (suite == null) {
			this.projectView.setRoot(null);
			return;
		}
		logger.log(DEBUG, suite.toStringVerbose());

		this.suite = suite;
		var root = new TreeItem<>(suite);
		addToSuiteNodeChildren(root);
		root.setExpanded(true);
		this.projectView.setRoot(root);
		this.status.setText("Sync");
	}

	private void setDataSource(DataSource dataSource) {
		logger.log(INFO, "Set source: %s", dataSource);
		this.dataSource = dataSource;
		this.source.setText(Optional.ofNullable(dataSource).map(DataSource::toString).orElse("None"));
	}

	@Undo
	private void openSuite(DataSource source, Parser parser) throws IOException {
		Suite suite = parser.read(source.get());

		history.happen(new LoggedHistoryEvent<Object[]>(
				new Object[]{ this.dataSource, this.suite },
				new Object[]{ source, suite },
				"Change suite to " + suite.getTitle(),
				() -> {
					setDataSource(source);
					setWorkingSuite(suite);
				},
				data -> {
					setDataSource((DataSource) data[0]);
					setWorkingSuite((Suite) data[1]);
				},
				data -> {
					logger.log(INFO, "Open suite using %s from %s",
							parser.getClass().getSimpleName(), data[0]);
				}
		));
	}

	@FXML
	private void openFileDialog(ActionEvent actionEvent) {
		var chooser = new FileChooser();
		chooser.setTitle("Open file...");

		try {
			File file = chooser.showOpenDialog(getWindow());
			if (file == null)
				return;
			openSuite(new FileSource(file), this.parser);
		} catch (Exception e) {
			logger.log(ERROR, e);
			new Alert(Alert.AlertType.ERROR, "File can't be parsed with "
					+ XmlQaseParser.class.getSimpleName()).show();

			if (e instanceof RuntimeException re)
				throw re;
			throw new RuntimeException(e);
		}
	}

	@FXML
	private void saveFile(ActionEvent actionEvent) {}

	@FXML
	private void saveFileDialog(ActionEvent actionEvent) {}

	@FXML
	private void undo(ActionEvent actionEvent) {
		history.undo();
	}

	@FXML
	private void redo(ActionEvent actionEvent) {
		history.redo();
	}

	@FXML
	private void copy(ActionEvent actionEvent) {}

	@FXML
	private void cut(ActionEvent actionEvent) {}

	@FXML
	private void paste(ActionEvent actionEvent) {}

	@FXML
	private void cloneChosen(ActionEvent actionEvent) {}

	@FXML
	private void showHistory(ActionEvent actionEvent) {}

	private double sideDividerPosition;

	@FXML
	private void toggleProjectTreeView(ActionEvent actionEvent) {
		projectViewPane.setVisible(!projectViewPane.isVisible());
		logger.log(INFO, "Project view toggle: %b",
				projectViewPane.isVisible());
		if (projectViewPane.isVisible()) {
			sideSplit.getItems().add(0, projectViewPane);
			sideSplit.setDividerPosition(0, sideDividerPosition);
		}
		else {
			sideDividerPosition = sideSplit.getDividerPositions()[0];
			sideSplit.getItems().remove(0);
		}
	}

	private double logDivider;

	@FXML
	private void toggleLogView(ActionEvent actionEvent) {
		logField.setVisible(!logField.isVisible());
		logger.setFlushing(logField.isVisible());
		logger.log(INFO, "Log view toggle: %b", logField.isVisible());
		if (logField.isVisible()) {
			mainSplit.getItems().add(logField);
			mainSplit.setDividerPosition(
					mainSplit.getDividerPositions().length - 1,
					logDivider
			);
		}
		else {
			logDivider = mainSplit.getDividerPositions()[mainSplit.getDividerPositions().length - 1];
			mainSplit.getItems().remove(logField);
		}
	}

	@FXML
	private void newFileDialog(ActionEvent actionEvent) {}

	@FXML
	private void newTestCase(ActionEvent actionEvent) {}

	@FXML
	private void newSuite(ActionEvent actionEvent) {}

	@FXML
	private void setDriverXmlQase() {
		setDriver(new XmlQaseParser());
	}

	@FXML
	private void setModePlain() {
		setEditorMode(new PlainEditor());
	}

	@FXML
	private void setModeRestApi(ActionEvent actionEvent) {
		setEditorMode(new RestApiMode());
	}
}