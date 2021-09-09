package memory.node.filter;

import memory.node.Node;
import memory.node.getter.ChecksumNodeGetter;

public class NoChecksumFilter implements NodeFilter {

    private static final ChecksumNodeGetter checksumNodeGetter = new ChecksumNodeGetter();

    @Override
    public boolean acceptTree(Node node) {
        return node.act(checksumNodeGetter) != null;
    }

}
