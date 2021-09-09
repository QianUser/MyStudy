package memory.test.template.matcher;

import java.lang.reflect.Method;
import java.util.Arrays;

public class DefaultMethodMatcher implements MethodMatcher {

    @Override
    public boolean match(Method method1, Method method2) {
        return method1.getName().equals(method2.getName())
                && Arrays.equals(method1.getParameterTypes(), method2.getParameterTypes())
                && method1.getReturnType().equals(method2.getReturnType());
    }

}
