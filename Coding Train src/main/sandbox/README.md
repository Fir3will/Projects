### Sandbox
This is meant to be a physics sandbox, not using any engines, all self-coded physics, etc.  
UML:
#### Sandbox.java
##### Fields
`-` size:`int`  
`-` balls:`Ball[]`  
`-` lastTime:`double`  
`-` startSpot:`Vector2F`  
##### Methods
`+` Sandbox()  
`+` **main**(`String[]`):`void`  
`+` update(`int`):`void`  
`+` mouseMoved(`float`, `float`):`void`  
`+` mouse(`float`, `float`, `boolean`, `int`):`void`  
`+` paint(`G2D`):`void`
#### Ball.java
##### Fields
`+` pos:`Vector2F`  
`+` vel:`Vector2F`  
`+` clr:`Color`  
`+` radius:`float`  
`-` **DAMPING**:`float`  
`-` **RESTITUTION**:`float`  
##### Methods
`+` Ball(`float`)  
`+` update(`float`):`void`  
`+` resolveCollision(`float`, `Vector2F`, `Vector2F`, `float`):`void`  
`+` collided(`Ball`):`boolean`
***