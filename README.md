# Multithreaded-Minotaur
Programming Assignment 2 for COP 4520. Coded in Java using Visual Studio Code

Filenames:
Problem 1: MinotaurBirthdayParty.java
Problem 2: MinotaurCrystalVase.java

# Run Instructions:
1. Open Terminal
2. Navigate to directory containing MinotaurBirthdayParty.java and/or MinotaurCrystalVase.java
3. Compile using: 
```bash
  javac MinotaurBirthdayParty.java
```
or
```bash
  javac MinotaurCrystalVase.java
```
4. Run using: 
```bash
  java MinotaurBirthdayParty
```
or
```bash
  java MinotaurCrystalVase
```
# Problem Statement
Problem 1: Minotaur’s Birthday Party (50 points)

The Minotaur invited N guests to his birthday party. When the guests arrived, he made the following announcement.

The guests may enter his labyrinth, one at a time and only when he invites them to do so. At the end of the labyrinth, the Minotaur placed a birthday cupcake on a plate. When a guest finds a way out of the labyrinth, he or she may decide to eat the birthday cupcake or leave it. If the cupcake is eaten by the previous guest, the next guest will find the cupcake plate empty and may request another cupcake by asking the Minotaur’s servants. When the servants bring a new cupcake the guest may decide to eat it or leave it on the plate.

The Minotaur’s only request for each guest is to not talk to the other guests about her or his visit to the labyrinth after the game has started. The guests are allowed to come up with a strategy prior to the beginning of the game. There are many birthday cupcakes, so the Minotaur may pick the same guests multiple times and ask them to enter the labyrinth. Before the party is over, the Minotaur wants to know if all of his guests have had the chance to enter his labyrinth. To do so, the guests must announce that they have all visited the labyrinth at least once.

Now the guests must come up with a strategy to let the Minotaur know that every guest entered the Minotaur’s labyrinth. It is known that there is already a birthday cupcake left at the labyrinth’s exit at the start of the game. How would the guests do this and not disappoint his generous and a bit temperamental host?

Create a program to simulate the winning strategy (protocol) where each guest is represented by one running thread. In your program you can choose a concrete number for N or ask the user to specify N at the start.

Problem 2: Minotaur’s Crystal Vase (50 points)

The Minotaur decided to show his favorite crystal vase to his guests in a dedicated showroom with a single door. He did not want many guests to gather around the vase and accidentally break it. For this reason, he would allow only one guest at a time into the showroom. He asked his guests to choose from one of three possible strategies for viewing the Minotaur’s favorite crystal vase:

1) Any guest could stop by and check whether the showroom’s door is open at any time and try to enter the room. While this would allow the guests to roam around the castle and enjoy the party, this strategy may also cause large crowds of eager guests to gather around the door. A particular guest wanting to see the vase would also have no guarantee that she or he will be able to do so and when.

2) The Minotaur’s second strategy allowed the guests to place a sign on the door indicating when the showroom is available. The sign would read “AVAILABLE” or “BUSY.” Every guest is responsible to set the sign to “BUSY” when entering the showroom and back to “AVAILABLE” upon exit. That way guests would not bother trying to go to the showroom if it is not available.

3) The third strategy would allow the quests to line in a queue. Every guest exiting the room was responsible to notify the guest standing in front of the queue that the showroom is available. Guests were allowed to queue multiple times.

Which of these three strategies should the guests choose? Please discuss the advantages and disadvantages.

Implement the strategy/protocol of your choice where each guest is represented by 1 running thread. You can choose a concrete number for the number of guests or ask the user to specify it at the start.

# Proof of Correctness:

Worth mentioning:
Guest == thread
Labrynth == thread run() while-loop

Problem 1: 
My implementation uses a Reentrant Lock to ensure that only one thread is in the "labrynth" while-loop at a time. The guests are able to enter the labrynth multiple times, as stated in the problem description. The strategy behind finding out at what point every guest has eaten a cake is determined before anyting begins. A guest at random is chosen to be the "decider" guest. This decider guest will keep track of how many times he finds the cake missing in the labrynth. The other guests should only eat the cake one time and one time only. Using this strategy, the guests won't have to  communicate to eachother. Once the decider guest finds the cake missing an "n guests" amount of times, he can deduce that all guests have eaten the cake once, and announces it to the Minotaur, ending the game and breaking all threads out of their while-loops.

Problem 2:
For this problem, I implemented the second strategy that is mentioned in the problem statement. I used a boolean variable called availableBusy to let each guest know if the room was available or not. If the room wasnt available, they will re-loop and wait until it becomes available. Once the guest sees that the room is availabe, they will enter the room, make sure to change the availableBusy variable to false, indicating that the room is busy. The guest will then be checked to see if they have seen the vase yet, if not, change the threads hasSeen variable to true. Otherwise, wait for a bit and then leave the room, setting the availablility of the room to true on its way out. Once it is determined that all guests have seen the vase once (AKA hasSeen is true for all threads) then the program will output and terminate.

# Experimental Evaluation:
Processor used for testing: 11th Gen Intel(R) Core(TM) i7-1165G7 @ 2.80GHz (8 CPUs), ~2.8GHz
Ubuntu was used for compiling

My experimental evaluation throughout this assignment was not as tedious as the first programming assignment. Race conditions would occur at times, as well as infinite loops cause by unclosed conditions. In terms of run-time, both solutions seem to run at reasonable times. The vase solution seemed to run a bit slower than the birthday party, but it makes sense.

At n = 1000, MinotaurCrystalVase ran at ~5000ms \n
at n = 1000, MinotaurBirthdayParty ran at ~300ms 

I believed my runtimes were slowed down a bit due to the fact that I used Ubuntu to compile and run my solutions. I believe that they will run faster if ran natively.
