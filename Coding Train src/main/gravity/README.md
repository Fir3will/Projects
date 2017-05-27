### Gravity (INCOMPLETE)
This project involves the physics and formulas of gravity. Specifically Newton's theory of gravitation which is
```
F = G(m1 * m2) / d * d
```
This formula calculates the gravitational force between two bodies using their distance, `d`, their masses, `m1` and `m2`, and the gravitational constant, `G` (which is `6.67408(31) * 10^(-11)`).
This program also uses Swing components to add or remove planets.  
[Gravity Wikipedia](https://en.wikipedia.org/wiki/Gravity)  
UML:
#### Gravity.java
##### Fields
`+` **G**:`float`  
`-` zoom:`float`  
`-` lastPress:`Vector2F`  
`-` move:`Vector2F`  
`+` planets:`List`  
`-` paused:`boolean`  
`-` debugInfo:`boolean`  
`-` aft:`AffineTransform`  
`-` lst:`PlanetList`  
##### Methods
`+` Gravity()  
`+` **main**(`String[]`):`void`  
`+` update(`int`):`void`  
`+` key(`int`, `char`, `boolean`):`void`  
`+` mouseMoved(`float`, `float`):`void`  
`+` mouseWheel(`int`):`void`  
`+` paint(`G2D`):`void`  
`+` mouse(`float`, `float`, `boolean`, `int`):`void`
***
#### Planet.java
##### Fields
`+` name:`String`  
`+` pos:`Vector2F`  
`+` vel:`Vector2F`  
`+` acc:`Vector2F`  
`+` mass:`float`  
`+` trails:`List`  
`-` lastTick:`int`  
##### Methods
`+` Planet(`String`, `float`, `float`, `float`)  
`+` toString():`String`  
`+` acceleration(`Vector2F`):`Vector2F`  
`+` updatePlanet(`int`):`void`  
`+` paintPlanet(`G2D`, `boolean`):`void`  
`+` setVelocity(`float`, `float`):`Planet`
***
#### PlanetList.java
##### Fields
`-` game:`Gravity`  
`-` planetModel:`DefaultListModel`  
`-` addButton:`JButton`  
`-` deleteButton:`JButton`  
`-` nameField:`JTextField`  
`-` planetList:`JList`  
`-` posXField:`JFormattedTextField`  
`-` posYField:`JFormattedTextField`  
`-` velXField:`JFormattedTextField`  
`-` velYField:`JFormattedTextField`  
`-` accXField:`JFormattedTextField`  
`-` accYField:`JFormattedTextField`  
`-` **serialVersionUID**:`long`  
##### Methods
`+` PlanetList(`Gravity`)  
`+` actionPerformed(`ActionEvent`):`void`  
`+` refresh():`void`  
`+` refreshList():`void`  
`+` setCompsEnabled(`boolean`):`void`  
`-` getInputMass():`float`
***