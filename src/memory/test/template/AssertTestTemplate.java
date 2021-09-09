package memory.test.template;

import memory.test.template.chooser.MethodChooser;
import memory.test.template.filter.MethodFilter;
import memory.test.template.generator.ParamGenerator;
import memory.test.template.judge.TestJudge;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AssertTestTemplate<T> {

    private T object;

    private final MethodChooser methodChooser;

    private final ParamGenerator paramGenerator;

    private final TestJudge testJudge;

    private final List<Method> methods;

    public AssertTestTemplate(T object,
                               MethodFilter methodFilter, MethodChooser methodChooser,
                               ParamGenerator paramGenerator, TestJudge testJudge) {
        this.object = object;
        this.methodChooser = methodChooser;
        this.paramGenerator = paramGenerator;
        this.testJudge = testJudge;
        this.methods = getMethods(this.object, methodFilter);
    }

    public void test() throws InvocationTargetException, IllegalAccessException {
        Method method = methodChooser.choose(methods);
        Object[] params = paramGenerator.generate(method);
        Object result = null;
        Throwable throwable = null;
        try {
            result = method.invoke(object, params);
        } catch (InvocationTargetException e) {
            throwable = e.getTargetException();
        }
        if (!testJudge.judge(method, result, throwable)) {
            testJudge.onFail(method, result, throwable);
        }
    }

    public void setObject(T object) {
        this.object = object;
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

}
