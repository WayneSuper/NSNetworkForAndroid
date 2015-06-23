package com.wayne.net;

import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ���������̹߳�����
 * @author Wayne
 *
 */
public class MainThreadPool {
	/**
	 * ���ڱ���ȴ�ִ�е��������(FIFO)
	 */
	private ArrayBlockingQueue<Runnable> mQueue = new ArrayBlockingQueue<Runnable>(12, true);
	/**
	 * �̳߳�
	 */
	private AbstractExecutorService mExecutorService = new ThreadPoolExecutor(3, 5,10,TimeUnit.SECONDS,mQueue,new ThreadPoolExecutor.DiscardOldestPolicy());
	
	private static MainThreadPool instance;
	
	private MainThreadPool(){}
	/**
	 * ��ȡ��������
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
	 * ִ��
	 * @param r
	 */
	public void excute(Runnable r) {
		if(mExecutorService != null){
			mExecutorService.execute(r);
			NSLog.d("excute thread!");
		}
	}
	/**
	 * �ж�ִ��
	 */
	public void shutdown() {
		if(mExecutorService != null)
			mExecutorService.shutdown();
	}
	/**
	 * �����رգ������������������е��߳�
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
