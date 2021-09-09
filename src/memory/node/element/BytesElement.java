package memory.node.element;

import memory.visitor.Visitor;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Arrays;

public class BytesElement implements Element {

    private final byte[] bytes;

    public BytesElement(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public long length() {
        return bytes.length;
    }

    @Override
    public void write(Visitor writer) throws IOException {
        for (byte b : bytes) {
            writer.write(b);
            writer.forward();
        }
        writer.flush();
    }

    @Override
    public void updateChecksum(MessageDigest messageDigest) {
        messageDigest.update(bytes);
    }

    public byte[] getBytes() {
        return bytes;
    }

    @Override
    public String toString() {
        return Arrays.toString(bytes);
    }
}
