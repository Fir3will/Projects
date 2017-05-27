### Binary Tree
This program visualizes binary trees. It branches each node in the tree and draws it onto the canvas.  Binary trees are where as items are added to the tree, it places the new item in the perfect spot. Because of the tree's layout, it cannot accept duplicate values.  
[Binary Tree Wikipedia](https://en.wikipedia.org/wiki/Binary_tree)  
UML:
#### BinaryTree.java
##### Fields
`+` root:`Node`  
##### Methods
`+` BinaryTree()  
`+` **main**(`String[]`):`void`  
`+` update(`int`):`void`  
`+` paint(`G2D`):`void`  
`+` paint(`G2D`, `Node`, `int`, `int`, `int`, `int`):`void`
***
#### Node.java
##### Fields
`+` val:`int`  
`+` left:`Node`  
`+` right:`Node`  
##### Methods
`+` Node(`int`)  
`+` print():`void`  
`+` addNode(`Node`):`void`
***