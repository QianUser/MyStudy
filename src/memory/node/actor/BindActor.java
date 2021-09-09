package memory.node.actor;

import memory.node.Node;
import memory.node.actor.binder.*;
import memory.node.element.Element;

import java.util.*;

public class BindActor implements NodeActor {

    private final List<NodeBinder> bindNodes;

    public BindActor() {
        this.bindNodes = new ArrayList<>();
    }

    public void add(Node linkedNode, Node addRoot, List<Node> addNodes, AddNodeBinder.As as, boolean delete) {
        bindNodes.add(new AddNodeBinder(linkedNode, addRoot, addNodes, as, delete));
    }

    public void update(Node node, Element element) {
        bindNodes.add(new UpdateNodeBinder(node, element));
    }

    public void delete(Node node) {
        bindNodes.add(new DeleteNodeBinder(node));
    }

    @Override
    public void complete(Node node) {
        for (NodeBinder bindNode : bindNodes) {
            Class<?> clazz = bindNode.getClass();
            if (clazz == AddNodeBinder.class) {
                handleAddNodeBinder(bindNode);
            } else if (clazz == UpdateNodeBinder.class) {
                handleUpdateNodeBinder(bindNode);
            } else if (clazz == DeleteNodeBinder.class) {
                handleDeleteNodeBinder(bindNode);
            }
        }
    }

    private void handleAddNodeBinder(NodeBinder bindNode) {

    }

    private void handleUpdateNodeBinder(NodeBinder bindNode) {

    }

    private void handleDeleteNodeBinder(NodeBinder bindNode) {

    }

}
