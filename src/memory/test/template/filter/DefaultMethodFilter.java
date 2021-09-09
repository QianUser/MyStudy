package memory.test.template.filter;

import java.lang.reflect.Method;

public class DefaultMethodFilter implements MethodFilter {

    @Override
    public boolean accept(Method method) {
        return true;
    }

}
