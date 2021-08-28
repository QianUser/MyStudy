package memory.test.visitortest;

import memory.test.AlignedTest;
import memory.test.generator.ChoiceGenerator;
import memory.test.generator.ValueGenerator;
import memory.util.FileUtils;
import memory.util.Pair;
import memory.util.StringUtils;
import memory.visitor.Visitor;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

public class VisitorTest {

    private final SlowVisitor slowVisitor;

    private final Visitor visitor;

    private final AlignedTest<SlowVisitor, Visitor> alignedTest;

    private final ChoiceGenerator choiceGenerator;

    private final ValueGenerator<Long> longGenerator;

    private final ValueGenerator<Integer> intGenerator;

    private final ValueGenerator<Byte> byteGenerator;

    private final ValueGenerator<Character> charGenerator;

    private final ValueGenerator<byte[]> bytesGenerator;

    public VisitorTest(SlowVisitor slowVisitor, Visitor visitor,
                       ChoiceGenerator choiceGenerator,
                       ValueGenerator<Long> longGenerator,
                       ValueGenerator<Integer> intGenerator,
                       ValueGenerator<Byte> byteGenerator,
                       ValueGenerator<Character> charGenerator,
                       ValueGenerator<byte[]> bytesGenerator) {
        this.slowVisitor = slowVisitor;
        this.visitor = visitor;
        this.alignedTest = new AlignedTest<>(slowVisitor, visitor, new Class[]{Object.class}, new Class[]{Object.class});
        this.choiceGenerator = choiceGenerator;
        this.intGenerator = intGenerator;
        this.longGenerator = longGenerator;
        this.byteGenerator = byteGenerator;
        this.charGenerator = charGenerator;
        this.bytesGenerator = bytesGenerator;
    }

    public void test() throws InvocationTargetException, IllegalAccessException {
        Pair<Method, Method> pair = alignedTest.generate(choiceGenerator);
        Method method = pair.first == null ? pair.second : pair.first;
        if (method.getName().equals("close")) {
            test();
            return;
        }
        Class<?>[] types = method.getParameterTypes();
        if (types.length == 0) {
            alignedTest.test(pair);
        } else if (types.length == 1 && types[0] == long.class) {
            alignedTest.test(pair, longGenerator);
        } else if (types.length == 1 && types[0] == int.class) {
            alignedTest.test(pair, intGenerator);
        } else if (types.length == 1 && types[0] == byte.class) {
            alignedTest.test(pair, byteGenerator);
        } else if (types.length == 1 && types[0] == char.class) {
            alignedTest.test(pair, charGenerator);
        }  else if (types.length == 1 && types[0] == byte[].class) {
            alignedTest.test(pair, bytesGenerator);
        } else {
            throw new RuntimeException();
        }
    }

    public void after() throws IOException {
        visitor.close();
        slowVisitor.close();
    }

    public static void main(String[] args) throws IOException, InvocationTargetException, IllegalAccessException {
        String filename1 = "C:\\Users\\31654\\Downloads\\test\\1.hex";
        String filename2 = "C:\\Users\\31654\\Downloads\\test\\2.hex";
        Path path1 = Paths.get(filename1);
        Path path2 = Paths.get(filename2);
        if (Files.isDirectory(path1) || Files.isDirectory(path2)) {
            throw new RuntimeException();
        }
        long epoch = 16;
        long times = 16777216;
        long cycle = 1048576;
        for (int i = 1; i <= epoch; ++i) {

            if (i == epoch) {
                times *= 16;
                cycle *= 16;
            }

            if (Files.exists(path1)) {
                Files.delete(path1);
            }
            if (Files.exists(path2)) {
                Files.delete(path2);
            }

            SlowVisitor slowVisitor = new SlowVisitor(filename1, "rw");
            Visitor visitor = new Visitor(filename2, Visitor.Mode.RW);

            ChoiceGenerator choiceGenerator = new ChoiceGenerator() {
                private final Random random = new Random();
                private int count = 0;
                private int[] probabilities = null;
                @Override
                public Pair<Method, Method> generate(List<Pair<Method, Method>> pairs) {
                    if (count == 0) {
                        probabilities = new int[pairs.size()];
                        for (int i = 0; i < probabilities.length; ++i) {
                            probabilities[i] = random.nextInt(101);
                        }
                        for (int i = 1; i < probabilities.length; ++i) {
                            probabilities[i] += probabilities[i - 1];
                        }
                        if (probabilities[probabilities.length - 1] == 0) {
                            return generate(pairs);
                        }
                        count = random.nextInt(65536) + 1;
                    }
                    --count;
                    int value = random.nextInt(probabilities[probabilities.length - 1] + 1);
                    for (int i = 0; i < probabilities.length; ++i) {
                        if (probabilities[i] >= value) {
                            return pairs.get(i);
                        }
                    }
                    throw new RuntimeException();
                }
            };

            ValueGenerator<Long> longGenerator = new ValueGenerator<Long>() {
                private final Random random = new Random();
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
                @Override
                public Long generate(Method method) {
                    switch (method.getName()) {
                        case "forward":
                        case "next":
                            if (random.nextBoolean()) {
                                return visitor.length() - visitor.position();
                            } else {
                                return random.nextBoolean() ? next() : -next();
                            }
                        case "forwardChar":
                        case "nextChar":
                            if (random.nextBoolean()) {
                                return (visitor.length() - visitor.position()) / 2;
                            } else {
                                return random.nextBoolean() ? next() : -next();
                            }
                        case "back":
                        case "previous":
                            if (random.nextBoolean()) {
                                return visitor.position() - visitor.length();
                            } else {
                                return random.nextBoolean() ? next() : -next();
                            }
                        case "backChar":
                        case "previousChar":
                            if (random.nextBoolean()) {
                                return (visitor.position() - visitor.length()) / 2;
                            } else {
                                return random.nextBoolean() ? next() : -next();
                            }
                        case "eof":
                            switch (random.nextInt(4)) {
                                case 0:
                                    return visitor.length() - visitor.position();
                                case 1:
                                    return -visitor.position();
                                case 2:
                                    return -visitor.position() - 1;
                                case 3:
                                    return next();
                            }
                        case "eofChar":
                            switch (random.nextInt(4)) {
                                case 0:
                                    return (visitor.length() - visitor.position()) / 2;
                                case 1:
                                    return -visitor.position() / 2;
                                case 2:
                                    return (-visitor.position() - 1) / 2;
                                case 3:
                                    return next();
                            }
                    }
                    throw new RuntimeException();
                }
            };

            ValueGenerator<Integer> intGenerator = new ValueGenerator<Integer>() {
                private final Random random = new Random();
                @Override
                public Integer generate(Method method) {
                    if (random.nextInt(256) != 0) {
                        return visitor.cacheSize();
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
                                return visitor.cacheSize();
                            } else {
                                return random.nextInt(1200000000) + 1;
                            }
                    }
                    throw new RuntimeException();
                }
            };

            ValueGenerator<Byte> byteGenerator = new ValueGenerator<Byte>() {
                private final Random random = new Random();
                @Override
                public Byte generate(Method method) {
                    return (byte) random.nextInt();
                }
            };

            ValueGenerator<Character> charGenerator = new ValueGenerator<Character>() {
                private final Random random = new Random();
                @Override
                public Character generate(Method method) {
                    return (char) random.nextInt();
                }
            };

            ValueGenerator<byte[]> bytesGenerator = new ValueGenerator<byte[]>() {
                private final Random random = new Random();
                @Override
                public byte[] generate(Method method) {
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
                        visitor.cacheSize(Math.max(visitor.cacheSize(), size / 256));
                        for (byte b : bytes) {
                            visitor.write(b);
                            visitor.forward();
                        }
                        visitor.back(size);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return bytes;
                }
            };

            VisitorTest visitorTest = new VisitorTest(slowVisitor, visitor,
                    choiceGenerator,
                    longGenerator,
                    intGenerator,
                    byteGenerator,
                    charGenerator,
                    bytesGenerator);
            for (long j = 1; j <= times; ++j) {
                System.out.print("\r" + i + ": " + StringUtils.progress(j, times));
                if (j % cycle == 0) {
                    visitor.flush();
                    if (!FileUtils.isEqualContent(filename1, filename2)) {
                        throw new AssertionError();
                    }
                }
                visitorTest.test();
            }

            System.out.println();
            visitorTest.after();
        }
    }

}
