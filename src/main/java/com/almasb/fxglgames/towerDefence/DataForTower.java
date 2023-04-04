package com.almasb.fxglgames.towerDefence;

import java.util.Collections;
import java.util.List;
public class DataForTower {

    /**
     * is used as data storage for TowerComponent class
     * @param name
     * @param imageName
     * @param projectileImageName
     * @param effects
     */
    public String name;
    public String imageName;
    public int cost;
    public double fireRadius;
    public double fireRate;
    public int projectileSpeed;
    public String projectileImage;
    public record TowerData(
            String name,
            String imageName,
            int cost,
            double fireRadius,
            double fireRate,
            int projectileSpeed,
            String projectileImageName,
            List<String> effects

    ){
        @Override
        public List<String> effects(){
            return effects != null ? effects : Collections.emptyList();
        }
    }
}
