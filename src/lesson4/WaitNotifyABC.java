package lesson4;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WaitNotifyABC {
    private final Object mon = new Object();
    private volatile char currentLetter = 'A' ;
    private final static int ABC_COUNT = 5;

    public static void main(String[] args) {
        WaitNotifyABC w = new WaitNotifyABC();
        ExecutorService executorService = Executors.newFixedThreadPool(3);
         executorService.execute(()->{
            w.printA();
        });
        executorService.execute(()->{
            w.printB();
        });
        executorService.execute(()->{
            w.printC();
        });
        executorService.shutdown();


        /*    Thread ta = new Thread(() -> {
                w.printA();
            });
            Thread tb = new Thread(() -> {
                w.printB();
            });
            Thread tc = new Thread(() -> {
                w.printC();
            });
            ta.start();
            tb.start();
            tc.start();*/
        }
        public void printA() {
            synchronized (mon) {
                try {
                    for ( int i = 0 ; i < ABC_COUNT ; i++) {
                        while ( currentLetter != 'A' ) {
                            mon.wait();
                        }
                        System. out .print( "A" );
                        currentLetter = 'B' ;
                        mon.notifyAll();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    public void printB() {
        synchronized (mon) {
            try {
                for ( int i = 0 ; i < ABC_COUNT ; i++) {
                    while ( currentLetter != 'B' ) {
                        mon.wait();
                    }
                    System. out .print( "B" );
                    currentLetter = 'C' ;
                    mon.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void printC() {
        synchronized (mon) {
            try {
                for ( int i = 0 ; i < ABC_COUNT ; i++) {
                    while ( currentLetter != 'C' ) {
                        mon.wait();
                    }
                    System. out .print( "C" );
                    currentLetter = 'A' ;
                    mon.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
