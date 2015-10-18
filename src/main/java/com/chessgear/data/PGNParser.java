package com.chessgear.data;

import com.chessgear.game.Move;
import com.chessgear.game.Player;
import com.chessgear.game.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

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

    /**
     * Parses the pgn and stores relevant information in the class attributes.
     */
    private void parse(){
    }

    /**
     * Accessor for white player name.
     * @return White player name.
     */
    public String getWhitePlayerName() {
        return this.whitePlayerName;
    }

    /**
     * Accessor for black player name.
     * @return Black player name.
     */
    public String getBlackPlayerName() {
        return this.blackPlayerName;
    }

    /**
     * Gets the result of the game.
     * @return Result of the game.
     */
    public Result getResult() {
        return this.result;
    }

    /**
     * Retrieves a particular half move.
     * @param player Player who made the move.
     * @param fullMoveNumber Full move number of the move.
     * @return Move corresponding to the passed arguments.
     */
    public Move getHalfMove(Player player, int fullMoveNumber) {
        // TODO
    }

    /**
     * Given a pgn string, return a list of its tags.
     * @param pgn Pgn string.
     * @return List of tags contained within pgn string.
     */
    private static List<Tag> parseTags(String pgn){
        List<Tag> tags = new ArrayList<>();
        Scanner scanner = new Scanner(pgn);

        while (scanner.hasNextLine()) {

            String line = scanner.nextLine();

            int lineLength = line.length();
            // This is a tag.
            if (line.charAt(0) == '[' && line.charAt(lineLength - 1) == ']') {
                String strippedLine = line.replaceAll("\\[|\\]|\"", "");
                int splitIndex = strippedLine.indexOf(" ");
                String name = strippedLine.substring(0, splitIndex);
                String value = strippedLine.substring(splitIndex + 1);
            }

        }

        scanner.close();
        return tags;
    }

    /**
     * Strips the annotations/tags from a pgn string.
     * @param pgn Pgn string to strip.
     * @return PGN string containing only moves.
     */
    private static String stripAnnotations(String pgn) {
        // Remove everything within square brackets (tags)
        String result = pgn.replaceAll("\\[[^]]*\\]", "");
        // Remove everything within curly brackets
        result = result.replaceAll("\\{[^}]*\\}", "");
        // Recursively remove everything within parentheses - balanced removal
        result = result.replaceAll("\\((?:(?R)|[^()])*\\)", "");
        return result;
    }

    /**
     * Inner class tag. Encodes tag information.
     */
    private static class Tag {
        /**
         * Name of the tag.
         */
        String name;
        /**
         * Value of the tag.
         */
        String value;
    }
}

