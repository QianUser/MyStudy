package memory.node.actor;

import memory.node.Node;
import memory.node.element.BytesElement;
import memory.node.getter.ChecksumNodeGetter;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;

public class VerifyChecksumActor implements NodeActor {

    private static final ChecksumNodeGetter checksumNodeGetter = new ChecksumNodeGetter();

    private final MessageDigest messageDigest;

    public VerifyChecksumActor(MessageDigest messageDigest) {
        this.messageDigest = messageDigest;
    }

    @Override
    public void forward(Node node) {
        Node checksumNode = node.act(checksumNodeGetter);
        if (checksumNode != null) {
            node.getElement().updateChecksum(messageDigest);
            List<Node> children = node.getChildren();
            for (int i = 1; i < children.size(); ++i) {
                children.get(i).act(checksumNodeGetter).getElement().updateChecksum(messageDigest);
            }
            byte[] realChecksum = messageDigest.digest();
            byte[] expectedChecksum = checksumNode.getUnwrapped(BytesElement.class).getBytes();
            if (!Arrays.equals(expectedChecksum, realChecksum)) {
                throw new RuntimeException("校验码不相等，期望值：" + Arrays.toString(expectedChecksum) + "，实际值：" + Arrays.toString(realChecksum));
            }
        }
    }

}
