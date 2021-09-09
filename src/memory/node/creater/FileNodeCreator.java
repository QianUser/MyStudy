package memory.node.creater;

import memory.node.Node;
import memory.node.element.FileElement;
import memory.node.element.NullElement;
import memory.node.element.StringElement;
import memory.util.Utils;
import memory.visitor.Visitor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Objects;

public class FileNodeCreator implements NodeCreator {

    private final File file;

    private final Visitor visitor;

    public FileNodeCreator(File file, Visitor visitor) {
        this.file = file;
        this.visitor = visitor;
    }

    @Override
    public Node create() {
        try {
            return getFileNode(file, visitor);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Node getFileNode(File file, Visitor visitor) throws IOException {
        BasicFileAttributes attributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        String name = Utils.getFilename(file);
        String type = Utils.getFileType(attributes);
        FileTime createdTime = Utils.getFileCreatedTime(attributes);
        FileTime modifiedTime = Utils.getFileModifiedTime(attributes);
        boolean readOnly = Utils.isFileReadOnly(file);
        boolean hidden = Utils.isFileHidden(file);
        long length = Utils.getFileLength(attributes);
        Node result = new Node(new NullElement());
        new ChainNodeCreator(result, new Node(new StringElement("名称")), new Node(new StringElement(name))).create();
        new ChainNodeCreator(result, new Node(new StringElement("类型")), new Node(new StringElement(type))).create();
        new ChainNodeCreator(result, new Node(new StringElement("创建时间")), new Node(new StringElement(createdTime.toString()))).create();
        new ChainNodeCreator(result, new Node(new StringElement("修改时间")), new Node(new StringElement(modifiedTime.toString()))).create();
        new ChainNodeCreator(result, new Node(new StringElement("只读")), new Node(new StringElement(readOnly ? "是" : "否"))).create();
        new ChainNodeCreator(result, new Node(new StringElement("隐藏")), new Node(new StringElement(hidden ? "是" : "否"))).create();
        if (type.equals("文件")) {
            new ChainNodeCreator(result, new Node(new StringElement("长度")), new Node(new StringElement(String.valueOf(length)))).create();
        }
        if (length != 0) {
            new ChainNodeCreator(result, new Node(new StringElement("内容")), new Node(new FileElement(file, visitor))).create();
        }
        if (type.equals("文件夹")) {
            for (File subFile : Objects.requireNonNull(file.listFiles())) {
                result.setLastChild(getFileNode(subFile, visitor));
            }
        }
        return result;
    }

}
