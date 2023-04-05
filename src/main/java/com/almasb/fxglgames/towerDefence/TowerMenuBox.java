package com.almasb.fxglgames.towerDefence;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import com.almasb.fxglgames.towerDefence.TowerDefenceApp;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getAppCast;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getip;

/**
 * author: Andreas Kramer
 * based on code by AlmasB:-https://github.com/AlmasB/FXGLGames/tree/master/TowerDefense/src/main/java/com/almasb/fxglgames/td/ui/TowerSelectionBox
 * UI for sidebar, Player clicks on TowerSymbol and receives a TowerComponent
 */
public class TowerMenuBox extends VBox {
    /**
     * Creates a UI on the right side of the map, with clickable TowerSymbols
     * @param towerNames    - a list of Strings containing the names for the different textures for the different types of towers
     */
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
