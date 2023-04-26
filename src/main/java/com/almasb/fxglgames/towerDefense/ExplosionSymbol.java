package com.almasb.fxglgames.towerDefense;


import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class ExplosionSymbol extends Polygon {
    public ExplosionSymbol(Color color) {
        getPoints().addAll(
                0.0, 0.0, 32.0, 32.0, 64.0, 0.0,
                60.0, -5.0, 32.0, 25.0, 4.0, -5.0
        );

        setStrokeWidth(2.0);
        setStroke(Color.ORANGERED);
        setFill(color);
    }
}
