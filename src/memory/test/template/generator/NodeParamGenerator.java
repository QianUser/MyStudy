package memory.test.template.generator;

import memory.node.Node;
import memory.test.util.Pair;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;

public class NodeParamGenerator extends ParamGenerator {

    private final List<Node> nodes;

    private final Random random;

    public NodeParamGenerator(List<Node> nodes) {
        this.nodes = nodes;
        this.random = new Random();
    }

    @Override
    protected void generateParam(Method method, Object[] objects) {
        objects[0] = nodes.get(random.nextInt(nodes.size()));
    }

    @Override
    protected void generateParamsPair(Pair<Method, Method> methodPair, Object[] objects1, Object[] objects2) {
        throw new UnsupportedOperationException();
    }

}
