package com.almasb.fxglgames.towerDefence;

import java.util.Collections;
import java.util.List;
public class DataForTower {

    public record TowerData(
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
