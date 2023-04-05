package com.almasb.fxglgames.towerDefence;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.WaypointMoveComponent;
import com.almasb.fxgl.dsl.components.AutoRotationComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.texture.Texture;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGL.*;

import java.util.List;

public class Factory implements EntityFactory {
    @Spawns("testEntity")
    public Entity newTestEntity(SpawnData data)
    {
        return FXGL.entityBuilder(data)
                .viewWithBBox("purpleTestTexture.png")
                .type(TowerDefenceApp.Type.TOWER)
                .zIndex(TowerDefenceApp.Layer.STANDARD.ZIndex)
                .anchorFromCenter()
                .with(new TowerComponent(0.7,200,50))
                .build();
    }

    //creating entity of type TOWER
    @Spawns("towerComponent")
    public Entity newTestTower(SpawnData data)
    {
        Texture texture = new Texture(image("cannon.png"));
        texture.setFitHeight(45);
        texture.setFitWidth(45);
        var back = new Circle(25,25,25,Color.GRAY);
        var pane = new StackPane(back,texture);
        Entity entity = FXGL.entityBuilder(data)
                .viewWithBBox(texture)
                .type(TowerDefenceApp.Type.TOWER)
                .zIndex(1000)
                .anchorFromCenter()
                //.view(new Circle(200,Color.color(1,0,0,0.3)))
                .with(new TowerComponent(0.7,200,50))
                .build();
        //entity.setLocalAnchor(new Point2D(entity.getWidth()/2,entity.getHeight()-entity.getWidth()/2));

        entity.setLocalAnchorFromCenter();
        return entity;
    }

    /*
     *  Enemy entities
     */
    @Spawns("scrub")
    public Entity newScrub(SpawnData data)
    {
        final int SPEED = 100;

        List<Point2D> waypoints = data.get("waypoints");

        //List<Point2D>
        Entity entity = FXGL.entityBuilder(data)
                .type(TowerDefenceApp.Type.ENEMY)
                .viewWithBBox("scrub.png")
                .at(waypoints.get(0))
                .with(new WaypointMoveComponent(SPEED, waypoints))
                .build();
        //entity.setReusable(true);

        return entity;
    }
    public static void reinitializeScrub(Entity scrubEntity/*, SpawnData data*/)
    {
        // Reset every property that needs to be reset here
        // ^^^ Only call when entity.setReusable(true)

        //scrubEntity.setPosition();
        //scrubEntity.getComponent(WaypointMoveComponent.class).

        // Will need to fill this out later
    }


    @Spawns("Projectile")
    public Entity newProjectile(SpawnData data){

        Texture bullet = new Texture(image("projectile1.png"));
        bullet.setFitWidth(10);
        bullet.setFitHeight(10);
        //Node view = new Rectangle(10,10,Color.BLUE);
        //view.setRotate(90);

        Entity tower = data.get("tower");
        Entity prey = data.get("prey");
        //Point2D aim = prey.getCenter();

        return entityBuilder(data)
                .type(TowerDefenceApp.Type.PROJECTILE)
                .viewWithBBox(bullet)
                .collidable()
                .with(new TowerProjectileComponent(tower,prey))
                .with(new AutoRotationComponent())
                //.zIndex(444)
                .build();
    }

    /*
     *  TMX Tile Map entities
     */
    @Spawns("blocked_tile")
    public Entity newBlockedTile(SpawnData data)
    {
        final int
                WIDTH = data.<Integer>get("width"),
                HEIGHT = data.<Integer>get("height");

        return FXGL.entityBuilder(data)
                .bbox(BoundingShape.box(WIDTH, HEIGHT))
                .type(TowerDefenceApp.Type.BLOCKED_TILES)
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
