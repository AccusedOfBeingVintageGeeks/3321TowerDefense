package com.almasb.fxglgames.towerDefense;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.FXGLForKtKt;
import javafx.beans.binding.Bindings;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * Ui to display information about a selected tower
 * author: Andreas Kramer
 */
public class TowerInfo extends VBox {

    private final DataForTower data;
    public TowerInfo(DataForTower data, TowerComponent tower){
        this.data = data;
        this.setOpacity(0.8);

        var backGround = setUpBackGround();
        var text1 = setUpText("SELL");
        var text = setUpText(data.name() + "\n" + "Fire-rate: " + data.fireRate() + "\n" +
                "Fire-radius: " + data.fireRadius() + "\n");
        var sell_Button = setUpButton(backGround,text1,tower);
        getChildren().addAll(sell_Button,text);
    }
    private Rectangle setUpBackGround(){

        Rectangle back = new Rectangle(100,40, Color.RED);
        back.setStroke(Color.DARKRED);
        back.setStrokeWidth(5);
        return back;
    }
    private Text setUpText(String content){
        var text = FXGLForKtKt.getUIFactoryService().newText(content,20);
        text.setStroke(Color.ANTIQUEWHITE);
        text.setTabSize(5);
        return text;
    }

    /**
     * creates sets up the SELL button, its opacity will change when mouse hovers over, on clicked -> sell tower
     * @param backGround   Rectangle as background
     * @param text  text that says SELL
     * @param tower passes the tower on that is to be sold
     * @return returns a functioning SELL button
     */
    private StackPane setUpButton(Rectangle backGround,Text text, TowerComponent tower){
        StackPane sellButton = new StackPane();
        sellButton.getChildren().addAll(backGround,text);
        sellButton.opacityProperty().bind(
                Bindings.when(sellButton.hoverProperty())
                        .then(0.8)
                        .otherwise(0.5)
        );
        sellButton.setOnMouseClicked(e->
                FXGL.<TowerDefenseApp>getAppCast().onTowerSell(data, tower));
        return sellButton;
    }


}
