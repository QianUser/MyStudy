package memory.node.actor;

import memory.node.Node;

public interface NodeActor {

    default void forward(Node node) {}

    default void back(Node node) {}

    default void complete(Node node) {}

}
