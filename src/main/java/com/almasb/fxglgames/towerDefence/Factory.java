package com.almasb.fxglgames.towerDefence;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.WaypointMoveComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.BoundingShape;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.List;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameWorld;

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
     *  Enemy entities
     */
    @Spawns("scrub")
    public Entity newScrub(SpawnData data)
    {
        final int SPEED = 100;

        Entity pathEntity = getGameWorld().getEntitiesByType(TowerDefenceApp.Type.PATH).get(0);
        List<Point2D> waypoints = MapTools.getPointsFromPathEntity(pathEntity);

        //List<Point2D>
        Entity entity = FXGL.entityBuilder(data)
                .type(TowerDefenceApp.Type.ENEMY)
                .view("scrub.png")
                .at(waypoints.get(0))
                .with(new WaypointMoveComponent(SPEED, waypoints))
                .build();
        entity.setReusable(true);

        return entity;
    }
    public static void reinitializeScrub(Entity scrubEntity/*, SpawnData data*/)
    {
        // Reset every property that needs to be reset here.
        //scrubEntity.setPosition();
        //scrubEntity.getComponent(WaypointMoveComponent.class).

        // Will need to fill this out later
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

        return FXGL.entityBuilder(data)
                .bbox(BoundingShape.box(WIDTH, HEIGHT))
                //.view(new Rectangle(WIDTH, HEIGHT, Color.color(1, 0, 0, 0.3))) //uncomment for debugging
                .build();
    }
    @Spawns("path")
    public Entity newPath(SpawnData data)
    {
        return FXGL.entityBuilder(data)
                .type(TowerDefenceApp.Type.PATH)
                .build();
    }
}
