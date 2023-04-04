package com.almasb.fxglgames.towerDefence

import spock.lang.Specification

class TowerDefenceAppSpec extends Specification{

    void setup(){
        //add setup code
    }

    def "math works"() {
        given:
        int arg1 = 1
        int arg2 = 2
        int result = 0

        when: "Add them together"
        result = arg1 + arg2

        then: "result should be 3"
        result == 2
    }

}