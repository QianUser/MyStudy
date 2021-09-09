package memory.node.creater;

import memory.node.Node;
import memory.node.element.MemoryElement;
import memory.node.element.NullElement;
import memory.node.element.StringElement;
import memory.node.getter.LengthGetter;
import memory.visitor.Visitor;

import java.io.File;
import java.security.MessageDigest;
import java.util.Objects;
import java.util.function.Predicate;

import static memory.util.Utils.decode;

public class MemoryNodeCreator implements NodeCreator {

    private static class Item {

        private enum Token {

            LEFT("("), RIGHT(")");

            private final String value;

            Token(String value) {
                this.value = value;
            }

            public String getValue() {
                return value;
            }

        }

        private final Token token;

        private final String content;

        public Item(String content) {
            if (content == null) {
                throw new RuntimeException();
            }
            this.token = null;
            this.content = content;
        }

        public Item(Token token) {
            if (token == null) {
                throw new RuntimeException();
            }
            this.token = token;
            this.content = null;
        }

        public boolean isLeftItem() {
            return token == Token.LEFT;
        }

        public boolean isRightItem() {
            return token == Token.RIGHT;
        }

        public static Item leftItem() {
            return new Item(Item.Token.LEFT);
        }

        public static Item rightItem() {
            return new Item(Item.Token.RIGHT);
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Item)) {
                return false;
            }
            if (content == null) {
                return token == ((Item) obj).token;
            } else {
                return content.equals(((Item) obj).content);
            }
        }

        public String toString() {
            if (token == null) {
                return content;
            } else {
                return token.getValue();
            }
        }

    }

    protected final Visitor visitor;

    protected final File file;

    private final MessageDigest messageDigest;

    private final long index;

    public MemoryNodeCreator(Visitor visitor, File file, MessageDigest messageDigest, long index) {
        this.visitor = visitor;
        this.file = file;
        this.messageDigest = messageDigest;
        this.index = index;
    }

    @Override
    public Node create() {
        try {
            visitor.setFile(file);
            visitor.forward(index - visitor.getPosition());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        LengthGetter lengthGetter = new LengthGetter();
        Node root;
        try {
            root = getNode(visitor, lengthGetter, messageDigest, null, i -> false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Node node = root;
        while (node != null) {
            try {
                node = getNode(visitor, lengthGetter, messageDigest, node, Objects::isNull);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return root;
    }

    private static Node getNode(Visitor visitor, LengthGetter lengthGetter, MessageDigest messageDigest, Node previous, Predicate<Node> stop) throws Exception {
        if (stop.test(previous)) {
            return null;
        }
        if (previous != null && previous.getElement(StringElement.class).getString().equals(messageDigest.getAlgorithm() + "校验码") && previous.getChildren().isEmpty()) {
            Node node = new Node(new MemoryElement(visitor, new NullElement(), visitor.getPosition() + 2, messageDigest.getDigestLength()));
            previous.setLastChild(node);
            visitor.forward(messageDigest.getDigestLength() + 2);
            return node;
        } else if (previous != null && previous.getElement(MemoryElement.class).getElement(StringElement.class).getString().equals("内容") && previous.getChildren().size() == 2) {
            long length = previous.getParent().act(lengthGetter);
            Node node = new Node(new MemoryElement(visitor, new NullElement(), visitor.getPosition() + 2, length));
            previous.setLastChild(node);
            visitor.forward(length + 2);
            return node;
        }
        Item item = getItem(visitor);
        long position = visitor.getPosition();
        if (item == null) {
            return null;
        } else if (item.isLeftItem()) {
            item = getItem(visitor);
            if (Objects.requireNonNull(item).isLeftItem()) {
                Node node = new Node(new MemoryElement(visitor, new NullElement(), position));
                if (previous != null) {
                    previous.setLastChild(node);
                }
                visitor.backChar();
                return node;
            } else if (!item.isRightItem()) {
                Node node = new Node(new MemoryElement(visitor, new StringElement(item.toString()), position, visitor.getPosition() - position));
                if (previous != null) {
                    previous.setLastChild(node);
                }
                return node;
            } else {
                throw new RuntimeException();
            }
        } else if (item.isRightItem()) {
            return getNode(visitor, lengthGetter, messageDigest, Objects.requireNonNull(previous).getParent(), stop);
        } else {
            throw new RuntimeException();
        }
    }

    private static Item getItem(Visitor visitor) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        while (true) {
            if (!visitor.isEofChar()) {
                long position = visitor.getPosition();
                char c = decode(visitor);
                if ((c == '(' || c == ')') && visitor.getPosition() == position + 2) {
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

}
