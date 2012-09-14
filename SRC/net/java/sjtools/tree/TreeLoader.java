package net.java.sjtools.tree;

import java.util.Arrays;
import java.util.Collection;

public class TreeLoader {

	public static void load(Tree tree, Collection list) {
		Comparable[] items = (Comparable[]) list.toArray(new Comparable[list.size()]);
		Arrays.sort(items);
		loadItems(tree, items);
	}

	private static void loadItems(Tree tree, Comparable[] items) {
		if (items == null || items.length == 0) {
			return;
		}

		int index = 0;

		if (items.length > 2) {
			index = items.length / 2;
		}

		tree.add(items[index]);

		if (index - 1 >= 0) {
			loadItems(tree, subArray(items, 0, index - 1));
		}

		if (index + 1 <= items.length - 1) {
			loadItems(tree, subArray(items, index + 1, items.length - 1));
		}
	}

	private static Comparable[] subArray(Comparable[] items, int start, int end) {
		Comparable[] ret = new Comparable[end - start + 1];

		System.arraycopy(items, start, ret, 0, end - start + 1);

		return ret;
	}
}
