### Maze Generation and Solving
This algorithm generates a maze with a given size and allows the user to click on two points on the maze. Then the program finds the shortest path between the two points.  
[Maze Generation Algorithm Wikipedia](https://en.wikipedia.org/wiki/Maze_generation_algorithm)  
[Maze Solving Algorithm Wikipedia](https://en.wikipedia.org/wiki/Maze_solving_algorithm)  
UML:
#### Cell.java
##### Fields
`+` x:`int`  
`+` y:`int`  
`+` walls:`boolean[]`  
`-` visited:`boolean`  
##### Methods
`+` Cell(`int`, `int`)  
`+` visit():`void`  
`+` visited():`boolean`  
`+` drawCell(`G2D`):`void`  
`+` hasWallTo(`Cell`):`boolean`
***
#### Maze.java
##### Fields
`+` **SIZE**:`int`  
`-` stack:`Deque`  
`-` cells:`Cell[][]`  
`-` solve:`boolean`  
`-` done:`boolean`  
`-` justComplete:`boolean`  
`-` current:`Cell`  
`-` look:`Cell`  
`-` start:`Cell`  
`-` end:`Cell`  
`-` correctPath:`Deque`  
`-` pathArray:`Cell[]`  
`-` wasHere:`boolean[][]`  
##### Methods
`+` Maze()  
`+` mouse(`float`, `float`, `boolean`, `int`):`void`  
`+` **main**(`String[]`):`void`  
`+` update(`int`):`void`  
`+` key(`int`, `char`, `boolean`):`void`  
`+` paint(`G2D`):`void`  
`+` getRandomUnvisitedNeighbor(`Cell`):`Cell`  
`+` removeWallBetween(`Cell`, `Cell`):`void`  
`+` getCell(`int`, `int`):`Cell`  
`+` getRandomNotSeen(`Cell`):`Cell`  
`+` beginSolve():`void`
***
#### Side.java
##### Fields
`+` **NORTH**:`Side`  
`+` **SOUTH**:`Side`  
`+` **WEST**:`Side`  
`+` **EAST**:`Side`  
`+` xOff:`int`  
`+` yOff:`int`  
##### Methods
`-` Side(`String`, `int`, `int`, `int`)  
`+` **get**(`int`):`Side`  
`+` **values**():`Side[]`  
`+` **valueOf**(`String`):`Side`  
`+` **size**():`int`  
`+` getOpposite():`Side`
***