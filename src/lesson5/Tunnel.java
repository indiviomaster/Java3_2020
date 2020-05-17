package lesson5;

import java.util.concurrent.Semaphore;

public class Tunnel extends Stage {

    private static Semaphore smf;
    public Tunnel(Semaphore smf) {
        this.length = 80;
        this.description = "Тоннель " + length + " метров";
        this.smf = smf;
    }
    @Override
    public void go(Car c) {
        try {
            try {
                System.out.println(c.getName() + " готовится к этапу(ждет): " + description);
                smf.acquire();
                System.out.println(c.getName() + " начал этап: " + description);
                Thread.sleep(length / c.getSpeed() * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println(c.getName() + " закончил этап: " + description);
                smf.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}