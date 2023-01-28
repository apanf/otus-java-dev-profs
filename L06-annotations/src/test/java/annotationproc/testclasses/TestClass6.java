package annotationproc.testclasses;

import annotationproc.annotation.Before;
import annotationproc.annotation.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class TestClass6 {
    @BeforeEach
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

    @AfterEach
    public void method4() {
        throw new RuntimeException();
    }

    @AfterEach
    public void method5() {
        throw new RuntimeException();
    }
}