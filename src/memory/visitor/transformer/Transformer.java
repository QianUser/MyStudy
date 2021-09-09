package memory.visitor.transformer;

import java.io.IOException;
import java.io.RandomAccessFile;

public interface Transformer {

    void checkCacheSize(int cacheSize);

    long initLength(RandomAccessFile randomAccessFile) throws IOException;

    int initDecode(RandomAccessFile randomAccessFile, long start, long length, byte[] bytes) throws IOException;

    void decode(RandomAccessFile randomAccessFile, long start, long length, byte[] bytes) throws IOException;

    void encode(RandomAccessFile randomAccessFile, long start, long length, byte[] bytes) throws IOException;

}
