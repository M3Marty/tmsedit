package org.m3m.tmsedit.parser;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Iterator;
import java.util.Optional;

public class NodeListIterator implements Iterator<Node>, Iterable<Node> {

	private final NodeList list;
	private final int limit;
	private int pointer = 0;

	public NodeListIterator(Optional<NodeList> list) {
		if (list.isPresent()) {
			this.list = list.get();
			this.limit = this.list.getLength();
		}
		else {
			this.list = null;
			this.limit = 0;
		}
	}

	public NodeListIterator(NodeList list) {
		this.list = list;
		this.limit = this.list.getLength();
	}

	@Override
	public boolean hasNext() {
		return pointer < limit;
	}

	@Override
	public Node next() {
		return list.item(pointer++);
	}

	@Override
	public Iterator<Node> iterator() {
		return this;
	}
}