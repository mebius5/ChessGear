package com.chessgear.data;

/**
 * PGN parse exception class.
 * Created by Ran on 10/14/2015.
 */
public class PGNParseException extends Exception {

    /**
     * Constructs an exception with a message.
     * @param message Message for exception.
     */
    public PGNParseException(String message) {
        super(message);
    }

}