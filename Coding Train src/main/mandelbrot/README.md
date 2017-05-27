### Mandelbrot Set
Ahh, the Mandelbrot Set. A fascinating mathematic algorithm in which imaginary numbers and repetition are key focus. What this algorithm does is, it multiplies two complex numbers together from coordinate points to get a value of how many iterations does it take the equation to get above a specific threshold. In this case the threshold is `16`, which can be changed.  
[Mandelbrot Set Wikipedia](https://en.wikipedia.org/wiki/Mandelbrot_set)  
UML:
#### Mandelbrot.java
##### Fields
`-` iters:`int`  
`-` scale:`int`  
`-` w:`int`  
`-` h:`int`  
##### Methods
`+` Mandelbrot()  
`+` **main**(`String[]`):`void`  
`+` update(`int`):`void`  
`+` paint(`G2D`):`void`
***