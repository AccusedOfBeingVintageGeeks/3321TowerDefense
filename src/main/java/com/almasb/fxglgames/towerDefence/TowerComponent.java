package com.almasb.fxglgames.towerDefence;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.TransformComponent;
import javafx.geometry.Point2D;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.time.LocalTimer;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;

public class TowerComponent extends Component {
    /**
     * based on code by AlmasB
     * -https://github.com/AlmasB/FXGLGames/tree/master/TowerDefense/src/main/java/com/almasb/fxglgames/td/components/TowerComponent
     */
    private double speed, frameRateScalar;
    private boolean isDragged, isPlaced;
    private LocalTimer shotFrequency;
    private TransformComponent transformComponent;
    private double fireRateinSec = 0.7;

    public boolean getDragStatus() { return isDragged; }
    public void setDragStatus(boolean dragStatus) { isDragged = dragStatus; }

    public boolean getPlacedStatus(){ return isPlaced; }
    public void setPlacedStatus(boolean placedStatus){ isPlaced = placedStatus; }
    //getDamage class

    /**
     * TowerDefense Tower can be dragged and dropped, once dropped, shoots nearest enemy
     * @param speed
     */
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
    private void shoot(Entity enemy){

        Point2D localPos = this.getEntity().getCenter();
        Point2D aim = enemy.getPosition().subtract(localPos);
        //Point2D newAim = aim.subtract(entity.getHeight()/2,entity.getWidth()/2);

        var projectile = spawn("Projectile",
                new SpawnData(localPos)
                        .put("tower",entity)
                        .put("prey", enemy)
        );
        projectile.rotateToVector(aim);
    }

    /**
     * enables Tower movement and gets the nearest enemy to shoot (shoots entities of type TEST right now)
     * @param tpf time per frame
     */
    @Override
    public void onUpdate(double tpf)
    {
        frameRateScalar = tpf * 60;
        TowerDefenseApp.Type target = TowerDefenseApp.Type.ENEMY;
        if(this.isPlaced && shotFrequency.elapsed(Duration.seconds(fireRateinSec))){
            getGameWorld()
                    .getClosestEntity(entity,e ->e.isType(target))
                    .ifPresent(closestEnemy ->{
                        entity.rotateToVector(closestEnemy.getPosition().subtract(entity.getPosition()));
                        transformComponent.rotateBy(90);
                        shoot(closestEnemy);
                        shotFrequency.capture();
                    });
        }
    }

    /**
     * test method to move TowerComponent vertically
     */
    public void rotateUp()
    {
        transformComponent.rotateToVector(entity.getPosition().subtract(0,45));
        transformComponent.rotateBy(343);
    }

    /**
     * moves TowerComponent to 2D Point in WorldSpace
     * @param posInWorldSpace
     */
    public void moveToPos(Point2D posInWorldSpace)
    {
        transformComponent.setPosition(posInWorldSpace);
    }


}
