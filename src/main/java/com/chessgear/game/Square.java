package com.chessgear.game;

/**
 * Object representation of a square on the chessboard.
 * Created by Ran on 10/8/2015.
 */
public class Square {

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

    public boolean equals(Object o) {
        if (o instanceof Square) {
            Square other = (Square)o;
            if (this.x == other.x && this.y == other.y) return true;
        }
        return false;
    }

}
