package com.musiy.test_classes;

import com.musiy.framework.anno.*;

/**
 * Конкретный класс с тестом
 */
public class SampleTestClass extends AbstractTestClass {

    @BeforeClass
    public static void setupClass() {
        System.out.println("before class [SampleTestClass]");
    }

    @AfterClass
    public static void teardownClass() {
        System.out.println("after class [SampleTestClass]");
    }

    @Before
    public void beforeEachTestAlpha() {
        System.out.println("before test [SampleTestClass]");
    }

    @After
    public void afterEachTestBeta() {
        System.out.println("after test [SampleTestClass]");
    }

    @Test
    public void testSimple() {
        System.out.println("test simple");
    }

    @Test(desc = "throws RuntimeException should fail test")
    public void testThrows() {
        System.out.println("test throws");
        throw new RuntimeException("Пример исключения");
    }

    @Test
    public void testAdvanced() {
        System.out.println("test advanced");
    }
}
