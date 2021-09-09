package memory.test.test.node;

import memory.node.Node;
import memory.node.getter.NodeGetter;

public class NodeValidGetter implements NodeGetter<Boolean> {

    @Override
    public Boolean get(Node node) {
        if (node.getParent() != null) {
            if (!((node.getParent().getFirstChild() == node) == (node.getPrevSibling() == null))) {
                return false;
            }
            if (!((node.getParent().getLastChild() == node) == (node.getNextSibling() == null))) {
                return false;
            }
        } else {
            if (node.getPrevSibling() != null || node.getNextSibling() != null) {
                return false;
            }
        }
        if (node.getPrevSibling() != null) {
            if (node.getPrevSibling().getNextSibling() != node) {
                return false;
            }
        }
        if (node.getNextSibling() != null) {
            if (node.getNextSibling().getPrevSibling() != node) {
                return false;
            }
        }
        for (Node child : node.getChildren()) {
            if (child.getParent() != node) {
                return false;
            }
        }
        return true;
    }

}
