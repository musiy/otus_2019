package com.musiy.ioc.aop;

import java.util.Map;

/**
 * Каждый AOP модуль предоставляет свои анотации и свои обработчики к ним.
 * Для того что бы сообщить фреймворку IoC об обработчиках в каждом модуле
 * необходимо реализовать класс наследующий этот.
 */
public interface AopModuleDescription {

    /**
     * Возвращает обработчики анотаций
     */
    Map<Class<?>, AopMethodInterceptor> getInterceptors();
}
