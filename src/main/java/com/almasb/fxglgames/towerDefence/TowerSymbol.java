package com.almasb.fxglgames.towerDefence;

import com.almasb.fxgl.dsl.FXGLForKtKt;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.TransformComponent;
import javafx.beans.binding.Bindings;
import javafx.geometry.Point2D;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.time.LocalTimer;
import javafx.beans.property.IntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import com.almasb.fxgl.texture.Texture;

import static com.almasb.fxgl.dsl.FXGL.image;
import static com.almasb.fxgl.dsl.FXGLForKtKt.texture;

public class TowerSymbol extends VBox {

    private Texture texture;

    public TowerSymbol(){

        var backGround = new Circle(35,35,35);
        backGround.setStroke(Color.WHITESMOKE);
        backGround.setStrokeWidth(2.5);

        var text = FXGLForKtKt.getUIFactoryService().newText("50" + "");
        text.setStroke(Color.BLACK);
        texture = texture("cannon.png");

        var stackPane = new StackPane(backGround,texture);

        setSpacing(10);
        setAlignment(Pos.TOP_CENTER);

        getChildren().addAll(stackPane,text);

    }
    public void bindToMoney(IntegerProperty property) {
        texture.opacityProperty().bind(
                Bindings.when(property.greaterThanOrEqualTo(50))
                        .then(1.0)
                        .otherwise(0.25)
        );
    }


}
