package annotationproc.testclasses;

import annotationproc.annotation.After;
import annotationproc.annotation.Before;
import annotationproc.annotation.Test;

import java.io.IOException;

public class TestClass2 {
    @Before
    public void method1() {
        throw new RuntimeException();
    }

    @Before
    public void method2() {
        // nop
    }

    @Test
    public void method3() {
        // nop
    }

    @Test
    public void method4() {
        // nop
    }

    @After
    public void method5() {
        //nop
    }

    @After
    public void method6() {
        throw new RuntimeException();
    }
}