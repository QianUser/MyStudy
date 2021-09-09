package memory.node.element;

import memory.visitor.Visitor;

import java.io.IOException;
import java.security.MessageDigest;

public class MemoryElement implements ElementWrapper {

    private static final int chunkSize = 16777216;

    private final Visitor reader;

    private Element element;

    private long start;

    private long length;

    public MemoryElement(Visitor reader, Element element, long start) {
        this(reader, element, start, element.length());
    }

    public MemoryElement(Visitor reader, Element element, long start, long length) {
        this.reader = reader;
        this.element = element;
        this.start = start;
        this.length = length;
    }

    public Visitor getReader() {
        return reader;
    }

    @Override
    public Element getElement() {
        return element;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Element> T getElement(Class<T> clazz) {
        return (T) element;
    }

    @Override
    public void setElement(Element element) {
        this.element = element;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    @Override
    public long length() {
        return length;
    }

    @Override
    public void write(Visitor writer) throws IOException {
        reader.forward(start - reader.getPosition());
        for (long i = 0; i < length; ++i) {
            writer.write(reader.peek());
            reader.forward();
            writer.forward();
        }
        writer.flush();
    }

    @Override
    public void updateChecksum(MessageDigest messageDigest) {
        byte[] bytes = new byte[(int) Math.min(chunkSize, length)];
        try {
            reader.forward(start - reader.getPosition());
            long length = this.length;
            while (length > 0) {
                int n = (int) Math.min(chunkSize, length);
                for (int i = 0; i < n; ++i) {
                    bytes[i] = reader.peek();
                    reader.forward();
                }
                messageDigest.update(bytes, 0, n);
                length -= n;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "[" + element + "(" + start + ", " + length + ")]";
    }
}
