package org.m3m.tmsedit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Window;
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

	private Parser parser;

	private Object objectBuffer;
	private String stringBuffer;

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
	}

	private void setDriver(Parser parser) {
		history.happen(new LoggedHistoryEvent<Parser>(
				this.parser, "Choose driver " + parser.getClass().getSimpleName(),
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

	private void setWorkingSuite(Suite suite) {
		logger.log(DEBUG, suite);
	}

	private void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		logger.log(INFO, "Set source: %s", dataSource);
	}

	private void openSuite(Parser parser) throws IOException {
		logger.log(INFO, "Open suite using %s", parser.getClass().getSimpleName());
		Suite suite = parser.read(dataSource.get());
		setWorkingSuite(suite);
	}

	@FXML
	private void openFileDialog(ActionEvent actionEvent) {
		var chooser = new FileChooser();
		chooser.setTitle("Open file...");

		try {
			File file = chooser.showOpenDialog(getWindow());
			if (file == null)
				return;
			setDataSource(new FileSource(file));
			openSuite(parser);
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
	public void showHistory(ActionEvent actionEvent) {}

	private double sideDividerPosition;

	@FXML
	public void toggleProjectTreeView(ActionEvent actionEvent) {
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
	public void toggleLogView(ActionEvent actionEvent) {
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
	public void newFileDialog(ActionEvent actionEvent) {}

	@FXML
	public void newTestCase(ActionEvent actionEvent) {}

	@FXML
	public void newSuite(ActionEvent actionEvent) {}

	@FXML
	public void setModeApiTestCases(ActionEvent actionEvent) {}

	@FXML
	public void setDriverXmlQase() {
		setDriver(new XmlQaseParser());
	}
}