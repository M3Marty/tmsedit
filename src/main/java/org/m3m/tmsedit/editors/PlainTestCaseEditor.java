package org.m3m.tmsedit.editors;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import org.m3m.tmsedit.documentation.TestCase;

import java.util.LinkedList;
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

		return idLabel.getParent().getParent().getParent();
	}

	public class StepEditor implements Supplier<Parent> {

		@Override
		public Parent get() {
			return null;
		}
	}
}
