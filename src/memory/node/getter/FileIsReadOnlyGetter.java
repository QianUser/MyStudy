package memory.node.getter;

import memory.node.Node;
import memory.node.element.StringElement;
import memory.node.filter.NodeFilter;

public class FileIsReadOnlyGetter implements NodeGetter<Boolean> {

    private static final ValueNodeGetter valueNodeGetter = new ValueNodeGetter();

    @Override
    public Boolean get(Node node) {
        Node keyNode = node.getChild(new NodeFilter() {
            @Override
            public boolean acceptNode(Node node) {
                return node.getUnwrapped(StringElement.class).getString().equals("只读");
            }
        });
        if (keyNode == null) {
            return null;
        }
        Node valueNode = keyNode.act(valueNodeGetter);
        return valueNode == null ? null : valueNode.getUnwrapped(StringElement.class).getString().equals("是");
    }

}
