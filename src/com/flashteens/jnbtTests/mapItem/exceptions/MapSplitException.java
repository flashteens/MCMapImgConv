package com.flashteens.jnbtTests.mapItem.exceptions;

public class MapSplitException extends Exception {

	private static final long serialVersionUID = -9083950728208454009L;

	public MapSplitException() {
	}

	public MapSplitException(String message) {
		super(message);
	}

	public MapSplitException(Throwable cause) {
		super(cause);
	}

	public MapSplitException(String message, Throwable cause) {
		super(message, cause);
	}

	public MapSplitException(String message, Throwable cause, //
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public static final String INVALID_IMG_SIZE = "invalid image size";
	public static final String MAP_ID_OVERFLOW = "map id overflow";
	public static final String USER_CANCELLED = "user cancelled";

}
