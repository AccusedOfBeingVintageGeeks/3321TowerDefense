package com.almasb.fxglgames.towerDefense;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.time.Timer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.util.Duration;
import java.io.InputStream;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * Every level will need a WaveManager object managing the spawning of waves of enemy entities.
 * @author koda koziol
 */
public class WaveManager {
    private final SpawnData enemySpawnData;
    final int WAVE_BREAK_TIME = 45;

    private List<WaveData> waveDataLevelList;
    private int currentWaveIndex = 0, countdown = WAVE_BREAK_TIME;
    private Timer waveBreakTimer = new Timer();
    private boolean isActivelySpawning;

    public boolean isActivelySpawning() {return isActivelySpawning;}
    public int getSecondsToNextWave(){return countdown;}

    /**
     * @return true iff the last enemy in the last wave has been spawned.
     */
    public Boolean areAllWavesSpawned(){return currentWaveIndex >= waveDataLevelList.size();}

    /**
     * WaveManager objects manage the spawning of waves of enemy entities.
     * @param enemySpawnData            Must include the key-value: "waypoints" - List of Point2D
     * @param waveDataLevelListFileName The name of the json file at the path /assets/levels/waveDataLists/
     */
    WaveManager(SpawnData enemySpawnData, String waveDataLevelListFileName){
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
        startBreakPeriod(WAVE_BREAK_TIME);
    }

    /**
     * Spawns the next wave of enemies. Will throw an exception if called after the last wave has already been spawned.
     */
    public void spawnNextWave(){
        waveBreakTimer.clear();
        if(currentWaveIndex >= waveDataLevelList.size())
            throw new IndexOutOfBoundsException("Last wave for this level has already been spawned");

        spawnWave(waveDataLevelList.get(currentWaveIndex));
    }

    /**
     * Begin regularly spawning a wave of enemies according to the passed waveData object.
     * @param waveData Includes the enemyQueue, spawnsPerQueueEntry, and deltaSpawnInMilliseconds.
     */
    private void spawnWave(WaveData waveData)
    {
        isActivelySpawning = true;

        final int[] currentEntryIndex = {0}, numConsecutiveSpawnsOfCurrentEntry = {0};
        getGameTimer().runAtInterval(
                ()->{
                        //Check waveData
                        TowerDefenseApp.EnemyType nextEnemyType = waveData.enemyQueue()[currentEntryIndex[0]];

                        if(nextEnemyType != null){
                            Entity enemy = spawn(nextEnemyType.name(),enemySpawnData);
                            Factory.reinitializeEnemy(enemy);
                        }

                        numConsecutiveSpawnsOfCurrentEntry[0]++;
                        if(numConsecutiveSpawnsOfCurrentEntry[0] >= waveData.spawnsPerQueueEntry())
                        {
                            numConsecutiveSpawnsOfCurrentEntry[0] = 0;
                            currentEntryIndex[0]++;

                            if(currentEntryIndex[0] == waveData.enemyQueue().length)// if we just spawned the last enemyEntry
                            {
                                isActivelySpawning = false;
                                startBreakPeriod(WAVE_BREAK_TIME);
                                currentWaveIndex++;
                            }

                        }
                    },
                Duration.millis(waveData.deltaSpawnInMilliseconds()),
                waveData.enemyQueue().length * waveData.spawnsPerQueueEntry()
        );
    }

    /**
     * This method starts or restarts the waveBreakTimer.
     * @param durationInSec An integer, how long should the next break be?
     */
    private void startBreakPeriod(int durationInSec){
        waveBreakTimer.clear();
        waveBreakTimer = getGameTimer();

        waveBreakTimer.runAtInterval(
                ()->{
                    if(countdown > 1)
                        countdown--;
                    else {
                        countdown = durationInSec;
                        if(currentWaveIndex <waveDataLevelList.size())
                            spawnNextWave();
                    }
                },
                Duration.seconds(1),
                durationInSec
        );
    }
}
