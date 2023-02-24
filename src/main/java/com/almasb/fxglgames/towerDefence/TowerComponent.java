package com.almasb.fxglgames.towerDefence;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.TransformComponent;
import javafx.geometry.Point2D;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.time.LocalTimer;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;

import java.util.Objects;

public class TowerComponent extends Component {
    /**
     * based on code by AlmasB
     * -https://github.com/AlmasB/FXGLGames/tree/master/TowerDefense/src/main/java/com/almasb/fxglgames/td/components/TowerComponent
     */
    private double speed, frameRateScalar;
    private boolean isDragged, isPlaced;
    private LocalTimer shotFrequency;
    private TransformComponent transformComponent;

    public boolean getDragStatus() { return isDragged; }
    public void setDragStatus(boolean dragStatus) { isDragged = dragStatus; }

    public boolean getPlacedStatus(){ return isPlaced; }
    public void setPlacedStatus(boolean placedStatus){ isPlaced = placedStatus; }
    //getDamage class

    public TowerComponent(double speed)
    {
        this.speed = speed;
        isDragged = false;
        isPlaced = false;
        shotFrequency = newLocalTimer();
        newLocalTimer().capture();
    }

    /**
     * Method enables TowerComponent to shoot Enemy using TowerProjectileComponent
     * @param enemy
     */
    public void shoot(Entity enemy){ //later needs to get enemy as parameter - make private

        Point2D localPos = this.getEntity().getPosition();
        Point2D aim = enemy.getPosition().subtract(localPos);

        var projectile = spawn("Projectile",
                new SpawnData(localPos)
                        .put("tower",entity)
                        .put("prey", enemy)
        );
        projectile.rotateToVector(aim);
    }

    public void placeTower(Entity placeableTile)
    {
        Point2D placingPoint = placeableTile.getCenter();
        this.moveToPos(placingPoint);
        isPlaced = true;
    }





    @Override
    public void onUpdate(double tpf)
    {
        frameRateScalar = tpf * 60;
        TowerDefenceApp.Type target = TowerDefenceApp.Type.TEST;
        if(this.isPlaced && shotFrequency.elapsed(Duration.seconds(1))){
            getGameWorld()
                    .getClosestEntity(entity,e ->e.isType(target))
                    .ifPresent(closestEnemy ->{
                        entity.rotateToVector(closestEnemy.getPosition().subtract(entity.getPosition()));
                        shoot(closestEnemy);
                        shotFrequency.capture();
                    });
        }
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
