package memory.test.generator;

import java.lang.reflect.Method;

public interface ValueGenerator<T> {

    T generate(Method method);

}
