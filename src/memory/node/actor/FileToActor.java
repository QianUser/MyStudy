package memory.node.actor;

import memory.node.Node;
import memory.node.getter.*;
import memory.util.Utils;
import memory.visitor.Visitor;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileToActor implements NodeActor {

    private final TypeGetter typeGetter = new TypeGetter();

    private final NameGetter nameGetter = new NameGetter();

    private static final FileCreatedTimeGetter fileCreatedTimeGetter = new FileCreatedTimeGetter();

    private static final FileModifiedTimeGetter fileModifiedTimeGetter = new FileModifiedTimeGetter();

    private static final FileIsReadOnlyGetter fileIsReadOnlyGetter = new FileIsReadOnlyGetter();

    private static final FileIsHiddenGetter fileIsHiddenGetter = new FileIsHiddenGetter();

    private static final LengthGetter lengthGetter = new LengthGetter();

    private static final ContentStartGetter contentStartGetter = new ContentStartGetter();

    protected final Visitor memoryReader;

    protected final Visitor fileWriter;

    private final String baseName;

    private final Map<Node, File> propertiesMap;

    public FileToActor(Visitor memoryReader, Visitor fileWriter, File baseDir, boolean isCopyProperties) throws IOException {
        this.memoryReader = memoryReader;
        this.fileWriter = fileWriter;
        this.baseName = baseDir.getCanonicalPath();
        if (isCopyProperties) {
            this.propertiesMap = new HashMap<>();
        } else {
            this.propertiesMap = null;
        }
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
        File file = new File(getRealFilename(stringBuilder.toString()));
        if (fileType.equals("文件夹")) {
            if (!file.mkdir()) {
                throw new RuntimeException();
            }
        } else {
            long length = node.act(lengthGetter);
            try {
                if (length == 0) {
                    if (!file.createNewFile()) {
                        throw new RuntimeException();
                    }
                } else {
                    long start = node.act(contentStartGetter);
                    memoryReader.forward(start - memoryReader.getPosition());
                    fileWriter.setFile(file);
                    for (long i = 0; i < length; ++i) {
                        fileWriter.write(memoryReader.peek());
                        memoryReader.forward();
                        fileWriter.forward();
                    }
                    fileWriter.flush();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (propertiesMap != null) {
            propertiesMap.put(node, file);
        }
    }

    @Override
    public void back(Node node) {
        if (propertiesMap == null || node.act(typeGetter) == null) {
            return;
        }
        File file = propertiesMap.get(node);
        Utils.setFileCreatedTime(file, node.act(fileCreatedTimeGetter));
        Utils.setFileModifiedTime(file, node.act(fileModifiedTimeGetter));
        Utils.setFileReadOnly(file, node.act(fileIsReadOnlyGetter));
        try {
            Utils.setFileHidden(file, node.act(fileIsHiddenGetter));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        propertiesMap.remove(node);
    }

    private String getRealFilename(String logicalFilename) {
        int rootIndex = 0;
        if (!logicalFilename.contains(File.separator)) {
            while (new File(baseName + File.separator + (rootIndex == 0 ? "" : rootIndex + "_") + logicalFilename).exists()) {
                ++rootIndex;
            }
        }
        return baseName + File.separator + (rootIndex == 0 ? "" : rootIndex + "_") + logicalFilename;
    }

}
