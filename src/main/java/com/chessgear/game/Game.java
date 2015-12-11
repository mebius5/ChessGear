package com.chessgear.game;

import com.chessgear.data.PGNParser;

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
     * Constructor for the Game class
     * @param whitePlayerName the name of the white player
     * @param blackPlayerName name of the black player
     * @param dateImported date imported
     * @param pgn the original pgn string
     * @param result the result of the game
     * @param id the id of the game
     */
    public Game(String whitePlayerName, String blackPlayerName, Date dateImported, String pgn, Result result, int id){
        this.whitePlayerName=whitePlayerName;
        this.blackPlayerName=blackPlayerName;
        this.dateImported=dateImported;
        this.pgn=pgn;
        this.result=result;
        this.id=id;
    }

    /**
     * Creates a new game from pgn parser.
     * @param parser Contains information from the PGN Parse
     */
    public Game(PGNParser parser) {
            this(parser.getWhitePlayerName(), parser.getBlackPlayerName(), new Date(), parser.getPGN(), parser.getResult(), 0);
    }

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

    /***
     * Accessor for the date imported
     * @return Date that the game was imported
     */
    public Date getDateImported() {
        return this.dateImported;
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
     * @return Result of the game.
     */
    public Result getResult() {
        return this.result;
    }

    /***
     * Accessor for the id of the game.
     * @return Id of the game
     */
    public int getID(){return this.id;}
}
