package com.musiy.ioc;

import com.musiy.aop_log.LogAopDescription;
import com.musiy.ioc.aop.AopMethodInterceptor;
import com.musiy.ioc.aop.AopModuleDescription;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Прототип IoC контейнера. Создаёт класс по переданному интерфейсу.
 */
public class IoC {

    private static IoC INSTANCE = new IoC();

    private Map<Class<?>, AopMethodInterceptor> interceptorMap = new HashMap<>();

    private Map<Class<?>, Map<MethodDesc, Set<AopMethodInterceptor>>> cache = new ConcurrentHashMap<>();

    private IoC() {
        init();
    }

    private void init() {
        interceptorMap = getInterceptors();
    }

    /**
     * Создаёт прокси объект по интерфейсу.
     * Здесь хорошо бы не передавать описание конкретного класса и найти конкретный класс самостоятельно
     * с помошью библиотеки Reflections, но сделано так, что бы не усложнять ДЗ.
     *
     * @param clazzInterface описание интерфейса, объект которого требуется создать
     * @param clazzConcrete  описание класса, объект которого будет проксирован
     * @param <T>            тип интерфейса
     * @return прокси
     */
    public static <T> T fromConcreteClass(Class<T> clazzInterface, Class<? extends T> clazzConcrete) {
        return INSTANCE.fromConcreteClassInternal(clazzInterface, clazzConcrete);
    }

    private <T> T fromConcreteClassInternal(Class<T> clazzInterface, Class<? extends T> clazzConcrete) {
        T obj = createObjectByDefaultConstructor(clazzConcrete);
        InvocationHandler handler = new ProxyInvocationHandler<>(obj,
                Collections.unmodifiableMap(getInterceptorsOnMethods(clazzConcrete)));
        @SuppressWarnings("unchecked")
        T proxy = (T) Proxy.newProxyInstance(clazzConcrete.getClassLoader(),
                new Class<?>[]{clazzInterface},
                handler);
        return proxy;
    }

    private Map<MethodDesc, Set<AopMethodInterceptor>> getInterceptorsOnMethods(Class<?> clazz) {
        if (cache.get(clazz) != null) {
            return cache.get(clazz);
        }
        var result = new HashMap<MethodDesc, Set<AopMethodInterceptor>>();
        for (Method method : clazz.getMethods()) {
            MethodDesc methodDesc = MethodDesc.fromMethod(method);
            if (result.containsKey(methodDesc)) {
                continue;
            }
            for (Annotation annotation : method.getAnnotations()) {
                Class<? extends Annotation> type = annotation.annotationType();
                AopMethodInterceptor interceptor = interceptorMap.get(type);
                if (interceptor != null) {
                    if (!result.containsKey(methodDesc)) {
                        result.put(methodDesc, new HashSet<>());
                    }
                    result.get(methodDesc).add(interceptor);
                }
            }
        }
        cache.put(clazz, result);
        return result;
    }

    private Map<Class<?>, AopMethodInterceptor> getInterceptors() {
        var result = new HashMap<Class<?>, AopMethodInterceptor>();
        for (AopModuleDescription moduleDescription : getAopModulesDescriptions()) {
            result.putAll(moduleDescription.getInterceptors());
        }
        return result;
    }

    /**
     * Создаёт объект с помощью конструктора по умолчанию
     *
     * @param clazzConcrete класс, объект которого будет создан
     * @param <T>           тип класса
     * @return объект
     * @throws RuntimeException если объект не может быть создан
     */
    private <T> T createObjectByDefaultConstructor(Class<? extends T> clazzConcrete) {
        Constructor<T> defaultConstructor = findDefaultConstructor(clazzConcrete);
        try {
            return defaultConstructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Can't create class using default constructor", e);
        }
    }


    /**
     * Найти конструктор по умолчанию
     *
     * @param clazzConcrete класс в котором выполняется поиск конструктора
     * @param <T>           тип класса
     * @return конструктор по умолчанию
     * @throws RuntimeException если конструктор не найден или не может быть вызван
     */
    private <T> Constructor<T> findDefaultConstructor(Class<? extends T> clazzConcrete) {
        @SuppressWarnings("unchecked")
        Constructor<T>[] constructors = (Constructor<T>[]) clazzConcrete.getDeclaredConstructors();
        if (constructors.length == 0) {
            throw new RuntimeException("Concrete class should be passed to create object");
        }
        return Arrays.stream(constructors)
                .filter(c -> c.getParameterCount() == 0)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Default constructor not found"));
    }

    /**
     * Возвращает список описаний всех зарегистрированных AOP-модулей.
     * todo Каждый AOP может предоставлять свои аннотации и обработчики.
     * todo Для простоты считаем что у нас только один AOP - Logger AOP.
     * todo По хорошему AOPы нужно находить как то иначе
     */
    private static List<AopModuleDescription> getAopModulesDescriptions() {
        return Collections.singletonList(new LogAopDescription());
    }
}
