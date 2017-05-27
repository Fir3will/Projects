### Traveling Salesperson using a Genetic Algorithm
I highly suggest looking at Traveling Salesperson project first before this one.

The genetic algorithm enables it to find the best path much quicker. Unlike the first project, what this allows the program to do is to alter the best by a little to find the next best algorithm. It might swap the first and last index of the best and check that against it. It is infinitely faster than the original algorithm.  
[Traveling Salesperson Problem Wikipedia](https://en.wikipedia.org/wiki/Travelling_salesman_problem)  
UML:
#### TSPGeneticAlgorithm.java
##### Fields
`-` amtOfCities:`int`  
`-` popSize:`int`  
`-` cities:`Vector2F[]`  
`-` bestOrder:`int[]`  
`-` bestDist:`double`  
`-` population:`int[][]`  
`-` populationFitness:`double[]`  
`-` generation:`int`  
##### Methods
`+` TSPGeneticAlgorithm()  
`+` **main**(`String[]`):`void`  
`+` update(`int`):`void`  
`+` paint(`G2D`):`void`  
`-` mutate(`int[]`):`void`  
`-` pickOne(`int[][]`, `double[]`):`int[]`  
`-` calculateFitness():`void`  
`-` nextGeneration():`void`  
`-` calculateDist(`int[]`):`double`  
`-` crossOver(`int[]`, `int[]`):`int[]`  
`-` normalizeFitness():`void`
***