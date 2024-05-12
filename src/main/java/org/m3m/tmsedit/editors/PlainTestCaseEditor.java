package org.m3m.tmsedit.editors;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import org.m3m.tmsedit.documentation.TestCase;
import org.m3m.tmsedit.navigation.Navigation;

import java.util.LinkedList;
import java.util.function.Function;
import java.util.function.Supplier;

public class PlainTestCaseEditor implements TestCaseEditor {

	@FXML
	public Label idLabel;
	@FXML
	public TextField titleField;
	@FXML
	public CheckBox isFlakyBox, isAutomatedBox, isMutedBox;
	@FXML
	public TextArea descriptionArea, preconditionArea, postconditionArea;
	@FXML
	public ChoiceBox<TestCase.Priority> priorityChoice;
	@FXML
	public ChoiceBox<TestCase.Severity> severityChoice;
	@FXML
	public ChoiceBox<TestCase.Layer> layerChoice;
	@FXML
	public ChoiceBox<TestCase.Type> typeChoice;
	@FXML
	public ChoiceBox<TestCase.Behavior> behaviorChoice;
	@FXML
	public ChoiceBox<TestCase.Status> statusChoice;

	@FXML
	public void initialize() {
		priorityChoice.getItems().addAll(TestCase.Priority.values());
		severityChoice.getItems().addAll(TestCase.Severity.values());
		layerChoice.getItems().addAll(TestCase.Layer.values());
		typeChoice.getItems().addAll(TestCase.Type.values());
		behaviorChoice.getItems().addAll(TestCase.Behavior.values());
		statusChoice.getItems().addAll(TestCase.Status.values());
	}

	private LinkedList<StepEditor> steps = new LinkedList<>();

	public Supplier<Parent> getStepEditor(int position) {
		return steps.get(position);
	}

	@Override
	public Parent with(TestCase testCase) {
		idLabel.setText(testCase.getId().toString());
		titleField.setText(testCase.getTitle());
		descriptionArea.setText(testCase.getDescription());
		preconditionArea.setText(testCase.getPreCondition());
		postconditionArea.setText(testCase.getPostCondition());

		isFlakyBox.setSelected(testCase.isFlaky());
		isAutomatedBox.setSelected(testCase.isAutomated());
		isMutedBox.setSelected(testCase.isMuted());

		priorityChoice.setValue(testCase.getPriority());
		severityChoice.setValue(testCase.getSeverity());
		layerChoice.setValue(testCase.getLayer());
		typeChoice.setValue(testCase.getType());
		behaviorChoice.setValue(testCase.getBehavior());
		statusChoice.setValue(testCase.getStatus());

		return getPane();
	}

	@Override
	public Parent getPane() {
		return idLabel.getParent().getParent().getParent();
	}

	@Override
	public Navigation getNavigation() {
		return new Navigation("Main[HSFD]", e -> switch (e.getCode()) {
				case H -> new Navigation("Header[TDPA]",
						s -> switch (s.getCode()) {
					case T -> titleField;
					case D -> descriptionArea;
					case P -> preconditionArea;
					case A -> postconditionArea;
					default -> null;
				});
				case S -> new Navigation("Steps[1-9]",
						s -> getStepEditor(s.getCharacter().charAt(0) - '1'));
				case F -> new Navigation("Flags[FAM]",
						s -> switch (s.getCode()) {
					case F -> isFlakyBox;
					case A -> isAutomatedBox;
					case M -> isMutedBox;
					default -> null;
				});
				case O -> new Navigation("Options[PSLTBA]",
						s -> switch (s.getCode()) {
					case P -> priorityChoice;
					case S -> severityChoice;
					case L -> layerChoice;
					case T -> typeChoice;
					case B -> behaviorChoice;
					case A -> statusChoice;
					default -> null;
				});
				default -> null;
		});
	}

	public class StepEditor implements Supplier<Parent> {

		@Override
		public Parent get() {
			return null;
		}
	}
}
