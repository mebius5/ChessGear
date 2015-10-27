package com.chessgear.game;

import com.chessgear.data.PGNParseException;

import java.util.ArrayList;
import java.util.List;

/**
 * Board state class.
 * Created by Ran on 10/8/2015.
 */
public class BoardState {

    /**
     * Array of all pieces in board.
     * Indexing is zero-indexed, [x][y] - i.e. b3 = [1][2], c7 = [2][6]
     */
    private Piece[][] pieces;

    /**
     * Flag indicating whether or not castling queenside for white is legal.
     */
    private boolean canWhiteCastleQueenSide;

    /**
     * Flag indicating whether or not castling kingside for white is legal.
     */
    private boolean canWhiteCastleKingSide;

    /**
     * Flag indicating whether or not castling queenside for black is legal.
     */
    private boolean canBlackCastleQueenSide;

    /**
     * Flag indicating whether or not castling kingside for black is legal.
     */
    private boolean canBlackCastleKingSide;

    /**
     * If the last move was a double pawn push, this square indicates the en passant capture square.
     */
    private Square enPassantTarget;

    /**
     * Counts the # of half moves since the last pawn push or piece capture.
     */
    private int halfMoveCounter;

    /**
     * Counts the # of full moves elapsed since beginning of game.
     */
    private int fullMoveCounter;

    /**
     * Whose turn it is currently.
     */
    private Player active;

    /**
     * Default constructor.
     */
    public BoardState() {
        this.pieces = new Piece[8][8];
    }

    /**
     * Reads in information from Forsythe-Edwards notation into a Boardstate object.
     * @param fen FEN notation of the board state.
     */
    public BoardState(String fen) {
        this.pieces = new Piece[8][8];

        String[] tokenizedFen = fen.split(" ");
        String[] tokenizedPiecePositions = tokenizedFen[0].split("/");
        // For each token
        for (int c = 0; c < 8 ; c++) {
            // We begin on the 8th rank, and work our way back.
            int rank = 7 - c;
            // We begin on the a file, and work our way to the right.
            int file = 0;

            // For each character on each rank:
            for (int d = 0; d < tokenizedPiecePositions[c].length(); d++) {
                char currentCharacter = tokenizedPiecePositions[c].charAt(d);
                // If it's numeric, it symbolizes the number of spaces we skip.
                if (Character.isDigit(currentCharacter)) {
                    int numberOfEmpties = Character.getNumericValue(currentCharacter);
                    file += numberOfEmpties;
                } else {
                    PieceType type = PieceType.parseCharacter(currentCharacter);
                    Player player;
                    if (Character.isUpperCase(currentCharacter)) {
                        player = Player.WHITE;
                    } else {
                        player = Player.BLACK;
                    }
                    this.pieces[file][rank] = new Piece(type, player, new Square(file, rank));
                    file++;
                }
            }
        }

        // Now read in active flag.
        switch (tokenizedFen[1]) {
            case "w":
                this.active = Player.WHITE;
                break;
            case "b":
                this.active = Player.BLACK;
                break;
        }

        // Read in castling flags.
        String castlingFlags = tokenizedFen[2];
        if (castlingFlags.contains("Q")) {
            this.canWhiteCastleQueenSide = true;
        }
        if (castlingFlags.contains("K")) {
            this.canWhiteCastleKingSide = true;
        }
        if (castlingFlags.contains("k")) {
            this.canBlackCastleKingSide = true;
        }
        if (castlingFlags.contains("q")) {
            this.canBlackCastleQueenSide = true;
        }

        // Read in en passant target.
        String enPassantTarget = tokenizedFen[3];
        if (!enPassantTarget.equals("-")) {
            this.enPassantTarget = new Square(enPassantTarget);
        }

        // Read in half move counter.
        this.halfMoveCounter = Integer.parseInt(tokenizedFen[4]);

        // Read in full move counter.
        this.fullMoveCounter = Integer.parseInt(tokenizedFen[5]);
    }

    /**
     * Sets the boardstate to the starting position.
     */
    public void setToDefaultPosition() {
        // Adds pieces to starting positions
        this.pieces[0][0] = new Piece(PieceType.ROOK, Player.WHITE, new Square(0,0));
        this.pieces[1][0] = new Piece(PieceType.KNIGHT, Player.WHITE, new Square(1,0));
        this.pieces[2][0] = new Piece(PieceType.BISHOP, Player.WHITE, new Square(2,0));
        this.pieces[3][0] = new Piece(PieceType.QUEEN, Player.WHITE, new Square(3,0));
        this.pieces[4][0] = new Piece(PieceType.KING, Player.WHITE, new Square(4,0));
        this.pieces[5][0] = new Piece(PieceType.BISHOP, Player.WHITE, new Square(5,0));
        this.pieces[6][0] = new Piece(PieceType.KNIGHT, Player.WHITE, new Square(6,0));
        this.pieces[7][0] = new Piece(PieceType.ROOK, Player.WHITE, new Square(7,0));
        for (int c = 0; c < 8; c++) {
            this.pieces[c][1] = new Piece(PieceType.PAWN, Player.WHITE, new Square(c, 1));
        }
        this.pieces[0][7] = new Piece(PieceType.ROOK, Player.BLACK, new Square(0,7));
        this.pieces[1][7] = new Piece(PieceType.KNIGHT, Player.BLACK, new Square(1,7));
        this.pieces[2][7] = new Piece(PieceType.BISHOP, Player.BLACK, new Square(2,7));
        this.pieces[3][7] = new Piece(PieceType.QUEEN, Player.BLACK, new Square(3,7));
        this.pieces[4][7] = new Piece(PieceType.KING, Player.BLACK, new Square(4,7));
        this.pieces[5][7] = new Piece(PieceType.BISHOP, Player.BLACK, new Square(5,7));
        this.pieces[6][7] = new Piece(PieceType.KNIGHT, Player.BLACK, new Square(6,7));
        this.pieces[7][7] = new Piece(PieceType.ROOK, Player.BLACK, new Square(7,7));
        for (int c = 0; c < 8; c++) {
            this.pieces[c][6] = new Piece(PieceType.PAWN, Player.BLACK, new Square(c, 6));
        }
        // Initialize flags and counters
        this.canWhiteCastleKingSide = true;
        this.canWhiteCastleQueenSide = true;
        this.canBlackCastleKingSide = true;
        this.canBlackCastleQueenSide = true;
        this.halfMoveCounter = 0;
        this.fullMoveCounter = 1;
        this.active = Player.WHITE;
    }

    /**
     * Returns the Forsyth-Edwards Notation for the current board state.
     * @return FEN string representation of current board state.
     */
    public String toFEN() {
        StringBuilder fenBuilder = new StringBuilder();
        // Build the piece string.
        for (int i = 7; i>=0; i--) {
            int numEmptySquares = 0;
            for (int j  = 0; j < 8; j++) {
                if (this.pieces[j][i] != null) {
                    if (numEmptySquares != 0) {
                        fenBuilder.append(numEmptySquares);
                        numEmptySquares = 0;
                    }
                    fenBuilder.append(this.pieces[j][i].getFENChar());
                } else {
                    numEmptySquares++;
                }
            }
            if (numEmptySquares != 0) {
                fenBuilder.append(numEmptySquares);
                numEmptySquares = 0;
            }
            if (i != 0) fenBuilder.append("/"); // No slash on the last rank
        }

        // Append a space.
        fenBuilder.append(" ");
        // Active color
        switch (this.active) {
            case WHITE:
                fenBuilder.append('w');
                break;
            case BLACK:
                fenBuilder.append('b');
                break;
            default:
        }
        fenBuilder.append(" ");

        // Castling availability
        if (this.canWhiteCastleQueenSide || this.canBlackCastleQueenSide || this.canWhiteCastleKingSide || this.canBlackCastleKingSide) {
            if (this.canWhiteCastleKingSide) {
                fenBuilder.append("K");
            }
            if (this.canWhiteCastleQueenSide) {
                fenBuilder.append("Q");
            }
            if (this.canBlackCastleKingSide) {
                fenBuilder.append("k");
            }
            if (this.canBlackCastleQueenSide) {
                fenBuilder.append("q");
            }
        } else {
            fenBuilder.append("-");
        }
        fenBuilder.append(" ");

        // En passant target
        if (this.enPassantTarget != null) {
            fenBuilder.append(this.enPassantTarget.toString());
        } else {
            fenBuilder.append("-");
        }

        fenBuilder.append(" ");

        // Halfmove
        fenBuilder.append(this.halfMoveCounter);
        fenBuilder.append(" ");

        // Fullmove
        fenBuilder.append(this.fullMoveCounter);

        return fenBuilder.toString();
    }

    /**
     * Returns the new boardstate after a move was done to it.
     * NOTE: This boardstate remains unchanged! We do not do a modification in place!
     * We clone the boardstate, then do a new move.
     * @param m Move to execute.
     * @return Returns the boardstate resulting from the move.
     */
    public BoardState doMove(Move m) {
        boolean capture = false;
        BoardState newBoardState = this.clone();

        // Move the piece.
        Piece originPiece = newBoardState.getPieceAt(m.getOrigin());

        // Check if it was a pawn move. This flag is used for setting the halfmove clock.
        boolean pawnMove = (originPiece.getType() == PieceType.PAWN);

        // Check if it was a king move. If so, we set all castling flags for this player to false.
        if (originPiece.getType() == PieceType.KING) {
            switch (m.getWhoMoved()) {
                case WHITE:
                    newBoardState.canWhiteCastleKingSide = false;
                    newBoardState.canWhiteCastleQueenSide = false;
                    break;
                case BLACK:
                    newBoardState.canBlackCastleKingSide = false;
                    newBoardState.canBlackCastleQueenSide = false;
                    break;
            }
        }

        // Check if it was a rook move, and if it was one of the corner rooks. We set that side's castling flag to false.
        if (originPiece.getType() == PieceType.ROOK) {
            if (m.getWhoMoved() == Player.WHITE) {
                if (m.getOrigin().equals(new Square("a1"))) {
                    newBoardState.canWhiteCastleQueenSide = false;
                }
                if (m.getOrigin().equals(new Square("h1"))) {
                    newBoardState.canWhiteCastleKingSide = false;
                }
            } else {
                if (m.getOrigin().equals(new Square("a8"))) {
                    newBoardState.canBlackCastleQueenSide = false;
                }
                if (m.getOrigin().equals(new Square("h8"))) {
                    newBoardState.canBlackCastleKingSide = false;
                }
            }
        }

        newBoardState.setPieceAt(m.getOrigin(), null); // Remove from original location.
        Piece destinationPiece = newBoardState.getPieceAt(m.getDestination());
        if (destinationPiece != null) capture = true;
        newBoardState.setPieceAt(m.getDestination(), originPiece);

        // Check if pawn was pushed two squares.
        newBoardState.enPassantTarget = m.getEnPassantTarget();

        // If castling.
        if (m.isCastling()) {
            String castlingDestination = m.getDestination().toString();
            Piece castlePiece;
            switch (castlingDestination) {
                case "g1":
                    // Get the piece at h1, move it to f1
                    castlePiece = newBoardState.pieces[7][0];
                    newBoardState.pieces[7][0] = null;
                    newBoardState.pieces[5][0] = castlePiece;
                    break;
                case "c1":
                    // Get the piece at a1, move it to d1
                    castlePiece = newBoardState.pieces[0][0];
                    newBoardState.pieces[0][0] = null;
                    newBoardState.pieces[3][0] = castlePiece;
                    break;
                case "g8":
                    // Get the piece at h8, move it to f8
                    castlePiece = newBoardState.pieces[7][7];
                    newBoardState.pieces[7][7] = null;
                    newBoardState.pieces[5][7] = castlePiece;
                    break;
                case "c8":
                    // Get the pice at a8, move it to d8
                    castlePiece = newBoardState.pieces[0][7];
                    newBoardState.pieces[0][7] = null;
                    newBoardState.pieces[3][7] = castlePiece;
                    break;
                default:
            }
        }

        // If promotion, change piece's type.
        if (m.getPromotionType() != null) {
            destinationPiece.setPieceType(m.getPromotionType());
        }

        // If nothing was captured, or if no pawn was moved, increment half move counter.
        if (!capture && !pawnMove) {
            newBoardState.halfMoveCounter++;
        } else {
            newBoardState.halfMoveCounter = 0;
        }

        // If black moved, increment full move counter
        if (m.getWhoMoved() == Player.BLACK) {
            newBoardState.fullMoveCounter++;
        }

        // Toggle active switch
        newBoardState.active = this.active.toggle();

        return newBoardState;
    }

    /**
     * Clone constructor.
     * @return A deep copy of this object.
     */
    public BoardState clone() {
        BoardState cloneBoardState = new BoardState();
        for (int c = 0; c < 8; c++) {
            for (int d = 0; d < 8; d++) {
                if (this.pieces[c][d] != null) cloneBoardState.pieces[c][d] = this.pieces[c][d].clone();
            }
        }
        cloneBoardState.canBlackCastleKingSide = this.canBlackCastleKingSide;
        cloneBoardState.canBlackCastleQueenSide = this.canBlackCastleQueenSide;
        cloneBoardState.canWhiteCastleQueenSide = this.canWhiteCastleQueenSide;
        cloneBoardState.canWhiteCastleKingSide = this.canWhiteCastleKingSide;
        cloneBoardState.enPassantTarget = this.enPassantTarget;
        cloneBoardState.active = this.active;
        cloneBoardState.halfMoveCounter = this.halfMoveCounter;
        cloneBoardState.fullMoveCounter = this.fullMoveCounter;

        return cloneBoardState;
    }

    /**
     * Returns a list of all pieces owned by a particular player on board of the given type.
     * @param player Player's pieces who we should look through.
     * @param type Type of piece we should look for.
     * @return A list of all pieces owned by the specified player, of the specified type.
     */
    public List<Piece> getAllPiecesOfType(Player player, PieceType type) {
        List<Piece> result = new ArrayList<>();
        for (int c = 0; c < 8; c++) {
            for (int d = 0; d < 8; d++) {
                if (this.pieces[c][d] != null && this.pieces[c][d].getOwner().equals(player) && this.pieces[c][d].getType().equals(type)) {
                    result.add(this.pieces[c][d]);
                }
            }
        }
        return result;
    }

    /**
     * Checks if move can be made.
     * @param origin Location of piece being moved
     * @param target The target square
     * @return True if piece can make the move; false if not.
     */
    public boolean canMakeMove(Square origin, Square target) {
        Piece piece = this.getPieceAt(origin);
        int xDisplace = origin.getXDisplacement(target);
        int yDisplace = origin.getYDisplacement(target);

        // Firstly, the piece has to exist.
        if (piece != null) {

            switch (piece.getType()) {
                case PAWN:
                    // Can't move if path is blocked
                    if (this.isBlocked(origin, target)) {
                        return false;
                    }
                    // Can't move more than 1 laterally or 2 horizontally, can't stay on same rank.
                    if (Math.abs(yDisplace) > 2 || yDisplace == 0 || Math.abs(xDisplace) > 1) {
                        return false;
                    }
                    switch (piece.getOwner()) {
                        case WHITE:
                            // Can't move backwards as white.
                            if (yDisplace < 0) {
                                return false;
                            }
                            // Can't move forwards 2 unless we're on the correct rank (2nd rank)
                            if (yDisplace == 2 && (piece.getLocation().getY() != 1 || xDisplace != 0)) {
                                return false;
                            }
                            // If we're moving laterally, we have to either be moving to the en passant target or taking a piece.
                            if (Math.abs(xDisplace) == 1) {
                                if (this.getPieceAt(target) == null && !this.enPassantTarget.equals(target)) {
                                    return false;
                                }
                            }
                            break;
                        case BLACK:
                            if (yDisplace > 0) return false;
                            // Can't move up 2 unless we're on the correct rank (7th rank)
                            if (yDisplace == -2 && (piece.getLocation().getY() != 6 || xDisplace != 0)) return false;
                            if (Math.abs(xDisplace) == 1) {
                                if (this.getPieceAt(target) == null && !this.enPassantTarget.equals(target)) return false;
                            }
                            break;
                    }
                    break;

                case BISHOP:
                    // Can't move if not on the same diagonal.
                    if (!origin.isOnDiagonal(target)) return false;
                    // Can't move if path is blocked.
                    if (this.isBlocked(origin, target)) return false;
                    break;

                case KNIGHT:
                    if (!((Math.abs(xDisplace) == 2 && Math.abs(yDisplace) == 1) || Math.abs(xDisplace) == 1 && Math.abs(yDisplace) == 2)) return false;
                    break;

                case ROOK:
                    if (!origin.isOnSameFile(target) && !origin.isOnSameRank(target)) return false;
                    if (this.isBlocked(origin, target)) return false;
                    break;

                case QUEEN:
                    if (!origin.isOnDiagonal(target) && !origin.isOnSameFile(target) && origin.isOnSameRank(target)) return false;
                    if (this.isBlocked(origin, target)) return false;
                    break;

                case KING:
                    // TODO validate king moves. Not essential for basic pgn parsing.
                    break;

            }
            // TODO
            // Must check for absolute pins, if we're on the same diagonal, rank, or file as the king.
            return true;
        }

        return false;
    }

    /**
     * Checks if move can be made.
     * @param origin Location of origin piece.
     * @param target Location of target piece.
     * @return True if can be made, else false.
     */
    public boolean canMakeMove(String origin, String target) {
        return this.canMakeMove(new Square(origin), new Square(target));
    }

    /**
     * Gets piece by target square.
     * @param type Type of piece.
     * @param owner Owner of piece.
     * @param target Square that piece is being moved to.
     * @param fileDisambiguation Disambiguation for file.
     * @param rankDisambiguation Disambiguation for rank.
     * @return Returns the piece.
     */
    public Piece getPieceByTarget(PieceType type, Player owner, Square target, char fileDisambiguation, int rankDisambiguation) {
        List<Piece> candidatePieces = this.getAllPiecesOfType(owner, type);

        for (Piece p : candidatePieces) {
            Square location = p.getLocation();
            if (this.canMakeMove(location, target)) {
                // Check if there's file disambiguation
                if (fileDisambiguation != 0) {
                    // If there is, then check if we're on the correct file
                    if (location.getFile() == fileDisambiguation) {
                        return p;
                    } else {
                        // If we're not, continue with the loop.
                        continue;
                    }
                }
                // Else, check if there's rank disambiguation
                if (rankDisambiguation != -1) {
                    // If there is, check if we're on the correct rank.
                    if (location.getRank() == rankDisambiguation) {
                        return p;
                    } else {
                        // If we're not, continue.
                        continue;
                    }
                }
                // If there's no disambiguation at all, we return p.
                return p;
            }
        }
        return null;
    }

    /**
     * Sets piece on the board at the specified square.
     * @param s Square to set piece at.
     * @param p Piece.
     */
    public void setPieceAt(Square s, Piece p) {
        this.pieces[s.getX()][s.getY()] = p;
        if (p!= null) p.setLocation(s);
    }

    /**
     * Gets the piece on the board at the specified square.
     * @param s Square to get piece at.
     * @return Piece at square.
     */
    public Piece getPieceAt(Square s) {
        return this.pieces[s.getX()][s.getY()];
    }

    /**
     * Checks if there is a piece between the two squares.
     * @param origin Origin square.
     * @param destination Destination square.
     * @return True if the path is blocked. False if not.
     */
    public boolean isBlocked(Square origin, Square destination) {
        int xDisplacement = origin.getXDisplacement(destination);
        int yDisplacement = origin.getYDisplacement(destination);

        // Along a rank
        if (xDisplacement == 0 && yDisplacement != 0) {
            int x = origin.getX();
            if (yDisplacement > 0) {
                for (int y = origin.getY() + 1; y < destination.getY(); y++) {
                    if (this.getPieceAt(new Square(x, y)) != null) {
                        return true;
                    }
                }
            } else {
                for (int y = origin.getY() - 1; y > destination.getY(); y--) {
                    if (this.getPieceAt(new Square(x, y)) != null) {
                        return true;
                    }
                }
            }
            return false;
        }

        // Along a file
        if (yDisplacement == 0 && xDisplacement != 0) {
            int y = origin.getY();
            if (xDisplacement > 0) {
                for (int x = origin.getX() + 1; x < destination.getX(); x++) {
                    if (this.getPieceAt(new Square(x, y)) != null) {
                        return true;
                    }
                }
            } else {
                for (int x = origin.getX() - 1; x > destination.getX(); x--) {
                    if (this.getPieceAt(new Square(x, y)) != null) {
                        return true;
                    }
                }
            }
            return false;
        }

        // Diagonal
        if (xDisplacement > 0) {
            if (yDisplacement > 0) {
                for (int c = 1; c < xDisplacement; c++) {
                    int x = origin.getX() + c;
                    int y = origin.getY() + c;
                    if (this.getPieceAt(new Square(x, y)) != null) {
                        return true;
                    }
                }
            } else {
                for (int c = 1; c < xDisplacement; c++) {
                    int x = origin.getX() + c;
                    int y = origin.getY() - c;
                    if (this.getPieceAt(new Square(x, y)) != null) {
                        return true;
                    }
                }
            }
        } else {
            if (yDisplacement > 0) {
                for (int c = 1; c < Math.abs(xDisplacement); c++) {
                    int x = origin.getX() - c;
                    int y = origin.getY() + c;
                    if (this.getPieceAt(new Square(x, y)) != null) {
                        return true;
                    }
                }
            } else {
                for (int c = 1; c < Math.abs(xDisplacement); c++) {
                    int x = origin.getX() - c;
                    int y = origin.getY() - c;
                    if (this.getPieceAt(new Square(x, y)) != null) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

}
