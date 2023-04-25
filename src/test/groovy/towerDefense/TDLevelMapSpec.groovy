package towerDefense

import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.GameWorld
import com.almasb.fxgl.entity.level.Level
import com.almasb.fxgl.entity.level.tiled.TMXLevelLoader
import com.almasb.fxglgames.towerDefense.Factory
import com.almasb.fxglgames.towerDefense.IndexPair
import com.almasb.fxglgames.towerDefense.TDLevelMap
import com.almasb.fxglgames.towerDefense.TowerDefenseApp
import javafx.geometry.Point2D
import spock.lang.Specification


class TDLevelMapSpec extends Specification {

    TDLevelMap map
    List<Entity> bTiles
    Entity path
    static TDLevelMap myMap

    void makeNewWorld(String tmx) {
        GameWorld world = new GameWorld()
        world.addEntityFactory(new Factory())
        TMXLevelLoader loader = new TMXLevelLoader()
        File file = new File(tmx)


        Level map = loader.load(file.toURL(), world)
        world.setLevel(map)

        bTiles = world.getEntitiesByType(TowerDefenseApp.Type.BLOCKED_TILES)
        path = world.getEntitiesByType(TowerDefenseApp.Type.PATH).get(0)
    }


    void setup() {
        makeNewWorld("src/main/resources/assets/levels/tmx/FirstTilemap.tmx")
        map = new TDLevelMap(45, 22, 16, bTiles, path)
    }

    def "New map has all tiles free"() {

        when: "Make new map"

        map = new TDLevelMap(45, 22, 16, bTiles, path)

        then: "tiles are free"
        map.isTileAvailable(map.getTileIndexFromPoint(map.getTilePosition(1,1)))

    }

//   def "initializePathPoints creates PathPoints for each polylineEntry"() {
//
//       given:
//       makeNewWorld("src/main/resources/assets/levels/tmx/TestTilemap.tmx")
//       myMap = new TDLevelMap(45, 4, 4, bTiles, path)
//
//       when: "initializePathPoints"
//       //initializePathPoints is run in the TDLevelMap constructor
//       println(myMap)
//       println(map)
//
//       then: "PathPoints are the same as in the tmx"
//       pathPointCoord == tmxCoord
//
//       where:
//
//       pathPointCoord                     |     tmxCoord
//       myMap.PathPoints.get(0).getX()     |       990
//       myMap.PathPoints.get(0).getY()     |       540
//       myMap.PathPoints.get(1).getX()     |       765
//       myMap.PathPoints.get(1).getY()     |       450
//       myMap.PathPoints.get(2).getX()     |       90
//       myMap.PathPoints.get(2).getY()     |       450
//       myMap.PathPoints.get(3).getX()     |       0
//       myMap.PathPoints.get(3).getY()     |       540
//
//   }

   def "getTileIndexFromPoint returns correct IndexPair"() {
       given:
       Point2D point = new Point2D(2*map.TileSize, 2*map.TileSize)

       when: "get tile index"
       IndexPair pair = map.getTileIndexFromPoint(point)

       then: "should return the x, y index of the point"
       pair.X == 2 && pair.Y == 2
   }

    def "getTilePosition from IndexPair"() {
        given:
        IndexPair index = new IndexPair(2,2)

        when: "get tile position from index pair"
        Point2D point = map.getTilePosition(index)

        then: "tile position should be the same as the tile at that index"
        point.getX()/map.TileSize == 2 && point.getY()/map.TileSize == 2
    }

    def "getTilePosition from column, row"() {

        when: "getTilePosition of tile 2,2"
        Point2D point = map.getTilePosition(2,2)

        then: "tile position should be the same as the tile at that index"
        point.getX()/map.TileSize == 2 && point.getY()/map.TileSize == 2

    }

    def "getTilePosition from IndexPair and x,y offset"() {

        given:
        IndexPair pair = new IndexPair(2,2)

        when: "getTilePosition of tile 3,3"
        Point2D point = map.getTilePosition(pair,45.0, 45.0)

        then: "tile position should be the same as the tile at that index"
        point.getX()/map.TileSize == 3 && point.getY()/map.TileSize == 3

    }

    def "getTilePosition from column, row and x,y offset"() {

        when: "getTilePosition of tile 3,3"
        Point2D point = map.getTilePosition(2,2,45.0, 45.0)

        then: "tile position should be the same as the tile at that index"
        point.getX()/map.TileSize == 3 && point.getY()/map.TileSize == 3

    }

    def "getTilePositionCenter from IndexPair"() {

        given:
        IndexPair pair = new IndexPair(2,2)

        when: "getTilePositionFromCenter"
        Point2D point = map.getTilePositionCenter(pair)

        then: "point is center of indexed tile"
        point.getX() == (double)(2*map.TileSize) + (map.TileSize/2) && point.getY()  == (double)(2*map.TileSize) + (map.TileSize/2)

    }

    def "getTilePositionCenter from Column, row"() {

        when: "getTilePositionFromCenter"
        Point2D point = map.getTilePositionCenter(2,2)

        then: "point is center of indexed tile"
        point.getX() == (double)(2*map.TileSize) + (map.TileSize/2) && point.getY()  == (double)(2*map.TileSize) + (map.TileSize/2)

    }

    def "isTileAvailable indexPair, available"() {
        given:
        IndexPair pair = new IndexPair(2,2)
        boolean bool

        when: "check if available"
        bool = map.isTileAvailable(pair)

        then: "tile should be available"
        bool
    }

    def "isTileAvailable indexPair, unavailable"() {
        given:
        IndexPair pair = new IndexPair(2,2)
        boolean bool
        map.setTileAvailability(false, pair)

        when: "check if available"
        bool = map.isTileAvailable(pair)

        then: "tile should be unavailable"
        !bool
    }

    def "isTileAvailable column,row , available"() {
        given:
        boolean bool

        when: "check if available"
        bool = map.isTileAvailable(2,2)

        then: "tile should be available"
        bool
    }

    def "isTileAvailable column,row , unavailable"() {
        given:
        boolean bool
        map.setTileAvailability(false, 2,2)

        when: "check if available"
        bool = map.isTileAvailable(2,2)

        then: "tile should be unavailable"
        !bool
    }

    def "isTileAvailable throws IndexOutOfBoundsException"() {
        given:
        boolean bool

        when: "check availability for index out of bounds"
        bool = map.isTileAvailable(-1,-1)

        then: "should throw exception"
        thrown(IndexOutOfBoundsException)
    }

    def "setTileAvailability column, row sets availability"() {
        when: "setTileAvailability"
        map.setTileAvailability(false,1,1)
        map.setTileAvailability(true,2,2)

        then: "availability matches"
        !(map.isTileAvailable(1,1)) && map.isTileAvailable(2,2)
    }

    def "setTileAvailability indexPair sets availability"() {
        given:
        IndexPair pair11 = new IndexPair(1,1)
        IndexPair pair22 = new IndexPair(2,2)

        when: "setTileAvailability"
        map.setTileAvailability(false,pair11)
        map.setTileAvailability(true,pair22)

        then: "availability matches"
        !(map.isTileAvailable(pair11)) && map.isTileAvailable(pair22)
    }

    def "setTileAvailability throws IndexOutOfBoundsException"() {
        when: "set outside of bounds"
        map.setTileAvailability(false, -1, -1)

        then:
        thrown(IndexOutOfBoundsException)
    }

}
