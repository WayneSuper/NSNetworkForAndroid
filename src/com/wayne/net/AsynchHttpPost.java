package com.wayne.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class AsynchHttpPost extends AsynchBaseRequest {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public AsynchHttpPost(String urlStr, Map<String, Object> params, ParseCallback mParseCallback, RequestCallback mRequestCallback) {
		super(urlStr, params, mParseCallback, mRequestCallback);
	}

	@SuppressWarnings("unchecked")
	@Override
	InputStream getRequestInputStream() throws IOException {
		byte[] entityByte = null;
		String uri = packageParams();
		if (uri != null) {
			entityByte = uri.getBytes();
		}
		URL url = new URL(uri);
		mUrlConnection = (HttpURLConnection) url.openConnection();
		mUrlConnection.setConnectTimeout(connTimeout);
		mUrlConnection.setReadTimeout(readTimeout);
		mUrlConnection.setRequestMethod(HttpMethod.POST.toString());
		// 如果通过post提交数据，必须设置允许对外输出数据
		mUrlConnection.setDoOutput(true);
		mUrlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		mUrlConnection.setRequestProperty("Content-Length", String.valueOf(entityByte == null ? String.valueOf(0) : String.valueOf(entityByte.length)));
		if (entityByte != null) {
			OutputStream os = mUrlConnection.getOutputStream();
			os.write(entityByte);
			os.flush();
			os.close();
		}
		if (mUrlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			return mUrlConnection.getInputStream();
		}
		return null;
	}

	private String packageParams() throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder(urlStr);
		sb.append("?");
		if(params != null){
			params.entrySet();
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				sb.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue().toString(), "utf-8")).append("&");
			}
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

}
