package memory.util;

import memory.node.Node;
import memory.node.element.MemoryElement;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.Instant;

public class FileProperty {

    public String name;
    public String type;
    public FileTime createdTime;
    public FileTime modifiedTime;
    public boolean readOnly;
    public boolean hidden;
    public long length;
    public Node<MemoryElement> content;

    public FileProperty() {}

    public FileProperty(File file) throws IOException {
        BasicFileAttributes attributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        this.name = name(file);
        this.type = type(attributes);
        this.createdTime = createdTime(attributes);
        this.modifiedTime = modifiedTime(attributes);
        this.readOnly = readOnly(file);
        this.hidden = hidden(file);
        this.length = length(attributes);
        if (length != 0) {
            this.content = new Node<>(new MemoryElement(file.getCanonicalPath(), "", 0, length));
        } else {
            this.content = null;
        }
    }

    public void set(File file) {
        modifiedTime(file, modifiedTime);
        readOnly(file, readOnly);
    }

    public Node<String> to() {
        Node<String> result = new Node<>("");
        result.insertAsLastChild(new Node<>("名称")).insertAsLastChild(new Node<>(name));
        result.insertAsLastChild(new Node<>("属性")).insertAsLastChild(new Node<>(type));
        result.insertAsLastChild(new Node<>("创建时间")).insertAsLastChild(new Node<>(createdTime.toString()));
        result.insertAsLastChild(new Node<>("修改时间")).insertAsLastChild(new Node<>(modifiedTime.toString()));
        result.insertAsLastChild(new Node<>("只读")).insertAsLastChild(new Node<>(readOnly ? "是" : "否"));
        result.insertAsLastChild(new Node<>("隐藏")).insertAsLastChild(new Node<>(hidden ? "是" : "否"));
        if (type.equals("文件")) {
            result.insertAsLastChild(new Node<>("长度")).insertAsLastChild(new Node<>(length + ""));
        }
        if (content != null) {
            result.insertAsLastChild(new Node<>("内容")).insertAsLastChild(content);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static FileProperty from(Node<?> node) {
        Node<?> typeKeyNode = node.getChild(i -> ((MemoryElement) i.getElement()).getName().equals("属性"));
        if (typeKeyNode == null) {
            return null;
        }
        Node<MemoryElement> typeValueNode = (Node<MemoryElement>) typeKeyNode.getChild(3);
        if (typeValueNode == null) {
            return null;
        }
        String filename = ((MemoryElement) typeKeyNode.getElement()).getFilename();
        String type = typeValueNode.getElement().getName();
        if (!type.equals("文件") && !type.equals("文件夹")) {
            return null;
        }
        String name = ((MemoryElement) node.getChild(i -> ((MemoryElement) i.getElement()).getName().equals("名称")).getChild(3).getElement()).getName();
        String createdTime = ((MemoryElement) node.getChild(i -> ((MemoryElement) i.getElement()).getName().equals("创建时间")).getChild(3).getElement()).getName();
        String modifiedTime = ((MemoryElement) node.getChild(i -> ((MemoryElement) i.getElement()).getName().equals("修改时间")).getChild(3).getElement()).getName();
        String readOnly = ((MemoryElement) node.getChild(i -> ((MemoryElement) i.getElement()).getName().equals("只读")).getChild(3).getElement()).getName();
        String hidden = ((MemoryElement) node.getChild(i -> ((MemoryElement) i.getElement()).getName().equals("隐藏")).getChild(3).getElement()).getName();
        long length = 0;
        Node<?> lengthKeyNode = node.getChild(i -> ((MemoryElement) i.getElement()).getName().equals("长度"));
        if (lengthKeyNode != null) {
            length = Long.parseLong(((MemoryElement) lengthKeyNode.getChild(3).getElement()).getName());
        }
        FileProperty fileProperty = new FileProperty();
        fileProperty.name = name;
        fileProperty.type = type;
        fileProperty.createdTime = FileTime.from(Instant.parse(createdTime));
        fileProperty.modifiedTime = FileTime.from(Instant.parse(modifiedTime));
        fileProperty.readOnly = readOnly.equals("是");
        fileProperty.hidden = hidden.equals("是");
        fileProperty.length = length;
        if (length != 0) {
            MemoryElement content = (MemoryElement) node.getChild(i -> ((MemoryElement) i.getElement()).getName().equals("内容")).getChild(3).getElement();
            fileProperty.content = new Node<>(new MemoryElement(filename, content.getName(), content.getStart(), content.getLength()));
        } else {
            fileProperty.content = null;
        }
        return fileProperty;
    }

    private static String name(File file) throws IOException {
        String canonicalPath = file.getCanonicalPath();
        return canonicalPath.substring(canonicalPath.lastIndexOf(File.separator) + 1);
    }

    private static String type(BasicFileAttributes attributes) {
        if (attributes.isDirectory()) {
            return "文件夹";
        } else if (attributes.isRegularFile()) {
            return "文件";
        }
        throw new RuntimeException();
    }

    private static FileTime createdTime(BasicFileAttributes attributes) {
        return attributes.creationTime();
    }

    private static FileTime modifiedTime(BasicFileAttributes attributes) {
        return attributes.lastModifiedTime();
    }

    private static void modifiedTime(File file, FileTime fileTime) {
        if (!file.setLastModified(fileTime.toMillis())) {
            throw new RuntimeException();
        }
    }

    private static boolean readOnly(File file) {
        return !file.canWrite();
    }

    private static void readOnly(File file, boolean readOnly) {
        if (readOnly && !file.setReadOnly()) {
            throw new RuntimeException();
        }
    }

    private static boolean hidden(File file) {
        return file.isHidden();
    }

    private static long length(BasicFileAttributes attributes) {
        if (attributes.isDirectory()) {
            return 0;
        } else if (attributes.isRegularFile()) {
            return attributes.size();
        }
        throw new RuntimeException();
    }

}