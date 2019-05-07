package com.musiy.ioc;

import com.musiy.ioc.aop.AopMethodInterceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * Класс для прокси объектов контейнера IoC
 *
 * @param <T> тип подлежащего объекта
 */
public class ProxyInvocationHandler<T> implements InvocationHandler {

    /**
     * Оригинальный объект
     */
    private T origin;

    /**
     * Обработчики
     */
    private Map<MethodDesc, Set<AopMethodInterceptor>> aopMethodInterceptors;

    public ProxyInvocationHandler(T origin, Map<MethodDesc, Set<AopMethodInterceptor>> aopMethodInterceptors) {
        this.origin = origin;
        this.aopMethodInterceptors = aopMethodInterceptors;
    }

    /**
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Set<AopMethodInterceptor> interceptors = aopMethodInterceptors.get(MethodDesc.fromMethod(method));
        if (interceptors != null) {
            interceptors.forEach(interceptor -> interceptor.invoke(proxy, origin, method, args));
        }
        return method.invoke(origin, args);
    }
}





















