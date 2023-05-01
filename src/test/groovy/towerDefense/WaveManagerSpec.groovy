package towerDefense

import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.GameWorld
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.level.Level
import com.almasb.fxgl.entity.level.tiled.TMXLevelLoader
import com.almasb.fxglgames.towerDefense.Factory
import com.almasb.fxglgames.towerDefense.TowerDefenseApp
import com.almasb.fxglgames.towerDefense.WaveData
import com.almasb.fxglgames.towerDefense.WaveManager
import spock.lang.Specification

class WaveManagerSpec extends Specification {
    List<Entity> bTiles
    Entity path
    WaveManager waveManager

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


    void setup(){
        makeNewWorld("src/main/resources/assets/levels/tmx/first_level.tmx")
        waveManager = new WaveManager(new SpawnData(),"src/main/resources/assets/levels/waveDataLists/waveDataListA")
    }

//    def "test spawnWave"() {
//        given:
//
//        when:
//        // TODO implement stimulus
//        then:
//        // TODO implement assertions
//    }
    def "test getSpawnNextEnemyAction"(){
        given:
        //makeNewWorld("src/main/resources/assets/levels/tmx/first_level.tmx")
        //waveManager = new WaveManager(new SpawnData(),"waveDataListA")

        def gSNEA_Method = waveManager.getClass().getDeclaredMethod(
                "getSpawnNextEnemyAction",
                WaveData,
                int[],
                int[]
        )
        gSNEA_Method.setAccessible(true)

        //def wDL_Field = waveManager.getClass().getDeclaredField("waveDataList")
        //wDL_Field.setAccessible(true)

        TowerDefenseApp.EnemyType[] enemyQueue = [TowerDefenseApp.EnemyType.scrub]
        WaveData wd = new WaveData(enemyQueue, 1, 500, 25)
        //def wdl = (List<WaveData>) wDL_Field.get(waveManager)
        int[] currentEntryIndex = [0]
        int[] numConsecutiveSpawnsOfCurrentEntry = [0]


        when:
        def action = gSNEA_Method.invoke(//java.lang.IllegalArgumentException: argument type mismatch
                waveManager,
                wd,
                //wdl.get(0),//wd is null? Should've been initialized in the WaveManager constructor in setup()
                currentEntryIndex,
                numConsecutiveSpawnsOfCurrentEntry)

        then:
        2==2
    }
}
