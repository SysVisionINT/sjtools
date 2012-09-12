package net.java.sjtools.search;

public class Node {
	private Comparable value = null;
	private Node left = null;
	private Node right = null;
	
	public Node(Comparable value) {
		this.value = value;
	}

	public Node getLeft() {
		return left;
	}
	
	public void setLeft(Node left) {
		this.left = left;
	}
	
	public Node getRight() {
		return right;
	}
	
	public void setRight(Node right) {
		this.right = right;
	}
	
	public Comparable getValue() {
		return value;
	}
}
