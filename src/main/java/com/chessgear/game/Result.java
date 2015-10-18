package com.chessgear.game;

import com.chessgear.data.PGNParseException;

/**
 * Game result enum.
 * Created by Ran on 10/14/2015.
 */
public enum Result {

    WHITE_WIN, BLACK_WIN, DRAW, INVALID;

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

}