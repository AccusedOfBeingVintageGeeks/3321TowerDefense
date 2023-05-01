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

    private List<WaveData> waveDataList;
    private int currentWaveIndex = 0, countdown = WAVE_BREAK_TIME;
    private Timer waveBreakTimer = new Timer();
    private boolean isActivelySpawning;

    public boolean isActivelySpawning() {return isActivelySpawning;}
    public int getSecondsToNextWave(){return countdown;}

    /**
     * @return true iff the last enemy in the last wave has been spawned.
     */
    public Boolean areAllWavesSpawned(){return currentWaveIndex >= waveDataList.size();}

    /**
     * WaveManager objects manage the spawning of waves of enemy entities.
     * @param enemySpawnData            Must include the key-value: "waypoints" - List of Point2D
     * @param waveDataLevelListName The name of the json file at the path /assets/levels/waveDataLists/
     */
    public WaveManager(SpawnData enemySpawnData, String waveDataLevelListName){
        this.enemySpawnData = enemySpawnData;
        try{
            InputStream stream = getAssetLoader().getStream("/assets/levels/waveDataLists/" + waveDataLevelListName + ".json");
            waveDataList = new ObjectMapper().readValue(stream, new TypeReference<>(){});
        }
        catch (Exception e) {
            //Failed to find or parse data, not sure how to handle it right now.
            //Seems like it would be a game-breaking problem.
            System.out.println(e.getMessage());
        }
    }

    /**
     * Call this method to activate the WaveManager at the start of the game.
     */
    public void activate(){
        startBreakPeriod(WAVE_BREAK_TIME);
    }

    /**
     * Spawns the next wave of enemies. Will throw an exception if called after the last wave has already been spawned.
     */
    public void spawnNextWave(){
        waveBreakTimer.clear();
        if(currentWaveIndex >= waveDataList.size())
            throw new IndexOutOfBoundsException("Last wave for this level has already been spawned");

        spawnWave(waveDataList.get(currentWaveIndex));
    }

    /**
     * Begin regularly spawning a wave of enemies according to the passed waveData object.
     * @param waveData Includes the enemyQueue, spawnsPerQueueEntry, and deltaSpawnInMilliseconds.
     */
    private void spawnWave(WaveData waveData) {
        isActivelySpawning = true;

        final int[] currentEntryIndex = {0}, numConsecutiveSpawnsOfCurrentEntry = {0};
        getGameTimer().runAtInterval(
                getSpawnNextEnemyAction(waveData, currentEntryIndex, numConsecutiveSpawnsOfCurrentEntry),
                Duration.millis(waveData.deltaSpawnInMilliseconds()),
                waveData.enemyQueue().length * waveData.spawnsPerQueueEntry()
        );
    }

    /**
     * This method returns a runnable that will perform the next enemy spawn.
     * @param waveData                              The data object containing the number and types of enemies in this specific wave.
     * @param currentEntryIndex                     Which entry are we spawning?
     * @param numConsecutiveSpawnsOfCurrentEntry    How many times has this particular WaveData enemy entry been spawned already?
     * @return                                      A runnable that will perform the next enemy spawn.
     */
    private Runnable getSpawnNextEnemyAction(WaveData waveData, int[] currentEntryIndex, int[] numConsecutiveSpawnsOfCurrentEntry){
        return ()->{
            TowerDefenseApp.EnemyType nextEnemyType = waveData.enemyQueue()[currentEntryIndex[0]];

            if(nextEnemyType != null)
                spawnEnemy(nextEnemyType.name());

            numConsecutiveSpawnsOfCurrentEntry[0]++;
            if(numConsecutiveSpawnsOfCurrentEntry[0] >= waveData.spawnsPerQueueEntry()) {
                numConsecutiveSpawnsOfCurrentEntry[0] = 0;
                currentEntryIndex[0]++;

                if(currentEntryIndex[0] == waveData.enemyQueue().length)
                    onLastSpawnInWave();
            }
        };
    }

    /**
     * This method spawns an enemy and calls it's reinitialization method (pooling only).
     * @param enemyName     This should match the name of the enemy's "Spawns()" annotation in the Factory.
     */
    private void spawnEnemy(String enemyName){
        Entity enemy = spawn(enemyName, enemySpawnData);
        Factory.reinitializeEnemy(enemy, enemySpawnData);
    }

    /**
     * This method is called once per wave, after the last enemy has been spawned
     */
    private void onLastSpawnInWave(){
        fundPlayer(waveDataList.get(currentWaveIndex).funding());
        isActivelySpawning = false;
        startBreakPeriod(WAVE_BREAK_TIME);
        currentWaveIndex++;
    }

    /**
     * Increase the players funds.
     * @param funding   How much should the player receive in funding?
     */
    private void fundPlayer(int funding){
        inc(TowerDefenseApp.MONEY, funding);
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
                        if(currentWaveIndex < waveDataList.size())
                            spawnNextWave();
                    }
                },
                Duration.seconds(1),
                durationInSec
        );
    }
}
