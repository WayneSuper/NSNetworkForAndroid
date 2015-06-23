package com.wayne.net;

public interface RequestCallback {
	/**
	 * «Î«Û≥…π¶
	 * @param result
	 */
	void onSuccess(Object result);
	
	void onFailed(int errorCode);
}
