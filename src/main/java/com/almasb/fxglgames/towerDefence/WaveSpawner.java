package com.almasb.fxglgames.towerDefence;

import com.almasb.fxgl.dsl.components.WaypointMoveComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.util.Duration;
import java.io.InputStream;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameWorld;
import static com.almasb.fxgl.dsl.FXGLForKtKt.spawn;

/**
 * Every level will need a WaveSpawner object managing the spawning of waves of enemy entities.
 * @author koda koziol
 */
public class WaveSpawner {
    final SpawnData enemySpawnData;
    private List<WaveData> waveDataLevelList;
    private int waveIndex = 0;

    /**
     * @return true if every wave for this level has been spawned.
     */
    public Boolean IsEveryWaveSpawned(){return waveIndex>=waveDataLevelList.size();}

    /**
     * WaveSpawner objects manage the spawning of waves of enemy entities.
     * @param enemySpawnData            Must include the key-value: "waypoints" - List of Point2D
     * @param waveDataLevelListFileName The name of the json file at the path /assets/levels/waveDataLists/
     */
    WaveSpawner(SpawnData enemySpawnData, String waveDataLevelListFileName){
        this.enemySpawnData = enemySpawnData;
        try{
            InputStream stream = getAssetLoader().getStream("/assets/levels/waveDataLists/" + waveDataLevelListFileName);
            waveDataLevelList = new ObjectMapper().readValue(stream, new TypeReference<>(){});
        }
        catch (Exception e) {
            //Failed to find or parse data, not sure how to handle it right now.
            //Seems like it would be a game-breaking problem.
            System.out.println(e.getMessage());
        }
    }

    /**
     * Spawns the next wave of enemies. Will throw an exception if called after the last wave has already been spawned.
     */
    public void spawnNextWave(){
        if(waveIndex >= waveDataLevelList.size())
            throw new IndexOutOfBoundsException("Last wave for this level has already been spawned");

        spawnWave(waveDataLevelList.get(waveIndex));
        waveIndex++;
    }

    /**
     * @return true if any enemy has reached the end of the path, false otherwise.
     */
    public boolean hasAnEnemyReachedTheEnd(){//TODO
        List<Entity> scrubs = getGameWorld().getEntitiesByType(TowerDefenceApp.Type.ENEMY);
        for (Entity enemy: scrubs) {
            if (enemy.getComponent(WaypointMoveComponent.class).atDestinationProperty().get())
                return true;
        }
        return false;
    }

    /**
     * @return true if every enemy in the current wave has been destroyed.
     */
    public boolean isWaveDefeated(){//TODO
        return false;
    }

    /**
     * Begin regularly spawning a wave of enemies according to the passed waveData object.
     * @param waveData Includes the enemyQueue, spawnsPerQueueEntry, and deltaSpawnInMilliseconds.
     */
    private void spawnWave(WaveData waveData)
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
