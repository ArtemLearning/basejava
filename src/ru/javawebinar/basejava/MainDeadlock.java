package ru.javawebinar.basejava;

public class MainDeadlock {

    public static void main(String[] args) {
        Object obj1 = new Object();
        Object obj2 = new Object();

        Thread thread1 = new Thread(() -> {
            try {
                synchronized (obj1) {
                    System.out.println("Thread1 - Object1");
                    Thread.sleep(100);
                    synchronized (obj2) {
                        System.out.println("Thread1 - Object2");
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        Thread thread2 = new Thread(() -> {
            try {
                synchronized (obj2) {
                    System.out.println("Thread2 - Object2");
                    Thread.sleep(100);
                    synchronized (obj1) {
                        System.out.println("Thread2 - Object1");
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        System.out.println("Start Deadlock");
        thread1.start();
        thread2.start();
    }
}