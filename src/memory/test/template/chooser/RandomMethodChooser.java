package memory.test.template.chooser;

import memory.test.util.Pair;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;

public class RandomMethodChooser extends MethodChooser {

    private final Random random = new Random();
    private int count = 0;
    private int[] probabilities = null;

    @Override
    protected int chooseIndex(List<Method> methods) {
        if (count == 0) {
            probabilities = new int[methods.size()];
            for (int i = 0; i < probabilities.length; ++i) {
                probabilities[i] = random.nextInt(101);
            }
            for (int i = 1; i < probabilities.length; ++i) {
                probabilities[i] += probabilities[i - 1];
            }
            if (probabilities[probabilities.length - 1] == 0) {
                return this.chooseIndex(methods);
            }
            count = random.nextInt(65536) + 1;
        }
        --count;
        int value = random.nextInt(probabilities[probabilities.length - 1] + 1);
        for (int i = 0; i < probabilities.length; ++i) {
            if (probabilities[i] >= value) {
                return i;
            }
        }
        throw new RuntimeException();
    }

    @Override
    public int choosePairIndex(List<Pair<Method, Method>> methodPairs) {
        if (count == 0) {
            probabilities = new int[methodPairs.size()];
            for (int i = 0; i < probabilities.length; ++i) {
                probabilities[i] = random.nextInt(101);
            }
            for (int i = 1; i < probabilities.length; ++i) {
                probabilities[i] += probabilities[i - 1];
            }
            if (probabilities[probabilities.length - 1] == 0) {
                return this.choosePairIndex(methodPairs);
            }
            count = random.nextInt(65536) + 1;
        }
        --count;
        int value = random.nextInt(probabilities[probabilities.length - 1] + 1);
        for (int i = 0; i < probabilities.length; ++i) {
            if (probabilities[i] >= value) {
                return i;
            }
        }
        throw new RuntimeException();
    }

}
