package me.olivejua.learningjava.thread;

public class ShareThreadPrac {
    public static void main(String[] args) {
        ShareThread shareThread = new ShareThread();
        Thread thread1 = new Thread(() -> {
            shareThread.setValue(100);
        });

        Thread thread2 = new Thread(() -> {
            shareThread.setValue(10);
        });

        thread1.setName("스레드 1");
        thread2.setName("스레드 2");
        thread1.start();
        thread2.start();
    }
}
