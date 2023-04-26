package com.almasb.fxglgames.towerDefense;

import com.almasb.fxgl.texture.Texture;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;


import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * UI node to display the player's current amount of money
 */
public class CurrencySymbol extends HBox {

    public CurrencySymbol(){
        Texture texture = new Texture(image("currency/" + "money.png"));
        texture.setFitHeight(20);
        texture.setFitWidth(20);
        texture.setTranslateY(3);

        Text text = getUIFactoryService().newText("", Color.WHITE,18);
        text.textProperty().bind(getip("money").asString());
        //text.setTranslateY(15);

        setSpacing(5);
        setAlignment(Pos.BASELINE_RIGHT);

        getChildren().addAll(texture,text);

    }
}
