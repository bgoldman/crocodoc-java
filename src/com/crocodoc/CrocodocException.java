package com.crocodoc;

/**
 * CrocodocException extends the default exception class. It doesn't do anything
 * fancy except be a unique kind of Exception.
 */
public class CrocodocException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * A short string representing the error code
     * 
     * @var code
     */
    private static String code = null;

    /**
     * The constructor function for CrocodocException
     * 
     * @param message
     *            A string representing the long form error message
     * @param code
     *            A string representing the short form error code
     */
    public CrocodocException(String message, String code) {
        super(message);
        this.code = code;
    }

    /**
     * Get the short form error code
     * 
     * @return string The short form error code
     */
    public String getCode() {
        return code;
    }
}
