package memory.node.getter;

import memory.node.Node;

public class ChecksumNodeGetter implements NodeGetter<Node> {

    @Override
    public Node get(Node node) {
        return node.getFirstChild() == null ? null : node.getFirstChild().getFirstChild();
    }

}
