### Sound Visualization
This program generates waves depending on the volume of the sound. The WAV files aren't provided, nor are they from user input, they just are hard coded into the program. Like so: `SoundMapping.class.getResource("/main/sound/test.wav");` so there isn't any way to change that.  
[Music Visualization Wikipedia](https://en.wikipedia.org/wiki/Music_visualization)  
UML:
#### SoundMapping.java
##### Fields
`-` zoom:`float`  
`-` heights:`float[]`  
`-` fmt:`AudioFormat`  
`-` clip:`Clip`  
##### Methods
`+` SoundMapping()  
`+` mouseWheel(`int`):`void`  
`+` **main**(`String[]`):`void`  
`+` update(`int`):`void`  
`+` key(`int`, `char`, `boolean`):`void`  
`+` initialize():`void`  
`+` paint(`G2D`):`void`
***