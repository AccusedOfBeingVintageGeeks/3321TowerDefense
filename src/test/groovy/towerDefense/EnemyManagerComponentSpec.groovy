package towerDefense

import com.almasb.fxglgames.towerDefense.EnemyManagerComponent
import javafx.scene.Node
import javafx.scene.shape.Circle
import javafx.scene.shape.Shape
import spock.lang.Specification

import static com.almasb.fxgl.dsl.FXGL.texture
import static com.almasb.fxgl.dsl.FXGL.texture
import static com.almasb.fxgl.dsl.FXGL.texture

class EnemyManagerComponentSpec extends Specification {
    def "getHealthIndex returns health index"() {
        given:

        int testIndex
        Node[] nodes = new Node[]{//low index = low health
                new Circle(), new Circle()
        }
        EnemyManagerComponent comp = new EnemyManagerComponent(2, nodes, 20)

        when:
        def getHealthIndex = comp.getClass().getDeclaredMethod("getHealthIndex")
        getHealthIndex.setAccessible(true)

        then:
        getHealthIndex.invoke(comp) == 1
    }
}
