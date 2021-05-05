package com.scheduler.coding;


public class MainDriver {

	private final static int THREAD_POOL_SIZE = 10;
	private final static int TASK_CAPACITY_SIZE = 10;

	public static void main(String[] args) {

		ThreadPool threadPoolExecutor = new ThreadPool(THREAD_POOL_SIZE);

		SchedulerService schedulerService = new SchedulerService(threadPoolExecutor, TASK_CAPACITY_SIZE);

		long currentEpocTime = System.currentTimeMillis();
		// Method 1
		schedulerService.schedule(currentEpocTime);

		// Method 2
		schedulerService.schedule(currentEpocTime + 2000, 5000);

		// Method 3 - This one I have added post our discussion
		Task task = new Task(currentEpocTime + 3000);
		schedulerService.schedule(task);
	}
}
