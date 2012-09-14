package net.java.sjtools.tree;

public interface Tree {
	public void add(Comparable value);
	public boolean contains(Comparable value);
	public Comparable find(Comparable value);
	public boolean isEmpty();
	public void clean();
}
