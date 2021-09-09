package memory.test.template.matcher;

import java.lang.reflect.Method;

public interface MethodMatcher {

    boolean match(Method method1, Method method2);

}
