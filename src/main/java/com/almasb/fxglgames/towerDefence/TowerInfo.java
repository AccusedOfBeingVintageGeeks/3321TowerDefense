package com.almasb.fxglgames.towerDefence;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.FXGLForKtKt;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class TowerInfo extends VBox {

    private DataForTower data;
    public TowerInfo(DataForTower data){
        this.data = data;
        this.setOpacity(0.8);

        var backGround = setUpBackGround();
        var text1 = setUpText("SELL");
        var text = setUpText(data.name() + "\n" + "Fire-rate: " + data.fireRate() + "\n" +
                "Fire-radius: " + data.fireRadius() + "\n" + "Damage: " + "tbc" + "\n");
        var sell_Button = setUpButton(backGround,text1);
        getChildren().addAll(sell_Button,text);

    }

    private Rectangle setUpBackGround(){

        Rectangle back = new Rectangle(100,40, Color.RED);
        back.setStroke(Color.DARKRED);
        back.setStrokeWidth(5);
        return back;
    }
    private Text setUpText(String content){
        var text = FXGLForKtKt.getUIFactoryService().newText(content);
        text.setStroke(Color.ANTIQUEWHITE);
        text.setTabSize(5);
        return text;
    }
    private StackPane setUpButton(Rectangle backGround,Text text){
        StackPane sellButton = new StackPane();
        sellButton.getChildren().addAll(backGround,text);

        sellButton.opacityProperty().bind(
                Bindings.when(sellButton.hoverProperty())
                        .then(0.8)
                        .otherwise(0.5)
        );
        sellButton.setOnMouseClicked(e->{
            {
                FXGL.<TowerDefenceApp>getAppCast().onTowerSell(data);
            }
        });
        return sellButton;
    }


}
