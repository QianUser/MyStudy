package memory.node.actor;

import memory.node.Node;
import memory.util.FileProperty;
import memory.util.Pair;
import memory.visitor.Visitor;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileToActor implements NodeActor {

    private final String baseName;

    private int rootIndex;

    private final Map<Node<?>, Pair<FileProperty, String>> map;

    public FileToActor(String baseName) {
        this.baseName = baseName;
        this.rootIndex = 0;
        this.map = new HashMap<>();
    }

    @Override
    public void act(Node<?> node) {
        FileProperty fileProperty = FileProperty.from(node);
        if (fileProperty == null) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder(fileProperty.name);
        Node<?> parent = node.getParent();
        while (parent != null) {
            FileProperty parentFileProperty = FileProperty.from(parent);
            if (parentFileProperty != null) {
                stringBuilder.insert(0, parentFileProperty.name + File.separator);
            } else {
                break;
            }
            parent = parent.getParent();
        }
        String realFilename = getRealFilename(stringBuilder.toString());
        map.put(node, new Pair<>(fileProperty, realFilename));
        if (fileProperty.type.equals("文件夹")) {
            if (!new File(realFilename).mkdir()) {
                throw new RuntimeException();
            }
        } else {
            try {
                Visitor visitor = new Visitor(realFilename, Visitor.Mode.RW);
                Visitor elementVisitor = new Visitor(fileProperty.content.getElement().getFilename(), Visitor.Mode.R);
                elementVisitor.forward(fileProperty.content.getElement().getStart());
                for (long i = 0; i < fileProperty.content.getElement().getLength(); ++i) {
                    visitor.write(elementVisitor.peek());
                    visitor.forward();
                    elementVisitor.forward();
                }
                visitor.close();
                elementVisitor.close();
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }
    }

    @Override
    public void postHandle(Node<?> node) {
        Pair<FileProperty, String> pair = map.get(node);
        if (pair == null) {
            return;
        }
        map.remove(node);
        pair.first.set(new File(pair.second));
    }

    private String getRealFilename(String logicalFilename) {
        if (!logicalFilename.contains(File.separator)) {
            rootIndex = 0;
            while (new File(baseName + File.separator + (rootIndex == 0 ? "" : rootIndex + "_") + logicalFilename).exists()) {
                ++rootIndex;
            }
        }
        return baseName + File.separator + (rootIndex == 0 ? "" : rootIndex + "_") + logicalFilename;
    }

}
