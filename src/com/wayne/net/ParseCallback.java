package com.wayne.net;

public interface ParseCallback {
	/**
	 * �����緵�صĽ�����н���(JSON/XML)
	 * @param response �������󷵻صĽ��
	 * @return ������Ľ��
	 */
	Object parse(String response);
}
