package com.wayne.net;

public interface RequestCallback {
	/**
	 * ����ɹ�
	 * @param result
	 */
	void onSuccess(Object result);
	
	void onFailed(int errorCode);
}
