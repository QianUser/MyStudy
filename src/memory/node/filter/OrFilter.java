package memory.node.filter;

import memory.node.Node;

public class OrFilter implements NodeFilter {

    private final NodeFilter nodeFilter;

    public OrFilter(NodeFilter... nodeFilters) {
        this.nodeFilter = new NodeFilter() {
            @Override
            public boolean acceptNode(Node node) {
                for (NodeFilter nodeFilter : nodeFilters) {
                    if (nodeFilter.acceptNode(node)) {
                        return true;
                    }
                }
                return false;
            }
            @Override
            public boolean acceptTree(Node tree) {
                for (NodeFilter nodeFilter : nodeFilters) {
                    if (nodeFilter.acceptNode(tree)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    @Override
    public boolean acceptNode(Node node) {
        return nodeFilter.acceptNode(node);
    }

}
