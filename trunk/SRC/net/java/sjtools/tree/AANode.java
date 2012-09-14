package net.java.sjtools.tree;

public class AANode {
	protected Comparable value = null;
	protected AANode left = null;
	protected AANode right = null;
	protected int level = 1;
	
	protected AANode(Comparable value) {
		this.value = value;
	}
}
