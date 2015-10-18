package com.chessgear.data;

import com.chessgear.game.Move;
import com.chessgear.game.Player;
import com.chessgear.game.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Ran on 10/14/2015.
 */
public class PGNParser {

    private String pgn;
    private String whitePlayerName;
    private String blackPlayerName;
    private Result result;

    /**
     * Constructs the parser with the string we need to parse.
     * @param pgn
     */
    public PGNParser(String pgn) {
        this.pgn = pgn;
        this.parse();
    }

    private void parse(){
    }

    public String getWhitePlayerName() {
        return this.whitePlayerName;
    }

    public String getBlackPlayerName() {
        return this.blackPlayerName;
    }

    public Result getResult() {
        return this.result;
    }

    public Move getHalfMove(Player player, int fullMoveNumber) {
    }

    private static List<Tag> parseTags(String pgn){
        List<Tag> tags = new ArrayList<>();
        Scanner scanner = new Scanner(pgn);

        return tags;
    }

    private static class Tag {
        String name;
        String content;
    }
}

