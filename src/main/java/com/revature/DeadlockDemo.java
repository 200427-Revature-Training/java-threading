package com.revature;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/*
 * Deadlock is a threading problem that can occur when resource utilization is intertwined and 
 * poorly managed.  In the simplest scenario, deadlock involves some thread A requiring to lock
 * two resource, while some thread B requires locks on the same to resources.
 * 
 * Lets call these resources "cat" and "dog".
 * 
 * A acquires a lock on "cat", then the thread scheduler gives time to thread B.
 * B acquires a lock on "dog", then attempts to acquire a lock on "cat", but is blocked by Thread A.
 * A activates again and attempts to get a lock on "dog", but is blocked by Thread B, which holding the
 * lock on "dog" and waiting for A to release "cat".
 * 
 * Some strategies to avoid deadlock:
 * 
 * * Apply a strategy for the order in which locks are created.
 * 
 * 
 * Other Threading Problems:
 * * Thread Starvation
 * * Thrashing
 * 
 * 
 */
public class DeadlockDemo {
	public void demoDead() {
		TransferAccount a = new TransferAccount(1000);
		TransferAccount b = new TransferAccount(1000);
		AtomicInteger aCounter = new AtomicInteger(0);
		AtomicInteger bCounter = new AtomicInteger(0);

		int iterations = 1000;
		Thread threadA = new Thread(() -> {
			for (int i = 0; i < iterations; i++) {
				a.transferTo(1, b);
				aCounter.incrementAndGet();
			}
		});

		Thread threadB = new Thread(() -> {
			for (int i = 0; i < iterations; i++) {
				b.transferTo(1, a);
				bCounter.incrementAndGet();
			}
		});

		Thread counterThread = new Thread(() -> {
			do {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Thread A iterations: " + aCounter.get());
				System.out.println("Thread B iterations: " + bCounter.get());
			} while (threadA.isAlive() || threadB.isAlive());
		});

		threadA.start();
		threadB.start();
		counterThread.start();
	}

	public static void demoFixed() {
		TransferAccountFixed a = new TransferAccountFixed(1, 1000);
		TransferAccountFixed b = new TransferAccountFixed(2, 1000);
		AtomicInteger aCounter = new AtomicInteger(0);
		AtomicInteger bCounter = new AtomicInteger(0);

		int iterations = 1000;
		Thread threadA = new Thread(() -> {
			for (int i = 0; i < iterations; i++) {
				a.transferTo(1, b);
				aCounter.incrementAndGet();
			}
		});
		threadA.setName("a");

		Thread threadB = new Thread(() -> {
			for (int i = 0; i < iterations; i++) {
				b.transferTo(1, a);
				bCounter.incrementAndGet();
			}
		});
		threadB.setName("b");

		Thread counterThread = new Thread(() -> {
			do {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Thread A iterations: " + aCounter.get());
				System.out.println("Thread B iterations: " + bCounter.get());
			} while (threadA.isAlive() || threadB.isAlive());
		});

		threadA.start();
		threadB.start();
		counterThread.start();
	}

	public static void demoLock() {
		TransferAccountLockable a = new TransferAccountLockable(1, 100000);
		TransferAccountLockable b = new TransferAccountLockable(2, 100000);
		AtomicInteger aCounter = new AtomicInteger(0);
		AtomicInteger bCounter = new AtomicInteger(0);

		int iterations = 10000000;
		Thread threadA = new Thread(() -> {
			for (int i = 0; i < iterations; i++) {
				a.transferTo(1, b);
				aCounter.incrementAndGet();
			}
		});
		threadA.setName("a");

		Thread threadB = new Thread(() -> {
			for (int i = 0; i < iterations; i++) {
				b.transferTo(1, a);
				bCounter.incrementAndGet();
			}
		});
		threadB.setName("b");

		Thread counterThread = new Thread(() -> {
			do {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Thread A iterations: " + aCounter.get());
				System.out.println("Thread B iterations: " + bCounter.get());
			} while (threadA.isAlive() || threadB.isAlive());
		});

		threadA.start();
		threadB.start();
		counterThread.start();
	}
	
	public static void main(String[] args) {
		demoLock();
	}
}

class TransferAccount {
	int balance = 0;

	synchronized void transferTo(int amount, TransferAccount b) {
		if (amount <= 0) {
			System.out.println("Transfer only valid for positive amounts.");
		}

		if (balance < amount) {
			System.out.println("Insufficient funds");
			return;
		}

		synchronized (b) {
			this.balance -= amount;
			b.balance += amount;
		}
	}

	public TransferAccount(int balance) {
		this.balance = balance;
	}

	public TransferAccount(int id, int balance) {
		this.balance = balance;
	}
}

class TransferAccountFixed {
	int id;
	int balance = 0;

	void transferTo(int amount, TransferAccountFixed b) {
		if (amount <= 0) {
			System.out.println("Transfer only valid for positive amounts.");
		}

		if (balance < amount) {
			System.out.println("Insufficient funds");
			return;
		}

		TransferAccountFixed first = this.id < b.id ? this : b;
		TransferAccountFixed second = first != this ? this : b;

		synchronized (first) {
			synchronized (second) {
				this.balance -= amount;
				b.balance += amount;
			}
		}
	}
	public TransferAccountFixed(int id, int balance) {
		this.id = id;
		this.balance= balance;
	}

}
class TransferAccountLockable {
	int id;
	int balance = 0;
	ReentrantLock Lock = new ReentrantLock();
	
	void transferTo(int amount, TransferAccountLockable b) {
		if (amount <= 0) {
			System.out.println("Transfer only valid for positive amounts.");
		}
		
		if (balance < amount) {
			System.out.println("Insufficient funds");
			return;
		}
		
		while(true) {
			if (this.Lock.tryLock()) {
				if (b.Lock.tryLock()) {
					this.balance -= amount;
					b.balance += amount;
					this.Lock.unlock();
					b.Lock.unlock();
					return;
				} else {
					this.Lock.unlock();
					Thread.yield();
					continue;
				}
			} else {
				Thread.yield();
			}
		}
	}
	public TransferAccountLockable(int id, int balance) {
		this.id = id;
		this.balance = balance;
	}
}