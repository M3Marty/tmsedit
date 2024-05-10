package org.m3m.tmsedit.editors;

import lombok.Getter;

public class RestApiMode implements SuiteEditor, TestCaseEditor, EditorMode {

	@Getter
	private SuiteEditor suiteEditor = new PlainSuiteEditor();
	@Getter
	private TestCaseEditor testCaseEditor = new RestApiTestCaseEditor();

}
