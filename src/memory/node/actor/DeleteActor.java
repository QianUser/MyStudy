package memory.node.actor;

import memory.node.Node;
import memory.node.element.StringElement;
import memory.node.getter.DeleteStatusNodeGetter;

public class DeleteActor implements NodeActor {

    private static final DeleteStatusNodeGetter deleteStatusNodeGetter = new DeleteStatusNodeGetter();

    private final BindActor bindActor;

    public DeleteActor(BindActor bindActor) {
        this.bindActor = bindActor;
    }

    @Override
    public void forward(Node node) {
        Node deleteStatusNode = node.act(deleteStatusNodeGetter);
        if (deleteStatusNode != null) {
            if (deleteStatusNode.getUnwrapped(StringElement.class).getString().equals("是")) {
                bindActor.delete(node);
            }
        }
    }

}
