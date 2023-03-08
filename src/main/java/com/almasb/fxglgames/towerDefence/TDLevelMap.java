package com.almasb.fxglgames.towerDefence;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entity;
import javafx.geometry.Point2D;
import javafx.scene.shape.Polyline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameWorld;

/**
 * For every level there will be a TDLevelMap object which will provide all the data needed for interacting with tiles.
 * @author koda koziol
 */
public class TDLevelMap {
    public int tileSize;
    public List<Point2D> pathPoints;
    private final boolean[][] isTileFree;

    /**
     * For every level there will be a TDLevelMap object which will provide all the data needed for interacting with tiles.
     * "getGameWorld().addEntityFactory()" and "setLevelFromMap()" must be called prior to calling this constructor.
     * @param tileSize      The length and width of the square tiles in pixels.
     * @param rows          The number of tiles in the vertical axis.
     * @param columns       The number of tiles in the horizontal axis.
     */
    TDLevelMap(int tileSize, int rows, int columns)
    {
        this.tileSize = tileSize;
        this.isTileFree = new boolean[rows][columns];

        initializeTileAvailability();
        initializePathPoints();
    }

    private void initializeTileAvailability()
    {
        //Set all tiles to free by default
        for (boolean[] isTileFreeRow : isTileFree)
            Arrays.fill(isTileFreeRow, true);

        //Now set blocked tiles to not free
        List <Entity> blockedEntities = getGameWorld().getEntitiesByType(TowerDefenceApp.Type.BLOCKED_TILES);
        for (Entity currentEntity : blockedEntities) {
            IndexPair currentEntityTileIndex = new IndexPair(
                    (int) currentEntity.getX() / tileSize,
                    (int) currentEntity.getY() / tileSize
            );
            IndexPair currentEntityDimensionsInTiles = new IndexPair(
                    (int) currentEntity.getWidth() / tileSize,
                    (int) currentEntity.getHeight() / tileSize
            );

            for (int c = currentEntityTileIndex.X; c < currentEntityTileIndex.X + currentEntityDimensionsInTiles.X; c++)
                for (int r = currentEntityTileIndex.Y; r < currentEntityTileIndex.Y + currentEntityDimensionsInTiles.Y; r++)
                    isTileFree[c][r] = false;
        }
    }
    private void initializePathPoints()
    {
        /*
         * Based on code by AlmasB:
         *  - https://github.com/AlmasB/FXGLGames/blob/master/TowerDefense/src/main/java/com/almasb/fxglgames/td/TowerDefenseApp.java
         *  - https://github.com/AlmasB/FXGLGames/blob/master/TowerDefense/src/main/java/com/almasb/fxglgames/td/data/Way.java
         */

        Entity pathEntity = getGameWorld().getEntitiesByType(TowerDefenceApp.Type.PATH).get(0); //gets first (only) object of type PATH
        Polyline polyline = pathEntity.getObject("polyline");
        List<Double> polylineEntries = polyline.getPoints();

        double
                offsetX = pathEntity.getX(),
                offsetY = pathEntity.getY();

        pathPoints = new ArrayList<>();

        for(int i = 0; i < polylineEntries.size(); i += 2)
        {
            double x = polylineEntries.get(i) + offsetX;
            double y = polylineEntries.get(i+1) + offsetY;
            pathPoints.add(new Point2D(x,y));
        }
    }


    /**
     * Takes a Point2D and returns the indices of the tile that contains that point.
     * @param point Some position in the level.
     * @return IndexPair of tile containing point.
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
     * @param indexPair The index of some tile.
     * @return          Point2D position
     */
    public Point2D getTilePosition(IndexPair indexPair)
    {
        return new Point2D(tileSize * indexPair.X,tileSize * indexPair.Y);
    }

    /**
     * Gets the offset position of the indexed tile.
     * @param indexPair Indices of tile
     * @param offsetX   Horizontal offset in pixels
     * @param offsetY   Vertical offset in pixels
     * @return          Position of indexed tile, plus offset
     */
    public Point2D getTilePosition(IndexPair indexPair, float offsetX, float offsetY)
    {
        return new Point2D(tileSize * indexPair.X + offsetX,tileSize * indexPair.Y + offsetY);
    }

    /**
     * Gets the position of the indexed tile.
     * @param x         Horizontal index of tile (column)
     * @param y         Vertical index of tile (row)
     * @return          Position of indexed tile
     */
    public Point2D getTilePosition(int x, int y)
    {
        return new Point2D(tileSize * x,tileSize * y);
    }

    /**
     * Gets the offset position of the indexed tile.
     * @param x         Horizontal index of tile (column)
     * @param y         Vertical index of tile (row)
     * @param offsetX   Horizontal offset in pixels
     * @param offsetY   Vertical offset in pixels
     * @return          Position of indexed tile, plus offset
     */
    public Point2D getTilePosition(int x, int y, float offsetX, float offsetY)
    {
        return new Point2D(tileSize * x + offsetX,tileSize * y + offsetY);
    }

    /**
     * Returns true if the indexed tile is free to place a tower on.
     * @param indexPair Indices of tile
     * @return          True if free to place, false if blocked or otherwise disallowed
     */
    public boolean isTileFree(IndexPair indexPair)
    {
        return isTileFree[indexPair.X][indexPair.Y];
    }

    /**
     * Returns true if the indexed tile is free to place a tower on.
     * @param x         Horizontal index of tile (column)
     * @param y         Vertical index of tile (row)
     * @return          True if free to place, false if blocked or otherwise disallowed
     */
    public boolean isTileFree(int x, int y)
    {
        return isTileFree[x][y];
    }

}
