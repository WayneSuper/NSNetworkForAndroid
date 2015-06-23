package com.wayne.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 单例模式 网络请求的入口
 * 
 * @author Wayne
 *
 */
public class AsynchHttpClient {

	public static final String TAG_DEFAULT = "defalut_tag";

	private static AsynchHttpClient instance;

	private MainThreadPool mainThreadPool;
	/**
	 * 根据请求页面的类名存储请求对象，以便之后能够取消请求
	 */
	private Map<String, List<AsynchBaseRequest>> requestMap = null;

	private AsynchHttpClient() {
		mainThreadPool = MainThreadPool.getInstance();
	}

	public static final AsynchHttpClient getInstance() {
		if (instance == null) {
			synchronized (AsynchHttpClient.class) {
				if (instance == null) {
					instance = new AsynchHttpClient();
				}
			}
		}
		return instance;
	}

	/**
	 * @param tag
	 *            请求标志
	 * @param method
	 *            {@link HttpMethod.GET}、{@link HttpMethod.POST}
	 * @param url
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @param parseCallback
	 *            解析回调
	 * @param requestCallback
	 *            请求回调
	 */
	public void request(String tag, HttpMethod method, String url, Map<String, Object> params, ParseCallback parseCallback, RequestCallback requestCallback) {
		createCacheMap();
		if (tag == null)
			tag = TAG_DEFAULT;
		AsynchBaseRequest mAsynchBaseRequest = null;
		List<AsynchBaseRequest> list = null;
		if (!requestMap.keySet().contains(tag)) {
			list = new ArrayList<AsynchBaseRequest>();
			requestMap.put(tag, list);
		} else {
			list = requestMap.get(tag);
		}

		if (method == null)
			throw new IllegalArgumentException("http method is NULL");
		if (method == HttpMethod.GET) {
			mAsynchBaseRequest = new AsynchHttpGet(url, params, parseCallback, requestCallback);
		} else if (method == HttpMethod.POST) {
			mAsynchBaseRequest = new AsynchHttpPost(url, params, parseCallback, requestCallback);
		} else {
			throw new IllegalArgumentException("http method is Error");
		}

		if (mainThreadPool != null)
			mainThreadPool.excute(mAsynchBaseRequest);
		list.add(mAsynchBaseRequest);

	}

	/**
	 * 设置为默认的请求标志{@link TAG_DEFAULT}
	 * 
	 * @param method
	 *            {@link HttpMethod.GET}、{@link HttpMethod.POST}
	 * @param url
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @param parseCallback
	 *            解析回调
	 * @param requestCallback
	 *            请求回调
	 */
	public void request(HttpMethod method, String url, Map<String, Object> params, ParseCallback parseCallback, RequestCallback requestCallback) {
		this.request(TAG_DEFAULT, method, url, params, parseCallback, requestCallback);
	}

	/**
	 * 通过设置的TAG取消请求 如果要关闭没有设置TAG的请求则传入 {@link TAG_DEFAULT}
	 * 
	 * @param tag
	 */
	public void cancel(String tag) {
		createCacheMap();
		List<AsynchBaseRequest> list = requestMap.get(tag);
		for (AsynchBaseRequest request : list) {
			if (request != null) {
				Thread thread = new Thread(request);
				if (thread.isAlive() || !Thread.interrupted()) {
					request.setInterrupted(true);
				}
				request.getHttpURLConnection().disconnect();
				list.remove(request);
			}
		}
		list.clear(); // 清空集合
		requestMap.remove(tag); // 清空集合
	}
	
	protected void cancel(AsynchBaseRequest request) {
		createCacheMap();
	}

	/**
	 * 取消所有请求
	 */
	public void cancelAll() {
		createCacheMap();
		Set<String> keySet = requestMap.keySet();
		for (String key : keySet) {
			List<AsynchBaseRequest> list = requestMap.get(key);
			for (AsynchBaseRequest request : list) {
				Thread thread = new Thread(request);
				if (thread.isAlive() || !Thread.interrupted()) {
					request.setInterrupted(true);
				}
				if (request != null) {
					request.getHttpURLConnection().disconnect();
					list.remove(request);
				}
			}
			list.clear();
			requestMap.remove(key);
		}
	}

	private void createCacheMap() {
		if (requestMap == null)
			requestMap = new HashMap<String, List<AsynchBaseRequest>>();
	}
}
