package com.crocodoc;

/**
 * CrocodocException extends the default exception class.
 * It doesn't do anything fancy except be a unique kind of Exception.
 */
public class CrocodocException extends Exception {
	public CrocodocException(String message) {
		super(message);
	}

	private static final long serialVersionUID = 1L;
}
