package annotationproc.testclasses;

import annotationproc.annotation.After;
import annotationproc.annotation.Before;

public class TestClass5 {
    @Before
    public void method1() {
        // nop
    }

    @Before
    public void method2() {
        // nop
    }

    @After
    public void method3() {
        throw new RuntimeException();
    }

    @After
    public void method4() {
        // nop
    }
}