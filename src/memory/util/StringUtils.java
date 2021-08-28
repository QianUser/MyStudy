package memory.util;

public class StringUtils {

    public static String escape(String s) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < s.length(); ++i) {
            if (s.charAt(i) == '(') {
                stringBuilder.append("(())");
            } else if (s.charAt(i) == ')') {
                stringBuilder.append("()()");
            } else {
                stringBuilder.append(s.charAt(i));
            }
        }
        return stringBuilder.toString();
    }

    public static String repeat(String s, int n) {
        if (n < 0) {
            throw new RuntimeException();
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < n; ++i) {
            stringBuilder.append(s);
        }
        return stringBuilder.toString();
    }

    public static String progress(long cur, long total) {
        if (cur > total) {
            throw new RuntimeException();
        }
        int t = (int) Math.round((double) cur / total * 100);
        return cur + "/" + total + " [" + StringUtils.repeat("*", t) + StringUtils.repeat("-", 100 - t) + "]";
    }

}
