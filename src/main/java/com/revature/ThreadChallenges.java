package com.revature;

import java.util.concurrent.atomic.AtomicInteger;

public class ThreadChallenges {

	public static void main(String[] args) {
		
		// Setup account - starts at 0
		Account account = new Account();
		SynchronizedAccount syncAccount = new SynchronizedAccount();
		
		// Setup iteration count
		int iterationCount = 100_000_000;

		// Thread a increments balance by 1 interationCount times
		Thread a = new Thread(() -> {
			for (int i = 0; i < iterationCount; i++) {
//				System.out.println(++account.balance);
//				++account.balance;
				account.balance.incrementAndGet();
				syncAccount.incrementBalance();
			}
		});

		// Thread b decrements balance by 1 interationCount times
		Thread b = new Thread(() -> {
			for (int i = 0; i < iterationCount; i++) {
//				System.out.println(--account.balance);
				
				account.balance.decrementAndGet();
				syncAccount.decrementBalance();
			}
		});

		// start both threads
		a.start();
		b.start();
		
		// wait for both threads to finish
		try {
			a.join();
			b.join();			
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		
		// print balance
		System.out.println(account.balance);
		System.out.println(syncAccount.getBalance());
		
	}
}

class Account {
	// volatile - don't cache value at the processor
//	volatile int balance = 0;
	
	AtomicInteger balance = new AtomicInteger(0);
}


/**
 * Synchronization locks the object.
 * When a thread runs a synchronized function on an object, no other thread may
 * access a synchronized method on that object until the first thread completes
 * the synchronized method.
 * 
 * The synchronized keyword can also be used in a synchronized block.
 *
 */
class SynchronizedAccount {
	private int balance;
	
	public synchronized void incrementBalance() {
		balance = balance + 1;
	}
	
	public synchronized void decrementBalance() {
		balance = balance - 1;
	}
	
	public int getBalance() {
		return balance;
	}
}