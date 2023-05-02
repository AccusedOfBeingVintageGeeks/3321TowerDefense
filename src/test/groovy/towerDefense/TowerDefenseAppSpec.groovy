package towerDefense

import com.almasb.fxgl.app.GameSettings
import com.almasb.fxglgames.towerDefense.TowerDefenseApp
import spock.lang.Specification

class TowerDefenseAppSpec extends Specification {


    //Looks like this was the only testable thing in the class. I was surprised I was able to get this much.
    def "setLevel sets correct vars"() {
        given:
        TowerDefenseApp app = new TowerDefenseApp()
        String levelName = "level", waveDataFile = "location"
        def lvlName = app.getClass().getDeclaredField("levelName")
        def waveData = app.getClass().getDeclaredField("waveDataFilename")
        lvlName.setAccessible(true)
        waveData.setAccessible(true)

        when: "Set level"
        app.setLevel(levelName, waveDataFile)

        then:
        lvlName.get(app) == levelName && waveData.get(app) == waveDataFile
    }

}
