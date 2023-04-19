package com.almasb.fxglgames.towerDefense;

import com.almasb.fxgl.dsl.FXGLForKtKt;
import javafx.beans.binding.Bindings;
import com.almasb.fxgl.entity.Entity;
import javafx.beans.property.IntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import com.almasb.fxgl.texture.Texture;

import static com.almasb.fxgl.dsl.FXGLForKtKt.texture;

/**
 * based on code by AlmasB: based on code by AlmasB:-https://github.com/AlmasB/FXGLGames/tree/master/TowerDefense/src/main/java/com/almasb/fxglgames/td/ui/TowerIcon
 * author: Andreas Kramer
 * TowerSymbol consisting of background, texture of the tower and a display for its cost
 */
public class TowerSymbol extends VBox{
    private Texture texture;
    private DataForTower data;
    private Entity entity;

    public TowerSymbol(DataForTower data){
        this.data = data;
        var backGround = new Circle(35,35,35,Color.LIGHTGREEN);
        backGround.setStroke(Color.BEIGE);
        backGround.setStrokeWidth(2.5);

        var text = FXGLForKtKt.getUIFactoryService().newText(data.cost() + "");
        text.setStroke(Color.BLACK);
        texture = texture("towers/" + data.imageName());
        texture.setFitHeight(60);
        texture.setFitWidth(60);

        var stackPane = new StackPane(backGround,texture);
        stackPane.toBack();
        setSpacing(5);
        setAlignment(Pos.TOP_CENTER);
        getChildren().addAll(stackPane,text);

    }
    public void bindToMoney(IntegerProperty property) {
        texture.opacityProperty().bind(
                Bindings.when(property.greaterThanOrEqualTo(data.cost()))
                        .then(1.0)
                        .otherwise(0.25)
        );
    }
}
