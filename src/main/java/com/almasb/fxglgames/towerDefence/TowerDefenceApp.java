/*
 * FXGL - JavaFX Game Library. The MIT License (MIT).
 * Copyright (c) AlmasB (almaslvl@gmail.com).
 * See LICENSE for details.
 */

package com.almasb.fxglgames.towerDefence;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.components.WaypointMoveComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.util.Duration;

import java.util.List;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.spawn;
// NOTE: this import above is crucial, it pulls in many useful methods

public class TowerDefenceApp extends GameApplication {

    /**
     * Types of entities in this game.
     */
    public enum Type {
        // If we give Entities a Type we can reference entities of that type. Optional
        TOWER, ENEMY, PROJECTILE, PATH, BLOCKED_TILES, TEST
    }

    /**
     * Standard sorting layers.
     */
    public enum Layer{
        //These MUST be ordered from lowest zIndex to highest
        GROUNDED, SHORT, STANDARD, TALL, AIRBORNE;

        //This is multiplied by 100 for edge cases where we want to have an entity between layers
        final int ZIndex = ordinal() * 100;
    }
    Entity testEntity, towerEntity;
    TDLevelMap testTDLevelMap;

    @Override
    protected void initSettings(GameSettings settings) {
        // initialize common game / window settings.
        settings.setTitle("Tower Defense");
        settings.setVersion("0.01");
        settings.setWidth(1080);
        settings.setHeight(720);
        settings.setGameMenuEnabled(true);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        //I believe this is important for saving state, but I need to do more research.
    }

    @Override
    protected void initInput() {
        //We can probably refactor later so that the UserActions below are initialized from methods in a UserActions class

        Input input = getInput();
        UserAction shootTest = new UserAction("shoot") {
            @Override
            protected void onAction(){
                towerEntity.getComponent(TowerComponent.class).shoot(testEntity);
            }
        };
        input.addAction(shootTest,KeyCode.SPACE);

        UserAction drag = new UserAction("Drag") {
            //For drag and drop
            boolean dragging = false;
            Entity draggedEntity;//Maybe we should set this in onActionBegin
            @Override
            protected void onActionBegin() {
                //loop through towers, check if draggable, check of mouse is over it
                List<Entity> towerEntities = getGameWorld().getEntitiesByComponent(TowerComponent.class);
                for (Entity towerEnt : towerEntities) {
                    if(!towerEnt.getComponent(TowerComponent.class).getPlacedStatus()
                            && getInput().getMousePositionWorld().distance(towerEnt.getAnchoredPosition()) < 0.5 * testTDLevelMap.TileSize) {
                        draggedEntity = towerEnt;
                        dragging = true;
                        break;
                    }
                }
            }

            @Override
            protected void onAction() {
                if(dragging){
                    Point2D dragPos = getInput().getMousePositionWorld();
                    draggedEntity.setAnchoredPosition(dragPos);
                }
            }

            @Override
            protected void onActionEnd() {
                if(dragging) {
                    dragging = false;

                    // Check if the tile that the mouse is positioned over is placeable.
                    IndexPair tileIndices = testTDLevelMap.getTileIndexFromPoint(getInput().getMousePositionWorld());

                    if(testTDLevelMap.isTileAvailable(tileIndices)) {
                        // Place tower on tile
                        draggedEntity.setAnchoredPosition(testTDLevelMap.getTilePositionCenter(tileIndices));
                        draggedEntity.getComponent(TowerComponent.class).setPlacedStatus(true);
                        testTDLevelMap.setTileAvailability(false, tileIndices);
                    }
                    else {
                        // Abort drag

                        // initPoint (the tower's position on the sidebar) needs to be a property of tower entities or their TowerComponent.
                        // Or maybe it gets it from the sidebar class if there will be such a thing?
                        Point2D initPoint = new Point2D(getAppWidth() - testTDLevelMap.TileSize,getAppHeight() * 0.4);
                        draggedEntity.setAnchoredPosition(initPoint);
                    }
                }
            }
        };

        input.addAction(drag, MouseButton.PRIMARY);
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new Factory());
        setLevelFromMap("tmx/FirstTilemap.tmx");        //Level entities must be spawned AFTER setting the level

        testTDLevelMap = new TDLevelMap(45,22,16);
        towerEntity = spawn("towerComponent",getAppWidth() - testTDLevelMap.TileSize * 3f/2, 0.6 * getAppHeight());
        testEntity = spawn("testEntity", getAppWidth()- testTDLevelMap.TileSize * 3f/2,0.5 * getAppHeight());
        //spawn("Projectile", FXGLMath.randomPoint(new Rectangle2D(0,0,getAppWidth(),getAppHeight())));

        //******* Refactor into WaveSpawner class later
        SpawnData scrubSpawnData = new SpawnData();
        scrubSpawnData.put("waypoints", testTDLevelMap.PathPoints);

        run(() -> {
            Entity scrubEntity = spawn("scrub",scrubSpawnData);
            Factory.reinitializeScrub(scrubEntity);
        }, Duration.millis(1000));
        //******* Refactor into WaveSpawner class later
    }

    @Override
    protected void onUpdate(double tpf) {
        List<Entity> scrubs = getGameWorld().getEntitiesByType(Type.ENEMY);
        for (Entity enemy: scrubs) {
            if(enemy.getComponent(WaypointMoveComponent.class).atDestinationProperty().get())
            {
                // An enemy has made it to the end.
                // There's probably a more efficient way of checking this...

                getGameController().pauseEngine();
                //getDialogService().showMessageBox("GAME OVER");
                getDialogService().showMessageBox("GAME OVER", () -> {
                    //Do something when player clicks 'OK'
                });
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
