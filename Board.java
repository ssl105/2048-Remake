/**
 * Filename: Board.java
 * Name: Scott Lee cs8bwabq ssl105@ucsd.edu
 * Date: Sat Jan 27
 *
 * This File holds the Board class which is the board for the game of 2048.
 * Methods can be added to the class to increase functionality
 */

/**
 * Sample Board
 * <p/>
 * 0   1   2   3
 * 0   -   -   -   -
 * 1   -   -   -   -
 * 2   -   -   -   -
 * 3   -   -   -   -
 * <p/>
 * The sample board shows the index values for the columns and rows
 * Remember that you access a 2D array by first specifying the row
 * and then the column: grid[row][column]
 */

import java.util.*;
import java.io.*;


/**
 * classname: Board
 * the purpose of this class is to create a board for the game of 2048.
 * this board has a random generator that can generate either 2 or 4 at a 
 * random location. The board also has a 2d array grid that holds the ints
 * and the score which keep tracks of the points earned during the game
 */
public class Board {
    public final int NUM_START_TILES = 2; 
    public final int TWO_PROBABILITY = 90;
    public final int GRID_SIZE;


    private final Random random; // a reference to the Random object, passed
    //in 
    // as a parameter in Boards constructors
    private int[][] grid;  // a 2D int array, its size being boardSize*board
    //Size
    private int score;     // the current score, incremented as tiles merge 


    // Constructs a fresh board with random tiles
    public Board(Random random, int boardSize) {
        this.random = random; 
        GRID_SIZE = boardSize; 
        this.grid = new int[GRID_SIZE][GRID_SIZE];

        //adding NUM_START_TILES random tiles
        for (int i = 0; i < NUM_START_TILES; i++) {		
            this.addRandomTile();
        }
    }


    // Construct a board based off of an input file
    // assume board is valid
    public Board(Random random, String inputBoard) throws IOException {
        //scanner object to read file
        Scanner input = new Scanner(new File(inputBoard));

        //setting the instance variables
        this.random = random; 
        GRID_SIZE = input.nextInt(); 
        this.score = input.nextInt();
        this.grid = new int[GRID_SIZE][GRID_SIZE];

        //loop to fill in grid
        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                grid[r][c] = input.nextInt();
            } 
        }

        input.close();
    }


    // Saves the current board to a file
    public void saveBoard(String outputBoard) throws IOException {
        //print writer object to print out board
        PrintWriter output = new PrintWriter(new File(outputBoard));

        //printing size and score 
        output.println(this.GRID_SIZE);	
        output.println(this.score);

        //printing board
        for (int r = 0; r < GRID_SIZE ; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                //printing out board elements
                output.print(this.grid[r][c] + " ");			

                //print new life when reaching
                // the last column of grid
                if ( c == GRID_SIZE - 1) {
                    output.println();
                }
            }
        }
        //closing the file
        output.close();

    }


    // Adds a random tile (of value 2 or 4) to a
    // random empty space on the board
    public void addRandomTile() {
        int count = 0;

        //counting the number of available tiles which is when tile = 0
        for (int r = 0; r < this.grid.length; r++) {
            for (int c = 0; c < this.grid[r].length; c++) {
                if (this.grid[r][c] == 0) {
                    count++;
                }
            }
        } 

        //getting random variables/ resetting count
        int location = this.random.nextInt(count);		
        int value = this.random.nextInt(100);
        count = -1;

        //walking through grid to place random value based on random 
        //location
        for (int r = 0; r < this.grid.length; r++) {
            for (int c = 0; c < this.grid[r].length; c++) {
                if (this.grid[r][c] == 0 ) {
                    count++;
                
                    //if the count is at location set the random 
                    //value at that location. place 2 if value is 
                    //less than 90 else place 4
                    if (count == location){
                        if (value < 90) {
                            this.grid[r][c] = 2;
                        } else {
                            this.grid[r][c] = 4;
                        }	 
                        return;
                    } 
               } 
            }
        }
    }


    /**
     * Method name: canMoveLeft()
     * checking if a move can be made left based on board
     * @return true - if a left move can be made
     * @return false - if a left move cannot be made
     */
    private boolean canMoveLeft(){
        //loop through columns except first 
        for (int r = 0; r < this.GRID_SIZE; r++) {
            for (int c = 1; c < this.GRID_SIZE; c++) {
                //check if current element is non zero
                if (this.grid[r][c] != 0) {
                    //if element on the left is zero or 
                    //the same then its movable	
                    if (this.grid[r][c-1] == 0 ||
                            this.grid[r][c-1] ==
                            this.grid[r][c]) {
                        return true;
                    } 
                }
            }
        }	
        //if above not true then false
        return false;	
    }


    /**
     * Method name: canMoveRight()
     * checking if a move right can be made  based on board
     * @return true - if a right move can be made
     * @return false - if a right move cannot be made
     */
    private boolean canMoveRight(){
        //loop through every column except last 
        for (int r = 0; r < this.GRID_SIZE; r++) {
            for (int c = 0; c < this.GRID_SIZE-1; c++) {
                //check if current element is non zero
                if (this.grid[r][c] != 0) {
                    //if element on the right is zero or 
                    //the same then its movable	
                    if (this.grid[r][c+1] == 0 ||
                            this.grid[r][c+1] ==
                            this.grid[r][c]) {
                        return true;
                    } 
                }
            }
        }	
        //if above not true then false
        return false;	
    }	


    /**
     * Method name: canMoveUp()
     * checking if a move up can be made  based on board
     * @return true - if a move up can be made
     * @return false - if a move up cannot be made
     */
    private boolean canMoveUp(){
        //loop through every row except first 
        for (int r = 1; r < this.GRID_SIZE; r++) {
            for (int c = 0; c < this.GRID_SIZE; c++) {
                //check if current element is non zero
                if (this.grid[r][c] != 0) {
                    //if element above is zero or 
                    //the same then its movable	
                    if (this.grid[r-1][c] == 0 ||
                            this.grid[r-1][c] ==
                            this.grid[r][c]) {
                        return true;
                    } 
                }
            }
        }	
        //if above not true then false
        return false;	
    }


    /**
     * Method name: canMoveDown()
     * checking if a move down can be made  based on board
     * @return true - if a move down can be made
     * @return false - if a move down cannot be made
     */
    private boolean canMoveDown(){
        //loop through every row except last 
        for (int r = 0; r < this.GRID_SIZE-1; r++) {
            for (int c = 0; c < this.GRID_SIZE; c++) {
                //check if current element is non zero
                if (this.grid[r][c] != 0) {
                    //if element below is zero or 
                    //the same then its movable	
                    if (this.grid[r+1][c] == 0 ||
                            this.grid[r+1][c] == 
                            this.grid[r][c]) {
                        return true;
                    } 
                }
            }
        }	
        //if above not true then false
        return false;	
    }


    // TODO PSA3
    // determins whether the board can move in a certain direction
    // return true if such a move is possible
    public boolean canMove(Direction direction){
        //conditional for left	
        if (direction.equals(Direction.LEFT)) {
            // return result of canMoveLeft()
            return this.canMoveLeft();	
        }

        //conditional for right
        if (direction.equals(Direction.RIGHT)) {
            //return result of canMoveRight()	
            return this.canMoveRight();
        }

        //condtional for up
        if (direction.equals(Direction.UP)) {
            //return result of canMoveUp()
            return this.canMoveUp();
        }

        //conditional for down
        if (direction.equals(Direction.DOWN)) {
            //return result of canMoveDown()
            return this.canMoveDown();
        }

        //if none of the conditionals pass then return false
        return false;
    }


    /**
     * Method name: moveLeft()
     * make a move left  based on board
     */
    private void moveLeft(){
        //join left
        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 1; c < GRID_SIZE; c++) {
                int left = grid[r][c-1];
                int current = grid[r][c];
                
                if (current != 0) {
                    if (left == current) { 
                        int sum = left + current;
                        grid[r][c-1] = sum;
                        grid[r][c] = 0;
                        
                        score += sum;
                    }
                }
                else if (current == 0) {
                    if (left !=0) {
                        grid[r][c] = left;
                        grid[r][c-1] = 0;
                    }
                }
                
                
            } 
        }  
        
        //shift to left
        for (int i = 0; i < GRID_SIZE -1; i++) {
            for (int r = 0; r < GRID_SIZE; r++) {
                for(int c = 1; c < GRID_SIZE; c++) {
                    int left = grid[r][c-1];
                    int current = grid[r][c];

                    if (current != 0) {
                        if (left == 0) {
                            grid[r][c-1] = current;
                            grid[r][c] = left;
                        }
                    }
                    
                }
            }
        }


    }


    /**
     * Method name: moveRight()
     * make a move right based on board
     */
    private void moveRight(){
        //join right 
        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = GRID_SIZE - 2; c >= 0; c--) {
                int right = grid[r][c+1];
                int current = grid[r][c];
                
                if (current != 0) {
                    if (right == current) { 
                        int sum = right + current;
                        grid[r][c+1] = sum;
                        grid[r][c] = 0;
                        
                        score += sum;
                    }
                }
                else if (current == 0) {
                    if (right !=0) {
                        grid[r][c] = right;
                        grid[r][c+1] = 0;
                    }
                }


            } 
        }  
        
        //shift to right 
        for (int i = 0; i < GRID_SIZE -1; i++) {
            for (int r = 0; r < GRID_SIZE; r++) {
                for(int c = GRID_SIZE - 2; c >= 0; c--) {
                    int right = grid[r][c+1];
                    int current = grid[r][c];
                    
                    if (current != 0) {
                        if (right == 0) {
                            grid[r][c+1] = current;
                            grid[r][c] = right;
                        }
                    }
                }
            }
        }

    }


    /**
     * Method name: moveUp()
     * make a move up based on board
     */
    private void moveUp(){
        //join up 
        for (int c = 0; c < GRID_SIZE; c++) {
            for (int r = 1; r < GRID_SIZE; r++) {
                int above = grid[r-1][c];
                int current = grid[r][c];
                
                if (current != 0) {
                    if (above == current) { 
                        int sum = above + current;
                        grid[r-1][c] = sum;
                        grid[r][c] = 0;
                        
                        score += sum;
                    }
                }
                else if ( current == 0) {
                    if (above != 0) {
                        grid[r][c] = above;
                        grid[r-1][c] = 0;
                    }
                }

            } 
        }  
        
        //shift up 
        for (int i = 0; i < GRID_SIZE -1; i++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                for(int r = 1; r < GRID_SIZE; r++) {
                    int above = grid[r-1][c];
                    int current = grid[r][c];

                    
                    if (current != 0) {
                        if (above == 0) {
                            grid[r-1][c] = current;
                            grid[r][c] = above;
                        }
                    }

                }
            }
        }
    
    }


    /**
     * Method name: moveDown()
     * make a move down based on board
     */
    private void moveDown(){
        //join down 
        for (int c = 0; c < GRID_SIZE; c++) {
            for (int r = GRID_SIZE -2; r >= 0; r--) {
                int below = grid[r+1][c];
                int current = grid[r][c]; 
                
                if (current != 0) {
                    if (below == current) { 
                        int sum = below + current;
                        grid[r+1][c] = sum;
                        grid[r][c] = 0;
                        
                        score += sum;
                    }
                }
                else if ( current == 0) {
                    if (below != 0) {
                        grid[r][c] = below;
                        grid[r+1][c] = 0;
                    }
                }


            } 
        }

        //shift down 
        for (int i = 0; i < GRID_SIZE -1; i++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                for(int r = GRID_SIZE -2; r >= 0; r--) {
                    int below = grid[r+1][c];
                    int current = grid[r][c];
                    
                    
                    if (current != 0) {
                        if (below == 0) {
                            grid[r+1][c] = current;
                            grid[r][c] = below;
                        }
                    }
                    
                }
            }
        }

    }


    // TODO PSA3
    // move the board in a certain direction
    // return true if such a move is successful
    public boolean move(Direction direction) {
        //conditional to see if a move can be made
        if (this.canMove(direction)) {
            //conditional for left 
            if (direction.equals(Direction.LEFT)) {
                this.moveLeft();
                return true;
            }

            //conditional for right 
            else if (direction.equals(Direction.RIGHT)) {
                this.moveRight();
                return true;
            }

            //conditional for up
            else if (direction.equals(Direction.UP)) {
                this.moveUp();
                return true;
            }

            //conditional for down
            else {
                this.moveDown();
                return true;
            }			
        } 
       return false; 
        

    }


    // No need to change this for PSA3
    // Check to see if we have a game over
    public boolean isGameOver() {
        //true if cannot move in any direction
        if (this.canMoveLeft() == false && this.canMoveRight() == false
                && this.canMoveUp() == false && 
                this.canMoveDown() == false) {	
            return true;
        } else {
            return false;
        }
    }


    // Return the reference to the 2048 Grid
    public int[][] getGrid() {
        return grid;
    }

    // Return the score
    public int getScore() {
        return score;
    }

    //@Override
    public String toString() {
        StringBuilder outputString = new StringBuilder();
        outputString.append(String.format("Score: %d\n", score));
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int column = 0; column < GRID_SIZE; column++)
                outputString.append(grid[row][column] == 0 ? "    -" :
                        String.format("%5d", grid[row][column]));

            outputString.append("\n");
        }
        return outputString.toString();
    }
}
