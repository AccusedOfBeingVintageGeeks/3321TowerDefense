package com.almasb.fxglgames.towerDefense;

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
    public record TowerData( //will be implemented later on
            String name,
            String imageName,
            String projectileImageName,
            List<String> effects
    ){
        @Override
        public List<String> effects(){
            return effects != null ? effects : Collections.emptyList();
        }
    }
}
