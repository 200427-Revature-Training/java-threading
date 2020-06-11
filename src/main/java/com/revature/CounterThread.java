package com.revature;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CounterThread {
	public static void main(String[] args) {

		Thread mainThread = Thread.currentThread();
		final CompleteableInput input = new CompleteableInput();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		Runnable inputTimeout = () -> {
			try {
				// wait 10 seconds (10,000 milliseconds)
				Thread.sleep(10000);
				if (!input.isComplete()) {
					System.out.println("Thread done");
					mainThread.interrupt();
					br.close();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};

		Thread counterThread = new Thread(inputTimeout);
		counterThread.start();
		
		try {
			System.out.println("Enter username: ");
			String username = br.readLine();

			System.out.println("Enter password: ");
			String password = br.readLine();
			System.out.println("Credentials entered successfully");
		} catch (IOException e) {
			System.out.println("Times up!");
		}
	}
}

class CompleteableInput {
	private boolean complete = false;

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

}
