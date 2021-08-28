package memory.node;

import memory.util.FileProperty;
import memory.util.Pair;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.List;
import java.util.Objects;

public class Nodes {

    public static Node<?> deleteStatusAndChecksumNode(Node<?> node, MessageDigest messageDigest, boolean delete) throws IOException {
        node.addDeleteStatusNodes(delete);
        node.addChecksumNodes(messageDigest);
        return node;
    }

    public static Node<String> passwordNode(String password) {
        return stringNode("解压密码", password);
    }

    public static Node<String> nameNode(String name) {
        return stringNode("名称", name);
    }

    public static Node<String> sourceNode(String source, String password) {
        Node<String> sourceKeyNode = new Node<>("来源");
        Node<String> sourceValueNode = new Node<>(source);
        Node<String> passwordNode = stringNode("提取码", password);
        sourceValueNode.insertAsLastChild(passwordNode);
        sourceKeyNode.insertAsLastChild(sourceValueNode);
        return sourceKeyNode;
    }

    public static Node<String> sourceNode(String source) {
        return stringNode("来源", source);
    }

    public static Node<String> stringNode(String name, String value) {
        Node<String> node = new Node<>(name);
        node.insertAsLastChild(new Node<>(value));
        return node;
    }

    @SafeVarargs
    public static Node<String> fileNode(String name, Pair<List<String>, List<Node<?>>>... pairs) throws IOException {
        if (pairs.length == 0) {
            throw new RuntimeException();
        }
        Node<String> result = new Node<>("");
        for (Pair<List<String>, List<Node<?>>> pair : pairs) {
            result.insertAsLastChild(fileNode(pair.first, pair.second));
        }
        if (name.length() != 0) {
            result.insertAsFirstChild(nameNode(name));
        }
        return result;
    }

    private static Node<String> fileNode(List<String> filenames, List<Node<?>> nodes) throws IOException {
        if (filenames.size() == 0) {
            throw new RuntimeException();
        }
        Node<String> result;
        if (filenames.size() == 1) {
            result = fileNode(new File(filenames.get(0)));
        } else {
            result = new Node<>("");
            for (String filename : filenames) {
                result.insertAsLastChild(fileNode(new File(filename)));
            }
        }
        for (Node<?> node : nodes) {
            result.insertAsLastChild(node);
        }
        return result;
    }

    private static Node<String> fileNode(File file) throws IOException {
        FileProperty fileProperty = new FileProperty(file);
        Node<String> result = fileProperty.to();
        if (fileProperty.type.equals("文件夹")) {
            for (File subFile : Objects.requireNonNull(file.listFiles())) {
                result.insertAsLastChild(fileNode(subFile));
            }
        }
        return result;
    }

}
