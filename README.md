# automata
Java program implementing Langton's Ant and Conway's Game of Life.


To run this program you need to have java and javafx installed.

  javac Machina.java

  java Machina 


The options for the file are:

Usage: SchwabJonasLab2 -cs [-q | -h][-r | -g | -l] --help

Grid Specifications:

  -c  Sets cell size in number of Pixels. Default is 7.
 
  -s  Sets grid size in number of cells. Default is 71.
  
  -b  Shows no boundary of cells.
  
Boundary Conditions:
  
  -f Shows no movie of individual steps. Shows only the final picture.
  
  -e Stops when ant touches edge.
  
  -i Stops after number of iterations.
  
  -o Loops by restarting after ant touches edge.
  
Types of ants:
  
  -l Creates Game of Life on square grid.
  
  -r Creates red ant on square grid.
  
  -g Creates green ant on square grid.
  
  -h Creates black ant on hexagonal grid. Requires rotational string argument after.
