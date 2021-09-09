package memory.node.getter;

import memory.node.Node;

public class NodeWithStatusLengthGetter implements NodeGetter<Long> {

    private static final NodeLengthGetter nodeLengthGetter = new NodeLengthGetter();

    private long length;

    private final boolean isReset;

    public NodeWithStatusLengthGetter(boolean isReset) {
        this.length = 0;
        this.isReset = isReset;
    }

    public void reset() {
        this.length = 0;
    }

    @Override
    public void forward(Node node) {
        length = node.act(nodeLengthGetter)
                + node.getChild(0).search(nodeLengthGetter)
                + node.getChild(1).search(nodeLengthGetter);
    }

    @Override
    public Long get(Node node) {
        if (isReset) {
            long result = length;
            length = 0;
            return result;
        } else {
            return length;
        }
    }

}
