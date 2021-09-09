package memory.node.actor;

import memory.node.Node;
import memory.node.element.StringElement;
import memory.node.getter.DeleteStatusNodeGetter;

public class DeleteStatusUpdateActor implements NodeActor {

    private static final DeleteStatusNodeGetter deleteStatusNodeGetter = new DeleteStatusNodeGetter();

    private final BindActor bindActor;

    private final boolean delete;

    public DeleteStatusUpdateActor(BindActor bindActor, boolean delete) {
        this.bindActor = bindActor;
        this.delete = delete;
    }

    @Override
    public void forward(Node node) {
        Node deleteStatusNode = node.act(deleteStatusNodeGetter);
        if (deleteStatusNode != null) {
            String s = delete ? "是" : "否";
            if (!deleteStatusNode.getUnwrapped(StringElement.class).getString().equals(s)) {
                bindActor.update(deleteStatusNode, new StringElement(s));
            }
        }
    }

}
