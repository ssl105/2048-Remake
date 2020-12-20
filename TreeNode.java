import java.util.*;
import javafx.util.Pair;

// node to build sub tree 
public class TreeNode {
    Pair<int [][], Integer> state;
    int playerType;
    ArrayList<Pair<Direction, TreeNode>> children;

    public TreeNode(Pair<int[][], Integer> state, int playerType) {
        this.state = state;
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