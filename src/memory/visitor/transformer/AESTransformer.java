package memory.visitor.transformer;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class AESTransformer implements Transformer {

    private static final int chunkSize = 16;

    private final Cipher cipher;

    private final SecretKeySpec key;

    private final BigInteger IV;

    public AESTransformer(byte[] key, byte[] IV) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.cipher = Cipher.getInstance("AES/CTR/NoPadding");
        this.key = new SecretKeySpec(key, "AES");
        this.IV = new BigInteger(IV);
    }

    @Override
    public void checkCacheSize(int cacheSize) {
        if (cacheSize % chunkSize != 0) {
            throw new RuntimeException("缓存大小必须是" + chunkSize + "的整数倍");
        }
    }

    @Override
    public long initLength(RandomAccessFile randomAccessFile) throws IOException {
        if (randomAccessFile.length() == 0) {
            return 0;
        }
        byte[] bytes = new byte[chunkSize];
        long length = randomAccessFile.length();
        decode(randomAccessFile, length - chunkSize, length, bytes);
        for (int i = chunkSize - 1; i >= 0; --i) {
            if (bytes[i] != 0) {
                return length - chunkSize + i;
            }
        }
        throw new RuntimeException("填充错误");
    }

    @Override
    public int initDecode(RandomAccessFile randomAccessFile, long start, long length, byte[] bytes) throws IOException {
        long position = start / chunkSize * chunkSize;
        decode(randomAccessFile, position, length , bytes);
        return (int) (start - position);
    }

    @Override
    public void decode(RandomAccessFile randomAccessFile, long start, long length, byte[] bytes) throws IOException {
        try {
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(addIV(start)));
            if (start >= 0) {
                randomAccessFile.seek(start);
                randomAccessFile.read(bytes);
            } else {
                randomAccessFile.seek(0);
                randomAccessFile.read(bytes, (int) -start, (int) (bytes.length + start));
            }
            System.arraycopy(cipher.doFinal(bytes), 0, bytes, 0, bytes.length);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void encode(RandomAccessFile randomAccessFile, long start, long length, byte[] bytes) throws IOException {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(addIV(start)));
            byte[] result;
            int limit = (int) Math.min(bytes.length + Math.min(start, 0), length - start);
            if (start + bytes.length >= length) {
                result = new byte[limit / chunkSize * chunkSize + chunkSize];
                result[limit] = -128;
            } else {
                result = new byte[limit % chunkSize == 0 ? limit : limit + chunkSize - limit % chunkSize];
            }
            System.arraycopy(bytes, (int) Math.max(-start, 0), result, 0, limit);
            randomAccessFile.seek(start);
            randomAccessFile.write(cipher.doFinal(result));
        } catch (InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new IOException(e);
        }
    }

    private byte[] addIV(long start) {
        byte[] bytes = IV.add(BigInteger.valueOf(start / chunkSize)).toByteArray();
        if (bytes.length < chunkSize) {
            byte[] result = new byte[chunkSize];
            System.arraycopy(bytes, 0, result, chunkSize - bytes.length, bytes.length);
            return result;
        } else {
            return bytes;
        }
    }

    private int ceil(int n) {
        return  n / chunkSize * chunkSize + chunkSize;
    }

}
