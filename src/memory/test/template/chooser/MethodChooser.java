package memory.test.template.chooser;

import memory.test.util.Pair;

import java.lang.reflect.Method;
import java.util.List;

public abstract class MethodChooser {

    public final Method choose(List<Method> methods) {
        return methods.get(chooseIndex(methods));
    }

    public final Pair<Method, Method> choosePair(List<Pair<Method, Method>> methodPairs) {
        return methodPairs.get(choosePairIndex(methodPairs));
    }

    protected abstract int chooseIndex(List<Method> methods);

    protected abstract int choosePairIndex(List<Pair<Method, Method>> methodPairs);

}
