package memory.node.element;

import memory.util.Utils;
import memory.visitor.Visitor;

import java.io.IOException;
import java.security.MessageDigest;

public class StringElement implements Element {

    private final String string;

    private final long length;

    public StringElement(String string) {
        this.string = string;
        this.length = Utils.encode(string).length() * 2L;
    }

    @Override
    public long length() {
        return length;
    }

    @Override
    public void write(Visitor writer) throws IOException {
        String s = Utils.encode(string);
        for (int i = 0; i < length; ++i) {
            writer.write(s.charAt(i));
            writer.forwardChar();
        }
        writer.flush();
    }

    @Override
    public void updateChecksum(MessageDigest messageDigest) {
        messageDigest.update(Utils.toBytes(Utils.encode(string)));
    }

    public String getString() {
        return string;
    }

    @Override
    public String toString() {
        return string;
    }
}
