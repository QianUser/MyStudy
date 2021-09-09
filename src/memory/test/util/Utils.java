package memory.test.util;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class Utils {

    public static void assertEquals(Object o1, Object o2) {
        if (o1 != o2 && !o1.equals(o2)) {
            throw new AssertionError(o1 + ", " + o2);
        }
    }

    public static String progress(long cur, long total) {
        if (cur > total) {
            throw new RuntimeException();
        }
        int t = (int) Math.round((double) cur / total * 100);
        return cur + "/" + total + " [" + memory.util.Utils.repeat("*", t) + memory.util.Utils.repeat("-", 100 - t) + "]";
    }

    public static boolean isEqualContent(File file1, File file2) throws Exception {
        try (
                RandomAccessFile randomAccessFile1 = new RandomAccessFile(file1, "r");
                RandomAccessFile randomAccessFile2 = new RandomAccessFile(file2, "r")
        ) {
            long length = randomAccessFile1.length();
            if (randomAccessFile2.length() != length) {
                return false;
            }
            while (length > 0) {
                int limit = (int) Math.min(length, 16777216);
                byte[] bytes1 = new byte[limit];
                byte[] bytes2 = new byte[limit];
                randomAccessFile1.read(bytes1);
                randomAccessFile2.read(bytes2);
                if (!Arrays.equals(bytes1, bytes2)) {
                    return false;
                }
                length -= limit;
            }
            return true;
        }
    }

}
