package towerDefense

import com.almasb.fxgl.entity.Entity
import com.almasb.fxglgames.towerDefense.TowerProjectileComponent
import spock.lang.Shared
import spock.lang.Specification

class TowerProjectileComponentSpec extends Specification {

    def "initializes correctly"() {
        given:
        Entity entity = new Entity()
        int speedvar = 10, damagevar = 15
        TowerProjectileComponent comp = new TowerProjectileComponent(entity, speedvar, damagevar)

        when:
        def prey = comp.getClass().getDeclaredField("prey")
        def speed = comp.getClass().getDeclaredField("speed")
        def damage = comp.getClass().getDeclaredField("damage")
        prey.setAccessible(true)
        speed.setAccessible(true)
        damage.setAccessible(true)

        then:
        entity == prey.get(comp)
        speedvar == speed.get(comp)
        damagevar == damage.get(comp)
    }

    //Cannot test remaining methods due to FXGL calls

}
