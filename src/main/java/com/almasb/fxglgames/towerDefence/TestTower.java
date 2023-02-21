package com.almasb.fxglgames.towerDefence;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.TransformComponent;
import javafx.geometry.Point2D;
public class TestTower extends Component {

    private TransformComponent transformComponent;
    private double speed, frameRateScalar;

    private boolean isDragged = false;

    public boolean getDragStatus(){
        return isDragged;
    }
    public void setDragStatus(boolean dragStatus)
    {
        isDragged = dragStatus;
    }
    TestTower(double speed)
    {
        this.speed = speed;
    }

    @Override
    public void onUpdate(double tpf) {
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
