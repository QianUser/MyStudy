package memory.node.getter;

import memory.node.Node;

public class DepthGetter implements NodeGetter<Integer> {

    @Override
    public Integer get(Node node) {
        int cnt = 0;
        while ((node = node.getParent()) != null) {
            ++cnt;
        }
        return cnt;
    }

}
