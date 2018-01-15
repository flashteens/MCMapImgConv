package com.flashteens.jnbtTests.mapItem.exceptions;

public class UserAbortException extends RuntimeException {

	private static final long serialVersionUID = 6925046069645070758L;

	public UserAbortException() {
	}

	public UserAbortException(String message) {
		super(message);
	}

	public UserAbortException(Throwable cause) {
		super(cause);
	}

	public UserAbortException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserAbortException(String message, Throwable cause, //
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
