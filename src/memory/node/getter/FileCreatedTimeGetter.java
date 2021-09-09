package memory.node.getter;

import memory.node.Node;
import memory.node.element.StringElement;
import memory.node.filter.NodeFilter;

import java.nio.file.attribute.FileTime;
import java.time.Instant;

public class FileCreatedTimeGetter implements NodeGetter<FileTime> {

    private static final ValueNodeGetter valueNodeGetter = new ValueNodeGetter();

    @Override
    public FileTime get(Node node) {
        Node keyNode = node.getChild(new NodeFilter() {
            @Override
            public boolean acceptNode(Node node) {
                return node.getUnwrapped(StringElement.class).getString().equals("创建时间");
            }
        });
        if (keyNode == null) {
            return null;
        }
        Node valueNode = keyNode.act(valueNodeGetter);
        return valueNode == null ? null : FileTime.from(Instant.parse(valueNode.getUnwrapped(StringElement.class).getString()));
    }

}
