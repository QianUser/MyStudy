package memory.node.creater;

import memory.node.Node;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class LoadNodeCreator implements NodeCreator {

    private final File file;

    public LoadNodeCreator(File file) {
        this.file = file;
    }

    @Override
    public Node create() {
        try (
                FileInputStream fileInputStream = new FileInputStream(file);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)
        ) {
            return (Node) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
