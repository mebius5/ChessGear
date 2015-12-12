package com.chessgear.data;

import com.chessgear.game.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    //Logger
    private static final Logger logger = LoggerFactory.getLogger(PGNParser.class);

    /**
     * Constructs the parser with the string we need to parse.
     * @param pgn String containing the PGN for a game.
     * @throws PGNParseException PGN couldn't be parsed.
     */
    public PGNParser(String pgn) throws PGNParseException {
        if(pgn==null){
            throw new PGNParseException("Null PGN string.");
        }
        this.pgn = pgn;
        parseInformation();
    }

    /**
     * Parses the pgn and stores relevant information in the class attributes.
     */
    private void parseInformation() throws PGNParseException {

        try {
            // Parse the tags.
            List<Tag> tags = parseTags(this.pgn);

            if(tags.size()<=3){
                throw new PGNParseException("PGN string must have at least tags for blackPlayerName, whitePlayerName, and result.");
            }

            // Get tag with name White
            for (Tag t : tags) {
                switch (t.getName()) {
                    case "White": this.whitePlayerName = t.getValue();
                        break;
                    case "Black": this.blackPlayerName = t.getValue();
                        break;
                    case "Result": this.result = Result.parseResult(t.getValue());
                        break;
                    case "Variant":
                        if(!t.getValue().equals("Standard")){
                            throw new PGNParseException("PGN Variant other than Standard detected!. Error thrown!");
                        }
                }
            }
        } catch (PGNParseException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    /**
     * Accessor for white player name.
     * @return White player name.
     */
    public String getWhitePlayerName(){
        try {
            if (this.whitePlayerName == null) this.parseInformation();
            return this.whitePlayerName;
        } catch (PGNParseException e){
            logger.error(e.getMessage());
        }
        return null;
    }

    /**
     * Accessor for black player name.
     * @return Black player name.
     */
    public String getBlackPlayerName() {
        try {
            if (this.blackPlayerName == null) this.parseInformation();
            return this.blackPlayerName;
        } catch (PGNParseException e){
            logger.error(e.getMessage());
        }
        return null;
    }

    /**
     * Gets the result of the game.
     * @return Result of the game.
     */
    public Result getResult() {
        try {
            if (this.result == null) this.parseInformation();
            return this.result;
        } catch (PGNParseException e){
            logger.error(e.getMessage());
        }
        return null;
    }

    /**
     * Returns the number of full moves in the game.
     * @return Number of full moves in the game.
     */
    public int getGameLength() {
        try {
            if (this.boardStates == null) this.parseMoves(this.pgn);
            return this.boardStates.size() / 2;
        }catch(PGNParseException e){
            logger.error(e.getMessage());
        }
        return 0;
    }

    /**
     * Retrieves a particular half move.
     * @param player Player who made the move.
     * @param fullMoveNumber Full move number of the move.
     * @return Move corresponding to the passed arguments.
     */
    public Move getHalfMove(Player player, int fullMoveNumber) {
        try {
            if (this.boardStates == null) this.parseMoves(this.pgn);
            switch (player) {
                case BLACK:
                    if (this.blackHalfMoves.size() >= fullMoveNumber) {
                        return this.blackHalfMoves.get(fullMoveNumber - 1);
                    } else {
                        return null;
                    }

                case WHITE:
                    if (this.whiteHalfMoves.size() >= fullMoveNumber) {
                        return this.whiteHalfMoves.get(fullMoveNumber - 1);
                    } else {
                        return null;
                    }
                default:
                    return null;
            }
        }catch(Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }

    /**
     * Accessor for black's half moves.
     * @return List of black's half moves.
     */
    public List<Move> getBlackHalfMoves() {
        try {
            if (this.blackHalfMoves == null) this.parseMoves(this.pgn);
            return this.blackHalfMoves;
        } catch(PGNParseException e){
            logger.error(e.getMessage());
        }
        return null;
    }

    /**
     * Accessor for white's half moves.
     * @return List of white's half moves.
     */
    public List<Move> getWhiteHalfMoves() {
        try {
            if (this.whiteHalfMoves == null) this.parseMoves(this.pgn);
            return this.whiteHalfMoves;
        } catch(PGNParseException e){
            logger.error(e.getMessage());
        }
        return null;
    }

    /**
     * Returns the game represented as a list of board states. 0 indexed at the starting position.
     * @return List of board states containing game progression.
     */
    public List<BoardState> getListOfBoardStates() {
        try {
            if (this.boardStates == null) this.parseMoves(this.pgn);
            return this.boardStates;
        }catch(Exception e){
            logger.error(e.getMessage());
        }
        return null;
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
        result = result.replaceAll("[!|?|+|x|#]", "");
        result = result.replaceAll("[0-9]+[.]+", "");
        result = result.replaceAll("1/2-1/2|0-1|1-0", "");
        result = result.replaceAll("\\s+", " ");
        return result.trim();
    }

    /**
     * Parses the moves contained within a pgn string.
     * @param pgn String containing the PGN for a game of chess.
     * @throws PGNParseException Something went wrong.
     */
    public void parseMoves(String pgn) throws PGNParseException {
        this.whiteHalfMoves = new ArrayList<>();
        this.blackHalfMoves = new ArrayList<>();
        this.boardStates = new ArrayList<>();

        try {
            parseInformation();
            String strippedPgn = stripAnnotations(pgn);
            BoardState currentBoardState = new BoardState();
            currentBoardState.setToDefaultPosition();
            this.boardStates.add(currentBoardState);

            String[] tokenizedMoves = strippedPgn.split(" ");
            Player active = Player.WHITE;

            for (String s : tokenizedMoves) {
                Move m;
                if (s.equals("O-O")) {
                    // Castles kingside
                    PieceType type = PieceType.KING;
                    Square target;
                    Square origin;
                    switch (active) {
                        case WHITE:
                            origin = new Square("e1");
                            target = new Square("g1");
                            break;
                        case BLACK:
                            origin = new Square("e8");
                            target = new Square("g8");
                            break;
                        default:
                            throw new PGNParseException("Invalid active player!");
                    }
                    m = new Move(active, type, origin, target, true, null);
                } else if (s.equals("O-O-O")) {
                    PieceType type = PieceType.KING;
                    Square target;
                    Square origin;
                    // Castles queenside.
                    switch (active) {
                        case WHITE:
                            origin = new Square("e1");
                            target = new Square("c1");
                            break;
                        case BLACK:
                            origin = new Square("e8");
                            target = new Square("c8");
                            break;
                        default:
                            throw new PGNParseException("Invalid active player!");
                    }
                    m = new Move(active, type, origin, target, true, null);
                } else {

                    // Regular move.
                    PieceType type = getPieceType(s);
                    Square target = extractTarget(s);
                    char fileDisambiguation = getFileDisambiguation(s);
                    if (fileDisambiguation != 0) {
                    }
                    int rankDisambiguation = getRankDisambiguation(s);
                    Piece p = currentBoardState.getPieceByTarget(type, active, target, fileDisambiguation, rankDisambiguation);
                    Square origin = p.getLocation();
                    PieceType promotionType = getPromotionType(s);
                    m = new Move(active, type, origin, target, false, promotionType);

                }
                switch (active) {
                    case WHITE:
                        this.whiteHalfMoves.add(m);
                        break;
                    case BLACK:
                    default:
                        this.blackHalfMoves.add(m);
                        break;
                }
                currentBoardState = currentBoardState.doMove(m);

                this.boardStates.add(currentBoardState);
                active = active.toggle();
            }
        } catch (PGNParseException e) {
            logger.error(e.getMessage());
            throw e;
        } catch (StringIndexOutOfBoundsException error){
            PGNParseException e = new PGNParseException("Unable to parse PGN due to string Out of Bound error. Most likely " +
                    "due to invalid PGN string");
            logger.error(e.getMessage());
            throw e;
        }
    }

    /**
     * Method to extract Square target from String token
     * @param token the string token passed in to retrieve target
     * @return a new Square that matches the String token
     */
    public static Square extractTarget(String token) {
        int lastIndex = getLastNumericIndex(token);
        int rank = (int)(token.charAt(lastIndex)) - '1';
        int file = (int)token.charAt(lastIndex - 1) - 'a';
        return new Square(file, rank);
    }

    /**
     * Returns the file disambiguation of a PGN move token.
     * @param token PGN String token.
     * @return File disambiguation character.
     */
    public static char getFileDisambiguation(String token) {
        int lastIndex = getLastNumericIndex(token);
        if (lastIndex > 1) {
            char candidateCharacter = token.charAt(lastIndex - 2);
            if (Character.isAlphabetic(candidateCharacter) && Character.isLowerCase(candidateCharacter)) {
                return candidateCharacter;
            }
        }
        return (char)0;
    }

    /**
     * Gets the rank disambiguation of a PGN move token.
     * @param token PGN String token.
     * @return Rank disambiguation.
     */
    public static int getRankDisambiguation(String token) {
        int lastIndex = getLastNumericIndex(token);
        if (lastIndex > 1 && Character.isDigit(token.charAt(lastIndex - 2))) {
            return Character.getNumericValue(token.charAt(lastIndex - 2));
        }
        return -1;
    }

    /**
     * Gets the type of piece that was moved.
     * @param token PGN move token string.
     * @return PieceType of piece that was moved.
     */
    public static PieceType getPieceType(String token) {

        int lastIndex = getLastNumericIndex(token);
        for (int c = 0; c < lastIndex; c++) {
            if (Character.isUpperCase(token.charAt(c))) {
                return PieceType.parseCharacter(token.charAt(c));
            }
        }
        return PieceType.PAWN;
    }

    /**
     * Gets promotion type.
     * @param token PGN move token string.
     * @return PieceType of pawn that was promoted.
     */
    public static PieceType getPromotionType(String token) {
        if (token.contains("=")) {
            return PieceType.parseCharacter(token.charAt(token.indexOf('=') + 1));
        } else {
            return null;
        }

    }

    /**
     * Gets the index of the last numeric character in a string.
     * @param token String for which to find last numeric index.
     * @return Last numeric index.
     */
    public static int getLastNumericIndex(String token) {
        int lastIndex = 0;
        for (int c = 0; c < token.length(); c++) {
            if (Character.isDigit(token.charAt(c))) lastIndex = c;
        }
        return lastIndex;
    }

    public String getPGN() {
        return this.pgn;
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

