package memory.node.getter;

import memory.node.Node;

public class IsLeafGetter implements NodeGetter<Boolean> {
    @Override
    public Boolean get(Node node) {
        return node.getFirstChild() == null;
    }
}
