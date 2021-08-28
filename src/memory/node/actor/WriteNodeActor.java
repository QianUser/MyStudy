package memory.node.actor;

import memory.node.Node;
import memory.node.element.MemoryElement;
import memory.visitor.Visitor;

import java.io.IOException;

import static memory.util.StringUtils.escape;

public class WriteNodeActor implements NodeActor {

    private final Visitor visitor;

    public WriteNodeActor(String filename) throws IOException {
        this.visitor = new Visitor(filename, Visitor.Mode.RW);
        this.visitor.forward(visitor.length() - visitor.position());
    }

    @Override
    public void act(Node<?> node) {
        assertChildNotNull(node);
        try {
            visitor.writeChar('(');
            visitor.forwardChar();
            Class<?> clazz = node.getElement().getClass();
            if (clazz == byte[].class) {
                byte[] bytes = (byte[]) node.getElement();
                for (byte b : bytes) {
                    visitor.write(b);
                    visitor.forward();
                }
            } else if (clazz == String.class) {
                String s = escape((String) node.getElement());
                for (int i = 0; i < s.length(); ++i) {
                    visitor.writeChar(s.charAt(i));
                    visitor.forwardChar();
                }
            } else if (clazz == MemoryElement.class) {
                MemoryElement element = (MemoryElement) node.getElement();
                Visitor elementVisitor = new Visitor(element.getFilename(), Visitor.Mode.R);
                elementVisitor.forward(element.getStart() - elementVisitor.position());
                for (long i = 0; i < element.getLength(); ++i) {
                    visitor.write(elementVisitor.peek());
                    visitor.forward();
                    elementVisitor.forward();
                }
                elementVisitor.close();
            } else {
                throw new RuntimeException();
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void postHandle(Node<?> node) {
        try {
            visitor.writeChar(')');
            visitor.forwardChar();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void afterComplete() {
        try {
            visitor.close();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private void assertChildNotNull(Node<?> node) {
        if (!node.isLeaf()) {
            return;
        }
        Class<?> clazz = node.getElement().getClass();
        if (clazz == byte[].class) {
            if (((byte[]) node.getElement()).length == 0) {
                throw new RuntimeException();
            }

        } else if (clazz == String.class) {
            if (((String) node.getElement()).length() == 0) {
                throw new RuntimeException();
            }
        } else if (clazz == MemoryElement.class) {
            MemoryElement memoryElement = (MemoryElement) node.getElement();
            if (memoryElement.getLength() == 0) {
                throw new RuntimeException();
            }
        } else {
            throw new RuntimeException();
        }
    }

}
