package com.wayne.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class AsynchHttpGet extends AsynchBaseRequest {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AsynchHttpGet(String urlStr, Map<String, Object> params, ParseCallback mParseCallback, RequestCallback mRequestCallback) {
		super(urlStr, params, mParseCallback, mRequestCallback);
	}

	@Override
	InputStream getRequestInputStream() throws IOException {
		String uri = packageParams();
		URL url = new URL(uri);
		mUrlConnection = (HttpURLConnection) url.openConnection();
		mUrlConnection.setConnectTimeout(connTimeout);
		mUrlConnection.setReadTimeout(readTimeout);
		mUrlConnection.setRequestMethod(HttpMethod.GET.toString());
		if(mUrlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			return mUrlConnection.getInputStream();
		}
		return null;
	}

	private String packageParams() throws UnsupportedEncodingException {
			StringBuilder sb = new StringBuilder(urlStr);
			sb.append("?");
			if(params != null){
				for (Map.Entry<String, Object> entry : params.entrySet()) {
					sb.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue().toString(), "utf-8")).append("&");
				}
			}
			sb.deleteCharAt(sb.length()-1);
			return sb.toString();
	}

}
