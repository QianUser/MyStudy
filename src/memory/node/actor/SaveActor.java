package memory.node.actor;

import memory.node.Node;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class SaveActor implements NodeActor {

    private final File file;

    public SaveActor(File file) {
        this.file = file;
    }

    @Override
    public void complete(Node node) {
        try (
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)
        ) {
            objectOutputStream.writeObject(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
