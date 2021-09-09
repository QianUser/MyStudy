package memory.node.getter;

import memory.node.Node;

public class DeleteStatusNodeGetter implements NodeGetter<Node> {

    @Override
    public Node get(Node node) {
        return node.getChild(1) == null ? null : node.getChild(1).getChild(1);
    }

}
