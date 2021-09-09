package memory.node.filter;

import memory.node.Node;
import memory.node.element.MemoryElement;

public class MemoryStarterFilter implements NodeFilter {

    private final long start;

    public MemoryStarterFilter(long start) {
        this.start = start;
    }

    @Override
    public boolean acceptNode(Node node) {
        return node.getElement(MemoryElement.class).getStart() == start;
    }

    @Override
    public boolean acceptTree(Node tree) {
        return (tree.getNextSibling() == null || tree.getNextSibling().getElement(MemoryElement.class).getStart() > start) && tree.getElement(MemoryElement.class).getStart() <= start;
    }

}
