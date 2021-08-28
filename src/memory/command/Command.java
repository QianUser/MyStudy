package memory.command;

import memory.indexer.Indexer;
import memory.node.Node;
import memory.node.Nodes;
import memory.node.actor.FileCheckActor;
import memory.node.actor.FileToActor;
import memory.node.actor.PrintActor;
import memory.node.actor.WriteNodeActor;
import memory.node.element.MemoryElement;
import memory.node.filter.NodeFilter;
import memory.util.Pair;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class Command {

    public static Node<MemoryElement> index(String filename) throws IOException {
        Indexer indexer = new Indexer(filename);
        Node<MemoryElement> index = indexer.index();
        indexer.close();
        return index;
    }

    public static Node<MemoryElement> index(String filename, long start) throws IOException {
        Indexer indexer = new Indexer(filename);
        Node<MemoryElement> index = indexer.index(start);
        indexer.close();
        return index;
    }

    public static void toFile(String memoryFilename, long start, String baseName) throws IOException {
        toFile(index(memoryFilename, start), baseName);
    }

    public static void toFile(Node<?> node, String baseName) {
        node.search(new FileToActor(baseName));
    }

    @SafeVarargs
    public static void fromFile(String memoryFilename, String name, Pair<List<String>, List<Node<?>>>... pairs) throws IOException, NoSuchAlgorithmException {
        if (name.isEmpty()) {
            throw new RuntimeException();
        }
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        Node<?> node = Nodes.deleteStatusAndChecksumNode(Nodes.fileNode(name, pairs), messageDigest, false);
        node.search(new WriteNodeActor(memoryFilename));
    }

    public static void fromNode(String memoryFilename, Node<?> node) throws NoSuchAlgorithmException, IOException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        Node<?> node2 = Nodes.deleteStatusAndChecksumNode(node, messageDigest, false);
        node2.search(new WriteNodeActor(memoryFilename));
    }

    public static void check(String memoryFilename, long start, String baseName) throws IOException {
        check(index(memoryFilename, start), baseName);
    }

    public static void check(Node<?> node, String baseName) throws IOException {
        node.search(new FileCheckActor(baseName));
    }

    public static void print(String memoryFilename, NodeFilter nodeFilter) throws IOException {
        print(index(memoryFilename), nodeFilter);
    }

    public static void print(String memoryFilename, long start, NodeFilter nodeFilter) throws IOException {
        print(index(memoryFilename, start), nodeFilter);
    }

    public static void print(Node<?> node, NodeFilter nodeFilter) {
        node.search(nodeFilter, new PrintActor());
    }

}
