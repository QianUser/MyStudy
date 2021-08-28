package memory.test;

import memory.test.generator.ChoiceGenerator;
import memory.test.generator.ValueGenerator;
import memory.util.Pair;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlignedTest<T, U> {

    private final TestTemplate<T, U> testTemplate;

    private final List<Pair<Method, Method>> pairs;

    public AlignedTest(T t, U u) {
        this(t, u, new Class[0], new Class[0]);
    }

    public AlignedTest(T t, U u, Class<?>[] cs1, Class<?>[] cs2) {
        this.testTemplate = new TestTemplate<>(t, u);
        this.pairs = new ArrayList<>();
        List<Method> tMs = getMethods(t, cs1);
        List<Method> uMs = getMethods(u, cs2);
        for (Method tM : tMs) {
            Method m = findAligned(tM, uMs);
            this.pairs.add(new Pair<>(tM, m));
            if (m != null) {
                uMs.remove(m);
            }
        }
        for (Method uM : uMs) {
            this.pairs.add(new Pair<>(null, uM));
        }

    }

    public Pair<Method, Method> generate(ChoiceGenerator choiceGenerator) {
        return choiceGenerator.generate(pairs);
    }

    public void test(Pair<Method, Method> pair, ValueGenerator<?>... valueGenerators) throws InvocationTargetException, IllegalAccessException {
        Object[] objects = new Object[valueGenerators.length];
        if (pair.first != null) {
            for (int i = 0; i < valueGenerators.length; ++i) {
                objects[i] = valueGenerators[i].generate(pair.first);
            }
        } else {
            for (int i = 0; i < valueGenerators.length; ++i) {
                objects[i] = valueGenerators[i].generate(pair.second);
            }
        }
        testTemplate.test(pair.first, pair.second, objects);
    }

    private static <R> List<Method> getMethods(R r, Class<?>... cs) {
        List<Method> result = new ArrayList<>(Arrays.asList(r.getClass().getMethods()));
        if (cs == null) {
            return result;
        }
        for (Class<?> c : cs) {
            result.removeAll(Arrays.asList(c.getMethods()));
        }
        return result;
    }

    private static Method findAligned(Method m, List<Method> ms) {
        for (Method item : ms) {
            if (m.getName().equals(item.getName())
                    && Arrays.equals(m.getParameterTypes(), item.getParameterTypes())
                    && m.getReturnType().equals(item.getReturnType())) {
                return item;
            }
        }
        return null;
    }

}
