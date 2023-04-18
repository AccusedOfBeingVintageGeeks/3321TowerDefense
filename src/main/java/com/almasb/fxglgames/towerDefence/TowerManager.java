package com.almasb.fxglgames.towerDefence;

import com.almasb.fxgl.dsl.FXGLForKtKt;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;

public class TowerManager {

    TowerManager(String towerSpecifications){
        try{
            InputStream stream = FXGLForKtKt.getAssetLoader().getStream("assets/towers/" + towerSpecifications);

            DataForTower data = new ObjectMapper().readValue(stream, new TypeReference<DataForTower>() {
            });


        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
