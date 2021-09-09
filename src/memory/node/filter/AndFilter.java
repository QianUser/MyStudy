package memory.node.filter;

import memory.node.Node;

public class AndFilter implements NodeFilter {

    private final NodeFilter nodeFilter;

    public AndFilter(NodeFilter... nodeFilters) {
        this.nodeFilter = new NodeFilter() {
            @Override
            public boolean acceptNode(Node node) {
                for (NodeFilter nodeFilter : nodeFilters) {
                    if (!nodeFilter.acceptNode(node)) {
                        return false;
                    }
                }
                return true;
            }
            @Override
            public boolean acceptTree(Node tree) {
                for (NodeFilter nodeFilter : nodeFilters) {
                    if (!nodeFilter.acceptTree(tree)) {
                        return false;
                    }
                }
                return true;
            }
        };
    }

    @Override
    public boolean acceptNode(Node node) {
        return nodeFilter.acceptNode(node);
    }

}
