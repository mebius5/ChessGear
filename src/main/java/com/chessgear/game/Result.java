package com.chessgear.game;

import com.chessgear.data.PGNParseException;

/**
 * Game result enum.
 * Created by Ran on 10/14/2015.
 */
public enum Result {

    WHITE_WIN, BLACK_WIN, DRAW, INVALID;

    /**
     * Parses a string containing a result.
     * @param resultString Result string.
     * @return Result.
     * @throws PGNParseException If invalid string.
     */
    public static Result parseResult(String resultString) throws PGNParseException {
        switch (resultString) {
            case "1-0":
                return WHITE_WIN;
            case "0-1":
                return BLACK_WIN;
            case "1/2-1/2":
                return DRAW;
            default:
                throw new PGNParseException("Could not parse result!");
        }
    }

    /**
     * Gets string implementation for the result..
     * @return String implementation for the result.
     */
    public String toString() {
        switch (this) {
            case WHITE_WIN:
                return "1-0";
            case BLACK_WIN:
                return "0-1";
            case DRAW:
                return "1/2-1/2";
            default:
                return "Invalid result";
        }
    }

}
