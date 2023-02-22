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
import javafx.geometry.Rectangle2D;
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
    Entity towerComponent;

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

        UserAction moveUp = new UserAction("Move Up") {
            @Override
            protected void onAction() {
                testEntity.getComponent(TestEntityComponent.class).moveUp();
                towerComponent.getComponent(TowerComponent.class).moveUp();
                //System.out.println("keydown");

            }
        };

        UserAction drag = new UserAction("Drag") {
            //For drag and drop
            boolean dragging = false;
            @Override
            protected void onActionBegin() {
                if(getInput().getMousePositionWorld().distance(testEntity.getCenter()) < 0.5 * 40) {
                    dragging = true;
                }else if(getInput().getMousePositionWorld().distance(towerComponent.getCenter()) < 0.5 * 40){
                    towerComponent.getComponent(TowerComponent.class).setDragStatus(true);
                    //testTower.getComponent(TestTower.class).moveToPos((getInput().getMousePositionWorld()));
                    //dragging = true;
                }
                System.out.println("mousedown");
            }

            @Override
            protected void onAction() {
                if(dragging){
                    testEntity.getComponent(TestEntityComponent.class).moveToPos(getInput().getMousePositionWorld());
                }else if(towerComponent.getComponent(TowerComponent.class).getDragStatus()){
                    towerComponent.getComponent(TowerComponent.class).moveToPos(getInput().getMousePositionWorld());
                    towerComponent.getComponent(TowerComponent.class).shoot(testEntity.getComponent(TestEntityComponent.class).getEntity());
                }

            }

            @Override
            protected void onActionEnd() {
                dragging = false;
                Point2D initPoint = new Point2D(getAppWidth() -45,getAppHeight() * 0.6);
                towerComponent.getComponent(TowerComponent.class).moveToPos(initPoint);
                // Check if the tile that the mouse is positioned over is placeable.

                // If so, move tower position to the center of that tile and activate it.

                // If the tile isn't placeable, or if the mouse isn't positioned over any tile,
                // then move the tower's position back to its initial position in the menu bar or whatever.
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

        towerComponent = spawn("towerComponent",getAppWidth() -45, 0.6 * getAppHeight());
        testEntity = spawn("testEntity", getAppWidth()-45,0.5 * getAppHeight());
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
