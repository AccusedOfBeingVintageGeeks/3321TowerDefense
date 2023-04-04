package com.almasb.fxglgames.towerDefence;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.TransformComponent;
import javafx.geometry.Point2D;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.time.LocalTimer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import com.almasb.fxgl.texture.Texture;

import static com.almasb.fxgl.dsl.FXGL.*;

import java.util.Objects;
import java.util.Optional;

public class TowerComponent extends Component {
    /**
     * based on code by AlmasB
     * -https://github.com/AlmasB/FXGLGames/tree/master/TowerDefense/src/main/java/com/almasb/fxglgames/td/components/TowerComponent
     */
    private double frameRateScalar;
    private boolean isDragged, isPlaced;
    private LocalTimer shotFrequency;
    private TransformComponent transformComponent;
    private double fireRateinSec;
    private double fireRadius;
    private Circle circleRadius;
    private String towerTexture;

    private String missileTexture;
    private int cost;


    public boolean getPlacedStatus(){ return isPlaced; }
    public void setPlacedStatus(boolean placedStatus){ isPlaced = placedStatus; }
    //getDamage class

    /**
     * TowerDefense Tower can be dragged and dropped, once dropped, shoots the nearest enemy
     * @param fireRateinSec
     * @param fireRadius
     * @param cost
     */
    public TowerComponent(double fireRateinSec,
                          double fireRadius,int cost)
    {
        this.fireRateinSec = fireRateinSec;
        this.fireRadius = fireRadius;
        this.cost = cost;

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
        /*
        if(this.isPlaced == false){
            circleRadius = new Circle(entity.getPosition().getX(),entity.getPosition().getY(),fireRadius,Color.LIGHTGREEN);
            circleRadius.setVisible(true);
            return;
        }
         */
        frameRateScalar = tpf * 60;
        TowerDefenceApp.Type target = TowerDefenceApp.Type.ENEMY;
        if(this.isPlaced && shotFrequency.elapsed(Duration.seconds(fireRateinSec))){
            getGameWorld()
                    .getClosestEntity(entity, e ->e.isType(target))
                    .ifPresent(closestEnemy ->{
                        if(closestEnemy.distanceBBox(entity) <= fireRadius) {
                            entity.rotateToVector(closestEnemy.getPosition().subtract(entity.getPosition()));
                            transformComponent.rotateBy(90);
                            shoot(closestEnemy);
                            shotFrequency.capture();
                        }else{
                            //do nothing
                            return;}

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
