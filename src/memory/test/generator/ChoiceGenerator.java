package memory.test.generator;

import memory.util.Pair;

import java.lang.reflect.Method;
import java.util.List;

public interface ChoiceGenerator {

    Pair<Method, Method> generate(List<Pair<Method, Method>> pairs);

}
