package com.almasb.fxglgames.towerDefence;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * The ReadyButton UI node is for the button itself and the wave info text above it.
 */
public class ReadyButton extends Parent {
    /**
     * This ReadyButton constructor takes integers width and height and an action event handler to be executed when clicked.
     */
    public ReadyButton(int width, int height, EventHandler<ActionEvent> clickAction){
        Button button = getUIFactoryService().newButton("READY");
        button.getStyleClass().add("ready_button");
        button.setPrefWidth(width);
        button.setPrefHeight(height);
        button.setOnAction(clickAction);

        String waveInfo = "Next Wave in #s";
        Text waveInfoText = getUIFactoryService().newText(waveInfo, Color.WHITE, 10);
        waveInfoText.getStyleClass().add("wave_info_text");

        getChildren().addAll(button, waveInfoText);
    }
}
