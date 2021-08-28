package memory.node.filter;

import memory.node.Node;

public interface NodeFilter {
    boolean accept(Node<?> node);
}
