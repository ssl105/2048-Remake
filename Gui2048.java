/**
 * Filename: Gui2048.java
 * 
 * @author Scott Lee cs8bwabq ssl105@ucsd.edu
 * 02/24/18
 *
 * This file holds the Gui2048 class. This program allows users to play
 * the 2048 game with arrow keys. Users can save the game and play till they
 * lose. 
 */
import javafx.application.*;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import java.util.*;

import java.io.*;

/**
 * Class Name: Gui2048
 * 
 * This class creates a interface for the user to play 2048. 
 * The Gui holds a board and displays it on screen with a pane. 
 * Users can control movement with the arrow keys and can save the board
 * with "s" key. 
 */
public class Gui2048 extends Application
{
    private String outputBoard; // The filename for where to save the Board
    private Board board; // The 2048 Game Board

    private GridPane pane; //pane that holds the 2048 game

    /** Add your own Instance Variables here */
    private Tile[][] tiles; //tile array that holds the boards tiles 
    private Text score; //text for the score
    private Integer value; // value that holds score
    private int gridSize;  //size of the grid
    private StackPane gameOver; //pane for when the game is over
    private boolean gameIsOver; //boolean indicating if game is over
    private boolean autoSolve; //boolean indicating if auto solver is on


    @Override
    public void start(Stage primaryStage)
    {
        // Process Arguments and Initialize the Game Board
        processArgs(getParameters().getRaw().toArray(new String[0]));

        // Create the pane that will hold all of the visual objects
        pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        pane.setStyle("-fx-background-color: rgb(141, 0, 211)");
        // Set the spacing between the Tiles
        pane.setHgap(15); 
        pane.setVgap(15);

        /** Add your Code for the GUI Here */
        //creating game over pane
        gameOver = new StackPane();
        gameOver.setStyle("-fx-background-color: rgb(141, 0, 211, 0.5)");
        Label gOver = new Label("Game Over!" + "\n" + "YOU LOSE!");
        gOver.setFont(Font.font("Times New Roman", FontWeight.BOLD, 
                    FontPosture.ITALIC, 70));
        gOver.setTextFill(Color.GOLD);
        gameOver.getChildren().add(gOver);

        //variables
        gridSize = board.GRID_SIZE;
        value = board.getScore(); 

        //creating headers: 2048 and Score:
        Label label = new Label("2048");
        label.setFont(Font.font("Times New Roman", FontWeight.BOLD, 
                    FontPosture.ITALIC, 40));
        label.setTextFill(Color.GOLD);

        score = new Text();
        score.setText("Score:" + value.toString());
        score.setFont(Font.font("Times New Roman", FontWeight.BOLD, 30));
        score.setFill(Color.GOLD); 

        //adding headers to pane
        pane.add(label, 0, 0, 2, 1);
        pane.add(score, gridSize-2, 0, 2, 1);
        GridPane.setHalignment(label, HPos.CENTER);
        GridPane.setHalignment(score, HPos.CENTER);

        //creating tile reference array and creating board on pane
        this.createTiles();

        //adding pane to a scene and stage
        Scene scene = new Scene(pane);
        primaryStage.setTitle("Gui2048");
        primaryStage.setScene(scene);
        primaryStage.show(); 
        
        //take in keys for controls
        scene.setOnKeyPressed(new myKeyHandler());

        //handle ai
        if (autoSolve) {
            Ai ai = new Ai(this.board.getState(), 3);
            Direction move = ai.computeDecision();
            this.board.move(move);

            //gameover conditional
            if (Gui2048.this.board.isGameOver()) {
                Gui2048.this.pane.add(Gui2048.this.gameOver, 0, 0,
                        Gui2048.this.gridSize, 
                        Gui2048.this.gridSize +1); 
                Gui2048.this.gameIsOver = true;
            }
        }

    }


    /**
     * Class Name: Tile
     * 
     * This class is a representation of a tile in 2048 game.
     * A tile a holds a rectangle square with a specific color and 
     * and a text indicating the value of the tile.
     */ 
    class Tile{
        private Rectangle tile; //square tile
        private Text text; //text for value 

        /**
         * No arg constructor for Tile. Initializes tile to be empty fill
         * with an empty text. 
         */
        public Tile() {
            tile = new Rectangle();
            tile.setWidth(Constants2048.TILE_WIDTH);
            tile.setHeight(Constants2048.TILE_WIDTH);
            tile.setFill(Constants2048.COLOR_EMPTY);

            text = new Text();
            text.setText("");
            text.setFont(Font.font("Times New Roman", FontWeight.BOLD, 
                        Constants2048.TEXT_SIZE_LOW));
            text.setFill(Color.BLACK); 
        }

        /**
         * General constructor for Tile. Intializes tile based on inputed
         * integer value. The integer determines what fill the tile is
         * and what text to display.
         *
         * @param i - integer indicating the value of the tile
         */ 
        public Tile(int i) {
            this();

            //conditionals for all values on the 2048 board.
            if (i == 2) {
                this.tile.setFill(Constants2048.COLOR_2);
                this.text.setText("2");
            }
            else if (i == 4) {
                this.tile.setFill(Constants2048.COLOR_4);
                this.text.setText("4");
            }
            else if (i == 8) {
                this.tile.setFill(Constants2048.COLOR_8);
                this.text.setText("8");
            }
            else if (i == 16) {
                this.tile.setFill(Constants2048.COLOR_16);
                this.text.setText("16");
            }   
            else if (i == 32) {
                this.tile.setFill(Constants2048.COLOR_32);
                this.text.setText("32");
            }
            else if (i == 64) {
                this.tile.setFill(Constants2048.COLOR_64);
                this.text.setText("64");
            }
            else if (i == 128) {
                this.tile.setFill(Constants2048.COLOR_128);
                this.text.setText("128");
                this.text.setFont(Font.font("Times New Roman", 
                            FontWeight.BOLD, 
                            Constants2048.TEXT_SIZE_MID));
            }
            else if (i == 256) {
                this.tile.setFill(Constants2048.COLOR_256);
                this.text.setText("256");
                this.text.setFont(Font.font("Times New Roman", 
                            FontWeight.BOLD, 
                            Constants2048.TEXT_SIZE_MID));
            }
            else if (i == 512) {
                this.tile.setFill(Constants2048.COLOR_512);
                this.text.setText("512");
                this.text.setFont(Font.font("Times New Roman", 
                            FontWeight.BOLD, 
                            Constants2048.TEXT_SIZE_MID));
            }
            else if (i == 1024) {
                this.tile.setFill(Constants2048.COLOR_1024);
                this.text.setText("1024");
                this.text.setFont(Font.font("Times New Roman", 
                            FontWeight.BOLD, 
                            Constants2048.TEXT_SIZE_HIGH));
            }
            else if (i == 2048) {
                this.tile.setFill(Constants2048.COLOR_2048);
                this.text.setText("2048");
                this.text.setFont(Font.font("Times New Roman", 
                            FontWeight.BOLD, 
                            Constants2048.TEXT_SIZE_HIGH));
            }
            else if (i > 2048) {
                this.tile.setFill(Constants2048.COLOR_OTHER);
                Integer num = i;
                this.text.setText(num.toString());
                this.text.setFont(Font.font("Times New Roman", 
                            FontWeight.BOLD, 
                            Constants2048.TEXT_SIZE_HIGH));
            }

        }

        /**
         * getter for the Rectangle of a Tile 
         * 
         * @return the rectangle of a tile
         */
        public Rectangle getTile() {
            return this.tile;
        }

        /**
         * getter for the text of a tile
         *
         * @return the text of a tile 
         */
        public Text getText() {
            return this.text;
        }

    }

    
    /**
     * Class Name: myKeyHandler
     *  
     * This class handles the events for user control over the game.
     * Up arrow moves the board up
     * Down arrow moves the board down
     * Left arrrow moves the board left
     * Right arrow move sthe board right
     * S saves the board
     */
    private class myKeyHandler implements EventHandler<KeyEvent> {
        /**
         * handles the events where arrow keys or "s" key is pressed 
         * by the user playing 2048
         *
         * @param e - KeyEvent indicating the key pressed by user
         */
        @Override
        public void handle(KeyEvent e){
            //don't KeyEvents if the game is over
            if (Gui2048.this.gameIsOver) {
                return;
            } 

            // only handle enter key when auto solve is on
            if (this.autoSolve) {
                if (e.getCode() == KeyCode.ENTER) {
                    this.autoSolve = false;
                }

                return;
            }


            //handling arrow keys and save board and checking game over
            //after every valid move. do nothing if move is invalid
            if (e.getCode() == KeyCode.UP) {
                if (Gui2048.this.board.move(Direction.UP)) {
                    //updating board and score and indicate the move
                    Gui2048.this.board.addRandomTile();
                    System.out.println("Moving Up");
                    Gui2048.this.updateTiles();
                    Gui2048.this.value = Gui2048.this.board.getScore();
                    Gui2048.this.score.setText("Score:" 
                                                        + value.toString());

                    //gameover conditional
                    if (Gui2048.this.board.isGameOver()) {
                        Gui2048.this.pane.add(Gui2048.this.gameOver, 0, 0,
                                Gui2048.this.gridSize, 
                                Gui2048.this.gridSize +1); 
                        Gui2048.this.gameIsOver = true;
                    }
                } else {
                    //nothing 
                } 

            } 
            else if (e.getCode() == KeyCode.RIGHT) {
                if (Gui2048.this.board.move(Direction.RIGHT)) {
                    //updating board and score and indicate the move
                    Gui2048.this.board.addRandomTile();
                    System.out.println("Moving Right");
                    Gui2048.this.updateTiles();
                    Gui2048.this.value = Gui2048.this.board.getScore();
                    Gui2048.this.score.setText("Score:" 
                                                        + value.toString());

                    //gameover conditional
                    if (Gui2048.this.board.isGameOver()) {
                        Gui2048.this.pane.add(Gui2048.this.gameOver, 0, 0,
                                Gui2048.this.gridSize, 
                                Gui2048.this.gridSize +1); 
                        Gui2048.this.gameIsOver = true;
                    }
                } else {
                    //nothing 
                }
            }
            else if (e.getCode() == KeyCode.LEFT) {
                if (Gui2048.this.board.move(Direction.LEFT)) {
                    //updating board and score and indicate the move
                    Gui2048.this.board.addRandomTile();
                    System.out.println("Moving Left");
                    Gui2048.this.updateTiles();
                    Gui2048.this.value = Gui2048.this.board.getScore();
                    Gui2048.this.score.setText("Score:" 
                                                        + value.toString());

                    //gameover conditional
                    if (Gui2048.this.board.isGameOver()) {
                        Gui2048.this.pane.add(Gui2048.this.gameOver, 0, 0,
                                Gui2048.this.gridSize, 
                                Gui2048.this.gridSize +1); 
                        Gui2048.this.gameIsOver = true;
                    }
                } else {
                    //nothing 
                }
            }
            else if (e.getCode() == KeyCode.DOWN) {
                if (Gui2048.this.board.move(Direction.DOWN)) {
                    //updating board and score and indicate the move
                    Gui2048.this.board.addRandomTile();
                    System.out.println("Moving Down");
                    Gui2048.this.updateTiles();
                    Gui2048.this.value = Gui2048.this.board.getScore();
                    Gui2048.this.score.setText("Score:" 
                                                       + value.toString());

                    //gameover conditional
                    if (Gui2048.this.board.isGameOver()) {
                        Gui2048.this.pane.add(Gui2048.this.gameOver, 0, 0,
                                Gui2048.this.gridSize, 
                                Gui2048.this.gridSize +1); 
                        Gui2048.this.gameIsOver = true;
                    }
                } else {
                    //nothing 
                }
            }
            else if (e.getCode() == KeyCode.S) {
                //saving the board
                System.out.println("Saving Board to " + outputBoard);
                try { 
                    board.saveBoard(outputBoard);
                } catch(IOException b) {
                    System.out.println("saveBoard threw an Exception");
                } 
            }
            else if (e.getCode() == KeyCode.ENTER) {
                this.autoSolve = true;
            }
            
        }
    }



    /** Add your own Instance Methods Here */

    /**
     * Creates the tiles based on the board and adds the tiles to 
     * the pane. 
     * 
     */
    private void createTiles() {
        //creating new tile array 
        tiles = new Tile[gridSize][gridSize];

        for (int r = 0; r < gridSize; r++) {
            for (int c = 0; c < gridSize; c++) {
                //create tile based on the value of the tile on board
                tiles[r][c] = new Tile(board.getGrid()[r][c]);

                pane.add(tiles[r][c].getTile(), c, r+1);
                pane.add(tiles[r][c].getText(), c, r+1);
                GridPane.setHalignment(tiles[r][c].getText(), 
                        HPos.CENTER);  

            }
        }
    }

    
    /**
     * Update the tile array based on the move made on the board.
     * By updating the tile array, the pane shows the changes made
     */
    private void updateTiles() {
        for (int r = 0; r < gridSize; r++) {
            for (int c = 0; c < gridSize; c++) {
                //update tile array based on the value on the board
                //conditionals for ALL values
                if (board.getGrid()[r][c] == 0) {
                    tiles[r][c].getText().setText("");
                    tiles[r][c].getTile().setFill( 
                            Constants2048.COLOR_EMPTY);

                }
                if (board.getGrid()[r][c] == 2) {
                    tiles[r][c].getTile().setFill(Constants2048.COLOR_2);
                    tiles[r][c].getText().setText("2");
                    tiles[r][c].getText().setFont(Font.font(
                                "Times New Roman", 
                                FontWeight.BOLD, 
                                Constants2048.TEXT_SIZE_LOW));
                }
                else if (board.getGrid()[r][c] == 4) {
                    tiles[r][c].getTile().setFill(Constants2048.COLOR_4);
                    tiles[r][c].getText().setText("4");
                    tiles[r][c].getText().setFont(Font.font(
                                "Times New Roman", 
                                FontWeight.BOLD, 
                                Constants2048.TEXT_SIZE_LOW));
                }
                else if (board.getGrid()[r][c] == 8) {
                    tiles[r][c].getTile().setFill(Constants2048.COLOR_8);
                    tiles[r][c].getText().setText("8");
                    tiles[r][c].getText().setFont(Font.font(
                                "Times New Roman", 
                                FontWeight.BOLD, 
                                Constants2048.TEXT_SIZE_LOW));
                }
                else if (board.getGrid()[r][c] == 16) {
                    tiles[r][c].getTile().setFill(Constants2048.COLOR_16);
                    tiles[r][c].getText().setText("16");
                    tiles[r][c].getText().setFont(Font.font(
                                "Times New Roman", 
                                FontWeight.BOLD, 
                                Constants2048.TEXT_SIZE_LOW));
                }   
                else if (board.getGrid()[r][c] == 32) {
                    tiles[r][c].getTile().setFill(Constants2048.COLOR_32);
                    tiles[r][c].getText().setText("32");
                    tiles[r][c].getText().setFont(Font.font(
                                "Times New Roman", 
                                FontWeight.BOLD, 
                                Constants2048.TEXT_SIZE_LOW));
                }
                else if (board.getGrid()[r][c] == 64) {
                    tiles[r][c].getTile().setFill(Constants2048.COLOR_64);
                    tiles[r][c].getText().setText("64");
                    tiles[r][c].getText().setFont(Font.font(
                                "Times New Roman", 
                                FontWeight.BOLD, 
                                Constants2048.TEXT_SIZE_LOW));
                }
                else if (board.getGrid()[r][c] == 128) {
                    tiles[r][c].getTile().setFill(Constants2048.COLOR_128);
                    tiles[r][c].getText().setText("128");
                    tiles[r][c].getText().setFont(Font.font(
                                "Times New Roman", 
                                FontWeight.BOLD, 
                                Constants2048.TEXT_SIZE_MID));
                }
                else if (board.getGrid()[r][c] == 256) {
                    tiles[r][c].getTile().setFill(Constants2048.COLOR_256);
                    tiles[r][c].getText().setText("256");
                    tiles[r][c].getText().setFont(Font.font(
                                "Times New Roman", 
                                FontWeight.BOLD, 
                                Constants2048.TEXT_SIZE_MID));
                }
                else if (board.getGrid()[r][c] == 512) {
                    tiles[r][c].getTile().setFill(Constants2048.COLOR_512);
                    tiles[r][c].getText().setText("512");
                    tiles[r][c].getText().setFont(Font.font(
                                "Times New Roman", 
                                FontWeight.BOLD, 
                                Constants2048.TEXT_SIZE_MID));
                }
                else if (board.getGrid()[r][c] == 1024) {
                    tiles[r][c].getTile().setFill(Constants2048.COLOR_1024);
                    tiles[r][c].getText().setText("1024");
                    tiles[r][c].getText().setFont(Font.font(
                                "Times New Roman", 
                                FontWeight.BOLD, 
                                Constants2048.TEXT_SIZE_HIGH));
                }
                else if (board.getGrid()[r][c] == 2048) {
                    tiles[r][c].getTile().setFill(Constants2048.COLOR_2048);
                    tiles[r][c].getText().setText("2048");
                    tiles[r][c].getText().setFont(Font.font(
                                "Times New Roman", 
                                FontWeight.BOLD, 
                                Constants2048.TEXT_SIZE_HIGH));
                }
                else if (board.getGrid()[r][c] > 2048) {
                    tiles[r][c].getTile().setFill(
                                                Constants2048.COLOR_OTHER);
                    Integer num = board.getGrid()[r][c];
                    tiles[r][c].getText().setText(num.toString());
                    tiles[r][c].getText().setFont(Font.font(
                                "Times New Roman", 
                                FontWeight.BOLD, 
                                Constants2048.TEXT_SIZE_HIGH));
                }
            }
        }
    }



    /** DO NOT EDIT BELOW */

    // The method used to process the command line arguments
    private void processArgs(String[] args)
    {
        String inputBoard = null;   // The filename for where to load the Board
        int boardSize = 0;          // The Size of the Board

        // Arguments must come in pairs
        if((args.length % 2) != 0)
        {
            printUsage();
            System.exit(-1);
        }

        // Process all the arguments 
        for(int i = 0; i < args.length; i += 2)
        {
            if(args[i].equals("-i"))
            {   // We are processing the argument that specifies
                // the input file to be used to set the board
                inputBoard = args[i + 1];
            }
            else if(args[i].equals("-o"))
            {   // We are processing the argument that specifies
                // the output file to be used to save the board
                outputBoard = args[i + 1];
            }
            else if(args[i].equals("-s"))
            {   // We are processing the argument that specifies
                // the size of the Board
                boardSize = Integer.parseInt(args[i + 1]);
            }
            else
            {   // Incorrect Argument 
                printUsage();
                System.exit(-1);
            }
        }

        // Set the default output file if none specified
        if(outputBoard == null)
            outputBoard = "2048.board";
        // Set the default Board size if none specified or less than 2
        if(boardSize < 2)
            boardSize = 4;

        // Initialize the Game Board
        try{
            if(inputBoard != null)
                board = new Board(new Random(), inputBoard);
            else
                board = new Board(new Random(), boardSize);
        }
        catch (Exception e)
        {
            System.out.println(e.getClass().getName() + 
                    " was thrown while creating a " +
                    "Board from file " + inputBoard);
            System.out.println("Either your Board(String, Random) " +
                    "Constructor is broken or the file isn't " +
                    "formated correctly");
            System.exit(-1);
        }
    }

    // Print the Usage Message 
    private static void printUsage()
    {
        System.out.println("Gui2048");
        System.out.println("Usage:  Gui2048 [-i|o file ...]");
        System.out.println();
        System.out.println("  Command line arguments come in pairs of the "+ 
                "form: <command> <argument>");
        System.out.println();
        System.out.println("  -i [file]  -> Specifies a 2048 board that " + 
                "should be loaded");
        System.out.println();
        System.out.println("  -o [file]  -> Specifies a file that should be " + 
                "used to save the 2048 board");
        System.out.println("                If none specified then the " + 
                "default \"2048.board\" file will be used");  
        System.out.println("  -s [size]  -> Specifies the size of the 2048" + 
                "board if an input file hasn't been"); 
        System.out.println("                specified.  If both -s and -i" + 
                "are used, then the size of the board"); 
        System.out.println("                will be determined by the input" +
                " file. The default size is 4.");
    }
}
