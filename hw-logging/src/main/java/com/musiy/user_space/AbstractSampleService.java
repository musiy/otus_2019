package com.musiy.user_space;

import com.musiy.aop_log.internal.Log;

public abstract class AbstractSampleService implements ISampleService {

    @Log
    @Override
    public Long test2(int a, String b, Point point) {
        System.out.println("== BEGIN original method SampleService::test2 ==");
        try {
            SampleServiceHelper.printValues(a, b, point);
            return (long) a;
        } finally {
            System.out.println("== END original method SampleService::test2 ==");
        }
    }
}
