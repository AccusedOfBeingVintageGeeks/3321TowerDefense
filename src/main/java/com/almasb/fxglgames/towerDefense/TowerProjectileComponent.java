package com.almasb.fxglgames.towerDefense;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.components.EffectComponent;
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
    private final Entity prey, tower;
    private final int speed, damage;

    /**
     * Projectile that travels from a Tower towards an Enemy (prey)
     * @param prey TowerComponent entity
     * @param speed projectile speed
     */
    public TowerProjectileComponent(Entity tower,Entity prey, int speed, int damage) {
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
        TowerComponent data = tower.getComponent(TowerComponent.class);

        int remainingHealth = prey.getComponent(EnemyManagerComponent.class).getRemainingHealth();
        data.onHitEffects().forEach(effect -> {
            if (FXGLMath.randomBoolean(effect.getChance())) {
                prey.getComponent(EffectComponent.class).startEffect(effect.getEffect());
            }});
        if(damage >= remainingHealth){

            inc(TowerDefenseApp.MONEY,5);
            var visual = spawn("visualEffectSlow",prey.getPosition());
            Animations.playVisualEffectSlowAnimation(visual);
        }
                entity.removeFromWorld();
        prey.getComponent(EnemyManagerComponent.class).dealDamage(damage);
    }
}
