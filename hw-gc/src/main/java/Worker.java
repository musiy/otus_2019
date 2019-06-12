class Worker {

    /**
     * Число объектов, которые создаются единоразово
     */
    private static final int OBJECTS_PER_ALLOCATION = 1_000;

    /**
     * Задеркжа перед аллокацией объектов в милисекундах
     */
    private static final int DELAY = 10;

    private ObjectStorage storage;

    Worker(ObjectStorage storage) {
        this.storage = storage;
    }

    void run() throws InterruptedException {
        // коофициент - число удаляемых объектов к числу созданных на каждом шаге итерации
        final double coof = 2.0 / 3.0;
        while (true) {
            storage.createObjectsAddToList(OBJECTS_PER_ALLOCATION);
            storage.removeObjectsRandomly((int) (coof * OBJECTS_PER_ALLOCATION));
            Thread.sleep(DELAY);
        }
    }

}
