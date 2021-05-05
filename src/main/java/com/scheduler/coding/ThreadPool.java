package com.scheduler.coding;


import java.util.concurrent.LinkedBlockingDeque;

public class ThreadPool {

	private final int threadPoolSize;
	private final WorkerThread[] workerThreads; // This is an unbounded queue
	private final LinkedBlockingDeque<Runnable> queue;

	public ThreadPool(int threadPoolSize) {
		this.threadPoolSize = threadPoolSize;
		this.workerThreads = new WorkerThread[this.threadPoolSize];
		this.queue = new LinkedBlockingDeque<>(); // This is unbounded queue

		// Starting the thread pool during initialization, here thread pool size is fixed
		for (int i = 0; i < this.threadPoolSize; i++) {
			workerThreads[i] = new WorkerThread();
			workerThreads[i].start();
		}
	}

	public void execute(Runnable task) {
		synchronized (queue) {
			queue.add(task);
			queue.notify(); // Once any new task comes it will unblock/notify the waiting thread
		}
	}

	private class WorkerThread extends Thread {

		@Override
		public void run() {
			Runnable task;
			while (true) {
				// Step 1
				synchronized (queue) {
					while (queue.isEmpty()) {
						try {
							queue.wait();
						} catch (InterruptedException e) {
							System.out.println("An error occurred while queue is waiting: " + e.getMessage());
						}
					}
					task = queue.poll();
				}

				// Step 2
				try {
					task.run();
				} catch (RuntimeException e) {
					System.out.println("Thread pool is interrupted due to an issue: " + e.getMessage());
				}
			}
		}
	}
}
