package com.almasb.fxglgames.towerDefense;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * This is the main menu class.
 */
public class MainMenu extends FXGLMenu {
    final int MARGIN = 10;

    /**
     * This is the constructor for the main menu.
     */
    public MainMenu() {
        super(MenuType.MAIN_MENU);

        //background
        Rectangle background = new Rectangle(getAppWidth(),getAppHeight(), Color.DARKOLIVEGREEN);

        //version
        Text version = getUIFactoryService().newText("v" + getSettings().getVersion() + ". Made in FXGL v" + getVersion() + ".");
        version.setTranslateY(getAppHeight() - version.getFont().getSize());
        version.setTranslateX(MARGIN);

        //title
        Text title = getUIFactoryService().newText("DEFENSE via TOWERS");
        title.fontProperty().unbind();
        title.fillProperty().unbind();
        title.getStyleClass().add("title_text");
        title.setTranslateY((getAppHeight() + title.getFont().getSize())*0.5);
        title.setTranslateX(MARGIN);

        //menu
        VBox menuBox = new VBox(
                new MenuButton("First Level", startLevel("first_level", "waveDataListA")),
                new MenuButton("The Quad", startLevel("the_quad", "waveDataListA")),
                new MenuButton("Quit", () -> fireExit())
        );
        menuBox.setTranslateX(getAppWidth()-400);

        //menu container to center vertically
        VBox container = new VBox(menuBox);
        container.setPrefSize(getAppWidth(),getAppHeight());
        container.setAlignment(Pos.CENTER);

        getContentRoot().getChildren().addAll(background, version, title, container);
    }

    /**
     * This method returns a runnable that sets the level data and starts the game.
     * @param levelName         The name of a .tmx file in assets/levels/tmx/
     * @param waveDataFilename  The name of a .json file in assets/levels/waveDataLists/
     * @return Runnable
     */
    private Runnable startLevel(String levelName, String waveDataFilename){
        return () -> {
            FXGL.<TowerDefenseApp>getAppCast().setLevel(levelName, waveDataFilename);
            fireNewGame();
        };
    }
}
