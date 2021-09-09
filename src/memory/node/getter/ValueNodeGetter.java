package memory.node.getter;

import memory.node.Node;

public class ValueNodeGetter implements NodeGetter<Node> {

    @Override
    public Node get(Node node) {
        return node.getChild(2);
    }

}
