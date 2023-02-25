package com.almasb.fxglgames.towerDefence;

import com.almasb.fxgl.core.math.FXGLMath;
import javafx.geometry.Point2D;

import java.util.Arrays;

/**
 * For every level there will be a LevelMap object. This has all the data needed to interact with tiles
 * @author koda koziol
 */
public class LevelMap {
    public int tileSize;
    private final boolean[][] isTileFree;
    LevelMap(int tileSize, int rows, int columns)
    {
        this.tileSize = tileSize;
        this.isTileFree = new boolean[rows][columns];
        initializeMap();
    }

    private void initializeMap()
    {
        for(int c = 0; c<isTileFree.length;c++) {
            for(int r = 0; r<isTileFree[c].length;r++) {

                isTileFree[c][r] = true;

                //set tiles with blocked_tile to false here
            }
        }
    }

    /**
     * Takes a Point2D and returns the indices of the tile that contains that point.
     * @param point
     * @return IndexPair
     */
    public IndexPair getTileIndexFromPoint(Point2D point)
    {
        return new IndexPair(
                FXGLMath.floor((float)(point.getX() / tileSize)),
                FXGLMath.floor((float)(point.getY() / tileSize))
        );
    }

    /**
     * Gets the position of the indexed tile.
     * @param indexPair
     * @return
     */
    public Point2D getTilePosition(IndexPair indexPair)
    {
        return new Point2D(tileSize * indexPair.X,tileSize * indexPair.Y);
    }

    /**
     * Gets the offset position of the indexed tile.
     * @param indexPair
     * @param offsetX
     * @param offsetY
     * @return
     */
    public Point2D getTilePosition(IndexPair indexPair, float offsetX, float offsetY)
    {
        return new Point2D(tileSize * indexPair.X + offsetX,tileSize * indexPair.Y + offsetY);
    }

    /**
     * Gets the position of the indexed tile.
     * @param x
     * @param y
     * @return
     */
    public Point2D getTilePosition(int x, int y)
    {
        return new Point2D(tileSize * x,tileSize * y);
    }

    /**
     * Gets the offset position of the indexed tile.
     * @param x
     * @param y
     * @param offsetX
     * @param offsetY
     * @return
     */
    public Point2D getTilePosition(int x, int y, float offsetX, float offsetY)
    {
        return new Point2D(tileSize * x + offsetX,tileSize * y + offsetY);
    }

    /**
     * Returns true if the indexed tile is free to place a tower on.
     * @param indexPair
     * @return
     */
    public boolean isTileFree(IndexPair indexPair)
    {
        return isTileFree[indexPair.X][indexPair.Y];
    }

    /**
     * Returns true if the indexed tile is free to place a tower on.
     * @param x
     * @param y
     * @return
     */
    public boolean isTileFree(int x, int y)
    {
        return isTileFree[x][y];
    }

}
