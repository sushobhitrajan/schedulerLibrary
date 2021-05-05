package com.scheduler.coding;

import java.util.PriorityQueue;

public class SchedulerService {

	private final ThreadPool threadPoolExecutor;
	private final PriorityQueue<Task> priorityQueue;
	SingleWorker singleWorker;

	public SchedulerService(ThreadPool threadPoolExecutor, int maxCapacity) {

		this.threadPoolExecutor = threadPoolExecutor;
		this.priorityQueue = new PriorityQueue<>(maxCapacity,
			(o1, o2) -> o1.getStartTimeInMilli() > o2.getStartTimeInMilli() ? 1 : o1.getStartTimeInMilli() == o2.getStartTimeInMilli() ? 0 : -1);

		// This one I forgot to add so added post our discussion
		this.singleWorker = new SingleWorker();
		this.singleWorker.start();
	}

	public void schedule(long startTime) {
		Task task = new Task(startTime);
		synchronized (priorityQueue) {
			priorityQueue.add(task);
			priorityQueue.notify(); // Here we have a notify method
		}
	}

	public void schedule(long startTime, long delayTime) {
		Task task = new Task(startTime, delayTime);
		synchronized (priorityQueue) {
			priorityQueue.add(task);
			priorityQueue.notify(); // Here we have a notify method
		}
	}

	public void schedule(Task task) {
		synchronized (priorityQueue) {
			priorityQueue.add(task);
			priorityQueue.notify(); // Here we have a notify method
		}
	}

	private class SingleWorker extends Thread {

		@Override
		public void run() {
			while (true) {
				synchronized (priorityQueue) {
					if (priorityQueue.isEmpty()) {
						try {
							priorityQueue.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

					Task task = priorityQueue.peek();
					if (shouldThisBeExecuted(task)) {
						// TODO: while polling there is a possibility that a new task gets added, will revisit
						threadPoolExecutor.execute(priorityQueue.poll());
					} else {
						try {
							// Optimization: Here we are blocking the thread, so that it's not going back again for task condition check
							// TODO: Wait
							long waitTimeInMilli = task.getStartTimeInMilli() - System.currentTimeMillis();
							priorityQueue.wait(waitTimeInMilli);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

		private boolean shouldThisBeExecuted(Task task) {
			long currentTime = System.currentTimeMillis();
			return currentTime >= task.getStartTimeInMilli();
		}
	}
}
