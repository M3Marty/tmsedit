package org.m3m.tmsedit.history;

import lombok.*;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.Consumer;

@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(cacheStrategy = EqualsAndHashCode.CacheStrategy.LAZY)
public class HistoryEvent<T> implements Iterable<HistoryEvent<?>> {

	@Getter(AccessLevel.PACKAGE)
	@Setter(AccessLevel.PACKAGE)
	@EqualsAndHashCode.Exclude
	private HistoryEvent<?> parent;
	private LinkedList<HistoryEvent<?>> following = new LinkedList<>();

	protected T data;
	@Getter
	@ToString.Include
	private String description;
	private Runnable redo;
	private Consumer<T> undo;

	public HistoryEvent(T data, String description, Runnable redo, Consumer<T> undo) {
		this.data = data;
		this.description = description;
		this.redo = redo;
		this.undo = undo;
	}

	public void redo() {
		redo.run();
	}

	public void undo() {
		undo.accept(data);
	}

	public void addFollowing(HistoryEvent<?> event) {
		this.following.addFirst(event);
		event.setParent(this);
	}

	public HistoryEvent<?> primaryFollowing() {
		return following.getFirst();
	}

	@Override
	public Iterator<HistoryEvent<?>> iterator() {
		return following.iterator();
	}
}