package memory.node.getter;

import memory.node.Node;

public class IsCognateGetter implements NodeGetter<Boolean> {

    private static final RootGetter rootGetter = new RootGetter();

    private final Node comparedNode;

    public IsCognateGetter(Node comparedNode) {
        this.comparedNode = comparedNode;
    }

    @Override
    public Boolean get(Node node) {
        return node.act(rootGetter) == comparedNode.act(rootGetter);
    }

}
