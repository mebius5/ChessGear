package com.chessgear.game;

/**
 * Object representation of a square on the chessboard.
 * Created by Ran on 10/8/2015.
 */
public final class Square {

    /**
     * Indicates file of square.
     */
    private int x;

    /**
     * Indicates rank of square.
     */
    private int y;

    /**
     * Constructs a square.
     * @param x X coordinate of square (a=0)
     * @param y Y coordinate of square (1=0)
     */
    public Square(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs a square.
     * @param square String containing location of square.
     */
    public Square(String square) {
        square = square.trim().toLowerCase();
        this.x = (int)square.charAt(0) - 'a';
        this.y = (int)square.charAt(1) - '1';

    }

    /**
     * Gets displacement from this square to the other square in the x dimension.
     * @param other Square to get displacement to.
     * @return Displacement in the x dimension.
     */
    public int getXDisplacement(Square other) {
        return other.x - this.x;
    }

    /**
     * Gets displacement from this square to other square in the y dimension.
     * @param other Square to get displacement to.
     * @return Displacement in the y dimension.
     */
    public int getYDisplacement(Square other) {
        return other.y - this.y;
    }

    /**
     * Checks if this square is on the same diagonal as another square.
     * @param other Square to check if is on the same diagonal.
     * @return True if yes, else false.
     */
    public boolean isOnDiagonal(Square other) {
        int xDisplace = this.getXDisplacement(other);
        int yDisplace = this.getYDisplacement(other);
        return Math.abs(xDisplace) == Math.abs(yDisplace);
    }

    /**
     * Checks if this square is on the same file as another square.
     * @param other Square to check if is on the same file.
     * @return True if yes, else false.
     */
    public boolean isOnSameFile(Square other) {
        int xDisplace = this.getXDisplacement(other);
        return xDisplace == 0;
    }

    /**
     * Checks if this square is on the same rank as another square.
     * @param other Square to check if is on the same rank.
     * @return True if yes, else false.
     */
    public boolean isOnSameRank(Square other) {
        int yDisplace = this.getYDisplacement(other);
        return yDisplace == 0;
    }

    /**
     * Accessor for x.
     * @return Integer representation of file.
     */
    public int getX() {
        return this.x;
    }

    /**
     * Accessor for y.
     * @return Integer representation of rank.
     */
    public int getY() {
        return this.y;
    }

    /**
     * Gets character representation of file.
     * @return File of square.
     */
    public char getFile() {
        return (char)('a' + this.x);
    }

    /**
     * Gets integer representation of rank.
     * @return Rank of square.
     */
    public int getRank() {
        return this.y + 1;
    }

    /**
     * Returns string of location.
     * @return String of the location of the square.
     */
    public String toString() {
        StringBuilder result = new StringBuilder();
        char file = this.getFile();
        result.append(file);
        result.append(this.getRank());

        return result.toString();
    }

    /**
     * Checks if the two objects are equivalent.
     * @param o Object to compare to.
     * @return True if equivalent, else false.
     */
    public boolean equals(Object o) {
        if (o instanceof Square) {
            Square other = (Square)o;
            if (this.x == other.x && this.y == other.y) return true;
        }
        if (o instanceof String) {
            Square other = new Square((String)o);
            if (this.x == other.x && this.y == other.y) return true;
        }
        return false;
    }

}
