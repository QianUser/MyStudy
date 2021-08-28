package memory.indexer;

import memory.node.Node;
import memory.node.element.MemoryElement;
import memory.visitor.Visitor;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Predicate;

import static memory.util.Constant.indexSize;

public class Indexer {

    private final String filename;

    private final Visitor visitor;

    public Indexer(String filename) throws IOException {
        this.filename = filename;
        this.visitor = new Visitor(filename, Visitor.Mode.R);
        this.visitor.cacheSize(indexSize);
    }

    public Node<MemoryElement> index() throws IOException {
        Node<MemoryElement> root = new Node<>(new MemoryElement(filename, "记忆", 0, 0));
        Node<MemoryElement> node;
        visitor.forward(-visitor.position());
        while ((node = index(visitor.position())) != null) {
            root.insertAsLastChild(node);
        }
        return root;
    }

    public Node<MemoryElement> index(long index) throws IOException {
        visitor.forward(index - visitor.position());
        Node<MemoryElement> root = node(null, i -> false);
        Node<MemoryElement> node = root;
        while (node != null) {
            node = node(node, Objects::isNull);
        }
        return root;
    }

    public void close() throws IOException {
        visitor.close();
    }

    @SuppressWarnings("unchecked")
    private Node<MemoryElement> node(Node<MemoryElement> previous, Predicate<Node<MemoryElement>> stop) throws IOException {
        if (stop.test(previous)) {
            return null;
        }
        if (previous != null && previous.getElement().getName().equals("SHA-256校验码") && previous.getChildren().isEmpty()) {
            Node<MemoryElement> node = new Node<>(new MemoryElement(filename, "", visitor.position() + 2, 32));
            previous.insertAsLastChild(node);
            visitor.forward(34);
            return node;
        } else if (previous != null && previous.getElement().getName().equals("内容") && previous.getChildren().size() == 2) {
            Node<MemoryElement> lengthNode = (Node<MemoryElement>) previous.getSibling(i -> ((MemoryElement) i.getElement()).getName().equals("长度")).getChild(3);
            long length = Long.parseLong(lengthNode.getElement().getName());
            Node<MemoryElement> node = new Node<>(new MemoryElement(filename, "", visitor.position() + 2, length));
            previous.insertAsLastChild(node);
            visitor.forward(length + 2);
            return node;
        }
        Item item = item();
        long position = visitor.position();
        if (item == null) {
            return null;
        } else if (item.isLeftItem()) {
            item = item();
            if (Objects.requireNonNull(item).isLeftItem()) {
                Node<MemoryElement> node = new Node<>(new MemoryElement(filename, "", position, 0));
                if (previous != null) {
                    previous.insertAsLastChild(node);
                }
                visitor.backChar();
                return node;
            } else if (!item.isRightItem()) {
                Node<MemoryElement> node = new Node<>(new MemoryElement(filename, item.getValue(), position, visitor.position() - position));
                if (previous != null) {
                    previous.insertAsLastChild(node);
                }
                return node;
            } else {
                throw new RuntimeException();
            }
        } else if (item.isRightItem()) {
            return node((Node<MemoryElement>) Objects.requireNonNull(previous).getParent(), stop);
        } else {
            throw new RuntimeException();
        }
    }

    private Item item() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        while (true) {
            if (!visitor.eofChar()) {
                long position = visitor.position();
                char c = character();
                if ((c == '(' || c == ')') && visitor.position() == position + 2) {
                    if (stringBuilder.length() == 0) {
                        return c == '(' ? Item.leftItem() : Item.rightItem();
                    } else {
                        visitor.backChar();
                        return new Item(stringBuilder.toString());
                    }
                } else {
                    stringBuilder.append(c);
                }
            } else {
                return null;
            }
        }
    }

    @Deprecated
    private char character2() throws IOException {
        char c = visitor.peekChar();
        visitor.forwardChar();
        if (c == '(' && !visitor.eofChar(1) && (visitor.peekChar() == '(' || visitor.peekChar() == ')') && visitor.nextChar() == ')') {
            c = visitor.peekChar();
            visitor.forwardChar(2);
        }
        return c;
    }

    private char character() throws IOException {
        char c = visitor.peekChar();
        visitor.forwardChar();
        if (c == '(' && !visitor.eofChar(2) && visitor.nextChar(2) == ')') {
            if (visitor.peekChar() == '(' && visitor.nextChar() == ')') {
                visitor.forwardChar(3);
            } else if (visitor.peekChar() == ')' && visitor.nextChar() == '(') {
                c = ')';
                visitor.forwardChar(3);
            }
        }
        return c;
    }

}