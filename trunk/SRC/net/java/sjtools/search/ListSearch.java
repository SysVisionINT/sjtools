package net.java.sjtools.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ListSearch {

	private Node root = null;

	public ListSearch() {}

	public ListSearch(Collection list) {
		List sorted = new ArrayList(list);
		Collections.sort(sorted);
		addList(sorted);
	}

	private void addList(List list) {
		if (list.isEmpty()) {
			return;
		}

		int index = 0;

		if (list.size() > 2) {
			index = list.size() / 2;
		}

		add((Comparable) list.get(index));
		
		if (index - 1 >= 0) {
			addList(getSubList(list, 0, index - 1));
		}
		
		if (index + 1 <= list.size() - 1) {
			addList(getSubList(list, index + 1, list.size() - 1));
		}
	}
	
	private List getSubList(List list, int start, int end) {
		List ret = new ArrayList(end - start + 1);
		
		for (int i = 0; i <= (end - start); i++) {
			ret.add(list.get(start + i));
		}
		
		return ret;
	}

	public void add(Comparable value) {
		if (root == null) {
			root = new Node(value);
		} else {
			insert(root, value);
		}
	}

	private void insert(Node node, Comparable value) {
		int comp = node.getValue().compareTo(value);

		if (comp == 0) {
			return;
		} else if (comp > 0) {
			if (node.getLeft() == null) {
				node.setLeft(new Node(value));
			} else {
				insert(node.getLeft(), value);
			}
		} else {
			if (node.getRight() == null) {
				node.setRight(new Node(value));
			} else {
				insert(node.getRight(), value);
			}
		}
	}

	public boolean contains(Comparable value) {
		return find(value) != null;
	}

	public Comparable find(Comparable value) {
		if (root == null) {
			return null;
		}

		Node current = root;

		while (current != null) {
			int comp = current.getValue().compareTo(value);

			if (comp == 0) {
				return current.getValue();
			} else if (comp > 0) {
				current = current.getLeft();
			} else {
				current = current.getRight();
			}
		}

		return null;
	}
}
