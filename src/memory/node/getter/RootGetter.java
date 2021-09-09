package memory.node.getter;

import memory.node.Node;

public class RootGetter implements NodeGetter<Node> {

    @Override
    public Node get(Node node) {
        while (node.getParent() != null) {
            node = node.getParent();
        }
        return node;
    }

}
