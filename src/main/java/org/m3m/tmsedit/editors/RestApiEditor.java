package org.m3m.tmsedit.editors;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.m3m.tmsedit.documentation.Suite;
import org.m3m.tmsedit.documentation.TestCase;

@RequiredArgsConstructor
public class RestApiEditor implements Editor {

	@Getter
	private SuiteEditor suiteEditor = new PlainSuiteEditor();
	@Getter
	private TestCaseEditor testCaseEditor = new RestApiTestCaseEditor();

	private final Label modeLabel;
}