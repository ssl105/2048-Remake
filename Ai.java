import java.util.*;
import javafx.util.Pair;

// node to build sub tree 
public class TreeNode {
    Pair<int [][], Integer> state;
    int playerType;
    ArrayList<Pair<Direction, TreeNode>> children;

    public TreeNode(Pair<int[][], int> state, int playerType) {
        this.state = Pair.with(state.getKey(), state.getValue());
        this.playerType = playerType;
    }

    boolean isTerminal() {
        if (children.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

}

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
        this.root = new Node(rootState, MAX_PLAYER);
        this.searchDepth = searchDepth;
        this.board = new Board(new Random(), rootState);
    }

    // recursive function to build game tree
    public buildTree(TreeNode node, int depth) {
        // end of tree
        if (depth == searchDepth) {
            return;
        }


        // max player
        if (node.playerType == MAX_PLAYER) {
            // find all children from all possible moves.
            for (int i = 0; i < MOVES.length; i++) {
                this.board.reset(node.state);

                if (this.board.canMove(MOVES[i])) {
                    // move the board and add child
                    this.board.move(MOVES[i]);
                    TreeNode curr = new TreeNode(board.getState(), CHANCE_PLAYER);
                    node.children.add(new Pair<Direction, TreeNode> (MOVES[i], curr));

                } else {
                    // check next move
                    continue;
                }

            }
        }


        // chance player
        if (node.playerType == CHANCE_PLAYER) {

        }

    }



}