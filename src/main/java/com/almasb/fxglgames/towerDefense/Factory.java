package com.almasb.fxglgames.towerDefense;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.WaypointMoveComponent;
import com.almasb.fxgl.dsl.components.AutoRotationComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.texture.Texture;
import javafx.beans.binding.Bindings;
import javafx.geometry.Point2D;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import static com.almasb.fxgl.dsl.FXGL.*;

import java.util.List;

public class Factory implements EntityFactory {

    //creating entity of type TOWER
    @Spawns("tower")
    public Entity newTestTower(SpawnData data)
    {
        DataForTower dataForTower = data.get("dataForTower");
        Texture texture = new Texture(image("towers/" + dataForTower.imageName()));
        texture.setFitHeight(45);
        texture.setFitWidth(45);

        var back = new Circle(25,25,25, Color.GRAY);
        var pane = new StackPane(back,texture);
        Entity entity = FXGL.entityBuilder(data)
                .with(new TowerComponent(dataForTower))
                .viewWithBBox(texture)
                .type(TowerDefenseApp.Type.TOWER)
                .zIndex(1000)
                .anchorFromCenter()
                //.view(new Circle(dataForTower.fireRadius(),Color.color(1,0,0,0.3)))

                .build();

        //when mouse hovers above entity -> its opacity changes
        texture.opacityProperty().bind(
                Bindings.when(entity.getViewComponent().getParent().hoverProperty())
                        .then(0.7)
                        .otherwise(1)
        );

        entity.setLocalAnchorFromCenter();
        return entity;
    }

    /*
     *  Enemy entities
     */
    @Spawns("scrub")
    public Entity newScrubEnemy(SpawnData data) {
        final int SPEED = 100;
        List<Point2D> waypoints = data.get("waypoints");

        Entity entity = FXGL.entityBuilder(data)
                .type(TowerDefenseApp.Type.ENEMY)
                .viewWithBBox("enemies/scrub.png")
                .at(waypoints.get(0))
                .with(new WaypointMoveComponent(SPEED, waypoints))
                .build();
        //entity.setReusable(true);

        return entity;
    }
    @Spawns("heavy")
    public Entity newHeavyEnemy(SpawnData data) {
        final int SPEED = 50;
        List<Point2D> waypoints = data.get("waypoints");

        Entity entity = FXGL.entityBuilder(data)
                .type(TowerDefenseApp.Type.ENEMY)
                .viewWithBBox("test/redTestTexture.png")
                .at(waypoints.get(0))
                .with(new WaypointMoveComponent(SPEED, waypoints))
                .build();
        //entity.setReusable(true);

        return entity;
    }

    /**
     * Given an enemy Entity and SpawnData, this method resets the properties and components of the enemyEntity
     * necessary to perform a proper respawn.
     * NOTE: It's only necessary to call this method if the entities reusable property is set to true.
     * @param enemyEntity   The enemy that needs to be reinitialized.
     * @param data          This data must contain the "waypoints" data.
     */
    public static void reinitializeEnemy(Entity enemyEntity, SpawnData data) {
        /*
        This works 99% of the time now, but occasionally an enemy is respawned and begins moving along the path at what appears to be twice the correct speed. It's speed property is correct, and I couldn't see any problems with the WaypointMoveComponent either. I suspect this is a bug in FXGL.
         */

        List<Point2D> waypoints = data.get("waypoints");

        enemyEntity.setPosition(waypoints.get(0));
        enemyEntity.getComponent(WaypointMoveComponent.class).move(waypoints);
        //System.out.println("enemySpeed = " + enemyEntity.getComponent(WaypointMoveComponent.class).getSpeed());
    }


    @Spawns("Projectile")
    public Entity newProjectile(SpawnData data){
        String textureName = data.get("projectile");
        int speed = data.get("projectileSpeed");
        int height = data.get("height");
        int width = data.get("width");
        Entity tower = data.get("tower");
        Entity prey = data.get("prey");

        Texture bullet = new Texture(image("projectiles/" + textureName));
        bullet.setFitWidth(height);
        bullet.setFitHeight(width);

        Entity entity = entityBuilder(data)
                .type(TowerDefenseApp.Type.PROJECTILE)
                .viewWithBBox(bullet)
                .collidable()
                .with(new TowerProjectileComponent(tower,prey,speed))
                .with(new AutoRotationComponent())
                //.zIndex(444)
                .build();
        return entity;
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
                .type(TowerDefenseApp.Type.BLOCKED_TILES)
                //.view(new Rectangle(WIDTH, HEIGHT, Color.color(1, 0, 0, 0.3))) //uncomment for debugging
                .build();
    }
    @Spawns("path")
    public Entity newPath(SpawnData data)
    {
        return FXGL.entityBuilder(data)
                .type(TowerDefenseApp.Type.PATH)
                .build();
    }
}
