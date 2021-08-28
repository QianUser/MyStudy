package memory.node.filter;

import memory.node.Node;
import memory.node.element.MemoryElement;

public class DeleteStatusAndChecksumFilter implements NodeFilter {

    @Override
    public boolean accept(Node<?> node) {
        String name = ((MemoryElement) node.getElement()).getName();
        if (name.equals("删除") || name.equals("SHA-256校验码")) {
            return false;
        } else if (node.getParent() != null) {
            String parentName = ((MemoryElement) node.getParent().getElement()).getName();
            return !parentName.equals("删除") && !parentName.equals("SHA-256校验码");
        } else {
            return true;
        }
    }

}
