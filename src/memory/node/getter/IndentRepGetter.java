package memory.node.getter;

import memory.node.Node;
import memory.util.Utils;

public class IndentRepGetter implements RepGetter {

    private static final DepthGetter depthGetter = new DepthGetter();

    @Override
    public String get(Node node) {
        return Utils.repeat("\t", node.act(depthGetter)) + node.getElement() + System.lineSeparator();
    }

}
