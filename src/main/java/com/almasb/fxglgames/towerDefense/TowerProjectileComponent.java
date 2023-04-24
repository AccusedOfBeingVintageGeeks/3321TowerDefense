package com.almasb.fxglgames.towerDefense;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.TransformComponent;

/**
 * based on code from AlmasB
 * -<a href="https://github.com/AlmasB/FXGLGames/tree/master/TowerDefense/src/main/java/com/almasb/fxglgames/td/components/BulletComponent">https://github.com/AlmasB/FXGLGames/tree/master/TowerDefense/src/main/java/com/almasb/fxglgames/td/components/BulletComponent</a>
 */
public class TowerProjectileComponent extends Component {

    private final Entity tower;
    private final Entity prey;
    private TransformComponent transformComponent;
    private final int speed, damage;

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
    public TowerProjectileComponent(Entity tower, Entity prey, int speed, int damage) {
        this.prey = prey;
        this.speed = speed;
        this.damage = damage;
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
        //later on: start effects/animations,deal damage,if prey is killed -> call functions for money,etc.
        //TowerComponent data = tower.getComponent(TowerComponent.class);
        entity.removeFromWorld();
        prey.getComponent(EnemyManagerComponent.class).dealDamage(damage);
    }



}
