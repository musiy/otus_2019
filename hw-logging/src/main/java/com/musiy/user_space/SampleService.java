package com.musiy.user_space;

import com.musiy.aop_log.internal.Log;

public class SampleService extends AbstractSampleService {

    @Override
    public Long test1(int a, String b, Point point) {
        System.out.println("== BEGIN original method SampleService::test1 ==");
        try {
            SampleServiceHelper.printValues(a, b, point);
            return (long) a;
        } finally {
            System.out.println("== END original method SampleService::test1 ==");
        }
    }

    @Log
    @Override
    public Integer test3(int a, String b, Point point) {
        System.out.println("== BEGIN original method SampleService::test3 ==");
        try {
            SampleServiceHelper.printValues(a, b, point);
            return a;
        } finally {
            System.out.println("== END original method SampleService::test3 ==");
        }
    }
}
