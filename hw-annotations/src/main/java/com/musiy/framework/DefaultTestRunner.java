package com.musiy.framework;

import com.musiy.framework.anno.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Простой запускальшик тестов
 */
public class DefaultTestRunner implements TestRunnerInterface {

    @Override
    public void run(Class<?> target) {
        try {
            System.out.println(String.format("== START TEST CLASS : [%s] ==", target.getName()));
            runTestClass(target);
        } catch (Exception e) {
            throw new RuntimeException("Error while executing test on class", e);
        } finally {
            System.out.println(String.format("== END TEST CLASS : [%s] ==", target.getName()));
        }
    }

    /**
     * Запуск тестов из переданного класса
     *  1. если исключение в before class:
     *     - оставшиеся before class не выполняем
     *     - тесты не выполняем
     *     - переходим сразу к after class
     *     - исключение не выводим сразу
     *  2. если исключение в after class
     *     - безусловно выполняем оставшиеся after-class
     *     - если исключение возникло в after class - сохраняем первое исключение, остальные добавляем в suppressed
     *       при этом, если из before class уже установлено первое исключение - оно считается первым
     *  3. если исключение в before test, схема подобна before class:
     *     - оставшиеся before test не выполняем
     *     - тесты не выполняем
     *     - переходим сразу к after test
     *     - исключение не выводим сразу
     *  4. если исключение в after test, схема подобна after class:
     *     - безусловно выполняем оставшиеся after-test
     *     - если исключение возникло в after test - сохраняем первое исключение, остальные добавляем в suppressed
     *       при этом, если из before test уже установлено первое исключение - оно считается первым
     *  5. если исключение в test
     *     - сразу не выводим исключение, переходим к after class
     * @param target описание класса
     * @param <T>    тип класса
     */
    private <T> void runTestClass(Class<T> target) {

        var beforeClassMethods = filterMethodsByAnnotationClass(target, BeforeClass.class);
        ensureStatic(beforeClassMethods, BeforeClass.class);

        var afterClassMethods = filterMethodsByAnnotationClass(target, AfterClass.class);
        ensureStatic(afterClassMethods, AfterClass.class);

        var beforeMethods = filterMethodsByAnnotationClass(target, Before.class);
        ensureNonStatic(beforeMethods, Before.class);

        var afterMethods = filterMethodsByAnnotationClass(target, After.class);
        ensureNonStatic(afterMethods, After.class);

        var testMethods = filterMethodsByAnnotationClass(target, Test.class);
        ensureNonStatic(testMethods, Test.class);

        List<Exception> classExceptionList = new LinkedList<>();
        try {
            // Сперва выполняем все before class методы
            System.out.println("== EXECUTING BEFORE CLASS METHODS ==");
            invokeMethods(beforeClassMethods, classExceptionList, true, null);

            if (classExceptionList.isEmpty()) {
                performTests(target, beforeMethods, afterMethods, testMethods);
            }
            System.out.println("== EXECUTING AFTER CLASS METHODS ==");
            invokeMethods(afterClassMethods, classExceptionList, false, null);
        } finally {
            if (!classExceptionList.isEmpty()) {
                Exception e = composeSuppressedExceptions(classExceptionList);
                System.out.println("== ERROR ON executing class handlers");
                e.printStackTrace();
            }
        }
    }

    /**
     * Выполнить тесты в классе
     * @param target        описание класса
     * @param beforeMethods методы выполняемые перед тестом
     * @param afterMethods  методы выполняемые после теста
     * @param testMethods   тестовые методы
     * @param <T>           тип объекта
     */
    private <T> void performTests(Class<T> target,
                                  List<Method> beforeMethods,
                                  List<Method> afterMethods,
                                  List<Method> testMethods) {
        for (Method m : testMethods) {
            List<Exception> exceptionList = new LinkedList<>();
            try {
                try {
                    T object = newInstance(target);
                    System.out.println("== EXECUTING BEFORE TEST METHODS ==");
                    invokeMethods(beforeMethods, exceptionList, true, object);

                    if (exceptionList.isEmpty()) {
                        System.out.println(String.format("== EXECUTING TEST %s ==", m.getName()));
                        try {
                            m.invoke(object);
                        } catch (Exception e) {
                            exceptionList.add((Exception) e.getCause());
                        }
                    }
                    System.out.println("== EXECUTING AFTER TEST METHODS ==");
                    invokeMethods(afterMethods, exceptionList, false, object);
                } catch (Exception e) {
                    exceptionList.add(e);
                }
            } finally {
                if (!exceptionList.isEmpty()) {
                    Exception e = composeSuppressedExceptions(exceptionList);
                    String message = String.format("== ERROR ON TEST %s", m.getName());
                    Test test = m.getAnnotation(Test.class);
                    if (!test.desc().isEmpty()) {
                        message += " (" + test.desc() + ")";
                    }
                    System.out.println(message);
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Составляет из списка исключений одно.
     * Первое сообщение считается основным, остальные добавляются ему как suppressed.
     * @param exceptionList список исключений
     * @return исключение
     */
    private Exception composeSuppressedExceptions(List<Exception> exceptionList) {
        var it = exceptionList.iterator();
        Exception primary = it.next();
        while (it.hasNext()) {
            primary.addSuppressed(it.next());
        }
        return primary;
    }

    /**
     * Вызывает методы из переданного списка методов
     * @param methodList    список методов для вызова
     * @param exceptionList список исключений для добавления (при возникновении)
     * @param stopOnError   прекратить вызов методов в списке при возникновении ошибки, иначе false
     * @param o             объект класса метод которого вызывается, для статических методов передавать null
     */
    private void invokeMethods(List<Method> methodList,
                               List<Exception> exceptionList,
                               boolean stopOnError,
                               Object o) {
        for (Method method : methodList) {
            try {
                method.invoke(o);
            } catch (Exception e) {
                exceptionList.add(e);
                if (stopOnError) {
                    break;
                }
            }
        }
    }

    /**
     * Проверяет, что все переданные методы статичные
     * @param beforeClassMethods список методов
     * @param annotation         используется для вывода сообщения об ошибке, если метод не статичный
     */
    private void ensureStatic(List<Method> beforeClassMethods, Class<? extends Annotation> annotation) {
        List<String> nonStaticMethods = beforeClassMethods.stream()
                .filter(m -> !Modifier.isStatic(m.getModifiers()))
                .map(Method::getName)
                .collect(Collectors.toList());
        if (!nonStaticMethods.isEmpty()) {
            throw new RuntimeException(
                    String.format("Annotation [%s] should be declared only on static methods, but found %s",
                            annotation.getSimpleName(), nonStaticMethods));
        }
    }

    /**
     * Проверяет, что все переданные методы не статичные
     * @param beforeClassMethods список методов
     * @param annotation         используется для вывода сообщения об ошибке, если метод статичный
     */
    private void ensureNonStatic(List<Method> beforeClassMethods, Class<? extends Annotation> annotation) {
        List<String> nonStaticMethods = beforeClassMethods.stream()
                .filter(m -> Modifier.isStatic(m.getModifiers()))
                .map(Method::getName)
                .collect(Collectors.toList());
        if (!nonStaticMethods.isEmpty()) {
            throw new RuntimeException(
                    String.format("Annotation [%s] should be declared only on non static methods, but found %s",
                            annotation.getSimpleName(), nonStaticMethods));
        }
    }

    /**
     * Создать объект с помощью констуктора по умолчанию
     * @param target описание класса
     * @param <T>    тип объета
     * @return объект
     */
    private <T> T newInstance(Class<T> target) {
        @SuppressWarnings(value = "unchecked")
        Constructor<T>[] constructors = (Constructor<T>[]) target.getDeclaredConstructors();
        Constructor<T> defaultConstructor = Arrays.stream(constructors)
                .filter(c -> c.getParameterTypes().length == 0)
                .findFirst()
                .orElseThrow(() -> new NoSuchMethodError("Default constructor absent"));
        try {
            return defaultConstructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Error while instantiating object", e);
        }
    }

    /**
     * Найти в классе методы отмеченные анотацией
     * @param target     описание класса
     * @param annotation анотация
     * @return список методов отмеченных анотацией
     */
    private List<Method> filterMethodsByAnnotationClass(Class<?> target,
                                                        Class<? extends Annotation> annotation) {
        return Arrays.stream(target.getMethods())
                .filter(m -> m.getDeclaredAnnotation(annotation) != null)
                .collect(Collectors.toList());
    }
}
