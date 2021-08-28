package memory.node.actor;

import memory.node.Node;
import memory.util.FileProperty;
import memory.visitor.Visitor;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static memory.util.FileUtils.walkFile;

public class FileCheckActor implements NodeActor {

    private final String baseName;

    private int rootIndex;

    private final Set<String> checkedFiles;

    private final Set<String> expectedFiles;

    public FileCheckActor(String baseName) throws IOException {
        this.baseName = new File(baseName).getCanonicalPath();
        this.rootIndex = 0;
        this.expectedFiles = walkFile(baseName);
        this.checkedFiles = new HashSet<>();
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
        File file = new File(realFilename);
        try {
            check(fileProperty, new FileProperty(file));
        } catch (IOException e) {
            throw new RuntimeException();
        }
        if (fileProperty.type.equals("文件") && fileProperty.length != 0) {
            try {
                Visitor visitor = new Visitor(realFilename, Visitor.Mode.R);
                Visitor elementVisitor = new Visitor(fileProperty.content.getElement().getFilename(), Visitor.Mode.R);
                elementVisitor.forward(fileProperty.content.getElement().getStart());
                for (long i = 0; i < fileProperty.content.getElement().getLength(); ++i) {
                    if (visitor.peek() != elementVisitor.peek()) {
                        throw new RuntimeException();
                    }
                    visitor.forward();
                    elementVisitor.forward();
                }
                visitor.close();
                elementVisitor.close();
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }
        checkedFiles.add(realFilename);
    }

    @Override
    public void afterComplete() {
        if (!expectedFiles.equals(checkedFiles)) {
            throw new RuntimeException();
        }
    }

    private String getRealFilename(String logicalFilename) {
        if (!logicalFilename.contains(File.separator)) {
            rootIndex = 0;
            while (checkedFiles.contains(baseName + File.separator + (rootIndex == 0 ? "" : rootIndex + "_") + logicalFilename)) {
                ++rootIndex;
            }
        }
        return baseName + File.separator + (rootIndex == 0 ? "" : rootIndex + "_") + logicalFilename;
    }


    private void check(FileProperty fileProperty1, FileProperty fileProperty2) {
        if (!Objects.equals(fileProperty1.type, fileProperty2.type)) {
            throw new RuntimeException();
        }
        if (!Objects.equals(fileProperty1.readOnly, fileProperty2.readOnly)) {
            throw new RuntimeException();
        }
        if (!Objects.equals(fileProperty1.hidden, fileProperty2.hidden)) {
            throw new RuntimeException();
        }
        if (!Objects.equals(fileProperty1.length, fileProperty2.length)) {
            throw new RuntimeException();
        }
    }

}
