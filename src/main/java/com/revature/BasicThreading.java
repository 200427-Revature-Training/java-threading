package com.revature;


/**
 * What is multithreading and what is a thread?
 * 
 * The concept of multithreading is an application having multiple lines of execution.
 * Every thread has its own stack - but they share the heap. By having multiple lines 
 * of execution, we can conduct a variety of tasks at the same time. 
 * 
 * Warning: 
 * 	1. Nearly everything threads is implementation specific.
 *  2. Using threads takes our application from a deterministic execution to a
 *  	non-deterministic execution.
 *  
 *  Parallel Programming - Multiple lines of execution 
 *  
 *  Concurrent Programming - Multiple lines of execution executing with overlapping time
 *  
 *  Concurrent programming is a modern programming which allows for performance benefits
 *  by leveraging multiple CPU cores at the same time. Threads can execute concurrently. 
 *  We do not decide if they do.  This behavior is completely implementation specific.
 *
 *  Threading can affect the Java application lifecycle.  This is because when using normal threads
 *  the application is considered closed or terminated when ALL threads have finished - not just the
 *  main thread. However, it is possible to define threads which will not keep the application running.
 *  These threads are called 'daemon threads'.  Daemon threads are a good option in the case that you
 *  need a separate thread to manage some background task that shouldn't keep the application
 *  alive.
 *
 *	* Thread - Low level thread - basic fundamental threading
 *  * Callable - Like a thread that resolves to value (Future or CompleteableFuture)
 *  * ExecuterService - Manage Thread pool and use threads to complete tasks.
 *  * ForkJoinPool/ForkJoinTask - Divide and conquer concurrency strategy.
 *  * ParallelStream - Streams are used for functional programming, there is a simple conversion
 *  		allow functional operations to happen concurrently.
 *  * For more: check java.concurrent 
 *
 */
public class BasicThreading {
	
	public static void main(String[] args) {
		System.out.println(Thread.currentThread().getName());
		
		/* Definition of new Threads - 3 Methods */
		/*
		 * 1. Extending the Thread class
		 * 2. Implementing the Runnable interface (preferred)
		 * 3. Implementation of Runnable using a lambda function
		 */
		
		Thread thread = new MyThread();
		
		// To start a thread call .start(). Note: .run() will not start a new thread!
		thread.start();
		
		MyRunnable runnable = new MyRunnable();
		Thread runnableThread = new Thread(runnable);
		runnableThread.start();
		
		Runnable lambdaRunnable = () -> {
			System.out.println("Hello from lambda created runnable thread: "
					+ Thread.currentThread().getName());
		};
		Thread lambdaThread = new Thread(lambdaRunnable);
		lambdaThread.start();
		
	}	
}

class MyThread extends Thread {
	@Override
	public void run() {
		System.out.println("Hello from thread named: " 
					+ Thread.currentThread().getName());
	}
}


/**
 * Runnable is a special interface called a Functional Interface.
 * Functional Interfaces are any interfaces that define exactly one abstract method.
 * Any functional interface can be implicitly implementing using a lambda function. 
 *
 * The logic behind this pattern is that Java must know the type that the lambda expression
 * represents. The only way it can know what method the lambda is intended to implement
 * on the assigned type is for that type to have only a single abstract method. As
 * such, lambda expressions are only valid for implementing interfaces with a single
 * abstract method.
 */
class MyRunnable implements Runnable {

	@Override
	public void run() {
		System.out.println("Hello from runnable thread: " 
					+ Thread.currentThread().getName());
	}
	
}
