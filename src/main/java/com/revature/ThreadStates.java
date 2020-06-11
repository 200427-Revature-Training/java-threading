package com.revature;

/**
 * States of a Thread
 * 1. NEW - That they have been instantiated, but the .run() method has not been called by .start()
 * 2. RUNNABLE - Any thread which is running or is qualified to run is in the RUNNABLE state.
 * 3. WAITING - The thread is waiting for an unspecified period of time eg. .join()
 * 4. TIMED_WAITING - The thread is waiting for a specified period of time. eg sleep(1000)
 * 5. BLOCKED - The thread is blocked from accessing a resource that it requires to continue.
 * 6. TERMINATED - The thread has been started and its run method has been completed.
 */
public class ThreadStates {
	static void demoNew() {
		Thread thread = new Thread();
		System.out.println(thread.getState());
	}
	
	static void demoRunnable() {
		System.out.println(Thread.currentThread().getState());
	}
	
	static void demoWaiting() throws InterruptedException {
		Thread mainThread = Thread.currentThread();
		Thread thread = new Thread(() -> {
			try {
				mainThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		thread.start();
		Thread.sleep(100);
		System.out.println(thread.getState());
	}
	
	static void demoTimedWaiting() throws InterruptedException {
		Thread mainThread = Thread.currentThread();
		Thread thread = new Thread(() -> {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		thread.start();
		Thread.sleep(10);
		System.out.println(thread.getState());
	}
	
	static void demoBlocked() throws InterruptedException {
		BlockingAccount account = new BlockingAccount();
		Runnable signinRunnable = () -> {
			account.signin();
		};
		
		Thread a = new Thread(signinRunnable);
		Thread b = new Thread(signinRunnable);
		
		// A will start and go into timed_waiting within login
		a.start();
		Thread.sleep(10);
		
		// B will start, and will try to call login, but will be blocked by A's ongoing login execution
		b.start();
		Thread.sleep(10);
		System.out.println(b.getState());
		
	}
	
	static void demoTerminated() throws InterruptedException {
		Thread a = new Thread(() -> {});
		a.start();
		a.join();
		System.out.println(a.getState());
	}
	
	public static void main(String[] args) throws InterruptedException {
		demoNew();
		demoRunnable();
		demoWaiting();
		demoTimedWaiting();
		demoBlocked();
		demoTerminated();
	}
}

class BlockingAccount {
	synchronized void signin() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
