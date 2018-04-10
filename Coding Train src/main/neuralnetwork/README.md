### Beesweeper
This program is a recreation of Minesweeper. But instead of mines, it's bees!  
[Minesweeper Wikipedia](https://en.wikipedia.org/wiki/Minesweeper_(video_game))  
UML:
#### Beesweeper.java
##### Fields
`+` size:`int`  
`+` cellSize:`int`  
`-` amtOfBees:`int`  
`-` amtOfBeesCaught:`int`  
`-` winState:`int`  
`-` cells:`Cell[][]`  
##### Methods
`+` Beesweeper()  
`+` mouse(`float`, `float`, `boolean`, `int`):`void`  
`+` **main**(`String[]`):`void`  
`+` update(`int`):`void`  
`+` key(`int`, `char`, `boolean`):`void`  
`-` reset(`int`):`void`  
`+` paint(`G2D`):`void`  
`+` getCell(`int`, `int`):`Cell`
***
#### Cell.java
##### Fields
`+` game:`Beesweeper`  
`+` x:`int`  
`+` y:`int`  
`+` revealed:`boolean`  
`+` flagged:`boolean`  
`+` state:`int`  
##### Methods
`+` Cell(`Beesweeper`, `int`, `int`)  
`+` paintCell(`G2D`):`void`  
`+` reveal():`void`  
`+` calcState():`void`
***