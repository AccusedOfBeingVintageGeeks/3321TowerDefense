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
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
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
        TOWER, ENEMY, PROJECTILE, PATH, TEST
    }
    Entity testEntity;

    @Override
    protected void initSettings(GameSettings settings) {
        // initialize common game / window settings.
        settings.setTitle("Tower Defence");
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

        UserAction moveUp = new UserAction("Move Up") {
            @Override
            protected void onAction() {
                testEntity.getComponent(TestEntityComponent.class).moveUp();
                //System.out.println("keydown");

            }
        };

        UserAction drag = new UserAction("Drag") {
            //For drag and drop
            boolean dragging = false;
            @Override
            protected void onActionBegin() {
                if(getInput().getMousePositionWorld().distance(testEntity.getCenter()) < 0.5 * 40)
                    dragging = true;
                System.out.println("mousedown");
            }

            @Override
            protected void onAction() {
                if(dragging)
                    testEntity.getComponent(TestEntityComponent.class).moveToPos(getInput().getMousePositionWorld());
            }

            @Override
            protected void onActionEnd() {
                dragging = false;
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

        testEntity = spawn("testEntity", getAppWidth()-45,0.5*getAppHeight());


        run(() -> {
            Entity scrubEntity = spawn("scrub", 50,50);
            Factory.reinitializeScrub(scrubEntity);
        }, Duration.millis(1000));
    }

    @Override
    protected void initPhysics() {
        //
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
