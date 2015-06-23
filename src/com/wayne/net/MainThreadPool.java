package com.wayne.net;

import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 网络请求线程管理类
 * @author Wayne
 *
 */
public class MainThreadPool {
	/**
	 * 用于保存等待执行的请求队列(FIFO)
	 */
	private ArrayBlockingQueue<Runnable> mQueue = new ArrayBlockingQueue<Runnable>(12, true);
	/**
	 * 线程池
	 */
	private AbstractExecutorService mExecutorService = new ThreadPoolExecutor(3, 5,10,TimeUnit.SECONDS,mQueue,new ThreadPoolExecutor.DiscardOldestPolicy());
	
	private static MainThreadPool instance;
	
	private MainThreadPool(){}
	/**
	 * 获取单例对象
	 * @return
	 */
	public static final MainThreadPool getInstance() {
		if(instance == null) {
			synchronized (MainThreadPool.class) {
				if(instance == null) {
					instance = new MainThreadPool();
					NSLog.d("create thread pool success");
				}
			}
		}
		return instance;
	}
	/**
	 * 执行
	 * @param r
	 */
	public void excute(Runnable r) {
		if(mExecutorService != null){
			mExecutorService.execute(r);
			NSLog.d("excute thread!");
		}
	}
	/**
	 * 中断执行
	 */
	public void shutdown() {
		if(mExecutorService != null)
			mExecutorService.shutdown();
	}
	/**
	 * 立即关闭，并挂起所有正在运行的线程
	 */
	public void shutdownNow() {
		if(mExecutorService != null) {
			mExecutorService.shutdownNow();
			try {
				mExecutorService.awaitTermination(1L,TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
