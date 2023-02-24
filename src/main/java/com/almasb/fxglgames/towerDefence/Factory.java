package com.almasb.fxglgames.towerDefence;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.AutoRotationComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.BoundingShape;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGL.*;

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
    //creating entity of type TOWER
    @Spawns("towerComponent")
    public Entity newTestTower(SpawnData data)
    {
        Entity entity = FXGL.entityBuilder(data)
                .view(new Circle(20,Color.RED))
                .type(TowerDefenceApp.Type.TOWER)
                .anchorFromCenter()
                .with(new TowerComponent(5))
                .build();

        return entity;
    }


    @Spawns("Projectile")
    public Entity newProjectile(SpawnData data){

        Node view = new Rectangle(10,10,Color.BLUE);
        //view.setRotate(90);

        Entity tower = data.get("tower");
        Entity prey = data.get("prey");

        return entityBuilder(data)
                .type(TowerDefenceApp.Type.PROJECTILE)
                .viewWithBBox(view)
                .collidable()
                .with(new TowerProjectileComponent(tower,prey))
                .with(new AutoRotationComponent())
                //.zIndex(444)
                .build();
    }

    /*
     *  Map entities
     */
    @Spawns("blocked_tile")
    public Entity newBlockedTile(SpawnData data)
    {
        final int
                WIDTH = data.<Integer>get("width"),
                HEIGHT = data.<Integer>get("height");

        Entity entity = FXGL.entityBuilder(data)
                .bbox(BoundingShape.box(WIDTH,HEIGHT))
                .view(new Rectangle(WIDTH, HEIGHT, Color.color(1, 0, 0, 0.3)))
                .build();

        return entity;
    }
    @Spawns("path")
    public Entity newPath(SpawnData data)
    {
        return FXGL.entityBuilder(data)
                .build();
    }
}
