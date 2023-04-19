package com.almasb.fxglgames.towerDefense;

/**
 * A pair of integers. Useful for methods dealing with 2-dimensional arrays.
 * @author koda koziol
 */
public class IndexPair {
    public int X, Y;
    IndexPair(int x, int y)
    {
        this.X = x;
        this.Y = y;
    }

    @Override
    public String toString() {
        return "(" + X + "," + Y + ")";
    }
}
