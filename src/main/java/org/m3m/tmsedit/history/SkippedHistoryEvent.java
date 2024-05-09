package org.m3m.tmsedit.history;

import java.util.function.Consumer;

public class SkippedHistoryEvent<T> extends LoggedHistoryEvent<T> {

	public SkippedHistoryEvent(
			T from, T to, String description, Runnable redo,
			Consumer<T> undo, Consumer<T> log
	) {
		super(from, to, description, redo, undo, log);
	}

	@Override
	public void addFollowing(HistoryEvent<?> event) {
		super.addFollowing(event);
		event.setParent(getParent());
	}
}
