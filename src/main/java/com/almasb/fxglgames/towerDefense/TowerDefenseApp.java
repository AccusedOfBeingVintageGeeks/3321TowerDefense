/*
 * FXGL - JavaFX Game Library. The MIT License (MIT).
 * Copyright (c) AlmasB (almaslvl@gmail.com).
 * See LICENSE for details.
 */

package com.almasb.fxglgames.towerDefense;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.GameView;
import com.almasb.fxgl.dsl.components.WaypointMoveComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.animation.Interpolator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;

public class TowerDefenseApp extends GameApplication {

    public static final String MONEY = "money";
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
    Entity towerEntity;
    ReadyUINode readyUINode;
    CurrencySymbol currencySymbol;
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
        settings.setVersion("0.04");
        settings.setWidth(WINDOW_WIDTH);
        settings.setHeight(WINDOW_HEIGHT);
        settings.setGameMenuEnabled(true);
        settings.setMainMenuEnabled(true);
        settings.getCSSList().add("main.css");

        settings.setFullScreenAllowed(true);
        settings.setFullScreenFromStart(true);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put(MONEY,0);
        //I believe this is important for saving state, but I need to do more research.
    }


    @Override
    protected void initInput() {
        Input input = getInput();

        UserAction drag = new UserAction("Drag") {//For drag and drop
            boolean dragging = false;
            Entity draggedEntity;
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
                        //Point2D initPoint = new Point2D(getAppWidth() - testTDLevelMap.TileSize,getAppHeight() * 0.4);
                        //draggedEntity.setAnchoredPosition(initPoint);
                        draggedEntity.removeFromWorld();
                        draggedEntity.getComponent(TowerComponent.class).deleteTowerInfo();
                        //draggedEntity.getComponent(TowerComponent.class).rotateUp();
                    }
                }
            }
        };
        input.addAction(drag, MouseButton.PRIMARY);
    }
    private TowerMenuBox towerMenuBox;
    private void loadTowers(){
        String towerSpecifications = "towerdata.json";
        try {
            InputStream stream = getAssetLoader().getStream("/assets/towerdata/" + towerSpecifications);
            dataForTowers = new ObjectMapper().readValue(stream, new TypeReference<List<DataForTower>>(){});
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    protected void initGame() {
        Rectangle background = new Rectangle(WINDOW_WIDTH - TILE_SIZE*2, 0,TILE_SIZE*2,getAppHeight());
        background.setFill(Color.gray(0.2));
        getGameScene().addGameView(new GameView(background,Layer.SHORT.ZIndex));

        getGameWorld().addEntityFactory(new Factory());
        setLevelFromMap("tmx/FirstTilemap.tmx");//Level entities must be spawned AFTER setting the level
        testTDLevelMap = new TDLevelMap(45,22,16);
        loadTowers();
        towerMenuBox = new TowerMenuBox(dataForTowers);
        towerMenuBox.setTranslateX(getAppWidth() - testTDLevelMap.TileSize * 3f/2 - 12);
        towerMenuBox.setTranslateY(0.2 * getAppHeight());

        SpawnData enemySpawnData = new SpawnData();
        enemySpawnData.put("waypoints", testTDLevelMap.PathPoints);
        waveManager = new WaveManager(enemySpawnData, "firstWavesDataLevelList.json");
    }
    @Override
    protected void initUI() {
        addUINode(towerMenuBox);
        currencySymbol = new CurrencySymbol();
        addUINode(currencySymbol,getAppWidth() - testTDLevelMap.TileSize * 3f/2 - 12,0.01 * getAppHeight());
        EventHandler<ActionEvent> readyClicked = event -> {
            List<Entity> currentlySpawnedEnemies = getGameWorld().getEntitiesByType(TowerDefenseApp.Type.ENEMY);
            if(currentlySpawnedEnemies.size() == 0 && !waveManager.areAllWavesSpawned())
                waveManager.spawnNextWave();
        };

        readyUINode = new ReadyUINode(TILE_SIZE*2, TILE_SIZE,readyClicked);
        addUINode(readyUINode,WINDOW_WIDTH - TILE_SIZE*2,WINDOW_HEIGHT - TILE_SIZE);
    }

    @Override
    protected void onUpdate(double tpf) {
        List<Entity> towers = getGameWorld().getEntitiesByType(Type.TOWER);
        for(Entity tower: towers ) {
            tower.getComponent(TowerComponent.class).initializeTowerInfo();
        }
        List<Entity> currentlySpawnedEnemies = getGameWorld().getEntitiesByType(Type.ENEMY);

        //Defeat?
        for (Entity enemy: currentlySpawnedEnemies) {
            if(enemy.getComponent(WaypointMoveComponent.class).atDestinationProperty().get()) {
                // An enemy has made it to the end.
                getDialogService().showMessageBox("GAME OVER", () -> {
                    //Do something when player clicks 'OK', like go back to the main menu
                    getGameController().gotoMainMenu();
                });
            }
        }
        //Victory?
        if(waveManager.areAllWavesSpawned() && currentlySpawnedEnemies.size() == 0) {
            getDialogService().showMessageBox("VICTORY", ()->{
                //Do something when player clicks 'OK', like go back to the main menu
                getGameController().gotoMainMenu();
            });
        }
        //Update UI
        readyUINode.update(
                currentlySpawnedEnemies.size() == 0,
                !waveManager.isActivelySpawning(),
                waveManager.getSecondsToNextWave(),
                false //TODO
        );

    }
    public void onTowerSelection(DataForTower towerData){
        towerEntity = spawnWithScale("tower",
                new SpawnData(getInput().getMousePositionWorld().getX()-TILE_SIZE/2,
                        getInput().getMousePositionWorld().getY()-TILE_SIZE/2).put("dataForTower",towerData),
                Duration.seconds(0),
                Interpolator.DISCRETE);
    }

    public void onTowerSell(DataForTower data, TowerComponent tower){
        //data.cost(); increase amount of money by about a third of tower cost
        IndexPair tileIndices = testTDLevelMap.getTileIndexFromPoint(tower.getEntity().getPosition());
        tower.deleteTowerInfo();
        tower.getEntity().getAnchoredPosition(testTDLevelMap.getTilePositionCenter(tileIndices));
        testTDLevelMap.setTileAvailability(true, tileIndices);
        tower.getEntity().removeFromWorld();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
