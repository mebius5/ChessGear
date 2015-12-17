package com.chessgear.data;

/**
 * Exception class for the PGNParser.
 */
public final class PGNParseException extends Exception {

    /**
     * Constructs an exception with a message.
     * @param message Message for exception.
     */
    public PGNParseException(String message) {
        super(message);
    }

}