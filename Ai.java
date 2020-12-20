import java.util.*;
import javafx.util.Pair;

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

public class Ai {

}