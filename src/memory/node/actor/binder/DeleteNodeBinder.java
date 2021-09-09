package memory.node.actor.binder;

import memory.node.Node;

public class DeleteNodeBinder implements NodeBinder {

    private final Node node;

    public DeleteNodeBinder(Node node) {
        this.node = node;
    }

    public Node getNode() {
        return node;
    }

}
