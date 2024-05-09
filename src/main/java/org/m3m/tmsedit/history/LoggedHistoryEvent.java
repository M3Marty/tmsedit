package org.m3m.tmsedit.history;

import lombok.EqualsAndHashCode;

import java.util.function.Consumer;

@EqualsAndHashCode(cacheStrategy = EqualsAndHashCode.CacheStrategy.LAZY, callSuper = true)
public class LoggedHistoryEvent<T> extends HistoryEvent<T> {

	private final Consumer<T> log;

	public LoggedHistoryEvent(T data, String description, Runnable redo,
			Consumer<T> undo, Consumer<T> log) {
		super(data, description, redo, undo);
		this.log = log;
	}

	@Override
	public void redo() {
		super.redo();
		log.accept(data);
	}

	@Override
	public void undo() {
		super.undo();
		log.accept(data);
	}
}