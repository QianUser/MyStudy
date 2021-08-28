package memory.util;

import memory.visitor.Visitor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class FileUtils {

    public static boolean isEqualContent(String filename1, String filename2) throws IOException {
        Visitor visitor1 = new Visitor(filename1, Visitor.Mode.R);
        Visitor visitor2 = new Visitor(filename2, Visitor.Mode.R);
        long length = visitor1.length();
        if (length != visitor2.length()) {
            visitor1.close();
            visitor2.close();
            return false;
        }
        for (long i = 0; i < length; ++i) {
            if (visitor1.peek() != visitor2.peek()) {
                visitor1.close();
                visitor2.close();
                return false;
            }
            visitor1.forward();
            visitor2.forward();
        }
        visitor1.close();
        visitor2.close();
        return true;
    }

    public static Set<String> walkFile(String filename) throws IOException {
        File file = new File(filename);
        if (!file.isDirectory()) {
            throw new RuntimeException();
        }
        Set<String> set = new HashSet<>();
        for (File subFile : Objects.requireNonNull(file.listFiles())) {
            Set<String> subSet = walkFile(subFile);
            set.addAll(subSet);
        }
        return set;
    }

    private static Set<String> walkFile(File file) throws IOException {
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
