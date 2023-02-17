package com.almasb.fxglgames.towerDefence;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Factory implements EntityFactory {
    @Spawns("testEntity")
    public Entity newTestEntity(SpawnData data)
    {
        Entity entity = FXGL.entityBuilder(data)
                .view(new Circle(20, Color.GREEN))
                .type(TowerDefenceApp.Type.TEST)//Types are defined in TowerDefenceApp.java
                .anchorFromCenter()
                //.zIndex(999)
                .with(new TestEntityComponent(5))
                .build();

        return entity;
    }

    /*
     *  Map entities
     */
    @Spawns("blocked_tile")
    public Entity newBlockedTile(SpawnData data)
    {
        return FXGL.entityBuilder(data)
                .build();
    }
    @Spawns("path")
    public Entity newPath(SpawnData data)
    {
        return FXGL.entityBuilder(data)
                .build();
    }
}
