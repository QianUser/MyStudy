package memory.node.getter;

import memory.node.Node;
import memory.node.element.StringElement;
import memory.node.filter.NodeFilter;

public class LengthGetter implements NodeGetter<Long> {

    private static final ValueNodeGetter valueNodeGetter = new ValueNodeGetter();

    @Override
    public Long get(Node node) {
        Node keyNode = node.getChild(new NodeFilter() {
            @Override
            public boolean acceptNode(Node node) {
                return node.getUnwrapped(StringElement.class).getString().equals("长度");
            }
        });
        if (keyNode == null) {
            return null;
        }
        Node valueNode = keyNode.act(valueNodeGetter);
        return valueNode == null ? 0L : Long.parseLong(valueNode.getUnwrapped(StringElement.class).getString());
    }

}
