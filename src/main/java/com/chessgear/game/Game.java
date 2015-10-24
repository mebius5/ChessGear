package com.chessgear.game;

import java.util.Date;

/**
 * Object representation of a game after import/parsing into the tool.
 * Created by Ran on 10/8/2015.
 */
public class Game {

    /**
     * Name of the white player.
     */
    private String whitePlayerName;

    /**
     * Name of the black player.
     */
    private String blackPlayerName;

    /**
     * Date of import.
     */
    private Date dateImported;

    /**
     * PGN String of game.
     */
    private String pgn;

    /**
     * Game result.
     */
    private Result result;

    /**
     * Id of game.
     */
    private int id;

    /**
     * Accessor for name of white player.
     * @return Name of white player.
     */
    public String getWhitePlayerName() {
        return this.whitePlayerName;
    }

    /**
     * Accessor for name of black player.
     * @return Name of black player.
     */
    public String getBlackPlayerName() {
        return this.blackPlayerName;
    }

    /**
     * Accessor for pgn of game.
     * @return Pgn of game.
     */
    public String getPgn() {
        return this.pgn;
    }

    /**
     * Accessor for the result of the game.
     * @return Result of game.
     */
    public Result getResult() {
        return this.result;
    }

}
