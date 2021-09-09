package memory.node.actor;

import memory.node.Node;
import memory.node.getter.DeleteStatusNodeGetter;

public class RemoveActor implements NodeActor {

    private static final DeleteStatusNodeGetter deleteStatusNodeGetter = new DeleteStatusNodeGetter();

    private final BindActor bindActor;

    public RemoveActor(BindActor bindActor) {
        this.bindActor = bindActor;
    }

    @Override
    public void forward(Node node) {
        if (node.act(deleteStatusNodeGetter) == null) {
            throw new RuntimeException("不能更改元数据");
        }
        bindActor.delete(node);
    }

}
