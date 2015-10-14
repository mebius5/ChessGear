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
    private Result result

    /**
     * Id of game.
     */
    private int id;

}
