package ru.javawebinar.basejava;

public class MainDeadlock {
    final String str1 = "Object1";
    final String str2 = "Object2";

    public static void main(String[] args) throws InterruptedException {
        MainDeadlock deadlock = new MainDeadlock();
        Thread thread1 = deadlock.createThread(deadlock.str1, deadlock.str2);
        Thread thread2 = deadlock.createThread(deadlock.str2, deadlock.str1);
        System.out.println("Start Deadlock");
        thread1.start();
        thread2.start();
    }

    private Thread createThread(String str1, String str2) {
        return new Thread(() -> {
            try {
                lockObject(str1, str2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void lockObject(String str1, String str2) throws InterruptedException {
        try {
            synchronized (str1) {
                System.out.println(Thread.currentThread() + " - " + str1);
                Thread.sleep(100);
                synchronized (str2) {
                    System.out.println(Thread.currentThread() + " - " + str2);
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}