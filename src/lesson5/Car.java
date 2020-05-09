package lesson5;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

public class Car implements Runnable {
    private static int CARS_COUNT;
    private static AtomicBoolean win = new AtomicBoolean(true);
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
    CyclicBarrier cb;
    CountDownLatch cdlStart,cdlStop;
    public Car(Race race, int speed, CyclicBarrier cb, CountDownLatch cdlStart,CountDownLatch cdlStop) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
        this.cb = cb;
        this.cdlStart = cdlStart;
        this.cdlStop = cdlStop;

    }
    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int)(Math.random() * 800));
            System.out.println(this.name + " готов");
            cdlStart.countDown();
            cb.await();

        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }
        if(win.getAndSet(false)){
            System.out.println(this.name + "WIN!!!");
        }

        cdlStop.countDown();
    }
}