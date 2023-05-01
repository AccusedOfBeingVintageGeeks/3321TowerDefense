package com.almasb.fxglgames.towerDefense;

import javafx.scene.Parent;
import javafx.scene.control.Button;

import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * This is the class for the buttons in the main menu VBox.
 */
/*
This will not be tested since it is a UI class
 */
public class MenuButton extends Parent {
    /**
     * Use this constructor create buttons in the main menu VBox.
     * @param label     What should the button say?
     * @param action    What should the button do?
     */
    public MenuButton(String label, Runnable action){
        Button button = getUIFactoryService().newButton(label);
        button.setOnAction(e -> action.run());
        button.setPrefWidth(300);

        getChildren().addAll(button);
    }
}
