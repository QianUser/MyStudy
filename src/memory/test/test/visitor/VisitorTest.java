package memory.test.test.visitor;

import memory.test.template.AlignedTestTemplate;
import memory.test.template.chooser.RandomMethodChooser;
import memory.test.template.filter.DefaultMethodFilter;
import memory.test.template.generator.VisitorParamGenerator;
import memory.test.template.judge.DefaultTestJudge;
import memory.test.template.matcher.DefaultMethodMatcher;
import memory.visitor.Visitor;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static memory.test.util.Utils.progress;

public class VisitorTest {

    private final Visitor visitor;

    private final SlowVisitor slowVisitor;

    private final AlignedTestTemplate<Visitor, SlowVisitor> alignedTestTemplate;

    public VisitorTest(List<File> files1, List<File> files2) throws IOException {
        this.visitor = new Visitor();
        this.slowVisitor = new SlowVisitor();
        this.alignedTestTemplate = new AlignedTestTemplate<>(
                this.visitor, this.slowVisitor,
                new DefaultMethodFilter(), new DefaultMethodMatcher(), new RandomMethodChooser(),
                new VisitorParamGenerator(visitor, files1, files2), new DefaultTestJudge());
    }

    public void test() throws InvocationTargetException, IllegalAccessException {
        alignedTestTemplate.test();
    }

    public void after() throws Exception {
        visitor.close();
        slowVisitor.close();
    }

    public static void main(String[] args) throws Exception {
        List<File> files1 = new ArrayList<>();
        List<File> files2 = new ArrayList<>();
        for (int i = 1; i <= 16; ++i) {
            files1.add(new File("generate\\test_visitor_" + i));
            files2.add(new File("generate\\test_slow_visitor_" + i));
        }
        VisitorTest visitorTest = new VisitorTest(files1, files2);
        long times = 1024 * 1024 * 1024;
        for (long i = 1; i <= times; ++i) {
                System.out.print("\r" + i + ": " + progress(i, times));
                visitorTest.test();
        }
        visitorTest.after();
    }

}
