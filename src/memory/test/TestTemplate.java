package memory.test;

import java.io.EOFException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestTemplate<T, U> {

    private final T t;

    private final U u;

    public TestTemplate(T t, U u) {
        this.t = t;
        this.u = u;
    }

    public void execT(Method tM, Object... args) throws InvocationTargetException, IllegalAccessException {
        tM.invoke(t, args);
    }

    public void execU(Method uM, Object... args) throws InvocationTargetException, IllegalAccessException {
        uM.invoke(u, args);
    }

    public void test(Method tM, Method uM, Object... args) throws InvocationTargetException, IllegalAccessException {
        if (tM == null) {
            execU(uM, args);
            return;
        }
        if (uM == null) {
            execT(tM, args);
            return;
        }
        Throwable eT = null;
        Throwable eU = null;
        Object tO = null;
        Object uO = null;
        try {
            tO = tM.invoke(t, args);
        } catch (InvocationTargetException e) {
            eT = e.getTargetException();
            if (eT.getClass() == EOFException.class) {
                eT = new IOException();
            }  // 特定
        }
        try {
            uO = uM.invoke(u, args);
        } catch (InvocationTargetException e) {
            eU = e.getTargetException();
            if (eU.getClass() == EOFException.class) {
                eU = new IOException();
            }  // 特定
        }
        if (eT == null && eU == null) {
            assertEquals(tO, uO);
        } else if (eT != null && eU != null) {
            assertEquals(eT.getClass(), eU.getClass());
        } else {
            throw new AssertionError(
                    "\n[tM: " + tM.getName() + "]\n" +
                            "[uM: " + uM.getName() + "]\n" +
                            "[eT: " + eT + "]\n" +
                            "[eU: " + eU + "]\n" +
                            "[tO: " + tO + "]\n" +
                            "[uO: " + uO + "]\n"
            );
        }
    }

    private static void assertEquals(Object o1, Object o2) {
        if (o1 != o2 && !o1.equals(o2)) {
            throw new AssertionError(o1 + ", " + o2);
        }
    }

}
