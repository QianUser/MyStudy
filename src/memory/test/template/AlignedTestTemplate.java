package memory.test.template;

import memory.test.template.chooser.MethodChooser;
import memory.test.template.filter.MethodFilter;
import memory.test.template.generator.ParamGenerator;
import memory.test.template.judge.TestJudge;
import memory.test.template.matcher.MethodMatcher;
import memory.test.util.Pair;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlignedTestTemplate<T, U> {

    private T object1;

    private U object2;

    private final MethodChooser methodChooser;

    private final ParamGenerator paramGenerator;

    private final TestJudge testJudge;

    private final List<Pair<Method, Method>> pairs;

    public AlignedTestTemplate(T object1, U object2,
                               MethodFilter methodFilter, MethodMatcher methodMatcher, MethodChooser methodChooser,
                               ParamGenerator paramGenerator, TestJudge testJudge) {
        this.object1 = object1;
        this.object2 = object2;
        this.methodChooser = methodChooser;
        this.paramGenerator = paramGenerator;
        this.testJudge = testJudge;
        this.pairs = new ArrayList<>();
        List<Method> methods1 = getMethods(this.object1, methodFilter);
        List<Method> methods2 = getMethods(this.object2, methodFilter);
        for (Method method1 : methods1) {
            Method method = findAligned(method1, methods2, methodMatcher);
            this.pairs.add(new Pair<>(method1, method));
            if (method != null) {
                methods2.remove(method);
            }
        }
        for (Method secondMethod : methods2) {
            this.pairs.add(new Pair<>(null, secondMethod));
        }
    }

    public void test() throws InvocationTargetException, IllegalAccessException {
        Pair<Method, Method> methodPair = methodChooser.choosePair(pairs);
        Pair<Object[], Object[]> paramsPair = paramGenerator.generatePair(methodPair);
        if (methodPair.getFirst() == null) {
            methodPair.getSecond().invoke(object1, paramsPair.getSecond());
        } else if (methodPair.getSecond() != null) {
            methodPair.getFirst().invoke(object2, paramsPair.getFirst());
        } else {
            Object result1 = null;
            Object result2 = null;
            Throwable throwable1 = null;
            Throwable throwable2 = null;
            try {
                result1 = methodPair.getFirst().invoke(object2, paramsPair.getFirst());
            } catch (InvocationTargetException e) {
                throwable1 = e.getTargetException();
            }
            try {
                result2 = methodPair.getFirst().invoke(object2, paramsPair.getSecond());
            }  catch (InvocationTargetException e) {
                throwable2 = e.getTargetException();
            }
            if (!testJudge.judge(methodPair.getFirst(), methodPair.getSecond(), result1, result2, throwable1, throwable2)) {
                testJudge.onFail(methodPair.getFirst(), methodPair.getSecond(), result1, result2, throwable1, throwable2);
            }
        }
    }

    public void setObject1(T object1) {
        this.object1 = object1;
    }

    public void setObject2(U object2) {
        this.object2 = object2;
    }

    private static <R> List<Method> getMethods(R r, MethodFilter filter) {
        List<Method> result = new ArrayList<>(Arrays.asList(r.getClass().getMethods()));
        result.removeIf(i -> {
            try {
                return !filter.accept(i);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return result;
    }

    private static Method findAligned(Method method, List<Method> methods, MethodMatcher methodMatcher) {
        for (Method item : methods) {
            if (methodMatcher.match(method, item)) {
                return item;
            }
        }
        return null;
    }

}
