package org.m3m.tmsedit;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.m3m.tmsedit.documentation.TestCase;
import org.m3m.tmsedit.editors.*;
import org.m3m.tmsedit.history.*;
import org.m3m.tmsedit.logging.TextAreaLogger;
import org.m3m.tmsedit.documentation.Suite;
import org.m3m.tmsedit.navigation.Navigation;
import org.m3m.tmsedit.parser.Parser;
import org.m3m.tmsedit.parsers.XmlQaseParser;
import org.m3m.tmsedit.source.DataSource;
import org.m3m.tmsedit.source.FileSource;

import java.io.*;
import java.util.Optional;
import java.util.function.Function;

import static java.lang.System.Logger.Level.*;

public final class TMSEditController {

	public static TextAreaLogger logger;

	private HistoryStack history;

	private DataSource dataSource;
	private Suite suite;

	private Parser parser;

	private Object objectBuffer;
	private String stringBuffer;

	private Editor editorMode;

	@FXML
	public TextArea logField;
	@FXML
	public Label statusLabel, sourceLabel, editorLabel, historyIdLabel, currentFocusLabel, driverLabel, modeLabel;
	@FXML
	public Parent projectViewPane;
	private Parent contentPane;
	public SplitPane sideSplit, mainSplit;
	@FXML
	private TreeView<Suite> projectView;

	private Function<KeyEvent, Object> navigaiton;

	private void setContent(Parent with) {
		if (contentPane == with)
			return;

		mainSplit.getItems().remove(0);
		mainSplit.getItems().add(0, with);

		this.contentPane = with;
	}

	@FXML
	@SuppressWarnings("unchecked")
	public void initialize() {
		logger = new TextAreaLogger(logField);
		toggleLogView(null);

		history = new HistoryStack(historyIdLabel);

		setDriverXmlQase();
		setEditorPlain();

		Platform.runLater(() -> {
			projectView.getScene()
					.focusOwnerProperty()
					.addListener((observableValue, previousFocus, newFocus) -> {
						currentFocusLabel.setText(newFocus.getId());
						if (newFocus.getId().equals("projectView"))
							modeLabel.setText("View");
						else
							modeLabel.setText("Edit");
					});
			projectView.getScene()
					.setOnKeyReleased(e -> {
						if (e.getCode() == KeyCode.ESCAPE) {
							currentFocusLabel.requestFocus();
							currentFocusLabel.setText("No Focus");
							navigaiton = editorMode.getNavigationOf(contentPane);
							modeLabel.setText(navigaiton.toString());
							e.consume();
							return;
						}

						if (navigaiton == null) {
							return;
						}

						Object result = navigaiton.apply(e);
						if (result == null) {
							navigaiton = null;
							projectView.requestFocus();
							e.consume();
							throw new IllegalStateException("No such navigation " + e);
						}

						if (result instanceof Navigation f) {
							navigaiton = f;
							modeLabel.setText(navigaiton.toString());
							e.consume();
							return;
						}

						if (result instanceof Node n) {
							n.requestFocus();
							e.consume();
							return;
						}
					});
		});


		projectView.getSelectionModel()
				.selectedItemProperty()
				.addListener((observable, oldValue, value) -> {
					if (value.getValue() instanceof TestCase testCase) {
						setContent(editorMode.with(testCase));
					}
					else {
						setContent(editorMode.with(value.getValue()));
					}
				});
	}

	@Undo(Undo.Type.SKIPPED)
	private void setEditorMode(Editor mode) {
		history.happen(new SkippedHistoryEvent<Editor>(
				this.editorMode, mode, "Choose mode " + mode.getClass().getSimpleName(),
				() -> {
					this.editorMode = mode;
					this.editorLabel.setText(this.editorMode.getClass().getSimpleName());
				},
				previousMode -> {
					this.editorMode = previousMode;
					this.editorLabel.setText(Optional.ofNullable(editorMode)
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
					driverLabel.setText(parser.getClass().getSimpleName());
				},
				previousParser -> {
					this.parser = previousParser;
					this.driverLabel.setText(Optional.ofNullable(this.parser)
							.map(v -> v.getClass().getSimpleName()).orElse("None"));
				},
				value -> {
					logger.log(INFO, "Choose driver %s", Optional.ofNullable(value)
							.map(v -> v.getClass().getSimpleName()).orElse("None"));
				}
		));
	}

	private Window getWindow() {
		return statusLabel.getScene().getWindow();
	}

	@UndoCalled("openSuite")
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

	@UndoCalled("openSuite")
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
		this.statusLabel.setText("Sync");
	}

	@UndoCalled("openSuite")
	private void setDataSource(DataSource dataSource) {
		logger.log(INFO, "Set source: %s", dataSource);
		this.dataSource = dataSource;
		this.sourceLabel.setText(Optional.ofNullable(dataSource).map(DataSource::toString).orElse("None"));
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

	private void leftOpened() {
		// TODO
	}


	//////////
	// File //
	//////////

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
	private void saveToSource(ActionEvent actionEvent) {}

	@FXML
	private void saveDialog(ActionEvent actionEvent) {}

	//////////
	// Edit //
	//////////

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


	////////////////
	// Navigation //
	////////////////

	@FXML
	private void focusOnProjectView(ActionEvent actionEvent) {
		projectView.requestFocus();
	}

	///////////
	// Tools //
	///////////

	@FXML
	private void cloneChosen(ActionEvent actionEvent) {}

	@FXML
	private void showHistory(ActionEvent actionEvent) {}

	//////////
	// View //
	//////////

	private double sideDividerPosition;

	@FXML
	private void toggleProjectTreeView(ActionEvent actionEvent) {
		projectViewPane.setVisible(!projectViewPane.isVisible());
		logger.log(INFO, "Project view toggle: %b",
				projectViewPane.isVisible());
		if (projectViewPane.isVisible()) {
			sideSplit.getItems().add(0, projectViewPane);
			sideSplit.setDividerPosition(0, sideDividerPosition);
			projectView.requestFocus();
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
			logField.requestFocus();
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
	private void setEditorPlain() {
		setEditorMode(new PlainEditor(modeLabel));
	}

	@FXML
	private void setEditorRestApi(ActionEvent actionEvent) {
		setEditorMode(new RestApiEditor(modeLabel));
	}
}