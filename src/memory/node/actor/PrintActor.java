package memory.node.actor;

import memory.node.Node;
import memory.util.StringUtils;

public class PrintActor implements NodeActor {

    @Override
    public void act(Node<?> node) {
        int depth = node.getDepth();
        System.out.println(StringUtils.repeat("\t", depth) + node);
    }

}
