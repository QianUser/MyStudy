package memory.node.filter;

import memory.node.Node;

public interface NodeFilter {

    default boolean acceptNode(Node node) {
        return true;
    }

    default boolean acceptTree(Node tree) {
        return true;
    }

}
