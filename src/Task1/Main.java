package Task1;

public class Main {
    private boolean flagT0 = false;
    private boolean flagT1 = true;
    private boolean flagT2 = true;
    public static void main(String[] args) {
        new Main().homeworkDemo();
    }
    public void homeworkDemo() {
        Thread t0 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                printA();
            }
        });
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                printB();
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                printC();
            }
        });
        t0.start();
        t1.start();
        t2.start();
    }
    public synchronized void printA() {
        while (flagT0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        flagT0 = true;
        System.out.print("A");
        flagT1 = false;
        notifyAll();
    }
    public synchronized void printB() {
        while (flagT1) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        flagT1 = true;
        System.out.print("B");
        flagT2 = false;
        notifyAll();
    }
    public synchronized void printC() {
        while (flagT2) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        flagT2 = true;
        System.out.print("C");
        flagT0 = false;
        notifyAll();
    }
}
