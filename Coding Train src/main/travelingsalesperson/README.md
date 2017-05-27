### Traveling Salesperson
The traveling salesperson problem is a great way to learn a language. The problem, basically is, there are a number of cities that the salesperson has to get to. What the algorithm has to do is calculate the best path to get there. This could be the shortest, the quickest, or easiest. And the algorithm has to be able to calculate all that. The way this algorithm works is it randomizes the order constantly until it finds one better than the current best. If it is better, then the new one becomes the best one and the cycle restarts infinitely.
[Traveling Salesperson Problem Wikipedia](https://en.wikipedia.org/wiki/Travelling_salesman_problem)  
UML:
#### TravelingSalesperson.java
##### Fields
`-` amtOfCities:`int`  
`-` cities:`Vector2F[]`  
`-` bestOrder:`int[]`  
`-` bestDist:`double`  
`-` order:`int[]`  
`-` orderDist:`double`  
##### Methods
`+` TravelingSalesperson()  
`+` **main**(`String[]`):`void`  
`+` update(`int`):`void`  
`+` paint(`G2D`):`void`  
`-` calculateDist(`int[]`):`double`
***