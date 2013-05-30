package test3;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class BetterProgrammerTaskTest {

    private static class NodeImpl implements BetterProgrammerTask.Node {
        private List<BetterProgrammerTask.Node> children;
        private int value;

        NodeImpl(int value) {
            this.value = value;
            children = new ArrayList<BetterProgrammerTask.Node>();
        }

        void setChildren(List<BetterProgrammerTask.Node>  children) {
            this.children = children;
        }

        @Override
        public int getValue() {
            return value;
        }

        @Override
        public List<BetterProgrammerTask.Node> getChildren() {
            return children;
        }
    }

    @Test
    public void shouldReturnRootForLevel1() {
        assertEquals(100, (new BetterProgrammerTask()).getLevelSum(new NodeImpl(100), 1));
    }

    @Test
    public void shouldReturnZeroForDepthZero() {
        assertEquals(0, (new BetterProgrammerTask()).getLevelSum(new NodeImpl(100), 0));
    }

    @Test
    public void shouldReturnZeroForDepthGreatherThanAvailable() {
        NodeImpl a = new NodeImpl(100);
        NodeImpl a1 = new NodeImpl(100);
        NodeImpl a2 = new NodeImpl(100);
        a.setChildren(Arrays.asList(new BetterProgrammerTask.Node[]{a1, a2}));

        assertEquals(0, (new BetterProgrammerTask()).getLevelSum(a, 3));
    }

    @Test
    public void shouldReturnSumOfNodesInDepth() {
        NodeImpl a = new NodeImpl(100);
        NodeImpl a1 = new NodeImpl(100);
        NodeImpl a2 = new NodeImpl(100);
        NodeImpl a11 = new NodeImpl(100);
        NodeImpl a12 = new NodeImpl(100);
        NodeImpl a21 = new NodeImpl(100);
        NodeImpl a211 = new NodeImpl(100);
        a.setChildren(Arrays.asList(new BetterProgrammerTask.Node[]{a1, a2}));
        a1.setChildren(Arrays.asList(new BetterProgrammerTask.Node[]{a11, a12}));
        a2.setChildren(Arrays.asList(new BetterProgrammerTask.Node[]{a21}));
        a21.setChildren(Arrays.asList(new BetterProgrammerTask.Node[]{a211}));
        assertEquals(300, (new BetterProgrammerTask()).getLevelSum(a, 3));
    }
}
