/*
 * FXGL - JavaFX Game Library. The MIT License (MIT).
 * Copyright (c) AlmasB (almaslvl@gmail.com).
 * See LICENSE for details.
 */

package com.almasb.fxglgames.towerDefence;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.GameView;
import com.almasb.fxgl.dsl.components.WaypointMoveComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;

public class TowerDefenseApp extends GameApplication {

    /**
     * Types of entities in this game. May be assigned to entities in the factory with the .type() method.
     */
    public enum Type {
        // If we give Entities a Type we can reference entities of that type. Optional
        TOWER, ENEMY, PROJECTILE, PATH, BLOCKED_TILES, TEST
    }

    /**
     * Types of enemies in this game.
     * Do not assign to entities in the factory, instead add a new entry to this enum with the same name
     * as the one passed to the factory @Spawns interface.
     */
    public enum EnemyType {
        scrub
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
    ReadyUINode readyUINode;
    TDLevelMap testTDLevelMap;
    WaveManager waveManager;

    final int
            WINDOW_WIDTH = 1080,
            WINDOW_HEIGHT = 720,
            TILE_SIZE = 45;

    @Override
    protected void initSettings(GameSettings settings) {
        // initialize common game / window settings.
        settings.setTitle("Tower Defense");
        settings.setVersion("0.01");
        settings.setWidth(WINDOW_WIDTH);
        settings.setHeight(WINDOW_HEIGHT);
        settings.setGameMenuEnabled(true);
        //settings.setMainMenuEnabled(true);
        settings.getCSSList().add("main.css");

        //settings.setFullScreenAllowed(true);
        //settings.setFullScreenFromStart(true);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        //I believe this is important for saving state, but I need to do more research.
    }


    @Override
    protected void initInput() {
        Input input = getInput();

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

            /*
             * let go of mousebutton -> Tower returns to menu bar
             */
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
                        //draggedEntity.getComponent(TowerComponent.class).rotateUp();
                    }
                }
            }
        };

        input.addAction(drag, MouseButton.PRIMARY);
    }

    @Override
    protected void initGame() {
        Rectangle background = new Rectangle(WINDOW_WIDTH - TILE_SIZE*2, 0,TILE_SIZE*2,getAppHeight());
        background.setFill(Color.gray(0.2));
        getGameScene().addGameView(new GameView(background,Layer.SHORT.ZIndex));

        getGameWorld().addEntityFactory(new Factory());
        setLevelFromMap("tmx/FirstTilemap.tmx");        //Level entities must be spawned AFTER setting the level

        testTDLevelMap = new TDLevelMap(TILE_SIZE,22,16);
        towerEntity = spawn("towerComponent",getAppWidth() - testTDLevelMap.TileSize * 3f/2, 0.6 * getAppHeight());
        testEntity = spawn("testEntity", getAppWidth()- testTDLevelMap.TileSize * 3f/2,0.5 * getAppHeight());

        SpawnData enemySpawnData = new SpawnData();
        enemySpawnData.put("waypoints", testTDLevelMap.PathPoints);
        waveManager = new WaveManager(enemySpawnData, "firstWavesDataLevelList.json");
    }
    @Override
    protected void initUI() {
        EventHandler<ActionEvent> readyClicked = event -> {
            if(waveManager.remainingEnemies() == 0 && !waveManager.isEveryWaveSpawned())
                waveManager.spawnNextWave();
        };
        readyUINode = new ReadyUINode(TILE_SIZE*2, TILE_SIZE,readyClicked);


        addUINode(readyUINode,WINDOW_WIDTH - TILE_SIZE*2,WINDOW_HEIGHT - TILE_SIZE);
    }

    @Override
    protected void onUpdate(double tpf) {
        List<Entity> scrubs = getGameWorld().getEntitiesByType(Type.ENEMY);
        for (Entity enemy: scrubs) {
            if(enemy.getComponent(WaypointMoveComponent.class).atDestinationProperty().get()) {
                // An enemy has made it to the end.
                // There's probably a more efficient way of checking this...
                getGameController().pauseEngine();
                //getDialogService().showMessageBox("GAME OVER");
                getDialogService().showMessageBox("GAME OVER", () -> {
                    //Do something when player clicks 'OK', like go back to the main menu
                });
            }
        }
        if(!waveManager.isActivelySpawning()) {
            readyUINode.setCountdownText(waveManager.getSecondsToNextWave());
            if(waveManager.remainingEnemies() == 0)
                readyUINode.setButtonClickable(true);
        }
        else {
            readyUINode.setBlankText();
            readyUINode.setButtonClickable(false);
        }

        //broken, may need events from WaveManager instead
        /*if(waveManager.remainingEnemies() == 0 && waveManager.isEveryWaveSpawned()) {
            getGameController().pauseEngine();
            getDialogService().showMessageBox("VICTORY", ()->{
                //Do something when player clicks 'OK', like go back to the main menu
            });
        }*/
    }

    public static void main(String[] args) {
        launch(args);
    }
}
