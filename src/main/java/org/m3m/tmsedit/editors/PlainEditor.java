package org.m3m.tmsedit.editors;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.Getter;

import java.io.IOException;

public class PlainEditor implements EditorMode {

	@Getter
	private SuiteEditor suiteEditor;
	@Getter
	private TestCaseEditor testCaseEditor;

	{
		var suiteLoader = new FXMLLoader(PlainEditor.class.getResource("plain/suite.fxml"));
		var testCaseLoader = new FXMLLoader(PlainEditor.class.getResource("plain/testcase.fxml"));

		try {
			Parent suitePane = suiteLoader.load();
			suiteEditor = suiteLoader.getController();
			suiteEditor.setPane(suitePane);

			Parent testCasePane = testCaseLoader.load();
			testCaseEditor = testCaseLoader.getController();
			testCaseEditor.setPane(testCasePane);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
}