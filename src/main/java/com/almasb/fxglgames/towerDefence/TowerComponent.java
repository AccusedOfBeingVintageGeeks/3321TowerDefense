package com.almasb.fxglgames.towerDefence;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.TransformComponent;
import javafx.geometry.Point2D;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Entity;

import static com.almasb.fxgl.dsl.FXGL.*;

import java.util.Objects;

public class TowerComponent extends Component {

    private TransformComponent transformComponent;
    private double speed, frameRateScalar;
    private boolean isDragged = false;

    public boolean getDragStatus()
    {
        return isDragged;
    }
    public void setDragStatus(boolean dragStatus)
    {
        isDragged = dragStatus;
    }
    //getDamage class

    TowerComponent(double speed)
    {
        this.speed = speed;
    }
    public void shoot(Entity enemy){ //later needs to get enemy as parameter - make private

        Point2D localPos = getEntity().getPosition();
        Point2D aim = enemy.getPosition().subtract(localPos);

        var projectile = spawn("Projectile",
                new SpawnData(localPos)
                        .put("tower",entity)
                        .put("prey", entity)
        );
        projectile.rotateToVector(aim);
    }


    @Override
    public void onUpdate(double tpf)
    {
        frameRateScalar = tpf * 60;
    }
    public void moveUp()
    {
        transformComponent.translateY(-speed * frameRateScalar);
    }
    public void moveToPos(Point2D posInWorldSpace)
    {
        transformComponent.setPosition(posInWorldSpace);
    }




}
