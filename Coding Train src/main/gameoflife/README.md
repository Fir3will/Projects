### Noise (OpenSimplex Noise/Perlin Noise)
OpenSimplexNoise is an algorithm that generates uniform random numbers based on the position given. This program visualizes the algorithm. For example:  
*Random Noise*  
{+-------------------------------------------------}: 0.013221741  
{------------------------------------------------+-}: 0.9606778  
{------+-------------------------------------------}: 0.1387335  
{------+-------------------------------------------}: 0.12685984  
{-----------------+--------------------------------}: 0.35229045  
{-----------------------+--------------------------}: 0.4767576  
{-----+--------------------------------------------}: 0.1033057  
*OpenSimplex Noise*  
{--------+-----------------------------------------}: 0.17801863  
{----------+---------------------------------------}: 0.20594329  
{------------+-------------------------------------}: 0.2592366  
{---------------+----------------------------------}: 0.31850594  
{------------------+-------------------------------}: 0.3646607  
{-------------------+------------------------------}: 0.3905292  
{--------------------+-----------------------------}: 0.40001208  

As is clearly visible, in `OpenSimplex Noise`, the change in random numbers is much more uniform, unlike basic random numbers.

[OpenSimplex Noise Wikipedia](https://en.wikipedia.org/wiki/OpenSimplex_noise)  
[Perlin Noise Wikipedia](https://en.wikipedia.org/wiki/Perlin_noise) (A bit different but more in-depth)   
UML:
#### Noise.java
##### Fields
`-` scale:`int`  
`-` w:`int`  
`-` h:`int`  
`-` osn:`OpenSimplexNoise`  
##### Methods
`+` Noise()  
`+` **main**(`String[]`):`void`  
`+` update(`int`):`void`  
`+` paint(`G2D`):`void`
***