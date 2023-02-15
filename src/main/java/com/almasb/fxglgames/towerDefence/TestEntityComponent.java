package com.almasb.fxglgames.towerDefence;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.TransformComponent;
import javafx.geometry.Point2D;

public class TestEntityComponent extends Component {
    private TransformComponent transformComponent;
    private double speed, framerateScalar;
    TestEntityComponent(double speed)
    {
        this.speed = speed;
    }

    @Override
    public void onUpdate(double tpf) {
        framerateScalar = tpf * 60;
    }

    public void moveUp()
    {
        transformComponent.translateY(-speed * framerateScalar);
    }
    public void moveToPos(Point2D posInWorldSpace)
    {
        transformComponent.setPosition(posInWorldSpace);
    }

}
