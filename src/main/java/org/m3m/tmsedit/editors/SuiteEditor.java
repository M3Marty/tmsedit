package org.m3m.tmsedit.editors;

import javafx.scene.Parent;
import javafx.scene.input.KeyEvent;
import org.m3m.tmsedit.documentation.Suite;
import org.m3m.tmsedit.navigation.Navigation;

import java.util.function.Function;

public interface SuiteEditor {

	Parent with(Suite suite);

	Parent getPane();

	Navigation getNavigation();
}