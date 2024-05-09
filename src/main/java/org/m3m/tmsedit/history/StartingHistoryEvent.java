package org.m3m.tmsedit.history;

public class StartingHistoryEvent extends HistoryEvent<Object> {

	public StartingHistoryEvent() {
		super(null, "Starting event", null, null);
	}

	@Override
	public void undo() { }

	@Override
	public void redo() { }

	@Override
	HistoryEvent<?> getParent() {
		return this;
	}
}