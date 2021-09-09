package memory.node.filter;

import memory.node.Node;
import memory.node.getter.DeleteStatusNodeGetter;

public class NoDeleteStatusFilter implements NodeFilter {

    private static final DeleteStatusNodeGetter deleteStatusNodeGetter = new DeleteStatusNodeGetter();

    @Override
    public boolean acceptTree(Node node) {
        return node.act(deleteStatusNodeGetter) != null;
    }

}
