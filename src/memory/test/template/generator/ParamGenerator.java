package memory.test.template.generator;

import memory.test.util.Pair;

import java.lang.reflect.Method;

public abstract class ParamGenerator {

    public final Object[] generate(Method method) {
        Object[] objects = new Object[method.getParameterCount()];
        generateParam(method, objects);
        return objects;
    }

    public final Pair<Object[], Object[]> generatePair(Pair<Method, Method> methodPair) {
        Object[] objects1 = new Object[methodPair.getFirst().getParameterCount()];
        Object[] objects2 = new Object[methodPair.getSecond().getParameterCount()];
        generateParamsPair(methodPair, objects1, objects2);
        return new Pair<>(objects1, objects2);
    }

    protected abstract void generateParam(Method method, Object[] objects);

    protected abstract void generateParamsPair(Pair<Method, Method> methodPair, Object[] objects1, Object[] objects2);

}
