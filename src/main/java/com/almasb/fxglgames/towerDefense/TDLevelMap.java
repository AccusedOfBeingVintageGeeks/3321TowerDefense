package com.almasb.fxglgames.towerDefense;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxglgames.towerDefense.IndexPair;
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
    public int TileSize, NumRows, NumColumns;
    private List<Point2D> pathPoints;
    public List<Point2D> getPathPoints(){return pathPoints;}
    private final boolean[][] isTileAvailable;
    List <Entity> blockedEntities;
    Entity pathEntity;

    /**
     * For every level there will be a TDLevelMap object which will provide all the data needed for interacting with tiles.
     * "getGameWorld().addEntityFactory()" and "setLevelFromMap()" must be called prior to calling this constructor.
     * @param tileSize      The length and width of the square tiles in pixels.
     * @param numRows          The number of tiles in the vertical axis.
     * @param numColumns       The number of tiles in the horizontal axis.
     */
    public TDLevelMap(int tileSize, int numColumns, int numRows, List<Entity> blockedEntities, Entity pathEntity) {
        this.blockedEntities = blockedEntities;
        this.pathEntity = pathEntity;
        TileSize = tileSize;
        NumColumns = numColumns;
        NumRows = numRows;
        isTileAvailable = new boolean[numColumns][numRows];

        initializeTileAvailability();
        initializePathPoints();
    }

    /**
     * This method initializes tile availability from the dimensions of BLOCKED_TILES type entities.
     */
    private void initializeTileAvailability() {
        //Set all tiles to free by default
        for (boolean[] isTileFreeRow : isTileAvailable)
            Arrays.fill(isTileFreeRow, true);

        //Now set blocked tiles to not free
        for (Entity currentEntity : blockedEntities) {
            IndexPair currentEntityTileIndex = new IndexPair(
                    (int) currentEntity.getX() / TileSize,
                    (int) currentEntity.getY() / TileSize
            );
            IndexPair currentEntityDimensionsInTiles = new IndexPair(
                    (int) currentEntity.getWidth() / TileSize,
                    (int) currentEntity.getHeight() / TileSize
            );

            for (int c = currentEntityTileIndex.X; c < currentEntityTileIndex.X + currentEntityDimensionsInTiles.X; c++)
                for (int r = currentEntityTileIndex.Y; r < currentEntityTileIndex.Y + currentEntityDimensionsInTiles.Y; r++)
                    isTileAvailable[c][r] = false;
        }
    }

    /**
     * This method initializes path points from the points in the path entity's polyline object.
     * Based on code by AlmasB:
     *  <a href="https://github.com/AlmasB/FXGLGames/blob/master/TowerDefense/src/main/java/com/almasb/fxglgames/td/TowerDefenseApp.java">TowerDefenseApp.java</a>,
     *  <a href="https://github.com/AlmasB/FXGLGames/blob/master/TowerDefense/src/main/java/com/almasb/fxglgames/td/data/Way.java">Way.java</a>
     *
     *  An explanation of how this code works:
     *  In the resources/assets/levels.tmx/(whatever tile map you're using).tmx there will be one object of class "path" at a specific location
     *  that also contains polyline information. Using the polyline information in there (where the information is stored as (x,y x,y) relative
     *  to the position of the first path object) the rest of the PathPoints are created based on the postitions of the polyline points taken from
     *  the tmx file.
     */
    private void initializePathPoints() {
        Polyline polyline = pathEntity.getObject("polyline");
        List<Double> polylineEntries = polyline.getPoints();

        double
                offsetX = pathEntity.getX(),
                offsetY = pathEntity.getY();

        pathPoints = new ArrayList<>();

        for(int i = 0; i < polylineEntries.size(); i += 2) {
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
    public IndexPair getTileIndexFromPoint(Point2D point) {
        return new IndexPair(
                FXGLMath.floor((float)(point.getX()))/TileSize,
                FXGLMath.floor((float)(point.getY()))/TileSize
        );
    }

    /**
     * Gets the position from the upper-left corner of the indexed tile.
     * @param indexPair The index of some tile.
     * @return          Point2D position
     */
    public Point2D getTilePosition(IndexPair indexPair) {
        return getTilePosition(indexPair.X,indexPair.Y);
    }

    /**
     * Gets the position from the upper-left corner of the indexed tile.
     * @param column    Horizontal index of tile
     * @param row       Vertical index of tile
     * @return          Position of indexed tile
     */
    public Point2D getTilePosition(int column, int row) {
        return new Point2D(TileSize * column, TileSize * row);
    }

    /**
     * Gets the offset position from the upper-left corner of the indexed tile.
     * @param indexPair Indices of tile
     * @param offsetX   Horizontal offset in pixels
     * @param offsetY   Vertical offset in pixels
     * @return          Position of indexed tile, plus offset
     */
    public Point2D getTilePosition(IndexPair indexPair, float offsetX, float offsetY) {
        return getTilePosition(indexPair.X,indexPair.Y,offsetX,offsetY);
    }

    /**
     * Gets the offset position from the upper-left corner of the indexed tile.
     * @param column    Horizontal index of tile
     * @param row       Vertical index of tile
     * @param offsetX   Horizontal offset in pixels
     * @param offsetY   Vertical offset in pixels
     * @return          Position of indexed tile, plus offset
     */
    public Point2D getTilePosition(int column, int row, float offsetX, float offsetY) {
        return new Point2D(TileSize * column + offsetX, TileSize * row + offsetY);
    }

    /**
     * Gets the position from the center of the indexed tile.
     * @param indexPair Indices of tile
     * @return          Position of indexed tile, from center.
     */
    public Point2D getTilePositionCenter(IndexPair indexPair){
        return getTilePositionCenter(indexPair.X,indexPair.Y);
    }

    /**
     * Gets the position from the center of the indexed tile.
     * @param column    Horizontal index of tile
     * @param row       Vertical index of tile
     * @return          Position of indexed tile, from center.
     */
    public Point2D getTilePositionCenter(int column, int row){
        return getTilePosition(column,row,TileSize/2f,TileSize/2f);
    }

    /**
     * Returns true if the indexed tile is free to place a tower on.
     * Returns false if the index doesn't correspond to any tile.
     * @param indexPair Indices of tile
     * @return          True if free to place, false if blocked or otherwise disallowed
     */
    public boolean isTileAvailable(IndexPair indexPair) {
        return isTileAvailable(indexPair.X, indexPair.Y);
    }

    /**
     * Returns true if the indexed tile is free to place a tower on.
     * Returns false if the index doesn't correspond to any tile.
     * @param column    Horizontal index of tile
     * @param row       Vertical index of tile
     * @return          True if free to place, false if blocked or otherwise disallowed
     * @throws IndexOutOfBoundsException if querying outside valid range
     */
    public boolean isTileAvailable(int column, int row) throws IndexOutOfBoundsException{
        if((column>=0 && column< NumColumns) && (row>=0 && row< NumRows))
            return isTileAvailable[column][row];
        else
            throw new IndexOutOfBoundsException();
    }

    /**
     *  Sets the availability of the indexed tile.
     * @param isAvailable    Is this tile being set to free or not?
     * @param indexPair Indices of tile
     */
    public void setTileAvailability(boolean isAvailable, IndexPair indexPair) {
        setTileAvailability(isAvailable, indexPair.X, indexPair.Y);
    }

    /**
     *  Sets the availability of the indexed tile.
     * @param isAvailable    Is this tile being set to free or not?
     * @param column    Horizontal index of tile
     * @param row       Vertical index of tile
     * @throws IndexOutOfBoundsException if setting outside valid range
     */
    public void setTileAvailability(boolean isAvailable, int column, int row) throws IndexOutOfBoundsException{
        if((column>=0 && column< NumColumns) && (row>=0 && row< NumRows))
            isTileAvailable[column][row] = isAvailable;
        else throw new IndexOutOfBoundsException();
    }
}
