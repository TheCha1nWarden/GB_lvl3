import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Car implements Runnable {
    private static int CARS_COUNT;
    private static CyclicBarrier cyclicBarrier;
    private CountDownLatch countDownLatchStart;
    private CountDownLatch countDownLatchFinish;
    private Semaphore semaphoreTunnel;
    private static boolean flagFinish = true;
    static {
        CARS_COUNT = 0;
    }
    private Race race;
    private int speed;
    private String name;
    public String getName() {
        return name;
    }
    public int getSpeed() {
        return speed;
    }

    public Semaphore getSemaphoreTunnel() {
        return semaphoreTunnel;
    }

    public Car(Race race, int speed, CountDownLatch countDownLatchStart, CountDownLatch countDownLatchFinish, Semaphore semaphoreTunnel) {
        this.race = race;
        this.speed = speed;
        this.countDownLatchStart = countDownLatchStart;
        this.countDownLatchFinish = countDownLatchFinish;
        this.semaphoreTunnel = semaphoreTunnel;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }
    @Override
    public void run() {
        try {
            if (cyclicBarrier == null) {
                cyclicBarrier = new CyclicBarrier(CARS_COUNT);
            }
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int)(Math.random() * 800));
            System.out.println(this.name + " готов");
            cyclicBarrier.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        countDownLatchStart.countDown();
        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }
        win(this);
        countDownLatchFinish.countDown();

    }
    public synchronized void win(Car car) {
        if (flagFinish) {
            flagFinish = false;
            System.out.println(car.getName() + " - WIN");
        }
    }
}

