package com.almasb.fxglgames.towerDefense;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.entity.Entity;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.animationBuilder;

public class Animations {

    public static void playVisualEffectSlowAnimation(Entity visualEffect) {
        animationBuilder()
                .onFinished(() ->
                        visualEffect.addComponent(new ExpireCleanComponent(Duration.seconds(0.5)).animateOpacity())
                )
                .duration(Duration.seconds(0.75))
                .interpolator(Interpolators.EXPONENTIAL.EASE_OUT())
                .translate(visualEffect)
                .from(visualEffect.getPosition().add(-10, 0))
                .to(visualEffect.getPosition().add(-10, 20))
                .buildAndPlay();
    }
}
