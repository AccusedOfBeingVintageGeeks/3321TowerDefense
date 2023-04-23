package com.almasb.fxglgames.towerDefense;

import com.almasb.fxglgames.towerDefense.TowerDefenseApp;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.layout.VBox;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getip;

/**
 * author: Andreas Kramer
 * based on code by AlmasB:-<a href="https://github.com/AlmasB/FXGLGames/tree/master/TowerDefense/src/main/java/com/almasb/fxglgames/td/ui/TowerSelectionBox">...</a>
 * Description: UI for sidebar, Player clicks on TowerSymbol and receives a TowerComponent
 */
public class TowerMenuBox extends VBox {
    /**
     * Creates a UI on the right side of the map, with clickable TowerSymbols
     * @param towerNames  - DataForTower containing information about specific tower
     */
    public TowerMenuBox(List<DataForTower> towerNames) {
        setSpacing(5);
        towerNames.forEach(name -> {
            var symbol = new TowerSymbol(name);
            symbol.bindToMoney(getip(TowerDefenseApp.MONEY));
            symbol.setOnMousePressed(e -> {
                {
                    FXGL.<TowerDefenseApp>getAppCast().onTowerSelection(name);
                }
            });

            getChildren().add(symbol);
        });
    }
}
