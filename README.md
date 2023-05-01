# 3321TowerDefense
You're in the repo of a Tower Defense game built as a class project for [Professor Minhaz Zibran](https://github.com/zibranm)'s CS 3321 at Idaho State University. The project is built on the [FXGL game library/engine](http://almasb.github.io/FXGL/) developed by [Professor Almas Baimagambetov](https://github.com/AlmasB) at the University of Brighton, which itself extends JavaFX.

You can find the wiki [here](https://github.com/AccusedOfBeingVintageGeeks/3321TowerDefense/wiki).

## Team members:
* [Grant Madson](https://github.com/madson812)
* [Koda Koziol](https://github.com/Dakoziol)
* [Andreas Kramer](https://github.com/kramandr)

## Diagrams
* [Class Diagram](https://github.com/AccusedOfBeingVintageGeeks/3321TowerDefense/wiki/Class-Diagram)
* [Dependency Diagram and SBOM](https://github.com/AccusedOfBeingVintageGeeks/3321TowerDefense/wiki/SBOM)
* [Sequence Diagrams](https://github.com/AccusedOfBeingVintageGeeks/3321TowerDefense/wiki/Sequence-Diagrams)
* [Use Case Diagram](https://github.com/AccusedOfBeingVintageGeeks/3321TowerDefense/wiki/Use-Case-Diagram)


## On Unit Testing
We have some unit tests, but we ran into challenges when trying to thoroughly test our builds just due to the nature of our project. Firstly, because game development is technically a visual art, a lot of the code is for layout and other subjective tasks that we couldn't really design tests around. Second, although our reliance on the FXGL library made development quick, it also complicated testing. Large chunks of our project have to extend FXGL classes and override FXGL methods, but we've done our best to extract our logic into testable methods. For what it's worth, we contacted FXGL's primary developer, Almas Baimagambetov, and he assured us that "The FXGL engine itself is tested internally already". For transparency's sake, we wanted to share this.


