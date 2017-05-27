### Evolutionary Steering Behaviors
Evolutionary Steering Behaviors is an evolutionary algorithm which involves steering behaviors. To start, there are vehicles on the board. These vehicles can be steered using their velocities and accelerations. What the goal of each vehicle to do is to go towards the green food particles and stray away from the red poison particles. If they get a food particle, they'll increase in health, but if they consume a poison particle, their health will drastically deteriorate. The amount of force towards and away from the particles depends on the randomly generated `dna` that each vehicle is given. After a while of the vehicle accumulating food, and possible poison, it will generate a clone of itself with the same "genetics", dna. But this time, there is a slight chance of mutation so that the genes are altered slightly, proving to be good or bad.  
[Genetic Algorithm](https://en.wikipedia.org/wiki/Genetic_algorithm)  
UML:
#### EvolutionarySB.java
##### Fields
`+` spawnChance:`float`  
`+` vehicles:`List`  
`+` food:`List`  
`+` poison:`List`  
`-` debug:`boolean`  
`-` speed:`boolean`  
##### Methods
`+` EvolutionarySB()  
`+` **main**(`String[]`):`void`  
`+` update(`int`):`void`  
`+` key(`int`, `char`, `boolean`):`void`  
`+` reset():`void`  
`+` paint(`G2D`):`void`
***
#### Vehicle.java
##### Fields
`+` mutationRate:`float`  
`+` maxSpeed:`float`  
`+` maxForce:`float`  
`+` pos:`Vector2F`  
`+` vel:`Vector2F`  
`+` acc:`Vector2F`  
`+` dna:`float[]`  
`+` health:`float`  
##### Methods
`+` Vehicle(`float[]`)  
`+` clone():`Vehicle`  
`+` update():`void`  
`+` paint(`G2D`, `boolean`):`void`  
`+` seek(`Vector2F`):`Vector2F`  
`+` eat(`List`, `float`, `float`):`Vector2F`  
`+` isDead():`boolean`  
`+` act(`List`, `List`):`void`  
`+` checkBoundaries():`void`
***