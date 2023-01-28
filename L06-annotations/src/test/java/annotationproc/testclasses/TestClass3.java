package annotationproc.testclasses;

import annotationproc.annotation.After;
import annotationproc.annotation.Before;
import annotationproc.annotation.Test;

public class TestClass3 {
    @Before
    public void method1() {
        // nop
    }

    @Before
    public void method2() {
        throw new RuntimeException();
    }

    @Test
    public void method3() {
        // nop
    }

    @Test
    public void method4() {
        throw new RuntimeException();
    }

    @After
    public void method5() {
        throw new RuntimeException();
    }

    @After
    public void method6() {
        throw new RuntimeException();
    }
}