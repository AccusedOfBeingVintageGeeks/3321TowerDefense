package com.almasb.fxglgames.towerDefence;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import javafx.util.Duration;
import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.spawn;

/**
 * Every level will need a WaveSpawner object managing the spawning of waves of enemy entities.
 * @author koda koziol
 */
public class WaveSpawner {
    SpawnData enemySpawnData;

    /**
     * WaveSpawner objects manage the spawning of waves of enemy entities.
     * @param enemySpawnData Must include the key-value: "waypoints" - List of Point2D
     */
    WaveSpawner(SpawnData enemySpawnData)
    {
        this.enemySpawnData = enemySpawnData;
    }

    /**
     * Begin regularly spawning a wave of enemies according to the passed waveData object.
     * @param waveData Includes the enemyQueue, spawnsPerQueueEntry, and deltaSpawnInMilliseconds.
     */
    public void SpawnWave(WaveData waveData)
    {
        final int[] currentEntry = {0}, consecutiveSpawnsOfCurrentEntry = {0};
        run(
                ()->{
                    //Check waveData
                    TowerDefenceApp.EnemyType nextEnemyType = waveData.enemyQueue()[currentEntry[0]];

                    if(nextEnemyType != null){
                        Entity scrubEntity = spawn(nextEnemyType.name(),enemySpawnData);
                        Factory.reinitializeScrub(scrubEntity);
                    }

                    consecutiveSpawnsOfCurrentEntry[0]++;
                    if(consecutiveSpawnsOfCurrentEntry[0] >= waveData.spawnsPerQueueEntry())
                    {
                        consecutiveSpawnsOfCurrentEntry[0] = 0;
                        currentEntry[0]++;
                    }
                },
                Duration.millis(waveData.deltaSpawnInMilliseconds()),
                waveData.enemyQueue().length * waveData.spawnsPerQueueEntry()
        );
    }
}
