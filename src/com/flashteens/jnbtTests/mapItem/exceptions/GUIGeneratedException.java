package com.flashteens.jnbtTests.mapItem.exceptions;

public class GUIGeneratedException extends Exception {

	private static final long serialVersionUID = 5668779568349265854L;

	public GUIGeneratedException() {
	}

	public GUIGeneratedException(String message) {
		super(message);
	}

	public GUIGeneratedException(Throwable cause) {
		super(cause);
	}

	public GUIGeneratedException(String message, Throwable cause) {
		super(message, cause);
	}

	public GUIGeneratedException(String message, Throwable cause, //
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
