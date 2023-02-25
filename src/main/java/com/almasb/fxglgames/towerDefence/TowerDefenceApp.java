/*
 * FXGL - JavaFX Game Library. The MIT License (MIT).
 * Copyright (c) AlmasB (almaslvl@gmail.com).
 * See LICENSE for details.
 */

package com.almasb.fxglgames.towerDefence;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

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
        TOWER, ENEMY, PROJECTILE, TEST
    }
    Entity testEntity;
    Entity towerEntity;
    LevelMap testLevelMap;

    @Override
    protected void initSettings(GameSettings settings) {
        // initialize common game / window settings.
        settings.setTitle("Tower Defense");
        settings.setVersion("0.01");
        settings.setWidth(1080);
        settings.setHeight(720);
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

        UserAction moveUp = new UserAction("Move Up") {
            @Override
            protected void onAction() {
                testEntity.getComponent(TestEntityComponent.class).moveUp();
                towerEntity.getComponent(TowerComponent.class).moveUp();
                //System.out.println("keydown");
            }
        };

        UserAction drag = new UserAction("Drag") {
            //For drag and drop
            boolean dragging = false;
            Entity draggedEntity;//Maybe we should set this in onActionBegin
            @Override
            protected void onActionBegin() {
                if(getInput().getMousePositionWorld().distance(testEntity.getCenter()) < 0.5 * 40) {
                    dragging = true;
                }else if(getInput().getMousePositionWorld().distance(towerEntity.getCenter()) < 0.5 * 40){
                    towerEntity.getComponent(TowerComponent.class).setDragStatus(true);
                }
            }

            @Override
            protected void onAction() {
                if(dragging){
                    testEntity.getComponent(TestEntityComponent.class).moveToPos(getInput().getMousePositionWorld());
                }else if(towerEntity.getComponent(TowerComponent.class).getDragStatus()){
                    towerEntity.getComponent(TowerComponent.class).moveToPos(getInput().getMousePositionWorld());
                }
            }

            /**
             * let go of mousebutton -> Tower returns to menu bar
             */
            @Override
            protected void onActionEnd() {
                dragging = false;

                IndexPair tileIndices = testLevelMap.getTileIndexFromPoint(getInput().getMousePositionWorld());

                if(testLevelMap.isTileFree(tileIndices)) {      //if tile(x,y) is free
                    //The circle is anchored from the center so there's an offset
                    float radiusOffset = 45f/2f;
                    Point2D snappedPos = testLevelMap.getTilePosition(tileIndices, radiusOffset, radiusOffset);
                    towerEntity.getComponent(TowerComponent.class).moveToPos(snappedPos);
                }
                else {
                    Point2D initPoint = new Point2D(getAppWidth() - testLevelMap.tileSize,getAppHeight() * 0.6);
                    towerEntity.getComponent(TowerComponent.class).moveToPos(initPoint);
                }
            }
        };

        input.addAction(moveUp, KeyCode.W);
        input.addAction(drag, MouseButton.PRIMARY);
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new Factory());
        //Level entities must be spawned AFTER setting the level
        setLevelFromMap("tmx/FirstTilemap.tmx");

        testLevelMap = new LevelMap(45,22,16);
        towerEntity = spawn("towerComponent",getAppWidth() - testLevelMap.tileSize, 0.6 * getAppHeight());
        testEntity = spawn("testEntity", getAppWidth()- testLevelMap.tileSize,0.5 * getAppHeight());
        //spawn("Projectile", FXGLMath.randomPoint(new Rectangle2D(0,0,getAppWidth(),getAppHeight())));
    }

    @Override
    protected void initPhysics() {
        //
    }

    @Override
    protected void onUpdate(double tpf) {
        // runs every frame, probably
        // tpf seems to be time per frame in seconds, or delta time between frames.
    }

    public static void main(String[] args) {
        launch(args);
    }
}
