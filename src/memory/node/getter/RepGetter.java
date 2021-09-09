package memory.node.getter;

import memory.node.Node;

public interface RepGetter extends NodeGetter<String> {

    @Override
    String get(Node node);

}
