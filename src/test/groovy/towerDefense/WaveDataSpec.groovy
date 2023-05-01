package towerDefense

import com.almasb.fxglgames.towerDefense.TowerDefenseApp
import com.almasb.fxglgames.towerDefense.WaveData
import spock.lang.Specification

class WaveDataSpec extends Specification {
    def "test enemyQueue"() {
        given:

        TowerDefenseApp.EnemyType[] queue = [TowerDefenseApp.EnemyType.scrub]
        TowerDefenseApp.EnemyType[] test
        WaveData data = new WaveData(queue, 1,1,1)
        when:
        test = data.enemyQueue()
        then:
        test == queue
    }

    def "test spawnsPerQueueEntry"() {
        given:
        int spawns = 1
        int test
        TowerDefenseApp.EnemyType[] queue = [TowerDefenseApp.EnemyType.scrub]
        WaveData data = new WaveData(queue , spawns,1,1)

        when:
        test = data.spawnsPerQueueEntry()
        then:
        test == spawns
    }

    def "test deltaSpawnInMilliseconds"() {
        given:
        int millis = 1
        int test
        TowerDefenseApp.EnemyType[] queue = [TowerDefenseApp.EnemyType.scrub]
        WaveData data = new WaveData(queue, 1,millis,1)

        when:
        test = data.deltaSpawnInMilliseconds()
        then:
        test == millis
    }

    def "test funding"() {
        given:
        int funds = 1
        int test
        TowerDefenseApp.EnemyType[] queue = [TowerDefenseApp.EnemyType.scrub]

        WaveData data = new WaveData(queue, 1,1,funds)

        when:
        test = data.funding()
        then:
        test == funds
    }
}
