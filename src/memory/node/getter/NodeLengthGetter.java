package memory.node.getter;

import memory.node.Node;

public class NodeLengthGetter implements NodeGetter<Long> {

    private long length;

    public NodeLengthGetter() {
        this.length = 0;
    }

    @Override
    public void forward(Node node) {
        length = length + 4 + node.getElement().length();
    }

    @Override
    public Long get(Node node) {
        long result = length;
        length = 0;
        return result;
    }

}
