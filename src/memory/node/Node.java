package memory.node;

import memory.node.actor.NodeActor;
import memory.node.element.MemoryElement;
import memory.node.filter.NodeFilter;
import memory.node.sorter.NodeSorter;
import memory.visitor.Visitor;

import java.io.*;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import static memory.util.Constant.chunkSize;
import static memory.util.TypeUtils.toBytes;

public class Node<T> implements Serializable {

    private static final long serialVersionUID = 8231678188433287822L;

    protected T element;

    private Node<?> parent;
    private Node<?> firstChild;
    private Node<?> prevSibling;
    private Node<?> nextSibling;
    private Node<?> lastChild;

    public Node(T element) {
        this.element = element;
    }

    public Node<?> getParent() {
        return parent;
    }

    public void setParent(Node<?> parent) {
        this.parent = parent;
    }

    public T getElement() {
        return element;
    }

    public void setElement(T element) {
        this.element = element;
    }

    public Node<?> getFirstChild() {
        return firstChild;
    }

    public void setFirstChild(Node<?> firstChild) {
        this.firstChild = firstChild;
    }

    public Node<?> getPrevSibling() {
        return prevSibling;
    }

    public void setPrevSibling(Node<?> prevSibling) {
        this.prevSibling = prevSibling;
    }

    public Node<?> getNextSibling() {
        return nextSibling;
    }

    public void setNextSibling(Node<?> nextSibling) {
        this.nextSibling = nextSibling;
    }

    public Node<?> getLastChild() {
        return lastChild;
    }

    public void setLastChild(Node<?> lastChild) {
        this.lastChild = lastChild;
    }

    public Node<?> getChild(int n) {
        if (n <= 0) {
            throw new RuntimeException();
        }
        Node<?> child = firstChild;
        for (int i = 2; i <= n; ++i) {
            child = child.nextSibling;
        }
        return child;
    }

    public Node<?> getChild(NodeFilter nodeFilter) {
        Node<?> child = firstChild;
        while (child != null) {
            if (nodeFilter.accept(child)) {
                return child;
            }
            child = child.nextSibling;
        }
        return null;
    }

    public List<Node<?>> getChildren() {
        return getChildren(i -> true);
    }

    public List<Node<?>> getChildren(NodeFilter nodeFilter) {
        List<Node<?>> nodes = new ArrayList<>();
        Node<?> child = firstChild;
        while (child != null) {
            if (nodeFilter.accept(child)) {
                nodes.add(child);
            }
            child = child.nextSibling;
        }
        return nodes;
    }

    public Node<?> getSibling(NodeFilter nodeFilter) {
        if (parent == null) {
            if (nodeFilter.accept(this)) {
                return this;
            }
        } else {
            Node<?> child = parent.firstChild;
            while (child != null) {
                if (nodeFilter.accept(child)) {
                    return child;
                }
                child = child.nextSibling;
            }
        }
        return null;
    }

    public Node<?> getOtherSibling(NodeFilter nodeFilter) {
        return getSibling(node -> node != this && nodeFilter.accept(node));
    }

    public List<Node<?>> getSiblings() {
        return getSiblings(i -> true);
    }

    public List<Node<?>> getSiblings(NodeFilter nodeFilter) {
        List<Node<?>> nodes = new ArrayList<>();
        if (parent == null) {
            if (nodeFilter.accept(this)) {
                nodes.add(this);
            }
        } else {
            Node<?> child = parent.firstChild;
            while (child != null) {
                if (nodeFilter.accept(child)) {
                    nodes.add(child);
                }
                child = child.nextSibling;
            }
        }
        return nodes;
    }

    public List<Node<?>> getOtherSiblings() {
        return getSiblings(node -> node != this);
    }

    public List<Node<?>> getOtherSiblings(NodeFilter nodeFilter) {
        return getSiblings(node -> node != this && nodeFilter.accept(node));
    }

    public int getDepth() {
        Node<?> node = this;
        int cnt = 0;
        while ((node = node.parent) != null) {
            ++cnt;
        }
        return cnt;
    }

    public boolean isLeaf() {
        return firstChild == null;
    }

    public Node<?> insertAsLastChild(Node<?> node) {
        if (lastChild != null) {
            lastChild.nextSibling = node;
            node.prevSibling = lastChild;
        } else {
            firstChild = node;
        }
        lastChild = node;
        lastChild.parent = this;
        return node;
    }

    public Node<?> insertAsFirstChild(Node<?> node) {
        if (firstChild != null) {
            firstChild.prevSibling = node;
            node.nextSibling = firstChild;
        } else {
            lastChild = node;
        }
        firstChild = node;
        firstChild.parent = this;
        return node;
    }

    public Node<?> insertAsNextSibling(Node<?> node) {
        this.nextSibling = node;
        node.prevSibling = this;
        if (this == parent.lastChild) {
            parent.lastChild = node;
        }
        node.parent = parent;
        return node;
    }

    public Node<?> insertAsPrevSibling(Node<?> node) {
        node.nextSibling = this;
        this.prevSibling = node;
        if (this == parent.firstChild) {
            parent.firstChild = node;
        }
        node.parent = parent;
        return node;
    }

    public void delete() {
        if (prevSibling != null) {
            prevSibling.nextSibling = nextSibling;
        }
        if (nextSibling != null) {
            nextSibling.prevSibling = prevSibling;
        }
        if (parent != null && this == parent.firstChild) {
            parent.firstChild = nextSibling;
        }
        if (parent != null && this == parent.lastChild) {
            parent.lastChild = prevSibling;
        }
    }

    public void act(NodeActor nodeActor) {
        nodeActor.act(this);
        nodeActor.postHandle(this);
        nodeActor.afterComplete();
    }

    public void search(NodeActor nodeActor) {
        search(this, i -> true, nodeActor);
        nodeActor.afterComplete();
    }

    public void search(NodeFilter nodeFilter, NodeActor nodeActor) {
        search(this, nodeFilter, nodeActor);
        nodeActor.afterComplete();
    }

    private void search(Node<?> node, NodeFilter nodeFilter, NodeActor nodeActor) {
        if (nodeFilter.accept(node)) {
            nodeActor.act(node);
        }
        for (Node<?> child : node.getChildren()) {
            search(child, nodeFilter, nodeActor);
        }
        if (nodeFilter.accept(node)) {
            nodeActor.postHandle(node);
        }
    }

    public Node<?> search(NodeFilter nodeFilter) {
        return search(this, nodeFilter);
    }

    private static Node<?> search(Node<?> node, NodeFilter nodeFilter) {
        if (nodeFilter.accept(node)) {
            return node;
        }
        for (Node<?> child : node.getChildren()) {
            return search(child, nodeFilter);
        }
        return null;
    }

    public Node<?> search(NodeSorter nodeSorter) {
        Node<?> node = this;
        while (node != null) {
            int sort = nodeSorter.sort(node);
            if (sort == 0) {
                return node;
            } else if (sort == 1) {
                return null;
            } else if (node.nextSibling == null || nodeSorter.sort(node.nextSibling) == 1) {
                node = node.firstChild;
            } else {
                node = node.nextSibling;
            }
        }
        return null;
    }

    public void addDeleteStatusNodes(boolean delete) {
        for (Node<?> node : getChildren()) {
            node.addDeleteStatusNodes(delete);
        }
        addDeleteStatusNode(delete);
    }

    public void addDeleteStatusNode(boolean delete) {
        Node<String> result = new Node<>("删除");
        result.insertAsFirstChild(delete ? new Node<>("是") : new Node<>("否"));
        insertAsFirstChild(result);
    }

    public void addChecksumNodes(MessageDigest messageDigest) throws IOException {
        for (Node<?> node : getChildren()) {
            node.addChecksumNodes(messageDigest);
        }
        addChecksumNode(messageDigest);
    }

    public void addChecksumNode(MessageDigest messageDigest) throws IOException {
        Node<String> result = new Node<>("SHA-256校验码");
        if (element.getClass() == byte[].class) {
            messageDigest.update((byte[]) element);
        } else if (element.getClass() == String.class) {
            messageDigest.update(toBytes((String) element));
        } else if (element.getClass() == MemoryElement.class) {
            MemoryElement memoryElement = (MemoryElement) element;
            Visitor visitor = new Visitor(memoryElement.getFilename(), Visitor.Mode.R);
            long length = memoryElement.getLength();
            byte[] bytes = new byte[(int) Math.min(chunkSize, length)];
            visitor.forward(memoryElement.getStart() - visitor.position());
            while (length > 0) {
                int n = (int) Math.min(chunkSize, length);
                for (int i = 0; i < n; ++i) {
                    bytes[i] = visitor.peek();
                    visitor.forward();
                }
                length -= n;
                messageDigest.update(bytes, 0, n);
            }
            visitor.close();
        } else {
            throw new RuntimeException();
        }
        for (Node<?> child : getChildren()) {
            Node<?> childCheckSum = child.getFirstChild().getFirstChild();
            messageDigest.update((byte[]) childCheckSum.element);
        }
        result.insertAsFirstChild(new Node<>(messageDigest.digest()));
        insertAsFirstChild(result);
    }

    public void save(String filename) throws IOException {
        File file = new File(filename);
        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            throw new RuntimeException();
        }
        try (
                FileOutputStream fileOutputStream = new FileOutputStream(filename);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)
        ) {
            objectOutputStream.writeObject(this);
        }
    }

    public static Node<?> load(String filename) throws IOException, ClassNotFoundException {
        try (
                FileInputStream fileInputStream = new FileInputStream(filename);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)
        ) {
            return (Node<?>) objectInputStream.readObject();
        }
    }

    @Override
    public String toString() {
        return element.toString();
    }

}