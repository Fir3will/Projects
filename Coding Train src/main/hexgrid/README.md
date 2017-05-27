### Hexagonal Grid (INCOMPLETE)
This program generates a hexagonal grid from a 2D `HexCell` grid. There are definitely some ways to improve this and make it run better. One problem that I need to fix is when a cell is clicked, it loops through them all to check which one contains the point at where the mouse was clicked. This is extremely inefficient and can be optimized.  
[Hexagonal Grid Wikipedia](https://en.wikipedia.org/wiki/Hexagonal_tiling)  
UML:
#### GameScreen.java
##### Fields
`-` grid:`HexCell[][]`  
`-` cw:`int`  
`-` ch:`int`  
`-` actualZoom:`float`  
`-` zoom:`float`  
`-` lastPress:`Vector2F`  
`-` move:`Vector2F`  
`-` aft:`AffineTransform`  
`-` sx:`int`  
`-` sy:`int`  
`+` game:`HexGrid`  
##### Methods
`+` GameScreen(`HexGrid`)  
`+` mouseWheel(`int`):`void`  
`+` updateScreen(`int`):`void`  
`+` paintScreen(`G2D`, `float`, `float`):`void`  
`+` mouse(`float`, `float`, `boolean`, `int`):`void`  
`+` mouseMoved(`float`, `float`):`void`  
`-` transform(`Vector2F`):`Vector2F`  
`+` getCell(`int`, `int`):`HexCell`  
`+` inBounds(`int`, `int`):`boolean`
***
#### HexCell.java
##### Fields
`+` x:`int`  
`+` y:`int`  
`+` rx:`float`  
`+` ry:`float`  
`-` smallR:`float`  
`-` bigR:`float`  
`-` poly:`Polygon`  
`-` **size**:`float`  
`-` **cos30**:`float`  
##### Methods
`+` HexCell(`int`, `int`)  
`+` contains(`float`, `float`):`boolean`  
`+` paintCell(`G2D`):`void`  
`+` updateCell(`int`):`void`
***
#### HexGrid.java
##### Fields
##### Methods
`+` HexGrid()  
`+` **main**(`String[]`):`void`  
`+` update(`int`):`void`  
`+` initialize():`void`  
`+` paint(`G2D`):`void`
***