package memory;

import memory.command.Command;
import memory.node.Nodes;
import memory.util.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;

public class Main {

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        String memoryFilename = Paths.get("D:", "记忆").toString();
        long length = new File(memoryFilename).length();
        Command.fromFile(memoryFilename, "步骤1",
                new Pair<>(
                        Collections.singletonList(Paths.get("D:", "步骤4").toString()),
                        Arrays.asList(Nodes.sourceNode("步骤2"),
                                Nodes.sourceNode("步骤2", "步骤2"))
                ),
                new Pair<>(
                        Collections.singletonList(Paths.get("D:", "步骤4").toString()),
                        Collections.singletonList(Nodes.sourceNode("步骤2"))
                ),
                new Pair<>(
                        Collections.singletonList(Paths.get("D:", "步骤4").toString()),
                        Collections.singletonList(Nodes.sourceNode("步骤2"))
                )
        );
        if (!new File(memoryFilename).setReadOnly()) {
            throw new RuntimeException();
        }
        Command.check(memoryFilename, length, Paths.get("resource", "check").toString());
    }

}
