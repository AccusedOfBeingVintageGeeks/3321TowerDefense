package com.almasb.fxglgames.towerDefense;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.TransformComponent;
import static com.almasb.fxgl.dsl.FXGL.*;


/**
 * based on code from AlmasB
 * -<a href="https://github.com/AlmasB/FXGLGames/tree/master/TowerDefense/src/main/java/com/almasb/fxglgames/td/components/BulletComponent">https://github.com/AlmasB/FXGLGames/tree/master/TowerDefense/src/main/java/com/almasb/fxglgames/td/components/BulletComponent</a>
 * author: Andreas Kramer
 * Projectile entity that is shot from a TowerComponent to hit an enemy
 */
public class TowerProjectileComponent extends Component {
    private final Entity prey;
    private final int speed, damage;

    /**
     * Projectile that travels from a Tower towards an Enemy (prey)
     * @param prey TowerComponent entity
     * @param speed projectile speed
     */
    public TowerProjectileComponent(Entity prey, int speed, int damage) {
        this.prey = prey;
        this.speed = speed;
        this.damage = damage;
    }

    /**
     * keeps projectile moving and headed towards prey
     * @param tpf time per frame
     */
    //Cannot test due to FXGL calls
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
    //Cannot test due to FXGL calls
    private void preyHit(){
        entity.removeFromWorld();
        int remainingHealth = prey.getComponent(EnemyManagerComponent.class).getRemainingHealth();
        if(damage >= remainingHealth)
            inc(TowerDefenseApp.MONEY,5);
        prey.getComponent(EnemyManagerComponent.class).dealDamage(damage);
    }
}
