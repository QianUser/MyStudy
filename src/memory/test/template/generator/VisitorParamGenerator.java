package memory.test.template.generator;

import memory.test.util.Pair;
import memory.visitor.Visitor;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;

public class VisitorParamGenerator extends ParamGenerator {

    private final Visitor visitor;

    private final List<File> files1;

    private final List<File> files2;

    private final Random random;

    public VisitorParamGenerator(Visitor visitor, List<File> files1, List<File> files2) {
        if (files1.size() != files2.size()) {
            throw new RuntimeException("文件数量不一致");
        }
        this.visitor = visitor;
        this.files1 = files1;
        this.files2 = files2;
        this.random = new Random();
    }

    @Override
    protected void generateParam(Method method, Object[] objects) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void generateParamsPair(Pair<Method, Method> methodPair, Object[] objects1, Object[] objects2) {
        if (objects1.length == 0) {
            return;
        }
        Class<?>[] types = methodPair.getFirst().getParameterTypes();
        if (objects1.length == 1 && types[0] == long.class) {
            objects1[0] = randomLong(visitor, methodPair.getFirst());
            objects2[0] = objects1[0];
        } else if (objects1.length == 1 && types[0] == int.class) {
            objects1[0] = randomInt(visitor);
            objects2[0] = objects1[0];
        } else if (objects1.length == 1 && types[0] == byte.class) {
            objects1[0] = randomByte();
            objects2[0] = objects1[0];
        } else if (objects1.length == 1 && types[0] == char.class) {
            objects1[0] = randomChar();
            objects2[0] = objects1[0];
        }  else if (objects1.length == 1 && types[0] == byte[].class) {
            objects1[0] = randomBytes(visitor);
            objects2[0] = objects1[0];
        } else if (objects1.length == 1 && types[0] == File.class) {
            int index = random.nextInt(files1.size());
            objects1[0] = files1.get(index);
            objects2[0] = files2.get(index);
        } else {
            throw new RuntimeException();
        }
    }

    private byte randomByte() {
        return (byte) random.nextInt();
    }

    private char randomChar() {
        return (char) random.nextInt();
    }

    private int randomInt(Visitor visitor) {
        if (random.nextInt(256) != 0) {
            return visitor.getCacheSize();
        }
        switch (random.nextInt(5)) {
            case 0:
                return random.nextInt(4) + 1;
            case 1:
                return random.nextInt(16) + 1;
            case 2:
                return random.nextInt(256) + 1;
            case 3:
                return random.nextInt(65536) + 1;
            case 4:
                if (random.nextInt(256) != 0) {
                    return visitor.getCacheSize();
                } else {
                    return random.nextInt(1200000000) + 1;
                }
        }
        throw new RuntimeException();
    }

    private long randomLong(Visitor visitor, Method method) {
        switch (method.getName()) {
            case "forward":
            case "next":
                if (random.nextBoolean()) {
                    return visitor.getLength() - visitor.getPosition();
                } else {
                    return random.nextBoolean() ? next() : -next();
                }
            case "forwardChar":
            case "nextChar":
                if (random.nextBoolean()) {
                    return (visitor.getLength() - visitor.getPosition()) / 2;
                } else {
                    return random.nextBoolean() ? next() : -next();
                }
            case "back":
            case "previous":
                if (random.nextBoolean()) {
                    return visitor.getPosition() - visitor.getLength();
                } else {
                    return random.nextBoolean() ? next() : -next();
                }
            case "backChar":
            case "previousChar":
                if (random.nextBoolean()) {
                    return (visitor.getPosition() - visitor.getLength()) / 2;
                } else {
                    return random.nextBoolean() ? next() : -next();
                }
            case "eof":
                switch (random.nextInt(4)) {
                    case 0:
                        return visitor.getLength() - visitor.getPosition();
                    case 1:
                        return -visitor.getPosition();
                    case 2:
                        return -visitor.getPosition() - 1;
                    case 3:
                        return next();
                }
            case "eofChar":
                switch (random.nextInt(4)) {
                    case 0:
                        return (visitor.getLength() - visitor.getPosition()) / 2;
                    case 1:
                        return -visitor.getPosition() / 2;
                    case 2:
                        return (-visitor.getPosition() - 1) / 2;
                    case 3:
                        return next();
                }
        }
        throw new RuntimeException();
    }

    private long next() {
        switch (random.nextInt(6)) {
            case 0:
                return random.nextInt(4) + 1;
            case 1:
                return random.nextInt(16) + 1;
            case 2:
                return random.nextInt(256) + 1;
            case 3:
                return random.nextInt(65536) + 1;
            case 4:
                return random.nextInt(2147483647) + 1;
            case 5:
                return 4L * random.nextInt(2147483647) + 1;
        }
        throw new RuntimeException();
    }

    private byte[] randomBytes(Visitor visitor) {
        int size;
        int value = random.nextInt(65536);
        if (value < 16) {
            size = random.nextInt(16777216) + 1;
        } else if (value < 256) {
            size = random.nextInt(65536) + 1;
        } else {
            size = random.nextInt(256) + 1;
        }
        byte[] bytes = new byte[size];
        random.nextBytes(bytes);
        try {
            visitor.setCacheSize(Math.max(visitor.getCacheSize(), size / 256));
            for (byte b : bytes) {
                visitor.write(b);
                visitor.forward();
            }
            visitor.back(size);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return bytes;
    }

}
