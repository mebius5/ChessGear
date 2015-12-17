package com.chessgear.game;

import com.chessgear.data.PGNParseException;

/**
 * Object representation for a game
 * Enum class with the different results
 * */
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
     * Gets the string representation of this result.
     * @return String representation of this result.
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
                return "INVALID RESULT";
        }
    }

}
