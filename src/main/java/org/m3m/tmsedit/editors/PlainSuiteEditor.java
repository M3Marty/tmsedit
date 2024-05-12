package org.m3m.tmsedit.editors;

import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import org.m3m.tmsedit.documentation.Suite;
import org.m3m.tmsedit.navigation.Navigation;

import java.util.function.Function;

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
		return getPane();
	}

	@Override
	public Parent getPane() {
		return idLabel.getParent().getParent();
	}

	@Override
	public Navigation getNavigation() {

		return null;
	}
}