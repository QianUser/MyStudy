package memory.node;

import memory.node.actor.NodeActor;
import memory.node.element.Element;
import memory.node.element.ElementWrapper;
import memory.node.filter.NodeFilter;
import memory.node.getter.NodeGetter;
import memory.node.iterator.NodeIterator;

import java.util.ArrayList;
import java.util.List;

public class Node {

    private static class Root {
        private Node root;
    }

    private Element element;
    private final Root root;

    private Node parent;
    private Node firstChild;
    private Node lastChild;
    private Node prevSibling;
    private Node nextSibling;

    public Node(Element element) {
        this.element = element;
        this.root = new Root();
        this.root.root = this;
    }

    public Element getElement() {
        return element;
    }

    @SuppressWarnings("unchecked")
    public <T extends Element> T getElement(Class<T> clazz) {
        return (T) element;
    }

    public Element getUnwrapped() {
        if (element instanceof ElementWrapper) {
            return ((ElementWrapper) element).getElement();
        } else {
            return element;
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Element> T getUnwrapped(Class<T> clazz) {
        if (element instanceof ElementWrapper) {
            return ((ElementWrapper) element).getElement(clazz);
        } else {
            return (T) element;
        }
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public void setUnwrapped(Element element) {
        if (element instanceof ElementWrapper) {
            ((ElementWrapper) element).setElement(element);
        } else {
            this.element = element;
        }
    }

    public Node getParent() {
        return parent;
    }

    public Node getFirstChild() {
        return firstChild;
    }

    public void setFirstChild(Element element) {
        setFirstChild(new Node(element));
    }

    public void setFirstChild(Node node) {
        if (node.parent != null) {
            throw new RuntimeException("不允许插入非根节点");
        }
        if (root == node.root) {
            throw new RuntimeException("不允许插入同源节点");
        }
        node.parent = this;
        node.prevSibling = null;
        node.nextSibling = firstChild;
        if (firstChild == null) {
            lastChild = node;
        } else {
            firstChild.prevSibling = node;
        }
        firstChild = node;
        node.root.root = root.root;
    }

    public Node getLastChild() {
        return lastChild;
    }

    public void setLastChild(Element element) {
        setLastChild(new Node(element));
    }

    public void setLastChild(Node node) {
        if (node.parent != null) {
            throw new RuntimeException("不允许插入非根节点");
        }
        if (root == node.root) {
            throw new RuntimeException("不允许插入同源节点");
        }
        node.parent = this;
        node.prevSibling = lastChild;
        node.nextSibling = null;
        if (lastChild == null) {
            firstChild = node;
        } else {
            lastChild.nextSibling = node;
        }
        lastChild = node;
        node.root.root = root.root;
    }

    public Node getChild(int n) {
        if (n < 0) {
            return null;
        }
        Node child = firstChild;
        while (child != null && n > 0) {
            child = child.nextSibling;
            --n;
        }
        return child;
    }

    public Node getChild(NodeFilter nodeFilter) {
        Node child = firstChild;
        while (child != null) {
            if (nodeFilter.acceptNode(child)) {
                return child;
            }
            child = child.nextSibling;
        }
        return null;
    }

    public List<Node> getChildren() {
        return getChildren(new NodeFilter() {
        });
    }

    public List<Node> getChildren(NodeFilter nodeFilter) {
        List<Node> nodes = new ArrayList<>();
        Node child = firstChild;
        while (child != null) {
            if (nodeFilter.acceptNode(child)) {
                nodes.add(child);
            }
            child = child.nextSibling;
        }
        return nodes;
    }

    public Node getPrevSibling() {
        return prevSibling;
    }

    public void setPrevSibling(Element element) {
        setPrevSibling(new Node(element));
    }

    public void setPrevSibling(Node node) {
        if (node.parent != null) {
            throw new RuntimeException("不允许插入非根节点");
        }
        if (root == node.root) {
            throw new RuntimeException("不允许插入同源节点");
        }
        if (parent == null) {
            throw new RuntimeException("不允许插入根节点的兄弟节点");
        }
        node.parent = parent;
        node.prevSibling = prevSibling;
        node.nextSibling = this;
        if (parent.firstChild == this) {
            parent.firstChild = node;
        }
        prevSibling = node;
        node.root.root = root.root;
    }

    public Node getNextSibling() {
        return nextSibling;
    }

    public void setNextSibling(Element element) {
        setNextSibling(new Node(element));
    }

    public void setNextSibling(Node node) {
        if (node.parent != null) {
            throw new RuntimeException("不允许插入非根节点");
        }
        if (root == node.root) {
            throw new RuntimeException("不允许插入同源节点");
        }
        if (parent == null) {
            throw new RuntimeException("不允许插入根节点的兄弟节点");
        }
        node.parent = parent;
        node.prevSibling = this;
        node.nextSibling = nextSibling;
        if (parent.lastChild == this) {
            parent.lastChild = node;
        }
        nextSibling = node;
        node.root.root = root.root;
    }

    public Node getSibling(NodeFilter nodeFilter) {
        return parent.getChild(nodeFilter);
    }

    public Node getOtherSibling(NodeFilter nodeFilter) {
        return parent.getChild(new NodeFilter() {
            @Override
            public boolean acceptNode(Node node) {
                return node != Node.this && nodeFilter.acceptNode(node);
            }
        });
    }

    public List<Node> getSiblings() {
        return parent.getChildren();
    }

    public List<Node> getSiblings(NodeFilter nodeFilter) {
        return parent.getChildren(nodeFilter);
    }

    public List<Node> getOtherSiblings() {
        return parent.getChildren(new NodeFilter() {
            @Override
            public boolean acceptNode(Node node) {
                return node != Node.this;
            }
        });
    }

    public List<Node> getOtherSiblings(NodeFilter nodeFilter) {
        return parent.getChildren(new NodeFilter() {
            @Override
            public boolean acceptNode(Node node) {
                return node != Node.this && nodeFilter.acceptNode(node);
            }
        });
    }

    public void delete() {
        if (parent != null) {
            if (parent.firstChild == this) {
                parent.firstChild = nextSibling;
            }
            if (parent.lastChild == this) {
                parent.lastChild = prevSibling;
            }
            if (prevSibling != null) {
                prevSibling.nextSibling = nextSibling;
            }
            if (nextSibling != null) {
                nextSibling.prevSibling = prevSibling;
            }
            prevSibling = nextSibling = parent = null;
            root.root = this;
        }
    }

    public void act(NodeActor nodeActor) {
        nodeActor.forward(this);
        nodeActor.back(this);
        nodeActor.complete(this);
    }

    public <T> T act(NodeGetter<T> nodeGetter) {
        act((NodeActor) nodeGetter);
        return nodeGetter.get(this);
    }

    public Node search(NodeIterator nodeIterator) {
        return nodeIterator.next(this);
    }

    public Node search(NodeIterator nodeIterator, NodeFilter nodeFilter) {
        Node node = this;
        while (node != null) {
            if (nodeFilter.acceptNode(node)) {
                return node;
            }
            node = nodeIterator.next(node);
        }
        return null;
    }

    public void search(NodeIterator nodeIterator, NodeActor nodeActor) {
        search(nodeIterator, new NodeFilter() {}, nodeActor);
    }

    public <T> T search(NodeIterator nodeIterator, NodeGetter<T> nodeGetter) {
        search(nodeIterator, (NodeActor) nodeGetter);
        return nodeGetter.get(this);
    }

    public void search(NodeIterator nodeIterator, NodeFilter nodeFilter, NodeActor nodeActor) {
        Node node = this;
        while (node != null) {
            if (nodeFilter.acceptNode(node)) {
                nodeActor.forward(node);
                nodeActor.back(node);
            }
            node = nodeIterator.next(node);
        }
        nodeActor.complete(this);
    }

    public <T> T search(NodeIterator nodeIterator, NodeFilter nodeFilter, NodeGetter<T> nodeGetter) {
        search(nodeIterator, nodeFilter, (NodeActor) nodeGetter);
        return nodeGetter.get(this);
    }

    public Node search(NodeFilter nodeFilter) {
        if (nodeFilter.acceptTree(this)) {
            return search(this, nodeFilter);
        }
        return null;
    }

    public void search(NodeActor nodeActor) {
        search(this, new NodeFilter() {
        }, nodeActor);
        nodeActor.complete(this);
    }

    public <T> T search(NodeGetter<T> nodeGetter) {
        search((NodeActor) nodeGetter);
        return nodeGetter.get(this);
    }

    public void search(NodeFilter nodeFilter, NodeActor nodeActor) {
        if (nodeFilter.acceptTree(this)) {
            search(this, nodeFilter, nodeActor);
        }
        nodeActor.complete(this);
    }

    public <T> T search(NodeFilter nodeFilter, NodeGetter<T> nodeGetter) {
        search(nodeFilter, (NodeActor) nodeGetter);
        return nodeGetter.get(this);
    }

    private static Node search(Node node, NodeFilter nodeFilter) {
        if (nodeFilter.acceptNode(node)) {
            return node;
        }
        for (Node child : node.getChildren()) {
            if (nodeFilter.acceptTree(child)) {
                Node searched = search(child, nodeFilter);
                if (searched != null) {
                    return searched;
                }
            }
        }
        return null;
    }

    private static void search(Node node, NodeFilter nodeFilter, NodeActor nodeActor) {
        boolean accept = nodeFilter.acceptNode(node);
        if (accept) {
            nodeActor.forward(node);
        }
        for (Node child : node.getChildren()) {
            if (nodeFilter.acceptTree(child)) {
                search(child, nodeFilter, nodeActor);
            }
        }
        if (accept) {
            nodeActor.back(node);
        }
    }

}