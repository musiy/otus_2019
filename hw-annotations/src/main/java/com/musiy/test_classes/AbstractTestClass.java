package com.musiy.test_classes;

import com.musiy.framework.anno.After;
import com.musiy.framework.anno.AfterClass;
import com.musiy.framework.anno.Before;
import com.musiy.framework.anno.BeforeClass;

/**
 * Абстрактный класс для демонстраци того, что методы с аннотациями
 * {@see BeforeClass}, {@see AfterClass}, {@see Before}, {@see After} будут вызованы в конкретном классе.
 */
public abstract class AbstractTestClass {

    @BeforeClass
    public static void setupAbstractTestClass() {
        System.out.println("before class [abstract]");
        //throw new RuntimeException("setupAbstractTestClass");
    }

    @AfterClass
    public static void teardownAbstractTestClass() {
        System.out.println("after class [abstract]");
    }

    @Before
    public void beforeEachTestBeta() {
        System.out.println("before test [abstract]");
    }

    @After
    public void afterEachTestAlpha() {
        System.out.println("after test [abstract]");
    }
}
