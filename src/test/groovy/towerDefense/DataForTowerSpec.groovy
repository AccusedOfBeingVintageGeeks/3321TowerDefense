package towerDefense

import com.almasb.fxglgames.towerDefense.DataForTower
import spock.lang.Specification

class DataForTowerSpec extends Specification {


    def "test effects"() {
        given:
        String listString = " E "
        String test
        LinkedList<String> list = new LinkedList<String>()
        list.add(listString)
        DataForTower data = new DataForTower(" ", " " , 1, 1.0, 1.0, 1, 1, " ", 1, 1, list)

        when:
        test = data.effects().get(0)
        then:
        test == listString
    }

    def "test name"() {
        given:
        String name = "Me"
        String test
        DataForTower data = new DataForTower(name, " " , 1, 1.0, 1.0, 1, 1, " ", 1, 1, new LinkedList<String>())

        when:
        test = data.name()
        then:
        test == name
    }

    def "test imageName"() {
        given:
        String imgName = "img"
        String test
        DataForTower data = new DataForTower(" ", imgName , 1, 1.0, 1.0, 1, 1, " ", 1, 1, new LinkedList<String>())

        when:
        test = data.imageName()
        then:
        test == imgName
    }

    def "test cost"() {
        given:
        int cost = 1
        int test
        DataForTower data = new DataForTower(" ", " " , cost, 1.0, 1.0, 1, 1, " ", 1, 1, new LinkedList<String>())

        when:
        test = data.cost()
        then:
        test == cost
    }

    def "test fireRadius"() {
        given:
        double fireRad = 1.0
        double test
        DataForTower data = new DataForTower(" ", " " , 1, fireRad, 1.0, 1, 1, " ", 1, 1, new LinkedList<String>())

        when:
        test = data.fireRadius()
        then:
        test == fireRad
    }

    def "test fireRate"() {
        given:
        double fireRate = 1.0
        double test
        DataForTower data = new DataForTower(" ", " " , 1, 1.0, fireRate, 1, 1, " ", 1, 1, new LinkedList<String>())

        when:
        test = data.fireRate()
        then:
        test == fireRate
    }

    def "test projectileSpeed"() {
        given:
        int projSpeed = 1
        int test
        DataForTower data = new DataForTower(" ", " " , 1, 1.0, 1.0, projSpeed, 1, " ", 1, 1, new LinkedList<String>())

        when:
        test = data.projectileSpeed()
        then:
        test == projSpeed
    }

    def "test projectileDamage"() {
        given:
        int projDmg = 1
        int test
        DataForTower data = new DataForTower(" ", " " , 1, 1.0, 1.0, 1, projDmg, " ", 1, 1, new LinkedList<String>())

        when:
        test = data.projectileDamage()
        then:
        test == projDmg
    }

    def "test projectileImageName"() {
        given:
        String imgName = "img"
        String test
        DataForTower data = new DataForTower(" ", " " , 1, 1.0, 1.0, 1, 1, imgName, 1, 1, new LinkedList<String>())

        when:
        test = data.projectileImageName()
        then:
        imgName == test
    }

    def "test projectileHeight"() {
        given:
        int projHeight = 1
        int test
        DataForTower data = new DataForTower(" ", " " , 1, 1.0, 1.0, 1, 1, " ", projHeight, 1, new LinkedList<String>())

        when:
        test = data.projectileHeight()
        then:
        test == projHeight
    }

    def "test projectileWidth"() {
        given:
        int projWidth = 1
        int test
        DataForTower data = new DataForTower(" ", " " , 1, 1.0, 1.0, 1, 1, " ", 1, projWidth, new LinkedList<String>())

        when:
        test = data.projectileWidth()
        then:
        test == projWidth
    }
}
