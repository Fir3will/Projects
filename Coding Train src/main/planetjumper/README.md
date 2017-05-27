### Planet Jumper
This branches off of the the Gravity project that I created. I highly suggest reading that as this is very similar. But in general, this is different in the sense that the user controls a small rocket that they can thrust around the planet in the center. Very fun to play with and helps learn a lot about gravity and the physics behind it.  
[Gravity Project](https://github.com/Fir3will/Projects/tree/master/Coding%20Train%20src/main/gravity)  
UML:
#### Jumper.java
##### Fields
`+` **G**:`float`  
`+` planets:`List`  
`+` player:`Player`  
`-` path:`Vector2F[]`  
`-` sas:`boolean`  
`-` prograde:`boolean`  
`-` retrograde:`boolean`  
##### Methods
`+` Jumper()  
`+` **main**(`String[]`):`void`  
`+` update(`int`):`void`  
`+` key(`int`, `char`, `boolean`):`void`  
`+` paint(`G2D`):`void`
***
#### Planetoid.java
##### Fields
`+` name:`String`  
`+` pos:`Vector2F`  
`+` mass:`float`  
##### Methods
`+` Planetoid(`String`, `float`, `float`, `float`)  
`+` toString():`String`  
`+` acceleration(`Vector2F`):`Vector2F`  
`+` paintPlanet(`G2D`):`void`
***
#### Player.java
##### Fields
`+` game:`Jumper`  
`+` pos:`Vector2F`  
`+` vel:`Vector2F`  
`+` acc:`Vector2F`  
`+` rot:`float`  
`+` rotVel:`float`  
##### Methods
`+` Player(`Jumper`)  
`+` updatePlayer(`int`):`void`  
`+` paintPlayer(`G2D`):`void`
***