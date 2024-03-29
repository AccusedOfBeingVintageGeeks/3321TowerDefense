package com.almasb.fxglgames.towerDefense;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.TransformComponent;
import javafx.geometry.Point2D;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.time.LocalTimer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * based on code by AlmasB
 * -<a href="https://github.com/AlmasB/FXGLGames/tree/master/TowerDefense/src/main/java/com/almasb/fxglgames/td/components/TowerComponent">...</a>
 * author: Andreas Kramer
 * TowerComponent shoots Entities of type Enemy once set in place
 */

//Unable to test. Every method relies on FXGL calls.
public class TowerComponent extends Component {

    private final DataForTower data;
    private final LocalTimer shotFrequency;
    private final TowerInfo info;
    private final Circle circle;
    private final TransformComponent transformComponent;
    private boolean isPlaced;
    public boolean getPlacedStatus(){ return isPlaced; }
    public void setPlacedStatus(boolean placedStatus){ isPlaced = placedStatus; }
    //getDamage class

    /**
     * Tower-defense Tower can be dragged and dropped, once dropped, shoots the nearest enemy
     * @param towerData TowerData includes information about a specific tower like names, fireRate, fireRadius, etc
     */
    public TowerComponent(DataForTower towerData)
    {
        this.data = towerData;
        this.transformComponent = new TransformComponent();
        isPlaced = false;
        info = new TowerInfo(data,this);
        info.setVisible(false);
        circle = new Circle(data.fireRadius() + 15, Color.color(1,0,0,0.3));
        this.shotFrequency = newLocalTimer();
        shotFrequency.capture();
        addUINode(info);
        addUINode(circle);
    }

    public DataForTower getDataForTower(){
        return data;
    }

    /**
     * Method enables TowerComponent to shoot Enemy using TowerProjectileComponent
     * @param enemy target that gets shot
     */
    private void shoot(Entity enemy){
        shotFrequency.capture();
        Point2D localPos = this.getEntity().getCenter();
        Point2D aim = enemy.getPosition().subtract(localPos);
        var projectile = spawn("Projectile",
                new SpawnData(localPos)
                        .put("tower",entity)
                        .put("prey", enemy)
                        .put("projectile",data.projectileImageName())
                        .put("projectileSpeed",data.projectileSpeed())
                        .put("damage",data.projectileDamage())
                        .put("height",data.projectileHeight())
                        .put("width",data.projectileWidth())
        );
        projectile.rotateToVector(aim);
    }
    private void rotateTowerToTarget(Entity tower, Entity target){
        tower.rotateToVector(target.getPosition().subtract(tower.getPosition()));
        transformComponent.rotateBy(90);
    }

    /**
     * creates UI including TowerInfo displaying towerData and a circle displaying the fireRadius
     */
    public void initializeTowerInfo(){
        info.setTranslateX(entity.getX() + 40);
        info.setTranslateY(entity.getY());
        circle.setTranslateX(entity.getAnchoredPosition().getX());
        circle.setTranslateY(entity.getAnchoredPosition().getY());
        this.circle.setVisible(!this.isPlaced);
        entity.getViewComponent().getParent().setOnMouseClicked(e -> this.info.setVisible(!this.info.isVisible()));
    }
    public void deleteTowerInfo(){
        removeUINode(circle);
        removeUINode(info);
    }
    /**
     * Scans Game-world for the nearest enemy entity
     * @param tpf time per frame
     */
    @Override
    public void onUpdate(double tpf)
    {
        initializeTowerInfo();
        TowerDefenseApp.Type targetType = TowerDefenseApp.Type.ENEMY;
        Duration firePause = Duration.seconds(data.fireRate());
        if(this.isPlaced && shotFrequency.elapsed(firePause)){
            getGameWorld()
                    .getClosestEntity(entity, e ->e.isType(targetType))
                    .ifPresent(closestEnemy ->{
                        if(closestEnemy.distanceBBox(entity) <= data.fireRadius()) {
                            rotateTowerToTarget(entity,closestEnemy);
                            shoot(closestEnemy);
                        }//do nothing

                    });
        }
        //do nothing
    }
}
