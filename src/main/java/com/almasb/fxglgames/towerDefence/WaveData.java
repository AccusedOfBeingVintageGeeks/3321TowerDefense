package com.almasb.fxglgames.towerDefence;

/**
 * WaveData objects hold data necessary to spawn a wave of enemies.
 * @param enemyQueue                A queue of enemies to spawn.
 * @param spawnsPerQueueEntry       Number of times an enemy will be spawned before moving on to the next entry in the enemyQueue.
 * @param deltaSpawnInMilliseconds  Time between spawns.
 */
public record WaveData(TowerDefenceApp.EnemyType[] enemyQueue, int spawnsPerQueueEntry, int deltaSpawnInMilliseconds) {

}
