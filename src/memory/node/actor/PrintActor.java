package memory.node.actor;

import memory.node.Node;
import memory.node.getter.IndentRepGetter;
import memory.node.getter.RepGetter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

public class PrintActor implements NodeActor {

    private final Writer writer;

    private final RepGetter repGetter;

    public PrintActor() {
        this(new PrintWriter(System.out), new IndentRepGetter());
    }

    public PrintActor(Writer writer) {
        this(writer, new IndentRepGetter());
    }

    public PrintActor(RepGetter repGetter) {
        this(new PrintWriter(System.out), repGetter);
    }

    public PrintActor(Writer writer, RepGetter repGetter) {
        this.writer = writer;
        this.repGetter = repGetter;
    }

    @Override
    public void forward(Node node) {
        try {
            writer.write(node.act(repGetter));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void complete(Node node) {
        try {
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
