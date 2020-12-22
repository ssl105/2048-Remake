# 2048-Remake With AI Solver
This is my personalized remake of the game 2048 I made in 2018 for a class project. After taking an 
intro course in AI in Fall of 2020, I went back to this project to implement an expectimax algorithm
that would automatically solve the game.

## How To Run The Program
### Compile and Run
 - Download the files into a folder on your desktop.
 - Compile the GUI using javac: "javac Gui2048.java" (If you haven't set the java path on your system
 follow the tutorial here: https://beginnersbook.com/2013/05/first-java-program)
 - Run the program: "java Gui2048.java"
 - Additional flags can be used to load: 
 "-i filename.board" indicates the file to load in
 "-o filename.board" indicates the file to save to (Defaults to 2048.board)
 "-s size" indicates the size of the board (Defaults to 4)
                              
### Controls
 - Use the arrow keys to move the pieces in the desired location. (WASD keys do not work)
 - "s" will save the board to the given output board or default board.
 - Enter key will move the piece based on the expectimax algorithm.
 
## Additional Information About The AI
### Expectimax Algorithm
The expectimax algorithm creates a search tree based on the possible future states of the board (child nodes)
starting at a given state (root node). The state of a board represents the tile positions and score of the 
game. The possible future states of a board begins when the player moves in a direction. This
creates at most 4 new states (child nodes) for the 4 directions. It is less than 4 if the player cannot move
in a certain direction. After the player moves, a random node is placed in an open tile spot. 
This creates additional states based on the number of open tile spots. Eventually we have to limit the size of 
the tree to a specific depth so that we can calculate the value of end states (leaf nodes). 
To evaluate a leaf node we take its score and add any heuristic functions implemented (explained in next section). 
If the node is a chance node (where a random tiles was placed) we take the expected value of its child nodes, 
which is the sum of (the probability of the child node  * value of the child node) for all child nodes.
If the node is a player node where the player made chose a direction, the value is determined by the the maximum
score returned by one of its children.

### Heuristic Function
In order implement more effective strategy, I added an additional heuristic that favors the position of 
higher scoring tiles to the bottom right using a manhattan distance. The heurstic produces a higher score
for pieces located further from the bottom right weighted by the tile piece. This heuristic function
is then subtracted from the score when evaluating the value of a leaf node in the expectimax algorithm.

### Other Information
Currently the game randomly spawns a 4 point tile 10% of the time; however, the AI does not take this into 
consideration and assumes all random tile spawns are 2's. 
