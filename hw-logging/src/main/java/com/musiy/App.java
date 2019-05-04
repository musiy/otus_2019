package com.musiy;

import com.musiy.ioc.IoC;
import com.musiy.user_space.ISampleService;
import com.musiy.user_space.Point;
import com.musiy.user_space.SampleService;

public class App {
    public static void main(String[] args) {
        ISampleService sampleService1 = IoC.fromConcreteClass(ISampleService.class, SampleService.class);
        sampleService1.test1(10, "Онегин добрый мой приятель", new Point(11, 13));
        sampleService1.test2(20, "Once upon a time", new Point(311, 413));
        sampleService1.test3(30, "Вергилий", new Point(1984, 2019));

        ISampleService sampleService2 = IoC.fromConcreteClass(ISampleService.class, SampleService.class);
        sampleService2.test1(10, "Онегин добрый мой приятель", new Point(11, 13));
        sampleService2.test2(20, "Once upon a time", new Point(311, 413));
        sampleService2.test3(30, "Вергилий", new Point(1984, 2019));
    }
}
