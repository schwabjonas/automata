# Automata
Java program implementing Langton's Ant and Conway's Game of Life.

Automata/Langton's Ant Background: 

Langton's Ant Wikipedia: https://en.wikipedia.org/wiki/Langton%27s_ant

Online Example (Tom den Ottelander): https://tomdenottelander.com/projects/langtonsant/index.html

Online Example (btmr.de): https://brtmr.de/2015/10/05/hexadecimal-langtons-ant-2.html

Test Case Examples (Hexagonal Ant): https://docs.google.com/document/d/1LeGN1XDV3CkQ-48nOg89WBpwJroVcd_gXx5gqxBP04w/edit?usp=sharing


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
  
Test Case Examples:

  Ex 1: 
  javac Machina.java
  java Machina -h RMUN -e
  
    
