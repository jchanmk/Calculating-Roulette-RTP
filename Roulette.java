import java.io.IOException;
import java.util.Random;
import java.lang.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Roulette {
    public static void main (String[]args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: java Roulette <number of threads>");
            System.exit(1);
        }
        int numberOfThreads = Integer.parseInt(args[0]); // how many threads to do trials
        long trials = 10000000;           // number of total trials
        long rollsPerThread = trials/numberOfThreads;   // number of rolls each thread executes

        long startTime = System.nanoTime();

        for(int i = 0; i < numberOfThreads; i++){
            RouletteRoll roll = new RouletteRoll(rollsPerThread);
            roll.start();

            try{
                roll.join();
            } catch(InterruptedException ie){ }
        }

        long endTime = System.nanoTime();           // calculate runtime
        long elapsedTime = endTime - startTime;

        long totalTime = TimeUnit.MILLISECONDS.toSeconds(elapsedTime);
        double blackCount = RouletteRoll.totalBlacks.get();

        double RTP = (blackCount/trials)*100;           // final calculation for RTP

        //System.out.println(blackCount + " " + trials + " " + (blackCount/trials));
        System.out.println("Simulation Time: " + totalTime + " Milliseconds");
        System.out.println("RTP: " + RTP + "%");
    }
}


class RouletteRoll extends Thread {
    public static long numberOfRolls;
    public int count = 0;
    public static AtomicInteger totalBlacks = new AtomicInteger(0);

    public RouletteRoll(long number){
        this.numberOfRolls = number;
    }

    public void run(){
        Random rand = new Random();
        for(int i = 0; i<numberOfRolls; i++) {
            int randomRoll = rand.nextInt(37);  // simulates a roll
            if (randomRoll <= 17) {   // about half are black, these statements simulate the position of the ball on the spin
               // System.out.println(Thread.currentThread().getId() + " " + randomRoll + " Black");
                count+=2;
            }
        }
        totalBlacks.addAndGet(count);
    }
}
