package memory.visitor.transformer;

import java.io.IOException;
import java.io.RandomAccessFile;

public class DefaultTransformer implements Transformer {

    @Override
    public void checkCacheSize(int cacheSize) {}

    @Override
    public long initLength(RandomAccessFile randomAccessFile) throws IOException {
        return randomAccessFile.length();
    }

    @Override
    public int initDecode(RandomAccessFile randomAccessFile, long start, long length, byte[] bytes) throws IOException {
        decode(randomAccessFile, start, length , bytes);
        return 0;
    }

    @Override
    public void decode(RandomAccessFile randomAccessFile, long start, long length, byte[] bytes) throws IOException {
        if (start >= 0) {
            randomAccessFile.seek(start);
            randomAccessFile.read(bytes);
        } else {
            randomAccessFile.seek(0);
            randomAccessFile.read(bytes, (int) -start, (int) (bytes.length + start));
        }
    }

    @Override
    public void encode(RandomAccessFile randomAccessFile, long start, long length, byte[] bytes) throws IOException {
        if (start >= 0) {
            randomAccessFile.seek(start);
            randomAccessFile.write(bytes, 0, (int) Math.min(bytes.length, length - start));
        } else {
            randomAccessFile.seek(0);
            randomAccessFile.write(bytes, (int) -start, (int) Math.min(bytes.length + start, length));
        }
    }

}
