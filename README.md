# Automata
Java program implementing generalized Langton's Ant and Conway's Game of Life.

Ex 1 (More Below): 
  - javac Machina.java
  - java Machina -h RSNUSRL
  - Produces:

  ![unnamed (2)](https://user-images.githubusercontent.com/83527286/121607439-df4d5800-ca04-11eb-91cb-c80659e8418f.png)

## Automata/Langton's Ant Background: 

- Test Case Examples (Hexagonal Ant): https://docs.google.com/document/d/1LeGN1XDV3CkQ-48nOg89WBpwJroVcd_gXx5gqxBP04w/edit?usp=sharing

- Langton's Ant Wikipedia: https://en.wikipedia.org/wiki/Langton%27s_ant

- Online Example (Tom den Ottelander): https://tomdenottelander.com/projects/langtonsant/index.html

- Online Example (btmr.de): https://brtmr.de/2015/10/05/hexadecimal-langtons-ant-2.html

###### To run this program you need to have java and javafx installed.

  - javac Machina.java

  - java Machina 

## Instructions Needed to Run the Program:

###### Grid Specifications:

  - -c  Sets cell size in number of Pixels. Default is 7.
 
  - -s  Sets grid size in number of cells. Default is 71.
  
  - -b  Shows no boundary of cells.
  
###### Boundary Conditions:
  
  - -f Shows no movie of individual steps. Shows only the final picture.
  
  - -e Stops when ant touches edge.
  
  - -i Stops after number of iterations.
  
  - -o Loops by restarting after ant touches edge.
  
###### Types of ants:
  
  - -l Creates Game of Life on square grid.
  
  - -r Creates red ant on square grid.
  
  - -g Creates green ant on square grid.
  
  - -h Creates black ant on hexagonal grid. Requires rotational string argument after.
  
###### Test Case Examples (Hexagonal Ant):

  Ex 2: 
  - javac Machina.java
  - java Machina -h RMUN
  - Produces: 
  <img width="408" alt="Screen Shot 2021-06-10 at 3 52 43 PM" src="https://user-images.githubusercontent.com/83527286/121606942-f0e23000-ca03-11eb-8d8f-81e54c0b2990.png">
  
  Ex 3: 
  - javac Machina.java
  - java Machina -h SLSSS
  - Produces:
  
  ![unnamed (1)](https://user-images.githubusercontent.com/83527286/121607245-8bdb0a00-ca04-11eb-9cec-20a8dcf634db.png)

  Ex 4: 
  - javac Machina.java
  - java Machina -h SLSSU
  - Produces:

<img width="421" alt="Screen Shot 2021-06-10 at 4 01 37 PM" src="https://user-images.githubusercontent.com/83527286/121607647-43701c00-ca05-11eb-8f43-a93fb02279d2.png">

###### Test Case Examples (Square Ant):

  Ex 5: 
  - java Machina.java
  - java Machina
  - Produces:

<img width="784" alt="Screen Shot 2021-06-10 at 4 14 51 PM" src="https://user-images.githubusercontent.com/83527286/121608742-63a0da80-ca07-11eb-86e5-fa4581da4052.png">

###### Test Case Examples (Game of Life):

  Ex 6:
  - java Machina.java
  - java Machina -l
  - Produces (Random Each Time):

<img width="781" alt="Screen Shot 2021-06-10 at 4 13 01 PM" src="https://user-images.githubusercontent.com/83527286/121608844-88954d80-ca07-11eb-926b-56a9a3139a45.png">
