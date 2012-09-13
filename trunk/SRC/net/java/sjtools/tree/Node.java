package net.java.sjtools.tree;

public class Node {
	protected Comparable value = null;
	protected Node left = null;
	protected Node right = null;
	protected int level = 1;
	
	protected Node(Comparable value) {
		this.value = value;
	}
}
