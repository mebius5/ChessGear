package com.chessgear.game;

/**
 * Move class.
 * Created by Ran on 10/8/2015.
 */
public final class Move {

    /**
     * Whose move was it.
     */
    private Player whoMoved;

    /**
     * What type of piece was moved.
     */
    private PieceType pieceType;

    /**
     * Origin square.
     */
    private Square origin;

    /**
     * Destination square.
     */
    private Square destination;

    /**
     * Null if not pawn promotion, else, type of piece promoted to.
     */
    private PieceType promotionType;

    /**
     * Flag that indicates whether the move is a castling move.
     */
    private boolean castling;

    /**
     * Construct object with all attributes.
     * @param whoMoved Who moved.
     * @param pieceType Type of piece that was moved.
     * @param origin Origin square.
     * @param destination Destination square.
     * @param castling Was the move a castling move?
     * @param promotionType If pawn was promoted, what type.
     */
    public Move(Player whoMoved, PieceType pieceType, Square origin, Square destination, boolean castling, PieceType promotionType) {
        this.whoMoved = whoMoved;
        this.pieceType = pieceType;
        this.origin = origin;
        this.destination = destination;
        this.castling = castling;
        this.promotionType = promotionType;
    }

    /**
     * Player who moved.
     * @return Player who made this move.
     */
    public Player getWhoMoved() {
        return this.whoMoved;
    }

    /**
     * Flag for if this move is a castling move.
     * @return True if castling, false if not castling.
     */
    public boolean isCastling() {
        return this.castling;
    }

    /**
     * Checks if this move was a double pawn push.
     * @return True if a pawn was moved forward two spaces.
     */
    public boolean isPawnPush() {
        if (this.pieceType == PieceType.PAWN) {
            int difference = this.origin.getY() - this.destination.getY();
            if (difference == 2 || difference == -2) {
                return true;
            }
        }
        return false;
    }

    /**
     * If this move was a double pawn push, return the location of the en passant square.
     * Else returns null.
     * @return The new en passant target square.
     */
    public Square getEnPassantTarget() {
        if (this.isPawnPush()) {
            int median = (this.origin.getY() + this.destination.getY()) / 2;
            int file = this.origin.getX();
            return new Square(file, median);
        }
        return null;
    }

    /***
     * Accessor for pieceType
     * @return The piece type of the piece being moved.
     */
    public PieceType getPieceType(){
        return this.pieceType;
    }
    /**
     * Accessor for origin.
     * @return Origin square of piece being moved.
     */
    public Square getOrigin() {
        return this.origin;
    }

    /**
     * Accessor for destination.
     * @return Destination square of piece being moved.
     */
    public Square getDestination() {
        return this.destination;
    }

    /**
     * If move was a pawn promotion, this indicates what type of piece pawn was promoted to.
     * @return Type of piece pawn promoted to.
     */
    public PieceType getPromotionType() {
        return this.promotionType;
    }

    /**
     * Returns string representation of this move.
     * @return String representation of this move.
     */
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(this.getOrigin().toString());
        result.append(this.getDestination().toString());
        return result.toString();
    }

    /**
     * Checks equality of with another object.
     * @param o Object to compare to.
     * @return True if equal, eslse false.
     */
    public boolean equals(Object o) {

        if (o instanceof Move) {

            Move other = (Move)o;
            if (this.whoMoved != other.whoMoved) return false;
            if (this.pieceType != other.pieceType) return false;
            if (!this.origin.equals(other.origin)) return false;
            if (!this.destination.equals(other.destination)) return false;
            if (!this.destination.equals(other.destination)) return false;
            if (this.promotionType != null) {
                if (!this.promotionType.equals(other.promotionType)) return false;
            } else {
                if (this.promotionType != other.promotionType) return false;
            }
            if (this.castling != other.castling) return false;

            return true;
        }
        return false;

    }

}