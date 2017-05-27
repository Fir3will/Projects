### Inverse Kinematics
I can't explain it in a `README`, I suggest running the program or watching Daniel Shiffman's explanation on it.  
[Inverse Kinematics Wikipedia](https://en.wikipedia.org/wiki/Inverse_kinematics)  
UML:
#### InverseKinematics.java
##### Fields
`-` segments:`Segment[]`  
##### Methods
`+` InverseKinematics()  
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
`+` follow(`float`, `float`):`void`  
`+` calculateEnd():`void`
***