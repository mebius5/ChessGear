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
     * @param x
     * @param y
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
     * Returns string of location.
     * @return
     */
    public String toString() {
        StringBuilder result = new StringBuilder();
        char file = (char)('a' + this.x);
        result.append(file);
        result.append(this.y + 1);

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
