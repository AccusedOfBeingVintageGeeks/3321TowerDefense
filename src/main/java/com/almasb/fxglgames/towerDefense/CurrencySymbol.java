package com.almasb.fxglgames.towerDefense;

import com.almasb.fxgl.texture.Texture;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.layout.VBox;



import static com.almasb.fxgl.dsl.FXGL.*;

public class CurrencySymbol extends HBox {

    public CurrencySymbol(){
        Texture texture = new Texture(image("currency/" + "money.png"));
        texture.setFitHeight(30);
        texture.setFitWidth(30);
        var text = getUIFactoryService().newText("", Color.WHITE,20);
        text.textProperty().bind(getip("money").asString());
        setSpacing(20);
        setAlignment(Pos.BASELINE_RIGHT);

        getChildren().addAll(texture,text);

    }
}
