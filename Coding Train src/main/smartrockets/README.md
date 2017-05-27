### Smart Rockets (Doesn't really work well)
Smart rockets that use a genetic algorithm to find the best path to a specific spot. Once all the rockets in the current generation die off, the most fit rocket's `DNAs` are copied and slightly mutated and placed in a new rocket. Using this algorithm, the best rocket will end up getting to that position.  
[Jer Thorpe's Smart Rockets](http://www.blprnt.com/smartrockets) (Good algorithm, and works great)  
[Genetic Algorithm](https://en.wikipedia.org/wiki/Genetic_algorithm)  
UML:
#### DNA.java
##### Fields
`+` velocities:`Vector2F[]`  
##### Methods
`+` DNA()  
`-` DNA(`Vector2F[]`)  
`+` **child**(`DNA`, `DNA`):`DNA`
***
#### Rocket.java
##### Fields
`+` pos:`Vector2F`  
`+` vel:`Vector2F`  
`-` acc:`Vector2F`  
`+` dna:`DNA`  
`+` dead:`boolean`  
`+` complete:`boolean`  
`+` worth:`float`  
##### Methods
`+` Rocket(`DNA`)  
`+` update():`void`  
`+` draw(`G2D`):`void`  
`+` applyForce(`Vector2F`):`void`
***
#### SmartRockets.java
##### Fields
`+` **MAX**:`int`  
`-` target:`Vector2F`  
`-` rockets:`Rocket[]`  
`-` obs:`Rectangle`  
`-` frame:`int`  
`-` gen:`int`  
##### Methods
`+` SmartRockets()  
`+` **main**(`String[]`):`void`  
`+` update(`int`):`void`  
`+` paint(`G2D`):`void`  
`+` evolve():`void`
***