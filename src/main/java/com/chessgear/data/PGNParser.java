package com.chessgear.data;

import com.chessgear.game.BoardState;
import com.chessgear.game.Move;
import com.chessgear.game.Player;
import com.chessgear.game.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * PGN Parser class.
 * Created by Ran on 10/14/2015.
 */
public class PGNParser {

    /**
     * Minimum possible length of a line containing a tag.
     */
    private static final int MINIMUM_TAG_LINE_LENGTH = 6;

    /**
     *
     */
    private String pgn;
    /**
     * White player name.
     */
    private String whitePlayerName;
    /**
     * Black player name.
     */
    private String blackPlayerName;
    /**
     * Result of the game.
     */
    private Result result;

    /**
     * White's half moves.
     */
    private List<Move> whiteHalfMoves;

    /**
     * Black's half moves.
     */
    private List<Move> blackHalfMoves;

    /**
     * List of board states of the game's progression.
     */
    private List<BoardState> boardStates;

    /**
     * Constructs the parser with the string we need to parse.
     * @param pgn String containing the PGN for a game.
     * @throws PGNParseException PGN couldn't be parsed.
     */
    public PGNParser(String pgn) throws PGNParseException {
        this.pgn = pgn;
        this.whiteHalfMoves = new ArrayList<>();
        this.blackHalfMoves = new ArrayList<>();
        this.boardStates = new ArrayList<>();
        this.parse();
    }

    /**
     * Parses the pgn and stores relevant information in the class attributes.
     */
    private void parse() throws PGNParseException {

        // Parse the tags.
        List<Tag> tags = parseTags(this.pgn);
        // Get tag with name White
        for (Tag t : tags) {
            switch (t.getName()) {
                case "White": this.whitePlayerName = t.getValue();
                    break;
                case "Black": this.blackPlayerName = t.getValue();
                    break;
                case "Result": this.result = Result.parseResult(t.getValue());
                    break;
            }
        }

        // Parse the half moves.
        this.parseMoves(this.pgn);

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
        switch (player) {
            case BLACK:
                return this.blackHalfMoves.get(fullMoveNumber);
            case WHITE:
                return this.whiteHalfMoves.get(fullMoveNumber);
            default:
                return null;
        }
    }

    /**
     * Returns the game represented as a list of board states. 0 indexed at the starting position.
     * @return List of board states containing game progression.
     */
    public List<BoardState> getListOfBoardStates() {
        return this.boardStates;
    }

    /**
     * Given a pgn string, return a list of its tags.
     * @param pgn Pgn string.
     * @return List of tags contained within pgn string.
     */
    public static List<Tag> parseTags(String pgn){
        List<Tag> tags = new ArrayList<>();
        Scanner scanner = new Scanner(pgn);

        while (scanner.hasNextLine()) {

            String line = scanner.nextLine();

            int lineLength = line.length();
            // This is a tag.
            if (lineLength > MINIMUM_TAG_LINE_LENGTH && line.charAt(0) == '[' && line.charAt(lineLength - 1) == ']') {
                String strippedLine = line.replaceAll("\\[|\\]|\"", "");
                int splitIndex = strippedLine.indexOf(" ");
                String name = strippedLine.substring(0, splitIndex);
                String value = strippedLine.substring(splitIndex + 1);
                Tag newTag = new Tag(name, value);
                tags.add(newTag);
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
    public static String stripAnnotations(String pgn) {
        // Remove everything within square brackets (tags)
        String result = pgn.replaceAll("\\[[^]]*\\]", "");
        // Remove everything within curly brackets
        result = result.replaceAll("\\{[^}]*\\}", "");
        // Remove everything within parentheses.
        result = result.replaceAll("\\([^)]*\\)", "");
        return result;
    }

    private void parseMoves(String pgn) {
        String strippedPgn = stripAnnotations(pgn);
        BoardState startingBoardState = new BoardState();
        startingBoardState.setToDefaultPosition();
        // TODO

    }

    /**
     * Inner class tag. Encodes tag information.
     */
    public static class Tag {
        /**
         * Name of the tag.
         */
        private String name;
        /**
         * Value of the tag.
         */
        private String value;

        /**
         * Field-based constructor.
         * @param name Name of tag.
         * @param value Value of tag.
         */
        public Tag(String name, String value) {
            this.name = name;
            this.value = value;
        }

        /**
         * Accessor for name.
         * @return Name of tag.
         */
        public String getName() {
            return this.name;
        }

        /**
         * Accessor for value.
         * @return Value of tag.
         */
        public String getValue() {
            return this.value;
        }
    }
}

