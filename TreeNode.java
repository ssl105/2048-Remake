import java.util.*;
import javafx.util.Pair;

// node to build sub tree 
public class TreeNode {
    Pair<int [][], Integer> state;
    int playerType;
    ArrayList<Pair<Direction, TreeNode>> children;

    public TreeNode(Pair<int[][], Integer> state, int playerType) {
        int[][] b = new int[state.getKey().length][];
        for(int i = 0; i < state.getKey().length; i++) {
            b[i] = state.getKey()[i].clone();
        }

        this.state = new Pair<int[][], Integer>(b, state.getValue());
        this.playerType = playerType;

        children = new ArrayList<Pair<Direction, TreeNode>> ();
    }

    // indicate if terminal node of the tree
    boolean isTerminal() {
        if (children.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    // evaluate the state of the board to prioritize higher scoring tiles
    // to be positioned to the bottom right of the board
    // value returned is larger if heavy weighted pieces are further
    // from the bottom right
    int evaluate() {
        int value = 0;
        int boardlen = this.state.getKey().length;

        // go through the board
        for (int i = 0; i < boardlen; i++) {
            for (int j = 0; j < boardlen; j++) {
                int [][] tile = this.state.getKey();
                int tileVal = tile[i][j];

                if (tileVal != 0) {
                    value = ((boardlen - i) + (boardlen -j)) * tileVal + value;
                }
            }
        }

        return value;

    }

}