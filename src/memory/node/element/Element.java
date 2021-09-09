package memory.node.element;

import memory.visitor.Visitor;

import java.io.IOException;
import java.security.MessageDigest;

public interface Element {

    long length();

    void write(Visitor writer) throws IOException;

    void updateChecksum(MessageDigest messageDigest);

}
