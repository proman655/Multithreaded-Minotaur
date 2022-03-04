// Pedro Roman
// COP 4520
// PA2 Problem #2

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;
import java.io.*;

// class
public class MinotaurCrystalVase extends Thread
{
    public static ArrayList<MinotaurVase> threads = new ArrayList<>();
    ReentrantLock lock = new ReentrantLock();
    int numOfThreads;

    MinotaurCrystalVase (int numOfThreads)
    {
        this.numOfThreads = numOfThreads;
    }

    // helper functions

    int getNumOfThreads()
    {
        return this.numOfThreads;
    }

    MinotaurVase getThread(int index)
    {
        return threads.get(index);
    }

    void MinotaurVaseRun(MinotaurCrystalVase mainThread) throws InterruptedException
    {
        for (int i = 1; i <= mainThread.numOfThreads; i++)
            threads.add(new MinotaurVase(i, mainThread));

        for (int i = 0; i < mainThread.numOfThreads; i++)
            threads.get(i).start();

        for (int i = 0; i < mainThread.numOfThreads; i++)
            threads.get(i).join();

        System.out.println("All " + mainThread.getNumOfThreads() + " guests have seen the vase at least once!");
    }

    // main function
    public static void main(String args[]) throws InterruptedException
    {   
        //Scanner object
        Scanner input = new Scanner(System.in);

        System.out.print("Enter Number of Guests: ");


        while(!input.hasNextInt())
        {
            input.nextLine();
            System.out.print("Please input an integer value");
        }

        int numOfGuests = input.nextInt();

        input.close();

        final long startTime = System.currentTimeMillis();

        MinotaurCrystalVase mainThread = new MinotaurCrystalVase(numOfGuests);

        mainThread.MinotaurVaseRun(mainThread);

        final long endTime = System.currentTimeMillis();

        final long runTime = endTime - startTime;
        System.out.println("Execution time: " + runTime + " ms");
    }
}

class MinotaurVase extends Thread
{   
    //
    static AtomicBoolean availableBusy = new AtomicBoolean(true);

    // boolean value to determine if all threads need to be stopped (after they have all seen the vase at least once)
    static AtomicBoolean stopThreads = new AtomicBoolean(false);
    boolean hasSeen = false;
    int threadNum;
    MinotaurCrystalVase mainThread;

    MinotaurVase(int threadNum, MinotaurCrystalVase mainThread)
    {
        this.threadNum = threadNum;
        this.mainThread = mainThread;
    }

    // function to determine if a guest has seen the vase
    boolean hasGuestSeen()
    {
        return hasSeen;
    }

    @Override
    public void run() 
    {
        // Infinite while loop lets the threads requeue indefinitely until they have all gotten to see the vase
        // Implemented strategy #2 from the problem description
        while (true)
        {   
            // check to see if all guests have seen the vase, break the loop for all threads if so
            if (stopThreads.get())
                break;
            boolean stopLoop = true;

            // check to see if availableBusy boolean is true or false ("AVAILABLE" or "BUSY" on the sign , respectively)
            if (availableBusy.get())
            {   
                // enter the room, set the boolean to false (sign to "BUSY"), and look at the vase
                availableBusy.set(false);

                this.hasSeen = true;
                try 
                {
                    Thread.sleep(10);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                availableBusy.set(true);
            }
            // room is not available, reenter the queue and wait again
            else
                continue;

            // For loop checks to make sure each guest has seen the vase at least once
            for (int i = 0; i < mainThread.getNumOfThreads(); i++)
                if (!mainThread.getThread(i).hasGuestSeen())
                {
                    stopLoop = false;
                    break;
                }

            // Stoploop flag tells all other threads to stop
            if (stopLoop)
            {
                stopThreads.set(true);
                break;
            }
        }
    }
}