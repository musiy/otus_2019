package com.musiy.aop_log;

import com.musiy.aop_log.internal.Log;
import com.musiy.aop_log.internal.LogHandler;
import com.musiy.ioc.aop.AopMethodInterceptor;
import com.musiy.ioc.aop.AopModuleDescription;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Описывает AOP модуль для логирования
 */
public class LogAopDescription implements AopModuleDescription {

    private Map<Class<?>, AopMethodInterceptor> interceptors = new HashMap<>();

    public LogAopDescription() {
        interceptors.put(Log.class, new LogHandler());
    }

    @Override
    public Map<Class<?>, AopMethodInterceptor> getInterceptors() {
        return Collections.unmodifiableMap(interceptors);
    }
}
