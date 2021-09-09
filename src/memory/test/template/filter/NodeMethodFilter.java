package memory.test.template.filter;

import memory.node.Node;

import java.lang.reflect.Method;

public class NodeMethodFilter implements MethodFilter {

    @Override
    public boolean accept(Method method) {
        return method.getParameterCount() == 1 && method.getParameterTypes()[0] == Node.class;
    }

}
