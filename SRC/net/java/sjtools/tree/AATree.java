package net.java.sjtools.tree;

import java.util.Collection;
import java.util.Iterator;

public class AATree implements Tree {

	private AANode root = null;

	public AATree() {}

	public AATree(Collection list) {
		for (Iterator i = list.iterator(); i.hasNext();) {
			add((Comparable) i.next());
		}
	}

	public void add(Comparable value) {
		root = insert(value, root);
	}

	private AANode insert(Comparable value, AANode node) {
		if (node == null) {
			node = new AANode(value);
		} else {
			int cmp = value.compareTo(node.value);

			if (cmp < 0) {
				node.left = insert(value, node.left);
			} else if (cmp > 0) {
				node.right = insert(value, node.right);
			}
		}

		node = skew(node);
		node = split(node);

		return node;
	}

	private AANode skew(AANode node) {
		if (node.left != null && node.left.level == node.level) {
			AANode tmp = node.left;
			node.left = tmp.right;
			tmp.right = node;
			node = tmp;
		}

		return node;
	}

	private AANode split(AANode node) {
		if (node.right != null && node.right.right != null && node.right.right.level == node.level) {
			AANode tmp = node.right;
			node.right = tmp.left;
			tmp.left = node;

			node = tmp;
			node.level++;
		}

		return node;
	}

	public boolean contains(Comparable value) {
		return find(value) != null;
	}

	public Comparable find(Comparable value) {
		if (root == null) {
			return null;
		}

		AANode current = root;

		while (current != null) {
			int cmp = value.compareTo(current.value);

			if (cmp < 0) {
				current = current.left;
			} else if (cmp > 0) {
				current = current.right;
			} else {
				return current.value;
			}
		}

		return null;
	}

	public boolean isEmpty() {
		return root == null;
	}

	public void clean() {
		root = null;
	}
}
