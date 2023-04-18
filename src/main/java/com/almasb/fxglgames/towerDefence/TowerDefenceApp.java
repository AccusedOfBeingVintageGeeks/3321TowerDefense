/*
 * FXGL - JavaFX Game Library. The MIT License (MIT).
 * Copyright (c) AlmasB (almaslvl@gmail.com).
 * See LICENSE for details.
 */

package com.almasb.fxglgames.towerDefence;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.FXGLForKtKt;
import com.almasb.fxgl.dsl.components.WaypointMoveComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.animation.Interpolator;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import javafx.util.Duration;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.spawn;
// NOTE: this import above is crucial, it pulls in many useful methods

public class TowerDefenceApp extends GameApplication {


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

    private List<DataForTower> dataForTowers;
    Entity testEntity, towerEntity;
    private DataForTower towerData;

    private TowerMenuBox towerMenuBox;

    private List<String> towerNames = List.of(
            "cannon.png",
            "machinegun.png",
            "missilelauncher.png"
    );
    private void loadTowers(){
        String towerSpecifications = "towerdata.json";
        try {
            InputStream stream = FXGLForKtKt.getAssetLoader().getStream("/assets/towers/" + towerSpecifications);
            dataForTowers = new ObjectMapper().readValue(stream, new TypeReference<List<DataForTower>>(){});
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private TowerSymbol towerSymbol;
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

        UserAction drag = new UserAction("Drag") {
            //For drag and drop
            boolean dragging = false;
            Entity draggedEntity;//Maybe we should set this in onActionBegin
            @Override
            protected void onActionBegin() {
                //loop through towers, check if draggable, check if mouse is over it
                List<Entity> towerEntities = getGameWorld().getEntitiesByComponent(TowerComponent.class);
                for (Entity towerEnt : towerEntities) {
                    if(!towerEnt.getComponent(TowerComponent.class).getPlacedStatus()
                            && getInput().getMousePositionWorld().distance(towerEnt.getAnchoredPosition()) < testTDLevelMap.TileSize) {
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
                        //Point2D initPoint = new Point2D(getAppWidth() - testTDLevelMap.TileSize,getAppHeight() * 0.4);
                        //draggedEntity.setAnchoredPosition(initPoint);
                        draggedEntity.removeFromWorld();
                        //draggedEntity.getComponent(TowerComponent.class).rotateUp();
                    }
                }
            }
        };

        input.addAction(drag, MouseButton.PRIMARY);
    }
    @Override
    protected void initUI(){
        addUINode(towerMenuBox);
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new Factory());
        setLevelFromMap("tmx/FirstTilemap.tmx");//Level entities must be spawned AFTER setting the level
        testTDLevelMap = new TDLevelMap(45,22,16);
        loadTowers();
        towerMenuBox = new TowerMenuBox(dataForTowers);
        towerMenuBox.setTranslateX(getAppWidth() - testTDLevelMap.TileSize * 3f/2 - 12);
        towerMenuBox.setTranslateY(0.1 * getAppHeight());

        SpawnData enemySpawnData = new SpawnData();
        enemySpawnData.put("waypoints", testTDLevelMap.PathPoints);

        WaveSpawner waveSpawner = new WaveSpawner(enemySpawnData);

        //Need to figure out a good way to store this data
        EnemyType[] enemyQueue = new EnemyType[]{
                EnemyType.scrub,
                EnemyType.scrub,
                EnemyType.scrub,
                null,
                EnemyType.scrub,
                EnemyType.scrub,
                null,
                EnemyType.scrub,
                null,
                null,
                EnemyType.scrub
        };

        WaveData waveData = new WaveData(enemyQueue,3,500);
        waveSpawner.SpawnWave(waveData);
    }

    @Override
    protected void onUpdate(double tpf) {
        List<Entity> towers = getGameWorld().getEntitiesByType(Type.TOWER);
        for(Entity tower: towers ) {
            tower.getComponent(TowerComponent.class).initializeInfo();
        }
        List<Entity> scrubs = getGameWorld().getEntitiesByType(Type.ENEMY);
        for (Entity enemy: scrubs) {
            if(enemy.getComponent(WaypointMoveComponent.class).atDestinationProperty().get())
            {
                // An enemy has made it to the end.
                // There's probably a more efficient way of checking this...
                //towerEntity.getComponent(TowerComponent.class).onUpdate(tpf);
                getGameController().pauseEngine();
                //getDialogService().showMessageBox("GAME OVER");
                getDialogService().showMessageBox("GAME OVER", () -> {
                    //Do something when player clicks 'OK'
                });
            }
        }
    }
    public void onTowerSelection(DataForTower towerData){
        towerEntity = spawnWithScale("towerComponent",
                new SpawnData(getInput().getMousePositionWorld().getX()-5,
                        getInput().getMousePositionWorld().getY()+5).put("dataForTower",towerData),
                Duration.seconds(0),
                Interpolator.DISCRETE);
    }

    public void onTowerSell(DataForTower data){
        //data.cost(); increase amount of money by about a third of tower cost
        IndexPair tileIndices = testTDLevelMap.getTileIndexFromPoint(towerEntity.getPosition());
        towerEntity.getComponent(TowerComponent.class).deleteInfo();
        towerEntity.setAnchoredPosition(testTDLevelMap.getTilePositionCenter(tileIndices));
        testTDLevelMap.setTileAvailability(true, tileIndices);
        towerEntity.removeFromWorld();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
