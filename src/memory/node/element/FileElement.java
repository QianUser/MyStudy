package memory.node.element;

import memory.visitor.Visitor;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;

public class FileElement implements Element {

    private static final int chunkSize = 16777216;

    private final File file;

    private final Visitor reader;

    public FileElement(File file, Visitor reader) {
        this.file = file;
        this.reader = reader;
    }

    @Override
    public long length() {
        return file.length();
    }

    @Override
    public void write(Visitor writer) throws IOException {
        reader.setFile(file);
        for (int i = 0; i < file.length(); ++i) {
            writer.write(reader.peek());
            reader.forward();
            writer.forward();
        }
        writer.flush();
    }

    @Override
    public void updateChecksum(MessageDigest messageDigest) {
        long length = file.length();
        byte[] bytes = new byte[(int) Math.min(chunkSize, length)];
        try {
            reader.setFile(file);
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

    public File getFile() {
        return file;
    }

    @Override
    public String toString() {
        return file.toString();
    }
}
