package com.almasb.fxglgames.towerDefense;

import com.almasb.fxgl.entity.component.Component;
import javafx.scene.Node;
import static com.almasb.fxgl.dsl.FXGL.*;


/**
 * All entities with the Type ENEMY must have this component. The EnemyManagerComponent keeps track of the enemies health and visuals, and is capable of removing the Entity that it is attached to from the GameWorld.
 */
/*
This class will not be tested because it is nearly entirely FXGL calls
 */
public class EnemyManagerComponent extends Component {

    private final int healthCapacity, reward;
    private int remainingHealth;
    private int healthIndex;
    final private Node[] healthTextures;
    public int getHealthCapacity(){return healthCapacity;}
    public int getRemainingHealth(){return healthCapacity;}

    /**
     * This constructor initializes the remaining health of the enemy as well as set the health textures.
     * @param healthCapacity    How much damage should this enemy be able to take before it is destroyed?
     * @param healthTextures    This should be an array of Nodes (so not necessarily textures) that this component can switch to as the enemy takes damage. They should be ordered from most to least damaged (low index = low health).
     */
    EnemyManagerComponent(int healthCapacity, Node[] healthTextures, int reward){
        this.healthCapacity = healthCapacity;
        this.healthTextures = healthTextures;
        this.reward = reward;
        remainingHealth = healthCapacity;
        healthIndex = healthTextures.length - 1;
    }

    /**
     * Calling this method will reduce this enemy's remaining health by the argued value. If calling this method reduces the remaining health to less than or equal to zero, then it will remove itself from the GameWorld.
     * @param costToHealth  How much damage should the enemy take?
     */
    public void dealDamage(int costToHealth){
        remainingHealth-=costToHealth;
        if(remainingHealth<=0) {
            getEntity().removeFromWorld();
            inc("money", reward);
        }
        updateSprite();
    }

    /**
     * This method checks this enemy's remaining health and, if necessary, updates its sprite to reflect remaining health.
     */
    private void updateSprite(){
        int tempHealthIndex = (int) Math.floor((double) remainingHealth /healthCapacity * (healthTextures.length-0.0000001));

        if (tempHealthIndex<0)
            tempHealthIndex=0;
        if(tempHealthIndex>=healthTextures.length)
            tempHealthIndex=healthTextures.length -1;

        if(tempHealthIndex != healthIndex){
            healthIndex = tempHealthIndex;
            getEntity().getViewComponent().clearChildren();
            getEntity().getViewComponent().addChild(healthTextures[healthIndex]);
        }
    }
}
