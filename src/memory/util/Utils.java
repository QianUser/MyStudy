package memory.util;

import memory.visitor.Visitor;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Utils {

    public static String getFileType(BasicFileAttributes attributes) {
        if (attributes.isDirectory()) {
            return "文件夹";
        } else if (attributes.isRegularFile()) {
            return "文件";
        }
        throw new RuntimeException();
    }

    public static String getFilename(File file) throws IOException {
        String canonicalPath = file.getCanonicalPath();
        return canonicalPath.substring(canonicalPath.lastIndexOf(File.separator) + 1);
    }

    public static FileTime getFileCreatedTime(BasicFileAttributes attributes) {
        return attributes.creationTime();
    }

    public static void setFileCreatedTime(File file, FileTime fileTime) {
        try {
            Process p = Runtime.getRuntime().exec("cmd /c powershell");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss.SSS");
            String time = simpleDateFormat.format(fileTime.toMillis());
            p.getOutputStream().write(("Set-ItemProperty -Path " + file.getCanonicalPath() + " -Name CreationTime -Value \"" + time + "\"").getBytes(StandardCharsets.UTF_8));
            p.getOutputStream().close();
            p.waitFor();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static FileTime getFileModifiedTime(BasicFileAttributes attributes) {
        return attributes.lastModifiedTime();
    }

    public static void setFileModifiedTime(File file, FileTime fileTime) {
        if (!file.setLastModified(fileTime.toMillis())) {
            throw new RuntimeException();
        }
    }

    public static boolean isFileReadOnly(File file) {
        return !file.canWrite();
    }

    public static void setFileReadOnly(File file, boolean readonly) {
        if (!file.setWritable(!readonly)) {
            throw new RuntimeException();
        }
    }

    public static boolean isFileHidden(File file) {
        return file.isHidden();
    }

    public static void setFileHidden(File file, boolean isHidden) throws IOException {
        if (isHidden) {
            Runtime.getRuntime().exec("attrib " + "\"" + file.getAbsolutePath() + "\"" + " +H");
        } else {
            Runtime.getRuntime().exec("attrib " + "\"" + file.getAbsolutePath() + "\"" + " -H");
        }
    }

    public static long getFileLength(BasicFileAttributes attributes) {
        if (attributes.isDirectory()) {
            return 0;
        } else if (attributes.isRegularFile()) {
            return attributes.size();
        }
        throw new RuntimeException();
    }

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

    public static String encode(String s) {
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

    public static char decode(Visitor visitor) throws Exception {
        char c = visitor.peekChar();
        visitor.forwardChar();
        if (c == '(' && !visitor.isEofChar(2) && visitor.nextChar(2) == ')') {
            if (visitor.peekChar() == '(' && visitor.nextChar() == ')') {
                visitor.forwardChar(3);
            } else if (visitor.peekChar() == ')' && visitor.nextChar() == '(') {
                c = ')';
                visitor.forwardChar(3);
            }
        }
        return c;
    }

    public static String repeat(String string, int times) {
        if (times < 0) {
            throw new RuntimeException("重复数量不能小于0");
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < times; ++i) {
            stringBuilder.append(string);
        }
        return stringBuilder.toString();
    }

    public static Set<String> walkDir(File baseDir) throws IOException {
        Set<String> set = new HashSet<>();
        for (File subFile : Objects.requireNonNull(baseDir.listFiles())) {
            set.addAll(walkFile(subFile));
        }
        return set;
    }

    public static Set<String> walkFile(File file) throws IOException {
        Set<String> set = new HashSet<>();
        BasicFileAttributes attributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        if (attributes.isRegularFile()) {
            set.add(file.getCanonicalPath());
        } else if (attributes.isDirectory()) {
            for (File subFile : Objects.requireNonNull(file.listFiles())) {
                Set<String> subSet = walkFile(subFile);
                set.addAll(subSet);
            }
            set.add(file.getCanonicalPath());
        } else {
            throw new IOException();
        }
        return set;
    }

}
