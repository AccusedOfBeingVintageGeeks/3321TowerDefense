package com.almasb.fxglgames.towerDefence;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.Entity;
public class TowerProjectileComponent extends Component {

    private Entity tower;
    private Entity prey;

    private int speed = 500;
    //speed can probably put into a different class (data class) and be retrieved from there
    //this should make it easier to change game settings

    public TowerProjectileComponent(Entity tower, Entity prey) {
        this.tower = tower;
        this.prey = prey;
    }

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


    private void preyHit(){
        //actions if prey gets hit
        //remove projectile and prey from world
        //later on: start effects/animations,deal damage,if prey is killed -> call functions for money,etc..
        TowerComponent data = tower.getComponent(TowerComponent.class);

        entity.removeFromWorld();
        /*
        var HP = prey.getComponent(HealthIntComponent.class);
        HP.damage(data.getDamage()); //needs method in TowerComponent to get damage
        */
        prey.removeFromWorld();

    }



}
