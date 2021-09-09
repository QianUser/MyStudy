package memory.node.iterator;

import memory.node.Node;

public class PostorderIterator implements NodeIterator {

    @Override
    public Node next(Node node) {
        if (node.getNextSibling() != null) {
            return next(node.getNextSibling());
        } else if (node.getParent() != null) {
            return node.getParent();
        } else {
            return null;
        }
    }

}
