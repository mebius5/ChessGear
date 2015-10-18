package com.chessgear.game;

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
    private boolean canWhiteCastleKingside;

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
        this.canWhiteCastleKingside = true;
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
        return "";// TODO
    }

    /**
     * Returns the new boardstate after a move was done to it.
     * NOTE: This boardstate remains unchanged! We do not do a modification in place!
     * We clone the boardstate, then do a new move.
     * @param m Move to execute.
     * @return
     */
    public BoardState doMove(Move m) {
        // TODO
        return null;
    }

    /**
     * Clone constructor.
     * @return A deep copy of this object.
     */
    public BoardState clone() {
        BoardState cloneBoardState = new BoardState();
        for (int c = 0; c < 8; c++) {
            for (int d = 0; d < 8; d++) {
                cloneBoardState.pieces[c][d] = this.pieces[c][d].clone();
            }
        }
        cloneBoardState.canBlackCastleKingSide = this.canBlackCastleKingSide;
        cloneBoardState.canBlackCastleQueenSide = this.canBlackCastleQueenSide;
        cloneBoardState.canWhiteCastleQueenSide = this.canWhiteCastleQueenSide;
        cloneBoardState.canWhiteCastleKingside = this.canWhiteCastleKingside;
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
    List<Piece> getAllPiecesOfType(Player player, PieceType type) {

        List<Piece> result = new ArrayList<>();

        for (int c = 0; c < 8; c++) {
            for (int d = 0; d < 8; d++) {
                if (this.pieces[c][d].getOwner().equals(player) && this.pieces[c][d].getType().equals(type)) {
                    result.add(this.pieces[c][d]);
                }
            }
        }

        return result;
    }

    /**
     *
     * @param origin
     * @param target
     * @return
     */
    public boolean canMakeMove(Square origin, Square target) {
        // TODO
        // NOTE: We must check for absolute pins!
        return false;
    }
}
