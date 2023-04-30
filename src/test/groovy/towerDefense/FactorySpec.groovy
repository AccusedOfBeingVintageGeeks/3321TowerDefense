package towerDefense

import com.almasb.fxgl.app.GameApplication
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.GameWorld
import com.almasb.fxglgames.towerDefense.TowerComponent
import com.almasb.fxglgames.towerDefense.TowerDefenseApp
import spock.lang.Shared
import spock.lang.Specification

import java.sql.Time

import static com.almasb.fxgl.dsl.FXGL.*

class FactorySpec extends Specification {

    @Shared
    GameApplication gameApp = new TowerDefenseApp()

    @Shared
    GameWorld world

    @Shared
    def thread

    synchronized void pause(){ wait(3000) }

    def setupSpec() {
        // Start the FXGL engine before running tests

        thread = Thread.start {
            gameApp.embeddedLaunch(TowerDefenseApp.main())
            //pause()
            world = getGameWorld()
            gameApp.setLevel("first_level", "waveDataListA")
            getGameController().startNewGame()
        }
        pause()
        //pause()

    }

    def cleanupSpec() {
        // Shutdown the FXGL engine after all tests have completed
        gameApp.embeddedShutdown()
    }

    def "test newTestEntity"() {
        given:
        boolean xkcd
        when: "fdjkl"
        List<Entity> towerEntities = getGameWorld().getEntitiesByComponent(TowerComponent.class)
        then: "fhdjkls"
        1 == 1
    }

}
