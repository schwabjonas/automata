import javafx.application.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.paint.Color.*;
import javafx.animation.*;
import javafx.util.*;
import java.util.logging.*;


/*
Several Langton's Ant implementations.
Also includes Game Of Life.

Code separates the display and the actual Ant algorithms nicely.
It includes square and hexagon grids.
Code includes careful input checking.

Sources:
Hexagonal grids: https://www.redblobgames.com/grids/hexagons/
Ants:

*/
public class Machina extends Application {
  private static Logger myLogger = Logger.getLogger("Machina");

  Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.BLACK, Color.WHITE} ;
  //Grid related variables
  //Default values so I can launch grid without setting sizes
  static int cellWidth = 10;
  static int gridWidth = 71;
  static int totalPixel;
  static boolean showsGridBoundary = true;

  static boolean restarts = true;
  static boolean stopsAtEdge = false;
  static boolean showsMovie = true;
  static boolean countsIterations = false;
  static int iterations = 0;

  enum   GridType {SQUARE, HEXAGON};
  static GridType gridType = GridType.SQUARE;
  Canvas cellCanvas = null;
  Canvas gridCanvas = null;
  GraphicsContext gc = null;
  static int timeBetweenUpdates = 100;   // 100 is usually a good value to see individual updates
  static int timeCycles = 10; // number of cycles to run

  static int gen[][] = new int[gridWidth][gridWidth];
  static Automata auto =  new AntSqr(gen);

  static String letterStr;
  static Color[] colorArray;

  public static void main(String[] args){
    // Reads in command line options
    Logger.getLogger("").setLevel(Level.ALL);

    try{
      for (int i = 0; i < args.length; i++) {
        if(args[i].equals("-t")) {
          try {
            timeCycles = Integer.parseInt(args[i + 1]);
          } catch(IllegalArgumentException e) {
            System.err.println("-t option expects an integer as next argument.");
            throw new RuntimeException(e);
          } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("-t option expects an integer as next argument.");
            System.err.println("No argument was supplied.");
            throw new RuntimeException(e);
          }
        }
      }
      for (int i = 0; i < args.length; i++) {
        if(args[i].equals("-d")) {
          try {
            timeBetweenUpdates = Integer.parseInt(args[i + 1]);
          } catch(IllegalArgumentException e) {
            System.err.println("-d option expects an integer as next argument.");
            throw new RuntimeException(e);
          } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("-d option expects an integer as next argument.");
            System.err.println("No argument was supplied.");
            throw new RuntimeException(e);
          }
        }
      }
      for (int i = 0; i < args.length; i++) {
        if(args[i].equals("-c")) {
          try {
            cellWidth = Integer.parseInt(args[i + 1]);
          } catch(IllegalArgumentException e) {
            System.err.println("-c option expects an integer as next argument.");
            throw new RuntimeException(e);
          } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("-c option expects an integer as next argument.");
            System.err.println("No argument was supplied.");
            throw new RuntimeException(e);
          }
        }
      }
      for (int i = 0; i < args.length; i++) {
        if(args[i].equals("-s")) {
          try {
            gridWidth = Integer.parseInt(args[i + 1]);
          } catch(IllegalArgumentException e) {
            System.err.println("-s option expects an integer as next argument.");
            throw new RuntimeException(e);
          } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("-s option expects an integer as next argument.");
            System.err.println("No argument was supplied.");
            throw new RuntimeException(e);
          }
          gen = new int[gridWidth][gridWidth];
        }
      }
      for (int i = 0; i < args.length; i++) {
        if(args[i].equals("-b")) {
          showsGridBoundary = false;
        }
      }
      for (int i = 0; i < args.length; i++) {
        if(args[i].equals("-f")) {
          showsMovie = false;
        }
      }
      // Shows final picture when ant touches edge. No movie of individual steps.
      for (int i = 0; i < args.length; i++) {
        if(args[i].equals("-e")) {
          restarts = false;
          countsIterations = false;
          stopsAtEdge = true;
        }
      }
      // Shows final picture after number of iterations. No movie of individual steps.
      for (int i = 0; i < args.length; i++) {
        if(args[i].equals("-i")) {
          restarts = false;
          countsIterations = true;
          stopsAtEdge = false;
          try {
            iterations = Integer.parseInt(args[i + 1]);
          } catch(IllegalArgumentException e) {
            System.err.println("-i option expects an integer as next argument.");
            throw new RuntimeException(e);
          } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("-i option expects an integer as next argument.");
            System.err.println("No argument was supplied.");
            throw new RuntimeException(e);
          }
        }
      }
      // Shows final picture when ant touches edge. No movie of individual steps.
      for (int i = 0; i < args.length; i++) {
        if(args[i].equals("-o")) {
          restarts = true;
          countsIterations = false;
          stopsAtEdge = false;
        }
      }
      // Initialize the desired Automata
      // Maybe change name: AntSqr() -> AntRed()
      // Generalize Green and Red ants to take rotation letters. Langton's original ant is simply "RL"
      for (int i = 0; i < args.length; i++) {
        if(args[i].equals("-h")) {
          gridType = GridType.HEXAGON;
          try {
            letterStr = args[i + 1];
            if (!Machina.isValidHexString(letterStr)) {
              throw new IllegalArgumentException("-h argument is not made of letters NRSULM: " + letterStr);
            }
          } catch(IllegalArgumentException e) {
            System.err.println("-h argument is not made of letters NRSULM: " + letterStr);
            throw new RuntimeException(e);
          } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("-h option expects a string as next argument.");
            System.err.println("No argument was supplied.");
            throw new RuntimeException(e);
          }
          Machina.colorArray = createColorArray(letterStr.length() + 1);
          auto = new AntHex(gen, letterStr);
        } else if (args[i].equals("-r")) {
          gridType = GridType.SQUARE;
          auto = new AntSqr(gen);
        } else if(args[i].equals("-g")) {
          gridType = GridType.SQUARE;
          auto = new AntGreen(gen);
        } else if(args[i].equals("-l")) {
          gridType = GridType.SQUARE;
          auto = new Life(gen);
        }
      }

      for (int i = 0; i < args.length; i++) {
        if(args[i].equals("--help")) {
          System.out.println("Usage: SchwabJonasLab2 -c -s -b -f [-e|-i|-o] [-l|-r|-g|-h] --help");
          System.out.println("Grid specifications:");
          System.out.println(" -c  Sets cell size in number of Pixels. Default is 7.");
          System.out.println(" -s  Sets grid size in number of cells. Default is 71.");
          System.out.println(" -b  Shows no boundary of cells.");
          System.out.println("Boundary conditions:");
          System.out.println(" -f  Shows no movie of individual steps. Shows only final picture.");
          System.out.println(" -e  Stops when ant touches edge.");
          System.out.println(" -i  Stops after number of iterations.");
          System.out.println(" -o  Loops by restarting after ant touches edge.");
          System.out.println("Types of ants:");
          System.out.println(" -l  Creates Game of life on square grid.");
          System.out.println(" -r  Creates red ant on square grid.");
          System.out.println(" -g  Creates green ant on square grid.");
          System.out.println(" -h  Creates black ant on hexagonal grid. Requires rotational string argument.");
          System.exit(0);
        }
      }
    } catch (Exception e) {
      // e.printStackTrace();
      System.err.println("Error: InvocationTargetException");
      System.err.println("Error: 'java Machina --help");
      System.err.println("Error: This will print how to use Langton's ant.");
      System.exit(0);
    }
    // Calculate total pixels for convenience
    totalPixel = gridWidth * cellWidth + (gridWidth + 1);

    // Launch Application
    Application.launch(args);
  }

  public void start(final Stage primaryStage) {
    // Build square or hexagon grids
    if (gridType == GridType.HEXAGON) {
      //cellCanvas = new Canvas(totalPixel, totalPixel);
      cellCanvas = new Canvas(gridWidth*cellWidth, gridWidth*cellWidth);
      gridCanvas = canvasHexGrid(gridWidth, cellWidth);
    } else {
      cellCanvas = new Canvas(totalPixel, totalPixel);
      gridCanvas = canvasSqrGrid(totalPixel, cellWidth);
    }
    // Create root/node object. Stackpane stacks several canvases on top of each other
    StackPane myLayout = new StackPane();
    myLayout.getChildren().add(cellCanvas);
    if (showsGridBoundary) {
      myLayout.getChildren().add(gridCanvas);
      gridCanvas.toFront();
    }
    gc = cellCanvas.getGraphicsContext2D();
    // create scene object
    Scene myScene = new Scene(myLayout, totalPixel, totalPixel);
    // Finalizing the stage object
    primaryStage.setTitle("Cellular Automata - Output");
    primaryStage.setScene(myScene);
    primaryStage.show();

    if (showsMovie) {
      if (countsIterations) {
        displayGen(gen);
        KeyFrame k = new KeyFrame(Duration.millis(timeBetweenUpdates),
        e->{
          // Update Ant and display generation
          auto.updateGen(gen);
          displayGen(gen);
        });
        Timeline t = new Timeline(k);
        t.setCycleCount(iterations);
        //t.setCycleCount(Timeline.INDEFINITE);
        t.play();
      } else if (stopsAtEdge) {
        // figure out the cyclecount until ant isOnEdge
        int cycleCount = 0;
        while  (!auto.isOnEdge(gen)) {
          auto.updateGen(gen);
          cycleCount++;
        }
        // create the timeline using determined cyclecount
        displayGen(gen);
        KeyFrame k = new KeyFrame(Duration.millis(timeBetweenUpdates),
        e->{
          // Update Ant and display generation
          auto.updateGen(gen);
          displayGen(gen);
        });
        Timeline t = new Timeline(k);
        t.setCycleCount(cycleCount);
        t.play();
      } else if (restarts) {
        displayGen(gen);
        KeyFrame k = new KeyFrame(Duration.millis(timeBetweenUpdates),
        e->{
          // Update Ant and display generation
          auto.updateGen(gen);
          displayGen(gen);
        });
        Timeline t = new Timeline(k);
        //t.setCycleCount(timeCycles);
        t.setCycleCount(Timeline.INDEFINITE);
        t.play();
      }
    } else {
      if (countsIterations) {
        for (int i=0; i < iterations; i++) {
          auto.updateGen(gen);
        }
        displayGen(gen);
      } else if (stopsAtEdge) {
        while (!auto.isOnEdge(gen)) {
          auto.updateGen(gen);
        }
        displayGen(gen);
      } else if (restarts) {
        System.err.println("Error: Restart option makes only sense with showsMovie option");
      }
    }
  }

  static private boolean isValidHexString(String str) {
    boolean isValid = true;
    String validLetters = "NRSULM";
    myLogger.warning("Hex String is " + str);
    for (int i = 0; i < str.length(); i++) {
      if(! validLetters.contains(Character.toString(str.charAt(i)))) {
        isValid = false;
      }
    }
    return isValid;
  }

  public void displayGen(int[][] gen) {
    // Set all cells to the appropriate and color
    Color cellColor = Color.WHITE;
    if (gridType == GridType.SQUARE) {
      for (int iy=0; iy < gen[0].length; iy++) {
        for (int ix=0; ix < gen.length; ix++) {
          switch(gen[ix][iy]) {
            case 0:
            fillSquare(ix, iy, cellWidth, Color.WHITE);
            break;
            case 1:
            fillSquare(ix, iy, cellWidth, Color.BLACK);
            break;
            case 2:
            fillSquare(ix, iy, cellWidth, Color.RED);
            break;
            case 3:
            fillSquare(ix, iy, cellWidth, Color.GREEN);
            break;
            default:
            fillSquare(ix, iy, cellWidth, Color.WHITE);
          }
        }
      }
    } else if (gridType == GridType.HEXAGON) {
      gc.setStroke(Color.TRANSPARENT);
      for (int iy=0; iy < gen[0].length; iy++) {
        for (int ix=0; ix < gen.length; ix++) {
          gc.setFill(colorArray[gen[ix][iy]]);
          fillHexagon(gc, ix, iy, cellWidth);
        }
      }

      //   // Debugging: Just showing all possible colors
      //   // Potential permanent implementation as a legend
      //   //
      //   for (int i=0; i < colorArray.length; i++){
      //     fillHexagon(gc,0,i,cellWidth);
      //   }
      // gc.setFill(Color.MAGENTA);
      // fillHexagon(gc,50,50,cellWidth);

  }
}

public void fillSquare(int x, int y, int cellWidth, Color myColor) {
  gc.setFill(myColor);
  gc.fillRect(x*(cellWidth +1), y*(cellWidth+1), cellWidth, cellWidth);
}

public void fillHexagon(GraphicsContext myGc, int x, int y, int cellWidth) {
  double hexSide = 1 / Math.sqrt(3);
  double hS = (1 - hexSide) / 2.0;
  double hL = hS + hexSide;

  // Initializes coordinates to store Polygon coordinates
  double[] xcoord = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
  double[] ycoord = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0};

  // Initialize proportion of Hexagon
  double[] xhexa  = {0.5, 1.0,  1.0,  0.5, 0.0,  0.0};
  double[] yhexa  = {0.0,  hS,   hL,  1.0,  hL,   hS};

  // Scales the hexagon coordinates to cellWidth
  for (int i=0; i<xhexa.length; i++) { xhexa[i] = xhexa[i] * cellWidth; }
  for (int i=0; i<yhexa.length; i++) { yhexa[i] = yhexa[i] * cellWidth; }

  // Create offsets. Special case for odd-rows
  double xoffset = (x + 0.0) * cellWidth;
  if (y % 2 != 0) {
    xoffset    = (x + 0.5) * cellWidth;
  }
  double yoffset = y * hL  * cellWidth;

  for (int i=0; i<xcoord.length; i++) { xcoord[i] = xhexa[i] + xoffset; }
  for (int i=0; i<ycoord.length; i++) { ycoord[i] = yhexa[i] + yoffset; }
  myGc.fillPolygon(xcoord,ycoord,6);

  // only draw boundary if fill color is transparent -- when drawing grid
  // I don't like the style -- it seems to specialized connecting TRANSPARENT with Grid
  // Works for now
  if (myGc.getFill().equals(Color.TRANSPARENT)) {
    myGc.strokePolygon(xcoord,ycoord,6);
  }
}

//Later update: cheange the way the grid gets set up for the sqrgrid to that of the canvasHexGrid
//Aka gc gets handed into fillsquare and use that to make the grid
private Canvas canvasSqrGrid(int canvasSize, int cellSize) {
  Canvas canvas = new Canvas(canvasSize, canvasSize);
  GraphicsContext gc = canvas.getGraphicsContext2D();
  gc.setLineWidth(1);
  // Draw vertical grid lines
  for (int x = 0; x < canvasSize; x += cellSize + 1) {
    if ( x % (10 * (cellSize + 1)) == 0) {
      gc.setStroke(Color.RED);
    } else {
      gc.setStroke(Color.BLUE);
    }
    gc.strokeLine(x ,0 ,x ,canvasSize );
  }
  // Draw horizontal grid lines
  for (int y = 0; y < canvasSize; y += cellSize + 1) {
    if ( y % (10 * (cellSize + 1)) == 0) {
      gc.setStroke(Color.RED);
    } else {
      gc.setStroke(Color.BLUE);
    }
    gc.strokeLine(0, y, canvasSize, y);
  }
  return canvas;
}

private Canvas canvasHexGrid(int gridSize, int cellSize) {
  Canvas canvas = new Canvas(gridSize*cellSize, gridSize*cellSize);
  GraphicsContext gc = canvas.getGraphicsContext2D();
  gc.setStroke(Color.BLACK);
  gc.setFill(Color.TRANSPARENT);
  for (int iy=0; iy < gridSize; iy++) {
    for (int ix=0; ix < gridSize; ix++) {
      fillHexagon(gc, ix, iy, cellSize);
    }
  }
  return canvas;
}

private static void createColorArrayTest() {
  myLogger.warning("I am testing in createColorArrayTest");
  Color[] testColorArray = createColorArray(7);
  assert testColorArray[0].equals(Color.BLACK) : "Error: createColorArrayTest()";
  assert testColorArray[1].equals(Color.CYAN) : "Error: createColorArrayTest()";
  assert testColorArray[2].equals(Color.GRAY) : "Error: createColorArrayTest()";
  assert testColorArray[3].equals(Color.GREEN) : "Error: createColorArrayTest()";
  assert testColorArray[4].equals(Color.ORANGE) : "Error: createColorArrayTest()";
  assert testColorArray[5].equals(Color.PINK) : "Error: createColorArrayTest()";
  assert testColorArray[6].equals(Color.WHITE) : "Error: createColorArrayTest()";
  assert testColorArray[testColorArray.length-1].equals(Color.WHITE) : "Error: createColorArrayTest()";
  myLogger.warning("I finished testing in createColorArrayTest");
}
// This creates an array of numColors colors
// with colorArray[0] being black
// with colorArray[1] being white
private static Color[] createColorArray(int numColors) {
  Color[] colorArray = new Color[numColors];

  // calculates how many colors needed using RGB colorcube
  Double r = Math.cbrt(numColors);
  int n = (int) Math.ceil(r);
  Double p = 1/(Double.valueOf(n)-1);
  Color[] basicArray = new Color[n*n*n];

  // For upto 8 colors I use a simple predetermined set of colors
  // For more than 8 colors I create colors algorithmically
  if (n == 2) {
    basicArray[0] = Color.BLUE;
    basicArray[1] = Color.CYAN;
    basicArray[2] = Color.GRAY;
    basicArray[3] = Color.GREEN;
    basicArray[4] = Color.ORANGE;
    basicArray[5] = Color.PINK;
    basicArray[6] = Color.RED;
    //White is background color
    basicArray[7] = Color.WHITE;
  } else {
    // assigns colors to each of the available spots in basicArray
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        for (int k = 0; k < n; k++) {
          basicArray[i*n*n + j*n + k] = new Color(i*p, j*p, k*p, 1.0);
        }
      }
    }

  }
  // transfers the exact number of colors needed in colorArray
  for (int i=0; i<numColors; i++) {
    colorArray[i] = basicArray[i];
  }

  // overides the first and last values switching black and white for simplicity
  colorArray[0] = new Color(0.0, 0.0, 0.0,  1.0);
  // Switch color 1 and last color (white)
  colorArray[colorArray.length-1] = colorArray[1];
  colorArray[1] = new Color(1.0, 1.0, 1.0,  1.0);
  //colorArray[colorArray.length-1] = new Color(1.0, 1.0, 1.0,  1.0);

  return colorArray;
}
}

interface Automata {
  public void initGen(int[][] gen); // Strictly not necessary; Not used outside of Ant
  public void updateGen(int[][] gen);
  public boolean isOnEdge(int[][] gen);
}

/*
Improved Ant implementation - Earlier version
*/
class AntSqr implements Automata{
  private int antx;
  private int anty;
  private Direction antDirection;
  private Color localCell;
  private static Logger logger = Logger.getLogger("AntSqr");
  enum Direction {NORTH, SOUTH, EAST, WEST}
  // default constructor
  AntSqr(int[][] gen) {
    initGen(gen);
  }

  public void  initGen(int[][] gen){
    // Set all cells to 0 and white
    for (int ix=0; ix < gen.length; ix++) {
      for (int iy=0; iy < gen[0].length; iy++) {
        gen[ix][iy] = 0;
      }
    }
    // Set ant at midpoint of grid and point ant North
    antx = (int) (gen.length    / 2);
    anty = (int) (gen[0].length / 2);
    antDirection = Direction.NORTH;
    localCell = Color.WHITE;
    gen[antx][anty] = 2;
  }

  public boolean isOnEdge(int[][] gen) {
    if ((antx == gen.length - 1) ||
    (anty == gen[0].length - 1) ||
    (antx == 0) ||
    (anty == 0)) {
      return true;
    } else {
      return false;
    }
  }

  public void updateGen(int[][] gen) {
    if (isOnEdge(gen)) {
      initGen(gen);
    }
    // Set color of previous cell
    if (localCell == Color.WHITE) {
      gen[antx][anty] = 1;
    } else {
      gen[antx][anty] = 0;
    }
    //Up-date the location and direction of ant
    if (antDirection == Direction.NORTH){
      anty = anty - 1;
      if (gen[antx][anty] == 0) {
        antDirection = Direction.EAST;
        localCell = Color.WHITE;
      } else if (gen[antx][anty] ==  1) {
        antDirection = Direction.WEST;
        localCell = Color.BLACK;
      }
    } else if (antDirection == Direction.EAST) {
      antx = antx + 1;
      if (gen[antx][anty] == 0) {
        antDirection = Direction.SOUTH;
        localCell = Color.WHITE;
      } else if (gen[antx][anty] ==  1) {
        antDirection = Direction.NORTH;
        localCell = Color.BLACK;
      }
    } else  if (antDirection == Direction.WEST) {
      antx = antx - 1;
      if (gen[antx][anty] == 0) {
        antDirection = Direction.NORTH;
        localCell = Color.WHITE;
      } else if (gen[antx][anty] ==  1) {
        antDirection = Direction.SOUTH;
        localCell = Color.BLACK;
      }
    } else if (antDirection == Direction.SOUTH) {
      anty = anty + 1;
      if (gen[antx][anty] == 0) {
        antDirection = Direction.WEST;
        localCell = Color.WHITE;
      } else if (gen[antx][anty] ==  1) {
        antDirection = Direction.EAST;
        localCell = Color.BLACK;
      }
    }
    //Mark new location red
    gen[antx][anty] = 2;
  }
}

/*
Improved Ant implementation - Code resembles human-readable Wikipedia description
*/
class AntGreen implements Automata{
  private int antx;
  private int anty;
  private Direction antDirection;
  private Color localCell;
  private static Logger logger = Logger.getLogger("AntGreen");
  enum Direction {NORTH, SOUTH, EAST, WEST}
  // default constructor
  AntGreen(int[][] gen) {
    initGen(gen);
  }

  public void  initGen(int [][] gen){
    // Set all cells to 0 and white
    setToZero(gen);
    // Set ant at midpoint of grid and point ant West
    antx = (int) (gen.length    / 2);
    anty = (int) (gen[0].length / 2);
    antDirection = Direction.WEST;
    // Color the cell Green
    localCell = Color.WHITE;
    gen[antx][anty] = 3;
  }
  private void setToZero(int[][] gen) {
    for (int ix=0; ix < gen.length;    ix++) {
      for (int iy=0; iy < gen[0].length; iy++) {
        gen[ix][iy] = 0;
      }
    }
  }
  public void updateGen(int[][] gen) {
    //if white cell, turn clockwise
    //if black cell, turn counter clockwise
    if (localCell == Color.WHITE) {
      turnClockwise();
    } else {
      turnCounterClockwise();
    }
    // flip color of cell
    flipColor(gen);
    //move forward one unit
    move(gen);
  }

  private void turnClockwise() {
    switch(antDirection) {
      case NORTH:
      antDirection = Direction.EAST;
      break;
      case EAST:
      antDirection = Direction.SOUTH;
      break;
      case SOUTH:
      antDirection = Direction.WEST;
      break;
      case WEST:
      antDirection = Direction.NORTH;
    }
  }
  private void turnCounterClockwise() {
    switch(antDirection) {
      case NORTH:
      antDirection = Direction.WEST;
      break;
      case EAST:
      antDirection = Direction.NORTH;
      break;
      case SOUTH:
      antDirection = Direction.EAST;
      break;
      case WEST:
      antDirection = Direction.SOUTH;
    }
  }
  private void flipColor(int[][] gen) {
    if (localCell == Color.WHITE) {
      gen[antx][anty] = 1; // Black
    } else {
      gen[antx][anty] = 0; // White
    }
  }
  private void move(int[][] gen) {
    //Reset the ant when reaching any edge
    if (isOnEdge(gen)) {
      initGen(gen);
    }
    switch(antDirection) {
      case NORTH:
      anty = anty - 1;
      break;
      case EAST:
      antx = antx + 1;
      break;
      case SOUTH:
      anty = anty + 1;
      break;
      case WEST:
      antx = antx - 1;
    }
    if (gen[antx][anty] == 0) {
      localCell = Color.WHITE;
    } else {
      localCell = Color.BLACK;
    }
    //Mark new location green
    gen[antx][anty] = 3;
  }
  public boolean isOnEdge(int[][] gen) {
    if ((antx == gen.length - 1) ||
    (anty == gen[0].length - 1) ||
    (antx == 0) ||
    (anty == 0)) {
      return true;
    } else {
      return false;
    }
  }
}

class AntHex implements Automata{
  private String letterStr;
  private int antx;
  private int anty;
  private Direction antDirection;
  private int localCell;
  private static Logger logger = Logger.getLogger("AntHex");
  enum Direction {NW, NE, EA, SE, SW, WE}
  // default constructor
  AntHex(int[][] gen, String letterStr) {
    this.letterStr = letterStr;
    initGen(gen);
  }

  public void initGen(int[][] gen){
    // Set all cells to 0 and white
    setToZero(gen);
    logger.warning("initgen of AntHex");
    // Set ant at midpoint of grid and point ant East
    // Set localCell to white, before turning ant location black in gen
    antx = (int) (gen.length    / 2);
    anty = (int) (gen[0].length / 2);
    antDirection = Direction.NE;
    localCell = 1;
    gen[antx][anty] = 0;

  }
  private void setToZero (int[][]gen){
    for (int ix=0; ix < gen.length; ix++) {
      for (int iy=0; iy < gen[0].length; iy++) {
        gen[ix][iy] = 1;
      }
    }
  }
  public void updateGen(int[][] gen) {
    if (isOnEdge(gen)) {
      initGen(gen);
    }
    switch (String.valueOf(letterStr.charAt(localCell-1))) {
      case "N":
      noChange();
      break;
      case "R":
      clockwise60();
      break;
      case "S":
      clockwise120();
      break;
      case "U":
      clockwise180();
      break;
      case "L":
      counterClockwise120();
      break;
      case "M":
      counterClockwise60();
    }
    // change the color of current cell to next color
    if (localCell == letterStr.length()) {
      gen[antx][anty] = 1;
    } else {
      gen[antx][anty] = localCell + 1;
    }
    move(gen);
  }

  private void noChange() {
    switch(antDirection) {
      case NW:
      antDirection = Direction.NW;
      break;
      case NE:
      antDirection = Direction.NE;
      break;
      case EA:
      antDirection = Direction.EA;
      break;
      case SE:
      antDirection = Direction.SE;
      break;
      case SW:
      antDirection = Direction.SW;
      break;
      case WE:
      antDirection = Direction.WE;
      break;
    }
  }
  private void clockwise60() {
    switch(antDirection) {
      case NW:
      antDirection = Direction.NE;
      break;
      case NE:
      antDirection = Direction.EA;
      break;
      case EA:
      antDirection = Direction.SE;
      break;
      case SE:
      antDirection = Direction.SW;
      break;
      case SW:
      antDirection = Direction.WE;
      break;
      case WE:
      antDirection = Direction.NW;
      break;
    }
  }
  private void clockwise120() {
    switch(antDirection) {
      case NW:
      antDirection = Direction.EA;
      break;
      case NE:
      antDirection = Direction.SE;
      break;
      case EA:
      antDirection = Direction.SW;
      break;
      case SE:
      antDirection = Direction.WE;
      break;
      case SW:
      antDirection = Direction.NW;
      break;
      case WE:
      antDirection = Direction.NE;
      break;
    }
  }
  private void clockwise180() {
    switch(antDirection) {
      case NW:
      antDirection = Direction.SE;
      break;
      case NE:
      antDirection = Direction.SW;
      break;
      case EA:
      antDirection = Direction.WE;
      break;
      case SE:
      antDirection = Direction.NW;
      break;
      case SW:
      antDirection = Direction.NE;
      break;
      case WE:
      antDirection = Direction.EA;
      break;
    }
  }
  private void counterClockwise120() {
    switch(antDirection) {
      case NW:
      antDirection = Direction.SW;
      break;
      case NE:
      antDirection = Direction.WE;
      break;
      case EA:
      antDirection = Direction.NW;
      break;
      case SE:
      antDirection = Direction.NE;
      break;
      case SW:
      antDirection = Direction.EA;
      break;
      case WE:
      antDirection = Direction.SE;
      break;
    }
  }
  private void counterClockwise60() {
    switch(antDirection) {
      case NW:
      antDirection = Direction.WE;
      break;
      case NE:
      antDirection = Direction.NW;
      break;
      case EA:
      antDirection = Direction.NE;
      break;
      case SE:
      antDirection = Direction.EA;
      break;
      case SW:
      antDirection = Direction.SE;
      break;
      case WE:
      antDirection = Direction.SW;
      break;
    }
  }
  private void move(int[][] gen){
    if (anty % 2 == 0) {
      // ant is on even row
      switch(antDirection){
        case NW:
        antx = antx - 1; anty = anty - 1;
        break;
        case NE:
        antx = antx + 0; anty = anty - 1;
        break;
        case EA:
        antx = antx + 1; anty = anty + 0;
        break;
        case SE:
        antx = antx + 0; anty = anty + 1;
        break;
        case SW:
        antx = antx - 1; anty = anty + 1;
        break;
        case WE:
        antx = antx - 1; anty = anty + 0;
      }
    } else {
      // ant is on odd row
      switch(antDirection){
        case NW:
        antx = antx + 0; anty = anty - 1;
        break;
        case NE:
        antx = antx + 1; anty = anty - 1;
        break;
        case EA:
        antx = antx + 1; anty = anty + 0;
        break;
        case SE:
        antx = antx + 1; anty = anty + 1;
        break;
        case SW:
        antx = antx + 0; anty = anty + 1;
        break;
        case WE:
        antx = antx - 1; anty = anty + 0;
      }
    }
    // This location is needed to determine move direction
    localCell = gen[antx][anty];
    gen[antx][anty] = 0;
  }
  public boolean isOnEdge(int[][] gen) {
    if((antx == gen.length - 1) || (anty == gen[0].length - 1) || (antx == 0) || (anty == 0)) {
      return true;
    } else {
      return false;
    }
  }
}

class Life implements Automata{
  // default constructor
  private static Logger logger = Logger.getLogger("Life");

  Life(int[][] gen) {
    initGen(gen);
  }

  public void initGen(int[][] gen){
    setToZero(gen);
    int startCells = (int) (gen.length * gen[0].length * 0.3);
    int n = 0;
    while(n < startCells) {
      int ix = (int)(Math.random() * gen.length);
      int iy = (int)(Math.random() * gen[0].length);
      if(gen[ix][iy] == 0) {
        gen[ix][iy] = 1;
        n++;
      }
    }
  }
  private void setToZero(int[][] gen) {
    for (int ix=0; ix < gen.length;    ix++) {
      for (int iy=0; iy < gen[0].length; iy++) {
        gen[ix][iy] = 0;
      }
    }
  }

  // Unlike other Ant types, Life does not re-initialize when an ant reaches the edge.
  // Life wraps around the edge. So the ants are never on the edge.
  public boolean isOnEdge(int[][] gen) {
    return false;
  }

  public void updateGen(int[][] gen) {
    int[][] genNext = new int[gen.length][gen[0].length];
    for (int   i = 0; i < gen.length; i++) {
      // Implement wrap around edge condition
      int ip1 = (gen.length + i+1)%(gen.length);
      int im1 = (gen.length + i-1)%(gen.length);
      for (int j = 0; j < gen[0].length; j++) {
        int jp1 = (gen[0].length + j+1)%(gen[0].length);
        int jm1 = (gen[0].length + j-1)%(gen[0].length);
        // Count the surrounding cells
        int m = gen[im1][jm1] +
        gen[i  ][jm1] +
        gen[ip1][jm1] +
        gen[ip1][j  ] +
        gen[ip1][jp1] +
        gen[i  ][jp1] +
        gen[im1][jp1] +
        gen[im1][j  ];
        // Apply the Life-of-Game roles
        if(m < 2) {
          genNext[i][j] = 0;
        } else if (m > 3){
          genNext[i][j] = 0;
        } else if (m == 3){
          genNext[i][j] = 1;
        } else {
          genNext[i][j] = gen[i][j];
        }
      }
    }
    // Copy genNext to current gen - Maybe better return gen
    for (int ix=0; ix < gen.length;    ix++) {
      for (int iy=0; iy < gen[0].length; iy++) {
        gen[ix][iy] = genNext[ix][iy];
      }
    }
  }
}
// Fine :-)
