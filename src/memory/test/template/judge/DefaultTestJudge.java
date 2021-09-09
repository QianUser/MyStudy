package memory.test.template.judge;

import java.lang.reflect.Method;

public class DefaultTestJudge implements TestJudge {

    @Override
    public boolean judge(Method method, Object result, Throwable throwable) {
        return throwable == null;
    }

    @Override
    public boolean judge(Method method1, Method method2, Object result1, Object result2, Throwable throwable1, Throwable throwable2) {
        if (throwable1 == null && throwable2 == null) {
            return result1.equals(result2);
        } else if (throwable1 != null && throwable2 != null) {
            return throwable1.getClass().equals(throwable2.getClass());
        } else {
            return false;
        }
    }

}
