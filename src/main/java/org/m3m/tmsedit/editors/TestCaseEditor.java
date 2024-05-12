package org.m3m.tmsedit.editors;

import javafx.scene.Parent;
import javafx.scene.input.KeyEvent;
import org.m3m.tmsedit.documentation.TestCase;
import org.m3m.tmsedit.navigation.Navigation;

import java.util.function.Function;

public interface TestCaseEditor {

	Parent with(TestCase testCase);

	Parent getPane();

	Navigation getNavigation();
}