package memory.node.getter;

import memory.node.Node;
import memory.node.actor.NodeActor;

public interface NodeGetter<T> extends NodeActor {
    T get(Node node);
}