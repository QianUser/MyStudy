package memory.node.element;

import memory.visitor.Visitor;

import java.io.IOException;
import java.security.MessageDigest;

public class NullElement implements Element {

    @Override
    public long length() {
        return 0;
    }

    @Override
    public void write(Visitor writer) throws IOException {}

    @Override
    public void updateChecksum(MessageDigest messageDigest) {}

    @Override
    public String toString() {
        return "";
    }
}
