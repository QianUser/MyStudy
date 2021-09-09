package memory.node.actor;

import memory.node.Node;
import memory.node.actor.binder.AddNodeBinder;

import java.util.ArrayList;
import java.util.List;

public class AddActor implements NodeActor {

    private final BindActor bindActor;

    private final Node linkedNode;

    private final AddNodeBinder.As as;

    private final boolean delete;

    private final List<Node> addNodes;

    public AddActor(BindActor bindActor, Node linkedNode, AddNodeBinder.As as, boolean delete) {
        this.linkedNode = linkedNode;
        this.bindActor = bindActor;
        this.as = as;
        this.delete = delete;
        this.addNodes = new ArrayList<>();
    }

    @Override
    public void forward(Node node) {
        addNodes.add(node);
    }

    @Override
    public void complete(Node node) {
        bindActor.add(linkedNode, node, addNodes, as, delete);
        addNodes.clear();
    }

}
