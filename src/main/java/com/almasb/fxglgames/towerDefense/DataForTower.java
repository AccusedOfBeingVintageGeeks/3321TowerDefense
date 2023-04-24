package com.almasb.fxglgames.towerDefense;

import java.util.Collections;
import java.util.List;

/**
 * is used as data storage for TowerComponent class
 * @param name
 * @param imageName
 * @param cost
 * @param fireRadius
 * @param fireRate
 * @param projectileSpeed
 * @param projectileImageName
 * @param effects
 */
public record DataForTower (
            String name,
            String imageName,
            int cost,
            double fireRadius,
            double fireRate,
            int projectileSpeed,
            String projectileImageName,
            int projectileHeight,
            int projectileWidth,
            List<String> effects
    ){
        @Override
        public List<String> effects(){
            return effects != null ? effects : Collections.emptyList();
        }
    }

