package com.almasb.fxglgames.towerDefence

import spock.lang.Specification

class IndexPairSpec extends Specification {
    def "test toString"() {
        when:
        IndexPair pair = new IndexPair(1,1)
        then: "ToString matches the format (x,y)"
        pair.toString() == "(1,1)"
    }

    def "IndexPair holds data correctly"() {

        when:
        IndexPair pair = new IndexPair(1,1)

        then: "IndexPair should hold the correct values"
        indexPair.X == x && indexPair.Y == y

        where:

        indexPair                       |      x        |       y
        new IndexPair(1,1)              |      1        |       1
        new IndexPair(1,2)              |      1        |       2
        new IndexPair(2,1)              |      2        |       1
        new IndexPair(2,2)              |      2        |       2

    }
}
