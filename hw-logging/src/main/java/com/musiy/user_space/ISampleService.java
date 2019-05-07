package com.musiy.user_space;

import com.musiy.aop_log.internal.Log;

public interface ISampleService {

    @Log
    Number test1(int a, String b, Point point);

    Number test2(int a, String b, Point point);

    Number test3(int a, String b, Point point);
}
