package memory.node.getter;

import memory.node.Node;

public class IsRootGetter implements NodeGetter<Boolean> {

    @Override
    public Boolean get(Node node) {
        return node.getParent() == null;
    }

}
