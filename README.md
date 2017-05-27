# Projects
## Coding Train
These codes are, loosely, based on the youtuber. He programs simple and practical programming challenges and I try and replicate the challenges, mostly using my own game engine. There are also other programs that I've tried out there as well. These codes include (in order):  
_[Beesweeper (Minesweeper)](../master/Coding%20Train%20src/main/beesweeper)_  
_[Binary Tree visualization](../master/Coding%20Train%20src/main/binarytree)_  
_[Diffusion-Limited Aggregation](../master/Coding%20Train%20src/main/diffusionlimitedaggregation)_  
_[Evolutionary Steering Behaviors](../master/Coding%20Train%20src/main/evolutionarysb)_  
_[Visualized Flowfield](../master/Coding%20Train%20src/main/flowfield)_  
_[Forward Kinematics](../master/Coding%20Train%20src/main/forwardkinematics)_  
_[Gravitational Physics (Gravity)](../master/Coding%20Train%20src/main/gravity)_  
_[Hexagonal Grid](../master/Coding%20Train%20src/main/hexgrid)_  
_[Inverse Kinematics](../master/Coding%20Train%20src/main/inversekinematics)_  
_[Mandelbrot Set](../master/Coding%20Train%20src/main/mandelbrot)_  
_[Maze Generation and Solving](../master/Coding%20Train%20src/main/maze)_  
_[Metaballs (Just try it)](../master/Coding%20Train%20src/main/metaballs)_  
_[Open Simplex Noise (Perlin Noise)](../master/Coding%20Train%20src/main/noise)_  
_[Rocket Propulsion in Space using Gravity Physics](../blob/master/Coding%20Train%20src/main/planetjumper)_  
_[Poisson Disc Sampling](../master/Coding%20Train%20src/main/poissonds)_  
_[Evolutionary Smart Rockets](../master/Coding%20Train%20src/main/smartrockets)_  
_[Sound Visualization](../master/Coding%20Train%20src/main/sound)_  
_[Sudoku](../master/Coding%20Train%20src/main/sound)_  
_[Traveling Salesperson Algorithm](../master/Coding%20Train%20src/main/travelingsalesperson)_  
_[Genetic Algorithm of the Traveling Salesperson Algorithm](../master/Coding%20Train%20src/main/tsp_ga)_  

All mini-projects have their own README.md files in which class UMLs are provided and more in-depth explanations of each project is provided.
## Game Base
This is the code for the Java game engine that I created. The system itself used Swing and draws to a `JPanel`. But even so, it provides a lot of usable interfaces for proper rendering. Here's an example of what a starting file would look like
```java
import main.G2D;
import main.Game;
import main.GameSettings;
import main.GameSettings.Quality;
import main.Main;
import java.awt.Color;

public class Main extends Game
{
  private int x;
  
  public Main()
  {
    System.out.println("Initialization");
  }
  
  @Override
  public void update(int ticks)
  {
    System.out.println("Runs every tick.");
  }
  
  @Override
  public void paint(G2D g2d)
  {
    g2d.setColor(Color.RED);
    g2d.drawRectangle(0, 0, x++, g2d.height - 1);
    System.out.println("Runs every tick also.");
    // But you can use g2d to render to the canvas.
  }
	
  public static void main(String[] args)
  {
    Main game = new Main();
		
    GameSettings settings = new GameSettings();
    settings.title = "Test";
    settings.version = "0.0.1";
    settings.quality = Quality.POOR;
    settings.width = 1024;
    settings.height = 768;
    settings.showFPS = true;
    settings.background = Color.WHITE;
    settings.maxFPS = -1;

    Main.initialize(game, settings);
  }
}
```

Very easy to use and fully customizeable. Some might even consider it vunerable.
## Library
This project contains all the practical code snippets that I've collected over the years. I've concatenated it into one library which is included in `Game Base` and also `Coding Train`
