### Poisson Disc Sampling
Poisson Disc Sampling is random position generation based on the distance to other random spots. The algorithm randomly generates positions but as long as they aren't close enough to another randomly generated position.  
[Poisson Disc Sampling Wikipedia](https://en.wikipedia.org/wiki/Supersampling#Poisson_disc)  
UML:
#### PoissonDS.java
##### Fields
`-` scl:`int`  
`-` width:`int`  
`-` height:`int`  
`-` k:`int`  
`-` grid:`Vector2F[][]`  
`-` active:`List`  
##### Methods
`+` PoissonDS()  
`+` mouse(`float`, `float`, `boolean`, `int`):`void`  
`+` **main**(`String[]`):`void`  
`+` update(`int`):`void`  
`+` paint(`G2D`):`void`  
`+` inBound(`int`, `int`):`boolean`
***