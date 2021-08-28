package memory.node.actor;

import memory.node.Node;

public interface NodeActor {

    void act(Node<?> node);

    default void postHandle(Node<?> node) {}

    default void afterComplete() {}

}
