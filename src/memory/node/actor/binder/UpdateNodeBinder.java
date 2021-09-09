package memory.node.actor.binder;

import memory.node.Node;
import memory.node.element.Element;

public class UpdateNodeBinder implements NodeBinder {

    private final Node node;

    private final Element element;

    public UpdateNodeBinder(Node node, Element element) {
        this.node = node;
        this.element = element;
    }

    public Element getElement() {
        return element;
    }

    public Node getNode() {
        return node;
    }

}
