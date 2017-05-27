### Forward Kinematics
I can't explain it in a `README`, I suggest running the program or watching Daniel Shiffman's explanation on it.  
[Forward Kinematics Wikipedia](https://en.wikipedia.org/wiki/Forward_kinematics)  
UML:
#### ForwardKinematics.java
##### Fields
`-` segments:`Segment[]`  
##### Methods
`+` ForwardKinematics()  
`+` **main**(`String[]`):`void`  
`+` update(`int`):`void`  
`+` paint(`G2D`):`void`
***
#### Segment.java
##### Fields
`+` a:`Vector2F`  
`+` b:`Vector2F`  
`+` length:`float`  
`+` angle:`float`  
##### Methods
`+` Segment(`float`, `float`, `float`)  
`+` update():`void`  
`+` show(`G2D`):`void`  
`+` calculateEnd():`void`
***