package memory.test.util;

import java.util.Objects;

public class Pair<T, U> {

    private T first;

    private U second;

    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public U getSecond() {
        return second;
    }

    public void setSecond(U second) {
        this.second = second;
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Pair
                && Objects.equals(first, ((Pair<?, ?>) obj).first)
                && Objects.equals(second, ((Pair<?, ?>) obj).second);
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }

}
