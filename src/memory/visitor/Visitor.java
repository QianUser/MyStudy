package memory.visitor;

import memory.util.Utils;
import memory.visitor.transformer.DefaultTransformer;
import memory.visitor.transformer.Transformer;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Visitor {

    private enum CacheState {
        N, C1, F_O, F_NO, NF_O, NF_NO
    }

    private int cacheSize;

    private Transformer transformer;

    private File file;

    private RandomAccessFile randomAccessFile;

    private long length;

    private long position;

    private byte[] cache1;

    private byte[] cache2;

    private boolean dirty1;

    private boolean dirty2;

    private CacheState cacheState;

    private int index;

    public Visitor() throws IOException {
        this(new DefaultTransformer(), null);
    }

    public Visitor(Transformer transformer) throws IOException {
        this(transformer, null);
    }

    public Visitor(File file) throws IOException {
        this(new DefaultTransformer(), file);
    }

    public Visitor(Transformer transformer, File file) throws IOException {
        this.file = file;
        this.transformer = transformer;
    }

    public int getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(int cacheSize) {
        if (cacheSize <= 0) {
            throw new RuntimeException("缓存大小：" + cacheSize + "小于等于0");
        }
        transformer.checkCacheSize(cacheSize);
        this.cacheSize = cacheSize;
    }

    public Transformer getTransformer() {
        return transformer;
    }

    public void setTransformer(Transformer transformer) throws IOException {
        reset();
        this.transformer = transformer;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) throws IOException {
        reset();
        this.file = file;
        this.randomAccessFile = new RandomAccessFile(file, file.exists() && !file.canWrite() ? "rw" : "r");
        this.length = transformer.initLength(randomAccessFile);
    }

    public long getLength() {
        return length;
    }

    public long getPosition() {
        return position;
    }

    public boolean isEof() {
        return position >= length;
    }

    public boolean isEof(long distance) {
        return position + distance >= length || position + distance < 0;
    }

    public boolean isEofChar() {
        return position + 1 >= length;
    }

    public boolean isEofChar(long distance) {
        return position + 2 * distance + 1 >= length || position + 2 * distance < 0;
    }

    public void forward() throws IOException {
        forward(1);
    }

    public void forward(long distance) throws IOException {
        if (distance < 0) {
            back(-distance);
            return;
        }
        if (position + distance > length) {
            throw new EOFException();
        }
        position += distance;
        switch (cacheState) {
            case N:
                initCache();
                break;
            case C1:
            case NF_NO:
                if (index + distance < cache1.length) {
                    index += distance;
                } else if (index + distance < (long) cache1.length + cacheSize) {
                    cache2(-index + cache1.length);
                    cacheState = CacheState.NF_O;
                    index = (int) (index + distance - cache1.length);
                } else {
                    flush();
                    cacheState = CacheState.N;
                    forward(distance);
                    return;
                }
                break;
            case NF_O:
                if (index + distance < cache2.length) {
                    index += distance;
                } else if (index + distance < (long) cache2.length + cacheSize) {
                    cache1(-index + cache2.length);
                    cacheState = CacheState.NF_NO;
                    index = (int) (index + distance - cache2.length);
                } else {
                    flush();
                    cacheState = CacheState.N;
                    forward(distance);
                    return;
                }
                break;
            case F_O:
                if (index + distance < cache1.length) {
                    index += distance;
                } else if (index + distance < (long) cache1.length + cache2.length) {
                    cacheState = CacheState.NF_O;
                    index = (int) (index + distance - cache1.length);
                } else if (index + distance < (long) cache1.length + cache2.length + cacheSize) {
                    int size1 = cache1.length;
                    cache1(-index + cache1.length + cache2.length);
                    cacheState = CacheState.NF_NO;
                    index = (int) (index + distance - size1 - cache2.length);
                } else {
                    flush();
                    cacheState = CacheState.N;
                    forward(distance);
                    return;
                }
                break;
            case F_NO:
                if (index + distance < cache2.length) {
                    index += distance;
                } else if (index + distance < (long) cache1.length + cache2.length) {
                    cacheState = CacheState.NF_NO;
                    index = (int) (index + distance - cache2.length);
                } else if (index + distance < (long) cache1.length + cache2.length + cacheSize) {
                    int size2 = cache2.length;
                    cache2(-index + cache1.length + cache2.length);
                    cacheState = CacheState.NF_O;
                    index = (int) (index + distance - cache1.length - size2);
                } else {
                    flush();
                    cacheState = CacheState.N;
                    forward(distance);
                    return;
                }
                break;
        }
    }

    public void forwardChar() throws IOException {
        forward(2);
    }

    public void forwardChar(long distance) throws IOException {
        forward(2 * distance);
    }

    public void back() throws IOException {
        back(1);
    }

    public void back(long distance) throws IOException {
        if (distance < 0) {
            forward(-distance);
            return;
        }
        if (position - distance < 0) {
            throw new EOFException();
        }
        position -= distance;
        switch (cacheState) {
            case N:
                initCache();
                break;
            case C1:
            case F_O:
                if (index >= distance) {
                    index -= distance;
                } else if ((long) index + cacheSize >= distance) {
                    cache2((long) -index - cacheSize);
                    cacheState = CacheState.F_NO;
                    index = (int) (index - distance + cacheSize);
                } else {
                    flush();
                    cacheState = CacheState.N;
                    back(distance);
                    return;
                }
                break;
            case F_NO:
                if (index >= distance) {
                    index -= distance;
                } else if ((long) index + cacheSize >= distance) {
                    cache1((long) -index - cacheSize);
                    cacheState = CacheState.F_O;
                    index = (int) (index - distance + cacheSize);
                } else {
                    flush();
                    cacheState = CacheState.N;
                    back(distance);
                    return;
                }
                break;
            case NF_O:
                if (index >= distance) {
                    index -= distance;
                } else if ((long) index + cache1.length >= distance) {
                    cacheState = CacheState.F_O;
                    index = (int) (index - distance + cache1.length);
                } else if ((long) index + cache1.length + cacheSize >= distance) {
                    cache2((long) -index - cache1.length - cacheSize);
                    cacheState = CacheState.F_NO;
                    index = (int) (index - distance + cache1.length + cacheSize);
                } else {
                    flush();
                    cacheState = CacheState.N;
                    back(distance);
                    return;
                }
                break;
            case NF_NO:
                if (index >= distance) {
                    index -= distance;
                } else if ((long) index + cache2.length >= distance) {
                    cacheState = CacheState.F_NO;
                    index = (int) (index - distance + cache2.length);
                } else if ((long) index + cache2.length + cacheSize >= distance) {
                    cache1((long) -index - cache2.length - cacheSize);
                    cacheState = CacheState.F_O;
                    index = (int) (index - distance + cache2.length + cacheSize);
                } else {
                    flush();
                    cacheState = CacheState.N;
                    back(distance);
                    return;
                }
                break;
        }
    }

    public void backChar() throws IOException {
        back(2);
    }

    public void backChar(long distance) throws IOException {
        back(2 * distance);
    }

    public byte peek() throws IOException {
        if (position == length) {
            throw new EOFException();
        }
        switch (cacheState) {
            case N:
                initCache();
                return cache1[index];
            case C1:
            case F_O:
            case NF_NO:
                return cache1[index];
            case F_NO:
            case NF_O:
                return cache2[index];
        }
        throw new RuntimeException();
    }

    public char peekChar() throws IOException {
        return Utils.toChar(peek(), next(1));
    }

    public byte next() throws IOException {
        return next(1);
    }

    public byte next(long distance) throws IOException {
        if (distance < 0) {
            return previous(-distance);
        }
        if (distance == 0) {
            return peek();
        }
        if (position + distance >= length) {
            throw new EOFException();
        }
        switch (cacheState) {
            case N:
                initCache();
                return next(distance);
            case C1:
            case NF_NO:
                if (index + distance < cache1.length) {
                    return cache1[(int) (index + distance)];
                } else if (index + distance < (long) cache1.length + cacheSize) {
                    cache2(-index + cache1.length);
                    cacheState = CacheState.F_O;
                    return cache2[(int) (index + distance - cache1.length)];
                } else {
                    flush();
                    randomAccessFile.seek(position + distance);
                    return randomAccessFile.readByte();
                }
            case NF_O:
                if (index + distance < cache2.length) {
                    return cache2[(int) (index + distance)];
                } else if (index + distance < (long) cache2.length + cacheSize) {
                    cache1(-index + cache2.length);
                    cacheState = CacheState.F_NO;
                    return cache1[(int) (index + distance - cache2.length)];
                } else {
                    flush();
                    randomAccessFile.seek(position + distance);
                    return randomAccessFile.readByte();
                }
            case F_O:
                if (index + distance < cache1.length) {
                    return cache1[(int) (index + distance)];
                } else if (index + distance < (long) cache1.length + cache2.length) {
                    return cache2[(int) (index + distance - cache1.length)];
                } else {
                    flush();
                    randomAccessFile.seek(position + distance);
                    return randomAccessFile.readByte();
                }
            case F_NO:
                if (index + distance < cache2.length) {
                    return cache2[(int) (index + distance)];
                } else if (index + distance < (long) cache1.length + cache2.length) {
                    return cache1[(int) (index + distance - cache2.length)];
                } else {
                    flush();
                    randomAccessFile.seek(position + distance);
                    return randomAccessFile.readByte();
                }
        }
        throw new RuntimeException();
    }

    public char nextChar() throws IOException {
        return Utils.toChar(next(2), next(3));
    }

    public char nextChar(long distance) throws IOException {
        return Utils.toChar(next(2 * distance), next(2 * distance + 1));
    }

    public byte previous() throws IOException {
        return previous(1);
    }

    public byte previous(long distance) throws IOException {
        if (distance < 0) {
            return next(-distance);
        }
        if (distance == 0) {
            return peek();
        }
        if (position - distance < 0) {
            throw new EOFException();
        }
        switch (cacheState) {
            case N:
                initCache();
                return previous(distance);
            case C1:
            case F_O:
                if (index >= distance) {
                    return cache1[(int) (index - distance)];
                } else if ((long) index + cacheSize >= distance) {
                    cache2((long) -index - cacheSize);
                    cacheState = CacheState.NF_NO;
                    return cache2[(int) (index - distance + cacheSize)];
                } else {
                    flush();
                    randomAccessFile.seek(position - distance);
                    return randomAccessFile.readByte();
                }
            case F_NO:
                if (index >= distance) {
                    return cache2[(int) (index - distance)];
                } else if ((long) index + cacheSize >= distance) {
                    cache1((long) -index - cacheSize);
                    cacheState = CacheState.NF_O;
                    return cache1[(int) (index - distance + cacheSize)];
                } else {
                    flush();
                    randomAccessFile.seek(position - distance);
                    return randomAccessFile.readByte();
                }
            case NF_O:
                if (index >= distance) {
                    return cache2[(int) (index - distance)];
                } else if ((long) index + cache1.length >= distance) {
                    return cache1[(int) (index - distance + cache1.length)];
                } else {
                    flush();
                    randomAccessFile.seek(position - distance);
                    return randomAccessFile.readByte();
                }
            case NF_NO:
                if (index >= distance) {
                    return cache1[(int) (index - distance)];
                } else if ((long) index + cache2.length >= distance) {
                    return cache2[(int) (index - distance + cache2.length)];
                } else {
                    flush();
                    randomAccessFile.seek(position - distance);
                    return randomAccessFile.readByte();
                }
        }
        throw new RuntimeException();
    }

    public char previousChar() throws IOException {
        return Utils.toChar(previous(2), previous(1));
    }

    public char previousChar(long distance) throws IOException {
        return Utils.toChar(previous(2 * distance), previous(2 * distance - 1));
    }

    public void write(byte b) throws IOException {
        switch (cacheState) {
            case N:
                initCache();
                cache1[index] = b;
                dirty1 = true;
                break;
            case C1:
            case F_O:
            case NF_NO:
                cache1[index] = b;
                dirty1 = true;
                break;
            case F_NO:
            case NF_O:
                cache2[index] = b;
                dirty2 = true;
                break;
        }
        if (position == length) {
            ++length;
        }
    }

    public void write(char c) throws IOException {
        write((byte) ((c & 0xff00) >> 8));
        forward(1);
        write((byte) (c & 0xff));
        back(1);
    }

    public void truncate() {
        // 在position处截断（不包含position）
    }

    public void flush() throws IOException {
        flush1();
        flush2();
    }

    public void close() throws IOException {
        flush();
        randomAccessFile.close();
    }

    private void reset() throws IOException {
        this.file = null;
        if (this.randomAccessFile != null) {
            this.randomAccessFile.close();
            this.randomAccessFile = null;
        }
        this.length = 0;
        this.position = 0;
        this.cache1 = new byte[0];
        this.cache2 = new byte[0];
        this.dirty1 = false;
        this.dirty2 = false;
        this.cacheState = CacheState.N;
        this.index = 0;
    }

    private void initCache() throws IOException {
        flush1();
        if (cache1.length != cacheSize) {
            cache1 = new byte[cacheSize];
        }
        cacheState = CacheState.C1;
        index = transformer.initDecode(randomAccessFile, position, length, cache1);
    }

    private void cache1(long distance) throws IOException {
        flush1();
        if (cache1.length != cacheSize) {
            cache1 = new byte[cacheSize];
        }
        long start = position + distance;
        transformer.decode(randomAccessFile, start, length , cache1);
    }

    private void cache2(long distance) throws IOException {
        flush2();
        if (cache2.length != cacheSize) {
            cache2 = new byte[cacheSize];
        }
        long start = position + distance;
        transformer.decode(randomAccessFile, start, length , cache2);
    }

    private void flush1() throws IOException {
        if (!dirty1) {
            return;
        }
        long start = 0;
        switch (cacheState) {
            case N:
                return;
            case C1:
            case F_O:
            case NF_NO:
                start = position - index;
                break;
            case F_NO:
                start = position - index + cache2.length;
                break;
            case NF_O:
                start = position - index - cache1.length;
                break;
        }
        transformer.encode(randomAccessFile, start, length, cache1);
        dirty1 = false;
    }

    private void flush2() throws IOException {
        if (!dirty2) {
            return;
        }
        long start = 0;
        switch (cacheState) {
            case N:
            case C1:
                return;
            case F_NO:
            case NF_O:
                start = position - index;
                break;
            case F_O:
                start = position - index + cache1.length;
                break;
            case NF_NO:
                start = position - index - cache2.length;
                break;
        }
        transformer.encode(randomAccessFile, start, length, cache2);
        dirty2 = false;
    }

}
