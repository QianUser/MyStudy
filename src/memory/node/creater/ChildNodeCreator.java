package memory.node.creater;

import memory.node.Node;
import memory.node.element.Element;
import memory.node.element.NullElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChildNodeCreator implements NodeCreator {

    private final Element element;

    private final List<Node> nodes;

    private final List<Element> elements;

    public ChildNodeCreator(NodeCreator... nodeCreators) {
        this(new NullElement(), nodeCreators);
    }

    public ChildNodeCreator(Node... nodes) {
        this(new NullElement(), nodes);
    }

    public ChildNodeCreator(Element element, NodeCreator... nodeCreators) {
        this.element = element;
        this.nodes = new ArrayList<>();
        for (NodeCreator nodeCreator : nodeCreators) {
            this.nodes.add(nodeCreator.create());
        }
        this.elements = null;
    }

    public ChildNodeCreator(Element element, Node... nodes) {
        this.element = element;
        this.nodes = Arrays.asList(nodes);
        this.elements = null;
    }

    public ChildNodeCreator(Element element, Element... elements) {
        this.element = element;
        this.nodes = null;
        this.elements = Arrays.asList(elements);
    }

    @Override
    public Node create() {
        Node root = new Node(element);
        if (nodes != null) {
            for (Node child : nodes) {
                root.setLastChild(child);
            }
        } else {
            for (Element childElement : elements) {
                root.setLastChild(new Node(childElement));
            }
        }
        return root;
    }

}
