package org.m3m.tmsedit.editors;

import javafx.scene.Parent;
import org.m3m.tmsedit.documentation.Suite;
import org.m3m.tmsedit.documentation.TestCase;

public interface Editor extends TestCaseEditor, SuiteEditor {

	SuiteEditor getSuiteEditor();
	TestCaseEditor getTestCaseEditor();

	@Override
	default Parent with(Suite suite) {
		return getSuiteEditor().with(suite);
	}

	@Override
	default Parent with(TestCase testCase) {
		return getTestCaseEditor().with(testCase);
	}
}