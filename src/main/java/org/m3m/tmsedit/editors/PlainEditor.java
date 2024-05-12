package org.m3m.tmsedit.editors;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.m3m.tmsedit.documentation.Suite;
import org.m3m.tmsedit.documentation.TestCase;

import java.io.IOException;

@RequiredArgsConstructor
public class PlainEditor implements Editor {

	@Getter
	private SuiteEditor suiteEditor;
	@Getter
	private TestCaseEditor testCaseEditor;

	private final Label modeLabel;

	{
		var suiteLoader = new FXMLLoader(PlainEditor.class.getResource("plain/suite.fxml"));
		var testCaseLoader = new FXMLLoader(PlainEditor.class.getResource("plain/testcase.fxml"));

		try {
			suiteLoader.load();
			suiteEditor = suiteLoader.getController();

			testCaseLoader.load();
			testCaseEditor = testCaseLoader.getController();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
}