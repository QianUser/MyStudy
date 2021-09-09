package memory.node.actor.binder;

import memory.node.Node;

import java.util.List;

public class AddNodeBinder implements NodeBinder {

    public enum As {
        SET_FIRST_CHILD, SET_LAST_CHILD, SET_PREVIOUS_SIBLING, SET_NEXT_SIBLING;
    }

    private final Node linkedNode;

    private final Node addRoot;

    private final List<Node> addNodes;

    private final As as;

    private final boolean delete;

    public AddNodeBinder(Node linkedNode, Node addRoot, List<Node> addNodes, As as, boolean delete) {
        this.linkedNode = linkedNode;
        this.addRoot = addRoot;
        this.addNodes = addNodes;
        this.as = as;
        this.delete = delete;
    }

    public Node getLinkedNode() {
        return linkedNode;
    }

    public List<Node> getAddNodes() {
        return addNodes;
    }

    public As getAs() {
        return as;
    }

    public boolean isDelete() {
        return delete;
    }

}
