package annotationproc.testclasses;

import annotationproc.annotation.After;
import annotationproc.annotation.Before;
import annotationproc.annotation.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class TestClass7 {
    @BeforeEach
    public void method1() {
        throw new RuntimeException();
    }

    @Test
    public void method2() {
        // nop
    }

    @Test
    public void method3() {
        // nop
    }

    @AfterEach
    public void method4() {
        throw new RuntimeException();
    }

    @After
    public void method5() {
        // nop
    }

    @After
    public void method6() {
        // nop
    }
}