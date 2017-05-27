### Flowfield (also known as vector field)
Flowfields are interesting structures in which each coordinate on the plane has a specific vector, or the way that it flows. In this case there are particles that are randomly distributed on a dynamic flow field. The vectors on the flow field gradually change and shift around using an `OpenSimplexNoise` algorithm.  
[Vector Field Wikipedia](https://en.wikipedia.org/wiki/Vector_field)  
UML:
#### FlowField.java
##### Fields
`-` scale:`int`  
`-` w:`int`  
`-` h:`int`  
`-` osn:`OpenSimplexNoise`  
`-` vecs:`Vector2F[][]`  
`-` particles:`List`  
##### Methods
`+` FlowField()  
`+` mouse(`float`, `float`, `boolean`, `int`):`void`  
`+` **main**(`String[]`):`void`  
`+` update(`int`):`void`  
`+` paint(`G2D`):`void`
***
#### Particle.java
##### Fields
`+` pos:`Vector2F`  
`+` vel:`Vector2F`  
##### Methods
`+` Particle()  
`+` update():`void`  
`+` applyForce(`Vector2F`):`void`
***