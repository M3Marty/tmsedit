package org.m3m.tmsedit.history;

import lombok.EqualsAndHashCode;

import java.util.function.Consumer;

@EqualsAndHashCode(cacheStrategy = EqualsAndHashCode.CacheStrategy.LAZY, callSuper = true)
public class LoggedHistoryEvent<T> extends HistoryEvent<T> {

	private final Consumer<T> log;
	private final T to;

	public LoggedHistoryEvent(T from, T to, String description, Runnable redo,
			Consumer<T> undo, Consumer<T> log) {
		super(from, description, redo, undo);
		this.log = log;
		this.to = to;
	}

	@Override
	public void redo() {
		super.redo();
		log.accept(to);
	}

	@Override
	public void undo() {
		super.undo();
		log.accept(data);
	}
}