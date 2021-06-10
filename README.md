# Automata
Java program implementing Langton's Ant and Conway's Game of Life.

Automata/Langton's Ant Background: 

- Langton's Ant Wikipedia: https://en.wikipedia.org/wiki/Langton%27s_ant

- Online Example (Tom den Ottelander): https://tomdenottelander.com/projects/langtonsant/index.html

- Online Example (btmr.de): https://brtmr.de/2015/10/05/hexadecimal-langtons-ant-2.html

- Test Case Examples (Hexagonal Ant): https://docs.google.com/document/d/1LeGN1XDV3CkQ-48nOg89WBpwJroVcd_gXx5gqxBP04w/edit?usp=sharing


To run this program you need to have java and javafx installed.

  - javac Machina.java

  - java Machina 

Grid Specifications:

  - -c  Sets cell size in number of Pixels. Default is 7.
 
  - -s  Sets grid size in number of cells. Default is 71.
  
  - -b  Shows no boundary of cells.
  
Boundary Conditions:
  
  - -f Shows no movie of individual steps. Shows only the final picture.
  
  - -e Stops when ant touches edge.
  
  - -i Stops after number of iterations.
  
  - -o Loops by restarting after ant touches edge.
  
Types of ants:
  
  - -l Creates Game of Life on square grid.
  
  - -r Creates red ant on square grid.
  
  - -g Creates green ant on square grid.
  
  - -h Creates black ant on hexagonal grid. Requires rotational string argument after.
  
Test Case Examples:

  Ex 1: 
  - javac Machina.java
  - java Machina -h RMUN
  - Produces: 
  <img width="408" alt="Screen Shot 2021-06-10 at 3 52 43 PM" src="https://user-images.githubusercontent.com/83527286/121606942-f0e23000-ca03-11eb-8d8f-81e54c0b2990.png">
  
  Ex 2: 
  - javac Machina.java
  - java Machina -h SLSSS
  - Produces:
  
  ![unnamed (1)](https://user-images.githubusercontent.com/83527286/121607245-8bdb0a00-ca04-11eb-9cec-20a8dcf634db.png)

  Ex 3: 
  - javac Machina.java
  - java Machina -h SLSSU
  - Produces:

  ![Screen Shot 2021-05-31 at 5 36 36 PM](https://user-images.githubusercontent.com/83527286/121607473-f55b1880-ca04-11eb-9a96-3a4e653f5102.png)

  Ex 4: 
  - javac Machina.java
  - java Machina -h RSNUSRL
  - Produces:

  ![unnamed (2)](https://user-images.githubusercontent.com/83527286/121607439-df4d5800-ca04-11eb-91cb-c80659e8418f.png)


