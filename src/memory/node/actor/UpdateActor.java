package memory.node.actor;

import memory.node.Node;
import memory.node.element.Element;
import memory.node.getter.DeleteStatusNodeGetter;

public class UpdateActor implements NodeActor {

    private static final DeleteStatusNodeGetter deleteStatusNodeGetter = new DeleteStatusNodeGetter();

    private final BindActor bindActor;

    private final Element element;

    public UpdateActor(BindActor bindActor, Element element) {
        this.bindActor = bindActor;
        this.element = element;
    }

    @Override
    public void forward(Node node) {
        if (node.act(deleteStatusNodeGetter) == null) {
            throw new RuntimeException("不能更改元数据");
        }
        bindActor.update(node, element);
    }

}
