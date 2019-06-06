package com.caimao.hq.core;

import java.util.concurrent.*;

public class ProcessorManager {
	/**
	 * 执行线程池
	 */
	public static ExecutorService executor;
	/**
	 * 线程池通常大小
	 */
	private static int POOL_SIZE = 20;
	/**
	 * 线程池最大
	 */
	private static int POOL_MAX_SIZE = 200;
	/**
	 * 每个线程最多活多少时间，分钟
	 */
	private static int KEEP_ALIVE_MINUTE = 60;
	/**
	 * 队列大小
	 */
	private static int QUEUE_CAPACITY=6000;
	static{
		init();
	}
	private static void init() {
		executor = new ThreadPoolExecutor(POOL_SIZE, POOL_MAX_SIZE, KEEP_ALIVE_MINUTE, TimeUnit.MINUTES,
				new LinkedBlockingQueue<Runnable>(QUEUE_CAPACITY), new RejectedExecutionHandler() {
			@Override
			public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
				System.out.println("这里要处理任务提交失败的情况");
			}
		});

	}

	public static void push(Runnable task){
		executor.execute(task);
	}


	public static void main(String[] args) {

	}

}
