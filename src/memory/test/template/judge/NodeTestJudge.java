package memory.test.template.judge;

import memory.node.Node;
import memory.test.test.node.NodeValidGetter;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;

public class NodeTestJudge implements TestJudge {

    private final List<Node> nodes;

    private final int count;

    private final Random random;

    private final NodeValidGetter nodeValidGetter;

    public NodeTestJudge(List<Node> nodes, int count) {
        this.nodes = nodes;
        this.count = count;
        this.random = new Random();
        this.nodeValidGetter = new NodeValidGetter();
    }

    @Override
    public boolean judge(Method method, Object result, Throwable throwable) {
        for (int i = 0; i < count; ++i) {
            if (!nodes.get(random.nextInt(nodes.size())).act(nodeValidGetter)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean judge(Method method1, Method method2, Object result1, Object result2, Throwable throwable1, Throwable throwable2) {
        throw new UnsupportedOperationException();
    }

}
