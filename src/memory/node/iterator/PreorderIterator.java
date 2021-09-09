package memory.node.iterator;

import memory.node.Node;

public class PreorderIterator implements NodeIterator {

    @Override
    public Node next(Node node) {
        if (node.getFirstChild() != null) {
            return node.getFirstChild();
        } else if (node.getNextSibling() != null) {
            return node.getNextSibling();
        } else {
            while ((node = node.getParent()) != null) {
                if (node.getNextSibling() != null) {
                    node = node.getNextSibling();
                }
            }
        }
        return null;
    }

}
