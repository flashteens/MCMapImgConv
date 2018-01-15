package com.flashteens.jnbtTests.mapItem.io;

import java.io.File;

public class Utils {

	private Utils() {
	}

	public static String childrenPathOf(String origPath, String child) {
		if (origPath.endsWith(File.separator)) {
			return origPath + child;
		} else {
			return origPath + File.separator + child;
		}
	}

	public static String childrenPathOf(String origPath, String... children) {
		String path = origPath;
		for (int i = 0; i < children.length; i++) {
			path = childrenPathOf(path, children[i]);
		}
		return path;
	}

	public static String relativePathOf(String base, String path) {
		// Ref: https://stackoverflow.com/questions/204784
		return new File(base).toURI().relativize(new File(path).toURI()).getPath();
	}

}
