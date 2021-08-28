package memory.test.visitortest;

import java.io.EOFException;
import java.io.IOException;

import static memory.util.TypeUtils.toChar;

public class CacheVisitor {

    private final byte[] cache;

    private long length;

    private long position;

    public CacheVisitor(int size) {
        this.cache = new byte[size];
        this.length = 0;
        this.position = 0;
    }

    public long length() {
        return length;
    }

    public long position() {
        return position;
    }

    public boolean eof() {
        return position >= length;
    }

    public boolean eof(long distance) {
        return position + distance >= length || position + distance < 0;
    }

    public void forwardChar() throws IOException {
        forward(2);
    }

    public void forwardChar(long distance) throws IOException {
        forward(2 * distance);
    }

    public void forward() throws IOException {
        forward(1);
    }

    public void forward(long distance) throws IOException {
        if (distance < 0) {
            back(-distance);
            return;
        }
        if (position + distance > length) {
            throw new EOFException();
        }
        position += distance;
    }

    public void backChar() throws IOException {
        back(2);
    }

    public void backChar(long distance) throws IOException {
        back(2 * distance);
    }

    public void back() throws IOException {
        back(1);
    }

    public void back(long distance) throws IOException {
        if (distance < 0) {
            forward(-distance);
            return;
        }
        if (position - distance < 0) {
            throw new EOFException();
        }
        position -= distance;
    }

    public char peekChar() throws IOException {
        return toChar(peek(), next(1));
    }

    public byte peek() throws IOException {
        if (position == length) {
            throw new EOFException();
        }
        return cache[(int) position];
    }

    public char nextChar() throws IOException {
        return toChar(next(2), next(3));
    }

    public char nextChar(long distance) throws IOException {
        return toChar(next(2 * distance), next(2 * distance + 1));
    }

    public byte next() throws IOException {
        return next(1);
    }

    public byte next(long distance) throws IOException {
        if (distance < 0) {
            return previous(-distance);
        }
        if (distance == 0) {
            return peek();
        }
        if (position + distance >= length) {
            throw new EOFException();
        }
        return cache[(int) (position + distance)];
    }

    public char previousChar() throws IOException {
        return toChar(previous(2), previous(1));
    }

    public char previousChar(long distance) throws IOException {
        return toChar(previous(2 * distance), previous(2 * distance - 1));
    }

    public byte previous() throws IOException {
        return previous(1);
    }

    public byte previous(long distance) throws IOException {
        if (distance < 0) {
            return next(-distance);
        }
        if (distance == 0) {
            return peek();
        }
        if (position - distance < 0) {
            throw new EOFException();
        }
        return cache[(int) (position - distance)];
    }

    public void writeChar(char c) throws IOException {
        write((byte) ((c & 0xff00) >> 8));
        forward(1);
        write((byte) (c & 0xff));
        back(1);
    }

    public void write(byte b) {
        cache[(int) position] = b;
        if (position == length) {
            ++length;
        }
    }

}
