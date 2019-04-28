package com.musiy.framework;

public interface TestRunnerInterface {

    /**
     * Запускальщик теста по классу
     * 1. @BeforeClass до всех тестов
     * 2. @BeforeTest перед каждым тестом
     * 3. @Test
     * 4. @AfterTest  после каждого теста
     * 5. @AfterClass после всех тестов
     * @param clazz ссылка на описание тестируемого класса
     */
    void run(Class<?> clazz);
}
