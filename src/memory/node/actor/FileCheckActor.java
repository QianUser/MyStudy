package memory.node.actor;

import memory.node.Node;
import memory.node.getter.*;
import memory.util.Utils;
import memory.visitor.Visitor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class FileCheckActor implements NodeActor {

    private final TypeGetter typeGetter = new TypeGetter();

    private final NameGetter nameGetter = new NameGetter();

    private static final FileCreatedTimeGetter fileCreatedTimeGetter = new FileCreatedTimeGetter();

    private static final FileModifiedTimeGetter fileModifiedTimeGetter = new FileModifiedTimeGetter();

    private static final FileIsReadOnlyGetter fileIsReadOnlyGetter = new FileIsReadOnlyGetter();

    private static final FileIsHiddenGetter fileIsHiddenGetter = new FileIsHiddenGetter();

    private static final LengthGetter lengthGetter = new LengthGetter();

    private static final ContentStartGetter contentStartGetter = new ContentStartGetter();

    protected final Visitor memoryReader;

    protected final Visitor fileReader;

    private final String baseName;

    private final boolean isCheckTime;

    private final Set<String> expectedFiles;

    private final Set<String> checkedFiles;

    public FileCheckActor(File memoryFile, Visitor memoryReader, Visitor fileReader, File baseDir, boolean isCheckTime) throws IOException {
        this.memoryReader = memoryReader;
        this.memoryReader.setFile(memoryFile);
        this.fileReader = fileReader;
        this.baseName = baseDir.getCanonicalPath();
        this.isCheckTime = isCheckTime;
        this.expectedFiles = Utils.walkDir(baseDir);
        this.checkedFiles = new HashSet<>();
    }

    @Override
    public void forward(Node node) {
        String fileType = node.act(typeGetter);
        if (fileType == null) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder(node.act(nameGetter));
        Node parent = node.getParent();
        while (parent != null) {
            String parentFileType = node.act(typeGetter);
            if (parentFileType == null) {
                continue;
            }
            stringBuilder.insert(0, parent.act(nameGetter) + File.separator);
            parent = parent.getParent();
        }
        String realFilename = getRealFilename(stringBuilder.toString());
        File file = new File(realFilename);
        try {
            checkAttributes(node, file);
            if (fileType.equals("文件") && node.act(lengthGetter) != 0) {
                long start = node.act(contentStartGetter);
                memoryReader.forward(start - memoryReader.getPosition());
                fileReader.setFile(file);
                for (long i = 0; i < fileReader.getLength(); ++i) {
                    if (memoryReader.peek() != fileReader.peek()) {
                        throw new RuntimeException();
                    }
                    memoryReader.forward();
                    fileReader.forward();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        checkedFiles.add(realFilename);
    }

    @Override
    public void complete(Node node) {
        if (!expectedFiles.equals(checkedFiles)) {
            throw new RuntimeException();
        }
    }

    private String getRealFilename(String logicalFilename) {
        int rootIndex = 0;
        if (!logicalFilename.contains(File.separator)) {
            while (checkedFiles.contains(baseName + File.separator + (rootIndex == 0 ? "" : rootIndex + "_") + logicalFilename)) {
                ++rootIndex;
            }
        }
        return baseName + File.separator + (rootIndex == 0 ? "" : rootIndex + "_") + logicalFilename;
    }


    private void checkAttributes(Node node, File file) throws IOException {
        BasicFileAttributes attributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        if (!Objects.equals(node.act(typeGetter), Utils.getFileType(attributes))) {
            throw new RuntimeException();
        }
        if (!Objects.equals(node.act(fileIsReadOnlyGetter), Utils.isFileReadOnly(file))) {
            throw new RuntimeException();
        }
        if (!Objects.equals(node.act(fileIsHiddenGetter), Utils.isFileHidden(file))) {
            throw new RuntimeException();
        }
        if (!Objects.equals(node.act(lengthGetter), Utils.getFileLength(attributes))) {
            throw new RuntimeException();
        }
        if (isCheckTime) {
            if (!Objects.equals(node.act(fileCreatedTimeGetter), Utils.getFileCreatedTime(attributes))) {
                throw new RuntimeException();
            }
            if (!Objects.equals(node.act(fileModifiedTimeGetter), Utils.getFileModifiedTime(attributes))) {
                throw new RuntimeException();
            }
        }
    }

}
