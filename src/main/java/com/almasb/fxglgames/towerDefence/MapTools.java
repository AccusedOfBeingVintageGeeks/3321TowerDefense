package com.almasb.fxglgames.towerDefence;

import com.almasb.fxgl.entity.Entity;
import javafx.geometry.Point2D;
import javafx.scene.shape.Polyline;

import java.util.ArrayList;
import java.util.List;

public class MapTools {
    public static List<Point2D> getPointsFromPathEntity(Entity pathEntity)
    {
        /*
         * Based on code by AlmasB:
         *  - https://github.com/AlmasB/FXGLGames/blob/master/TowerDefense/src/main/java/com/almasb/fxglgames/td/TowerDefenseApp.java
         *  - https://github.com/AlmasB/FXGLGames/blob/master/TowerDefense/src/main/java/com/almasb/fxglgames/td/data/Way.java
         */

        Polyline polyline = pathEntity.getObject("polyline");
        List<Double> polylineEntries = polyline.getPoints();
        double
                offsetX = pathEntity.getX(),
                offsetY = pathEntity.getY();

        List<Point2D> pointList = new ArrayList<>();

        for(int i = 0; i < polylineEntries.size(); i += 2)
        {
            double x = polylineEntries.get(i) + offsetX;
            double y = polylineEntries.get(i+1) + offsetY;
            pointList.add(new Point2D(x,y));
        }

        return pointList;
    }
}
