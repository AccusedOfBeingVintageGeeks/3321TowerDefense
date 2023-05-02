package towerDefense

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.GameWorld
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.level.Level
import com.almasb.fxgl.entity.level.tiled.TMXLevelLoader
import com.almasb.fxglgames.towerDefense.Factory
import com.almasb.fxglgames.towerDefense.TowerDefenseApp
import com.almasb.fxglgames.towerDefense.WaveData
import com.almasb.fxglgames.towerDefense.WaveManager
import javafx.util.Duration
import spock.lang.Specification

import static com.almasb.fxgl.dsl.FXGL.inc

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

        def gSNEA_Method = waveManager.getClass().getDeclaredMethod(
                "getSpawnNextEnemyAction",
                WaveData,
                int[],
                int[]
        )
        gSNEA_Method.setAccessible(true)

        TowerDefenseApp.EnemyType[] enemyQueue = [null, TowerDefenseApp.EnemyType.scrub, null]
        WaveData wd = new WaveData(enemyQueue, 1, 500, 25)
        int[] currentEntryIndex = [0], numConsecutiveSpawnsOfCurrentEntry = [0]
        int ogIndex = currentEntryIndex[0], ogNum = numConsecutiveSpawnsOfCurrentEntry[0]


        when:

        def actionResult = gSNEA_Method.invoke(
                waveManager,
                wd,
                currentEntryIndex,
                numConsecutiveSpawnsOfCurrentEntry)

        def thread = new Thread(actionResult as Runnable)
        thread.run()
        thread.join()


        then:

        (currentEntryIndex[0] == ogIndex + 1 //should increment since there's more enemies in the queue for this test
        && numConsecutiveSpawnsOfCurrentEntry[0] == ogNum) //should increment, but decrement again because for this test spawnsPerQueueEntry is just 1
    }


    def "areAllWavesSpawned returns correct value"() {

        given:
        def waveDataList = waveManager.getClass().getDeclaredField("waveDataList")
        waveDataList.setAccessible(true)
        List<WaveData> data = new LinkedList<WaveData>()
        data.add(new WaveData([TowerDefenseApp.EnemyType.scrub, TowerDefenseApp.EnemyType.scrub] as TowerDefenseApp.EnemyType[], 1, 1, 1))

        when:
        waveDataList.set(waveManager, data)

        then:
        !waveManager.areAllWavesSpawned()


    }

}
