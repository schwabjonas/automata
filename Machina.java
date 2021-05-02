import javafx.application.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.animation.*;
import javafx.util.*;
import java.util.*;

public class Machina extends Application {
  Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.BLACK, Color.WHITE} ;
  //Grid related variables
  // Default values so I can launch grid without setting sizes
  static int cellWidth = 10;
  static int gridWidth = 71;
  static int totalPixel;
  enum   GridType {SQUARE, HEXAGON};
  static GridType gridType = GridType.SQUARE;
  Canvas cellCanvas = null;
  Canvas gridCanvas = null;
  GraphicsContext gc = null;
  final int timebetweenupdates = 5;   // 5 is usually a good value
  static int gen[][] = new int[gridWidth][gridWidth];
  static Automata auto =  new Ant(gen);

  public static void main(String[] args){
    // Reads in command line options
    try{
      for (int i = 0; i < args.length; i++) {
        if(args[i].equals("-c")) {
          cellWidth = Integer.parseInt(args[i + 1]);
        }
      }
      for (int i = 0; i < args.length; i++) {
        if(args[i].equals("-s")) {
          gridWidth = Integer.parseInt(args[i + 1]);
        }
      }
      for (int i = 0; i < args.length; i++) {
        if(args[i].equals("-h")) {
          gridType = GridType.HEXAGON;
        } else if (args[i].equals("-q")) {
          gridType = GridType.SQUARE;
        }
      }
      // Initialize the desired Automata
      for (int i = 0; i < args.length; i++) {
        if(args[i].equals("-g")) {
          auto = new AntGreen(gen);
        } else if(args[i].equals("-l")) {
          auto = new Life(gen);
        }
      }
      for (int i = 0; i < args.length; i++) {
        if(args[i].equals("--help")) {
          System.out.println("Usage: Machina -cs [-q | -h][-r | -g | -l] --help");
          System.out.println(" -c  Sets cell size in number of Pixels. Default is 7.");
          System.out.println(" -s  Sets grid size in number of cells. Default is 71.");
          System.out.println(" -q  Creates quadrilateral (square) cells. Default.");
          System.out.println(" -h  Creates hexagonal cells.");
          System.out.println(" -a  Sets type of Automata: -g | -r | -l (default).");
          System.exit(0);
        }
      }
    } catch (Exception e) {
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
      cellCanvas = new Canvas(totalPixel, totalPixel);
      gridCanvas = canvasHexGrid(totalPixel, cellWidth);
    } else {
      cellCanvas = new Canvas(totalPixel, totalPixel);
      gridCanvas = canvasSqrGrid(totalPixel, cellWidth);
    }
    // Create root/node object. Stackpane stacks several canvases on top of each other
    StackPane myLayout = new StackPane();
    myLayout.getChildren().add(cellCanvas);
    myLayout.getChildren().add(gridCanvas);
    gridCanvas.toFront();
    gc = cellCanvas.getGraphicsContext2D();
    // create scene object
    Scene myScene = new Scene(myLayout, totalPixel, totalPixel);
    // Finalizing the stage object
    primaryStage.setTitle("Cellular Automata - Output");
    primaryStage.setScene(myScene);
    primaryStage.show();

    // Display the current generation
    displayGen(gen);

    KeyFrame k = new KeyFrame(Duration.millis(timebetweenupdates),
    e->{
      // Update Ant and display generation
      auto.updateGen(gen);
      displayGen(gen);
    });
    Timeline t = new Timeline(k);
    //t.setCycleCount(2000);
    t.setCycleCount(Timeline.INDEFINITE);
    t.play();

  }

  public void displayGen(int[][] gen) {
    // Set all cells to the appropriate and color
    Color cellColor = Color.WHITE;
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
  }

  public void fillSquare(int x, int y, int cellWidth, Color myColor) {
    gc.setFill(myColor);
    gc.fillRect(x*(cellWidth +1), y*(cellWidth+1), cellWidth, cellWidth);
  }

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

  private Canvas canvasHexGrid(int canvasSize, int cellSize) {
    Canvas canvas = new Canvas(canvasSize, canvasSize);
    GraphicsContext gc = canvas.getGraphicsContext2D();
    gc.setStroke(Color.BLACK);
    //gc.setFill(Color.YELLOW);
    gc.setFill(Color.TRANSPARENT);

    double hexSide = 1 / Math.sqrt(3);
    double hS = (1 - hexSide) / 2.0;
    double hL = hS + hexSide;

    double[] xcoord = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
    double[] ycoord = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
    double[] xhexa  = {0.5, 1.0,  1.0,  0.5, 0.0,  0.0};
    double[] yhexa  = {0.0,  hS,   hL,  1.0,  hL,   hS};
    for (int i=0; i<xhexa.length; i++) { xhexa[i] = xhexa[i] * cellSize; }
    for (int i=0; i<yhexa.length; i++) { yhexa[i] = yhexa[i] * cellSize; }
    double xoffset = 0.50 * cellSize;
    double yoffset =   hL * cellSize;

    for (int x = 0; x < canvasSize; x += cellSize * 1.0) {
      for (int y = 0; y < canvasSize; y += cellSize * (1 + hexSide)) {
        for (int i=0; i<xcoord.length; i++) { xcoord[i] = x + xhexa[i]; }
        for (int i=0; i<ycoord.length; i++) { ycoord[i] = y + yhexa[i]; }
        gc.fillPolygon(xcoord,ycoord,6);
        gc.strokePolygon(xcoord,ycoord,6);
        for (int i=0; i<xcoord.length; i++) { xcoord[i] = x + xhexa[i] + xoffset; }
        for (int i=0; i<ycoord.length; i++) { ycoord[i] = y + yhexa[i] + yoffset; }
        gc.fillPolygon(xcoord,ycoord,6);
        gc.strokePolygon(xcoord,ycoord,6);
      }
    }
    return canvas;
  }
}

interface Automata {
  public void initGen(int[][] gen); // Strictly not necessary; Not used outside of Ant
  public void updateGen(int[][] gen);
}

/*
Improved Ant implementation - Earlier version
*/
class Ant implements Automata{
  private int antx;
  private int anty;
  private Direction antDirection;
  private Color localCell;
  enum Direction {NORTH, SOUTH, EAST, WEST}
  // default constructor
  Ant(int[][] gen) {
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

  public void updateGen(int[][] gen) {
    //Reset the ant when reaching any edge
    if ((antx == gen.length - 1) ||
    (anty == gen[0].length - 1) ||
    (antx == 0) ||
    (anty == 0)) {
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
  private boolean isOnEdge(int[][] gen) {
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

class Life implements Automata{
  // default constructor
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
