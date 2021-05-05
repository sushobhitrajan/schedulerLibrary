package com.scheduler.coding;

public class Task implements Runnable {

	long startTimeInMilli; // this will be epoc time in milliseconds
	long delayTime;

	public Task(long startTimeInMilli) {
		this.startTimeInMilli = startTimeInMilli;
		this.delayTime = -1;
	}

	public Task(long startTimeInMilli, long delayTime) {
		this.startTimeInMilli = startTimeInMilli;
		this.delayTime = delayTime;
	}

	@Override
	public void run() {
		System.out.println("Hello Task !");
	}

	public long getStartTimeInMilli() {
		return startTimeInMilli;
	}

	public long getDelayTime() {
		return delayTime;
	}
}
