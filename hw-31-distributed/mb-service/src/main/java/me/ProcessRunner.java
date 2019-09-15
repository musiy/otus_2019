package me;

public interface ProcessRunner extends AutoCloseable {

    /** Запустить отдельный процесс с помощью команды */
    void start() throws Exception;

    /** Остановить процесс */
    void stop();

    /** Получить вывод процесса */
    String getOutput();
}
