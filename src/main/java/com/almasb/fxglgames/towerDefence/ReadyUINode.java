package com.almasb.fxglgames.towerDefence;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * The ReadyUINode is for the button itself and the wave info text above it.
 */
public class ReadyUINode extends Parent {
    Button button;
    Text waveInfoText;

    /**
     * This constructor takes the integers width and height and an action event handler to be executed when clicked.
     */
    public ReadyUINode(int width, int height, EventHandler<ActionEvent> clickAction){
        button = getUIFactoryService().newButton("READY");
        button.getStyleClass().add("ready_button");
        button.setPrefWidth(width);
        button.setPrefHeight(height);
        button.setOnAction(clickAction);
        button.fontProperty().unbind();
        button.textProperty().unbind();

        waveInfoText = getUIFactoryService().newText("");
        waveInfoText.getStyleClass().add("wave_info_text");
        waveInfoText.setTranslateX(3);
        waveInfoText.setTranslateY(-5);
        waveInfoText.fontProperty().unbind();

        getChildren().addAll(button, waveInfoText);
    }

    /**
     * This method sets whether the button is disabled or not, and changes the button's text appropriately.
     * @param isClickable   Should the button be clickable? Is the player allowed to be ready for the next round?
     */
    public void setButtonClickable(boolean isClickable){
        button.setDisable(!isClickable);
        if(isClickable)
            button.setText("READY");
        else {
            button.setText("UNDER ATTACK");
        }
    }

    /**
     * This method sets the waveInfo text to a countdown with the given integer.
     * @param countdownVal  How much time is left in seconds (integer) before the next round starts?
     */
    public void setCountdownText(int countdownVal){
        waveInfoText.setText("Next wave in " + countdownVal + "s");
        if(countdownVal <= 10)
            waveInfoText.setFill(Color.LIGHTCORAL);
        else
            waveInfoText.setFill(Color.WHITE);
    }

    /**
     * This method sets the waveInfo text to an empty string.
     */
    public void setBlankText(){
        waveInfoText.setText("");
    }
}
