### Metaballs
Metaballs are objects that change color depending on how far they are from other objects.  
[Metaballs Wikipedia](https://en.wikipedia.org/wiki/Metaballs)  
UML:
#### Metaballs.java
##### Fields
`-` scale:`int`  
`-` h:`int`  
`-` w:`int`  
`-` balls:`Metaball[]`  
`-` img:`BufferedImage`  
##### Methods
`+` Metaballs()  
`+` **main**(`String[]`):`void`  
`+` update(`int`):`void`  
`+` paint(`G2D`):`void`
***
#### Metaballs.Metaball.java
##### Fields
`+` pos:`Vector2F`  
`+` vel:`Vector2F`  
##### Methods
`+` Metaball(`Metaballs`)  
`+` update():`void`
***