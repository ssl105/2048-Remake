import java.util.*;
import javafx.util.Pair;

// ai agent to find next best move
public class Ai {
    public final int MAX_PLAYER = 0;
    public final int CHANCE_PLAYER = 1;
    public final Direction [] MOVES = new Direction[] {Direction.UP, Direction.LEFT, Direction.DOWN, Direction.RIGHT};

    TreeNode root;
    int searchDepth;
    Board board;

    // intialize ai object
    public Ai(Pair<int [][], Integer> rootState, int searchDepth) {
        this.root = new TreeNode(rootState, MAX_PLAYER);
        this.searchDepth = searchDepth;
        this.board = new Board(new Random(), rootState);
    }

    // recursive function to build game tree
    public void buildTree(TreeNode node, int depth) {
        if (node == null) {
            node = this.root;
        }
        // end of tree
        if (depth == searchDepth) {
            return;
        }


        // max player
        if (node.playerType == MAX_PLAYER) {
            // find all children from all possible moves.
            for (int i = 0; i < MOVES.length; i++) {
                this.board.reset(node.state);

                if (this.board.move(MOVES[i])) {
                    // move the board and add child
                    TreeNode curr = new TreeNode(this.board.getState(), CHANCE_PLAYER);
                    node.children.add(new Pair<Direction, TreeNode> (MOVES[i], curr));

                }

            }
        }

        // chance player
        else if (node.playerType == CHANCE_PLAYER) {
            this.board.reset(node.state);
            ArrayList<Pair<Integer, Integer>> tiles = this.board.getOpenTiles();
            
            //add child nodes to chance player
            for (int i = 0; i < tiles.size(); i++ ) {
                Pair<Integer, Integer> tile = tiles.get(i);
                this.board.addTile(tile.getKey(), tile.getValue(), 2);
                TreeNode curr = new TreeNode(this.board.getState(), MAX_PLAYER);
                node.children.add(new Pair<Direction, TreeNode> (null, curr));
                this.board.addTile(tile.getKey(), tile.getValue(), 0);
            }
        }

        // build a tree for each child of this node
        for (int i = 0; i < node.children.size(); i++) {
            this.buildTree(node.children.get(i).getValue(), depth + 1);
        }

    }

    // expectimax implementation
    // returns a (best direction, best value) for a max player
    // returns a (null, expected best value) for a chance player
    public Pair<Direction, Float> expectimax(TreeNode node) {
        if ( node == null) {
            node = this.root;
        }

        // terminal node
        if (node.isTerminal()) {
            float val = (float) node.state.getValue() - this.evaluate(node);

            return new Pair<Direction, Float> (null, val);
        }
        // max player
        else if (node.playerType == MAX_PLAYER) {
            float value = Float.MIN_VALUE; 
            Direction currD = null;

            // get the child the has the max score
            for (int i = 0; i < node.children.size(); i++) {
                Pair<Direction, Float> curr = this.expectimax(node.children.get(i).getValue());

                if (curr.getValue() >=  value) {
                    value = curr.getValue();
                    currD = node.children.get(i).getKey();

                }
            }

            return new Pair<Direction, Float> (currD, value);

        }
        // chance player
        else if (node.playerType == CHANCE_PLAYER) {
            float value = 0;

            for (int i = 0; i < node.children.size(); i++) {
                Pair<Direction, Float> curr = this.expectimax(node.children.get(i).getValue());
                value = value + curr.getValue()/node.children.size();
            }

            return new Pair<Direction, Float> (null, value);

        }

        return null;


    }

    // evaluate the state of the board to prioritize higher scoring tiles
    // positioned to the bottom right
    // value returned is larger if heavy weighted pieces are further
    // from the bottom right
    public int evaluate(TreeNode node) {
        int value = 0;
        int boardlen = node.state.getKey().length;

        // go through the board
        for (int i = 0; i < boardlen; i++) {
            for (int j = 0; j < boardlen; j++) {
                tileVal = node.state.getKey()[i][j];

                if (tileVal != 0) {
                    value = value + ((boardlen - i) + (boardlen -j)) * tileVal;
                }
            }
        }

        return value;

    }

    // find the best move using expectimax tree search algorithm
    public Direction computeDecision() {
        this.buildTree(this.root, 0);
        Pair<Direction, Float> result = this.expectimax(this.root);

        return result.getKey();
    }


}