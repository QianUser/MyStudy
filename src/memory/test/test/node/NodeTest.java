package memory.test.test.node;

import memory.node.Node;
import memory.node.element.StringElement;
import memory.test.template.AssertTestTemplate;
import memory.test.template.chooser.RandomMethodChooser;
import memory.test.template.filter.NodeMethodFilter;
import memory.test.template.generator.NodeParamGenerator;
import memory.test.template.judge.NodeTestJudge;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static memory.test.util.Utils.progress;

public class NodeTest {

    private final List<Node> nodes;

    private final AssertTestTemplate<Node> assertTestTemplate;

    private final Random random;

    public NodeTest(int count) {
        this.nodes = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            this.nodes.add(new Node(new StringElement(String.valueOf(i))));
        }
        this.assertTestTemplate = new AssertTestTemplate<>(
                nodes.get(0), new NodeMethodFilter(), new RandomMethodChooser(),
                new NodeParamGenerator(nodes), new NodeTestJudge(nodes, 16));
        this.random = new Random();
    }

    public void test() throws InvocationTargetException, IllegalAccessException {
        assertTestTemplate.setObject(nodes.get(random.nextInt(nodes.size())));
        assertTestTemplate.test();
    }

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        NodeTest nodeTest = new NodeTest(1024);
        long times = 1024;
        for (long i = 1; i <= times; ++i) {
            System.out.print("\r" + i + ": " + progress(i, times));
            nodeTest.test();
        }
    }

}
