package memory.test.template.judge;

import java.lang.reflect.Method;

public interface TestJudge {

    default void onFail(Method method, Object result, Throwable throwable) {
        throw new AssertionError(
                "\n[method: " + method.getName() + "]\n" +
                        "[throwable: " + throwable + "]\n" +
                        "[result: " + result + "]\n"
        );
    }

    default void onFail(Method method1, Method method2, Object result1, Object result2, Throwable throwable1, Throwable throwable2) {
        throw new AssertionError(
                "\n[method1: " + method1.getName() + "]\n" +
                        "[method2: " + method2.getName() + "]\n" +
                        "[throwable1: " + throwable1 + "]\n" +
                        "[throwable2: " + throwable2 + "]\n" +
                        "[result1: " + result1 + "]\n" +
                        "[result2: " + result2 + "]\n"
        );
    }

    boolean judge(Method method, Object result, Throwable throwable);

    boolean judge(Method method1, Method method2, Object result1, Object result2, Throwable throwable1, Throwable throwable2);

}
