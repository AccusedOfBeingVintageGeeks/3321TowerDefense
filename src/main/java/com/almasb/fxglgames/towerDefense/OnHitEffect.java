package com.almasb.fxglgames.towerDefense;

import com.almasb.fxgl.dsl.components.Effect;
public class OnHitEffect {

    private Effect effect;
    private double chance;

    public OnHitEffect(Effect effect, double chance) {
        this.effect = effect;
        this.chance = chance;
    }

    public Effect getEffect() {
        return effect;
    }

    public double getChance() {
        return chance;
    }
}
