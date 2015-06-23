package com.wayne.net;

public interface ParseCallback {
	/**
	 * 对网络返回的结果进行解析(JSON/XML)
	 * @param response 网络请求返回的结果
	 * @return 解析后的结果
	 */
	Object parse(String response);
}
