package test3;

import java.util.*;


public class BetterProgrammerTask {

    // Please do not change this interface
    public static interface Node {
        int getValue();
        List<Node> getChildren();
    }


    public static int getLevelSum(Node root, int N) {
        /*
          Please implement this method to
          traverse the tree and return the sum of the values (Node.getValue()) of all nodes
          at the level N in the tree.
          Node root is assumed to be at the level 1. All its children are level 2, etc.
         */

        if(N == 0) {
            return 0;
        }

        List<Node> currLevel = Arrays.asList(root);
        for(int currDepth = 1;  currDepth < N; currDepth++) {
            currLevel = getAllChildren(currLevel);
        }
        int result = 0;
        for(Node currChildren : currLevel) {
            result += currChildren.getValue();
        }
        return result;
    }

    private static List<Node> getAllChildren(List<Node> currLevel) {
        List<Node> result = new ArrayList<Node>();
        for(Node currNode : currLevel) {
            result.addAll(currNode.getChildren());
        }
        return result;
    }
}

