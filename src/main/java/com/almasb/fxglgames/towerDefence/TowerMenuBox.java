package com.almasb.fxglgames.towerDefence;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import com.almasb.fxglgames.towerDefence.TowerDefenceApp;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getAppCast;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getip;

public class TowerMenuBox extends VBox {


    public TowerMenuBox(List<String> towerNames) {
        setSpacing(5);
        towerNames.forEach(String -> {
            var symbol = new TowerSymbol();
            //symbol.bindToMoney(getip("money"));

            symbol.setOnMousePressed(e -> {
                {
                    FXGL.<TowerDefenceApp>getAppCast().onTowerSelection();
                }
            });
            getChildren().add(symbol);
        });
    }
}
