package com.musiy;

import com.musiy.framework.DefaultTestRunner;
import com.musiy.framework.TestRunnerInterface;
import com.musiy.test_classes.SampleTestClass;

public class App {

    public static void main(String[] args) {
        TestRunnerInterface testRunner = new DefaultTestRunner();
        testRunner.run(SampleTestClass.class);
    }
}
