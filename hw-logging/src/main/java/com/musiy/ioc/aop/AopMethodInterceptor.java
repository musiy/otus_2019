package com.musiy.ioc.aop;

import java.lang.reflect.Method;

/**
 * Общий интерфейс для всех обрабочиков анотаций
 */
@FunctionalInterface
public interface AopMethodInterceptor {

    /**
     * Обработчик вызова анотации
     *
     * @param proxy  объект прокси
     * @param origin оригинальный объект
     * @param method описание вызываемого метода
     * @param args   аргументы
     */
    void invoke(Object proxy, Object origin, Method method, Object[] args);
}
