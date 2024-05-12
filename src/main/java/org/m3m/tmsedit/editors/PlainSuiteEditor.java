package org.m3m.tmsedit.editors;

import javafx.scene.Parent;
import javafx.scene.control.*;
import org.m3m.tmsedit.documentation.Suite;

public class PlainSuiteEditor implements SuiteEditor {

	public Label idLabel;
	public TextField titleField;
	public TextArea descriptionArea;
	public TextArea preconditionArea;

	@Override
	public Parent with(Suite suite) {
		idLabel.setText(suite.getId().toString());
		titleField.setText(suite.getTitle());
		descriptionArea.setText(suite.getDescription());
		preconditionArea.setText(suite.getPreCondition());
		return idLabel.getParent().getParent();
	}
}