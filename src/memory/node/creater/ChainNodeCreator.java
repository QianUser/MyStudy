package memory.node.creater;

import memory.node.Node;
import memory.node.element.Element;
import memory.node.element.NullElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChainNodeCreator implements NodeCreator {

    private final Element element;

    private final List<Node> nodes;

    private final List<Element> elements;

    public ChainNodeCreator(NodeCreator... nodeCreators) {
        this(new NullElement(), nodeCreators);
    }

    public ChainNodeCreator(Node... nodes) {
        this(new NullElement(), nodes);
    }

    public ChainNodeCreator(Element element, NodeCreator... nodeCreators) {
        this.element = element;
        this.nodes = new ArrayList<>();
        for (NodeCreator nodeCreator : nodeCreators) {
            this.nodes.add(nodeCreator.create());
        }
        this.elements = null;
    }

    public ChainNodeCreator(Element element, Node... nodes) {
        this.element = element;
        this.nodes = Arrays.asList(nodes);
        this.elements = null;
    }

    public ChainNodeCreator(Element element, Element... elements) {
        this.element = element;
        this.nodes = null;
        this.elements = Arrays.asList(elements);
    }

    @Override
    public Node create() {
        Node root = new Node(element);
        Node node = root;
        if (nodes != null) {
            for (Node child : nodes) {
                node.setLastChild(child);
                node = child;
            }
        } else {
            for (Element childElement : elements) {
                Node child = new Node(childElement);
                node.setLastChild(child);
                node = child;
            }
        }
        return root;
    }

}
