package com.almasb.fxglgames.towerDefence;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.TransformComponent;
import javafx.geometry.Point2D;

/**
 * based on code from AlmasB
 * -https://github.com/AlmasB/FXGLGames/tree/master/TowerDefense/src/main/java/com/almasb/fxglgames/td/components/BulletComponent
 */
public class TowerProjectileComponent extends Component {

    private final Entity tower;
    private Entity prey;
    private TransformComponent transformComponent;
    private int speed;

    public TransformComponent getTransformComponent() {
        return transformComponent;
    }
    //speed can probably put into a different class (data class) and be retrieved from there
    //this should make it easier to change game settings

    /**
     * Projectile that travels from a Tower towards an Enemy (prey)
     * @param tower TowerComponent entity
     * @param prey TowerComponent entity
     * @param speed projectile speed
     */
    public TowerProjectileComponent(Entity tower, Entity prey, int speed) {
        this.prey = prey;
        this.speed = speed;
        this.tower = tower;
    }

    /**
     * keeps projectile moving and headed towards prey
     * @param tpf time per frame
     */
    @Override
    public void onUpdate(double tpf){
        if(!prey.isActive()){
            entity.removeFromWorld();
            return;
        }
        if(entity.distanceBBox(prey) < speed * tpf){
            preyHit();
            return;
        }
        entity.translateTowards(prey.getCenter(),speed * tpf);

    }


    /**
     * removes Projectile and Enemy (prey) from world
     */
    private void preyHit(){
        //actions if prey gets hit
        //remove projectile and prey from world
        //later on: start effects/animations,deal damage,if prey is killed -> call functions for money,etc..
        //TowerComponent data = tower.getComponent(TowerComponent.class);
        entity.removeFromWorld();
        /*
        var HP = prey.getComponent(HealthIntComponent.class);
        HP.damage(data.getDamage()); //needs method in TowerComponent to get damage
        */
        prey.removeFromWorld();

    }



}
