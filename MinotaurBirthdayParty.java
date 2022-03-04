// Pedro Roman
// COP 4520
// PA2 Problem #1

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;
import java.io.*;

public class MinotaurBirthdayParty extends Thread
{
    public static ArrayList<MinotaurLabyrinthGuest> threads = new ArrayList<>();
    ReentrantLock lock = new ReentrantLock();
    int numOfThreads;

    MinotaurBirthdayParty (int numOfThreads)
    {
        this.numOfThreads = numOfThreads;
    }

    int getNumOfThreads()
    {
        return this.numOfThreads;
    }

    MinotaurLabyrinthGuest getThread(int index)
    {
        return threads.get(index);
    }

    void MinotaurPartyRun(MinotaurBirthdayParty mainThread) throws InterruptedException
    {   
        // generate a random number to use in assigning a random guest as the deciding guest
        int decider = (int)(Math.random() * mainThread.getNumOfThreads() + 1);

        System.out.println("Guest " + decider + " is the deciding guest!");
        
        for (int i = 1; i <= mainThread.numOfThreads; i++)
        {   
            // thread to create a single deciding thread and the other threads to be regular threads
            if (i == decider)
                threads.add(new MinotaurLabyrinthGuest(i, mainThread, true));
            else
                threads.add(new MinotaurLabyrinthGuest(i, mainThread, false));
        }

        // start all threads
        for (int i = 0; i < mainThread.numOfThreads; i++)
            threads.get(i).start();

        for (int i = 0; i < mainThread.numOfThreads; i++)
            threads.get(i).join();
    }

    public static void main(String args[]) throws InterruptedException
    {   
        //Scanner object
        Scanner input = new Scanner(System.in);

        // prompt user to input the amount of guests for this run
        System.out.print("Enter Number of Guests: ");

        while(!input.hasNextInt())
        {
            input.nextLine();
            System.out.print("Please input an integer value");
        }

        int numOfGuests = input.nextInt();

        input.close();

        final long startTime = System.currentTimeMillis();

        MinotaurBirthdayParty mainThread = new MinotaurBirthdayParty(numOfGuests);

        mainThread.MinotaurPartyRun(mainThread);

        final long endTime = System.currentTimeMillis();

        final long runTime = endTime - startTime;
        System.out.println("Execution time: " + runTime + " ms");
    }
}

class MinotaurLabyrinthGuest extends Thread
{   
    // boolean to keep track of if the cake exists or not
    static AtomicBoolean cake = new AtomicBoolean(true);

    // boolean to break all threads out of the while loop as long as they are all done 
    static AtomicBoolean stopThreads = new AtomicBoolean(false);
    int visits;
    boolean isDecider = false;
    boolean hasTakenCake = false;
    int threadNum;
    MinotaurBirthdayParty mainThread;

    MinotaurLabyrinthGuest (int threadNum, MinotaurBirthdayParty mainThread, boolean isDecider)
    {
        this.threadNum = threadNum;
        this.mainThread = mainThread;
        this.isDecider = isDecider;
    }

    // determine if the current guest is the deciding guest
    boolean isGuestDecider()
    {
        return this.isDecider;
    }

    void doNothing()
    {
        return;
    }

    @Override
    public void run()
    {   
        if (isGuestDecider())
                this.visits = 1;
        

        // while loop to continue having the threads enter the labrynth when it is available
        while (true) 
        {   
            mainThread.lock.lock();

            // condition to check if the stopThreads boolean was set to true
            // if true, unlock any lock aqcuired and break the loop for that thread. 
            if (stopThreads.get())
            {
                mainThread.lock.unlock();
                break;
            }
            
            // condition to check if all threads have visited the labrynth
            // if visits equals the amount of threads, then print out output and set stopThreads boolean to true
            if (this.visits == mainThread.getNumOfThreads())
            {
                System.out.println("Labyrinth Visits: " + this.visits);
                System.out.println("All guests have entered! Guest: " + this.threadNum + " announced");
                stopThreads.set(true);
                mainThread.lock.unlock();
                break;
            }
            try
            {   
                // check to see if the cake has been taken, do nothing if it has been
                if (this.hasTakenCake) 
                    doNothing();
                
                // if the cake has not been taken and the current guest is not the decider,
                // set the cake to false (eat the cake) and leave the labrynth
                else if (cake.get() && !isGuestDecider())
                {
                    this.hasTakenCake = true;
                    cake.set(false);
                }

                // cake is not there, and the current guest is a decider, increment amount of vists by 1 and replace the cake
                else
                {   
                    if (isGuestDecider())
                    {
                        this.visits++;
                        cake.set(true);
                    }
                }
            } finally {
                mainThread.lock.unlock();
            }
        }
    }
}