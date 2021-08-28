package memory.node.element;

import java.io.Serializable;

public class MemoryElement implements Comparable<MemoryElement>, Serializable {

    private String filename;
    private String name;
    private long start;
    private long length;

    public MemoryElement(String filename, String name, long start, long length) {
        this.filename = filename;
        this.name = name;
        this.start = start;
        this.length = length;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    @Override
    public int compareTo(MemoryElement o) {
        return Long.compare(start, o.start);
    }

    @Override
    public String toString() {
        String newName = name.length() == 0 ? "<未命名>" : name;
        return "[" + newName + ", (" + start + ", " + length + ")]";
    }

}
