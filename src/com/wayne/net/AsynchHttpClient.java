package com.wayne.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ����ģʽ ������������
 * 
 * @author Wayne
 *
 */
public class AsynchHttpClient {

	public static final String TAG_DEFAULT = "defalut_tag";

	private static AsynchHttpClient instance;

	private MainThreadPool mainThreadPool;
	/**
	 * ��������ҳ��������洢��������Ա�֮���ܹ�ȡ������
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
	 *            �����־
	 * @param method
	 *            {@link HttpMethod.GET}��{@link HttpMethod.POST}
	 * @param url
	 *            �����ַ
	 * @param params
	 *            �������
	 * @param parseCallback
	 *            �����ص�
	 * @param requestCallback
	 *            ����ص�
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
	 * ����ΪĬ�ϵ������־{@link TAG_DEFAULT}
	 * 
	 * @param method
	 *            {@link HttpMethod.GET}��{@link HttpMethod.POST}
	 * @param url
	 *            �����ַ
	 * @param params
	 *            �������
	 * @param parseCallback
	 *            �����ص�
	 * @param requestCallback
	 *            ����ص�
	 */
	public void request(HttpMethod method, String url, Map<String, Object> params, ParseCallback parseCallback, RequestCallback requestCallback) {
		this.request(TAG_DEFAULT, method, url, params, parseCallback, requestCallback);
	}

	/**
	 * ͨ�����õ�TAGȡ������ ���Ҫ�ر�û������TAG���������� {@link TAG_DEFAULT}
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
		list.clear(); // ��ռ���
		requestMap.remove(tag); // ��ռ���
	}
	
	protected void cancel(AsynchBaseRequest request) {
		createCacheMap();
	}

	/**
	 * ȡ����������
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
