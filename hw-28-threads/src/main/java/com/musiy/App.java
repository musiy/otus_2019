package com.musiy;

public class App {

    public static void main(String[] args) throws InterruptedException {
        var app = new App();
        Thread thread1 = new Thread(app::run);
        Thread thread2 = new Thread(app::run);
        thread1.start();
        thread2.start();
        thread1.join();
    }

    private static String lastThreadName;

    private void run() {
        String currentThreadName = Thread.currentThread().getName();
        int i = 0;
        boolean inc = true;
        while (true) {
            synchronized (this) {
                while (currentThreadName.equals(lastThreadName)) {
                    wt();
                }
                i = inc ? i + 1 : i - 1;
                System.out.println(currentThreadName + ": " + i);
                if (i == 10) {
                    inc = false;
                }
                if (i == 1) {
                    inc = true;
                }
                lastThreadName = currentThreadName;
                sleep();
                notify();
            }
        }
    }

    private void wt() {
        try {
            wait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void sleep() {
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
