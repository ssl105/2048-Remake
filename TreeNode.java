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

    boolean isTerminal() {
        if (children.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

}