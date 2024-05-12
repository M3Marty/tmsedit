package org.m3m.tmsedit.navigation;

import javafx.scene.input.KeyEvent;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor
public class Navigation implements Function<KeyEvent, Object> {

	private final String description;
	private final Function<KeyEvent, Object> navigationFunction;

	@Override
	public String toString() {
		return description;
	}

	@Override
	public Object apply(KeyEvent keyEvent) {
		return navigationFunction.apply(keyEvent);
	}
}