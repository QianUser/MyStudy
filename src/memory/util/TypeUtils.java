package memory.util;

public class TypeUtils {

    public static char toChar(byte b1, byte b2) {
        return (char) (b1 << 8 | b2 << 24 >>> 24);
    }

    public static byte[] toBytes(String s) {
        if (s.length() > 0x3fffffff) {
            throw new RuntimeException("过长的字符串");
        }
        byte[] bytes = new byte[2 * s.length()];
        for (int i = 0; i < s.length(); ++i) {
            bytes[2 * i] = (byte) ((s.charAt(i) & 0xff00) >> 8);
            bytes[2 * i + 1] = (byte) (s.charAt(i) & 0xff);
        }
        return bytes;
    }

}
