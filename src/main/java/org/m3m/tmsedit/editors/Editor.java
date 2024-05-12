package org.m3m.tmsedit.editors;

import javafx.scene.Parent;
import javafx.scene.input.KeyEvent;
import org.m3m.tmsedit.documentation.Suite;
import org.m3m.tmsedit.documentation.TestCase;
import org.m3m.tmsedit.navigation.Navigation;

import java.util.function.Function;

public interface Editor {

	SuiteEditor getSuiteEditor();
	TestCaseEditor getTestCaseEditor();

	default Parent with(Suite suite) {
		return getSuiteEditor().with(suite);
	}

	default Parent with(TestCase testCase) {
		return getTestCaseEditor().with(testCase);
	}

	default Navigation getNavigationOf(Parent contentPane) {
		if (contentPane == getSuiteEditor().getPane())
			return getSuiteEditor().getNavigation();

		if (contentPane == getTestCaseEditor().getPane())
			return getTestCaseEditor().getNavigation();

		throw new IllegalStateException("Wrong editor");
	}
}