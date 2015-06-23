package com.wayne.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.util.Map;

import android.os.Handler;
import android.os.Message;

public abstract class AsynchBaseRequest implements Runnable, Serializable {
	public static final int ERROR_CODE_IO = 0x001;
	private static final long serialVersionUID = 7320152772815413560L;
	protected static final int SUCCESS = 1;
	protected static final int FAILED = -1;
	/**
	 * ���ӳ�ʱ
	 */
	protected int connTimeout = 5 * 1000;
	/**
	 * ��ȡ��ʱ
	 */
	protected int readTimeout = 5 * 1000;
	/**
	 * �����ַ
	 */
	protected String urlStr = null;
	/**
	 * �������
	 */
	protected Map<String, Object> params = null;
	/**
	 * �����ص�
	 */
	private ParseCallback mParseCallback;
	/**
	 * ����ص�
	 */
	private RequestCallback mRequestCallback;
	// ���Ӷ���
	protected HttpURLConnection mUrlConnection;
	// ��
	protected InputStream mInputStream;
	/**
	 * �жϱ�־
	 */
	private boolean interrupted = false;
	/**
	 *��Ϣ����
	 */
	private Handler mHandler;
	

	public void setConnTimeout(int connTimeout) {
		this.connTimeout = connTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}
	
	public boolean isInterrupted() {
		return interrupted;
	}

	public void setInterrupted(boolean interrupted) {
		this.interrupted = interrupted;
	}

	public AsynchBaseRequest(String urlStr, Map<String, Object> params, ParseCallback parseCallback, RequestCallback requestCallback) {
		this.urlStr = urlStr;
		this.params = params;
		this.mParseCallback = parseCallback;
		this.mRequestCallback = requestCallback;
		mHandler = new Handler(){
				public void handleMessage(android.os.Message msg) {
					switch (msg.what) {
					case SUCCESS:
						mRequestCallback.onSuccess(msg.obj);
						break;
					case FAILED:
						mRequestCallback.onFailed((Integer) msg.obj);
						break;
					}
				};
			};
	}

	@Override
	public void run() {
		try {
			if(interrupted) {
				NSLog.d("interrupted before access network");
				return;
			}
			mInputStream = getRequestInputStream();
			if (mInputStream != null) {
				if(interrupted) {
					NSLog.d("interrupted before parse response");
					return;
				}
				String result = new String(readInputStream(mInputStream));
				Object object = mParseCallback.parse(result);
				if(interrupted) {
					NSLog.d("interrupted before flush ui");
					return;
				}
				Message msg = mHandler.obtainMessage();
				msg.what = SUCCESS;
				msg.obj = object;
				mHandler.sendMessage(msg);
			} else {
				NSLog.e("the request result is NULL!");
			}
		} catch (IOException e) {
			mRequestCallback.onFailed(ERROR_CODE_IO); //IO�쳣
			Message msg = mHandler.obtainMessage();
			msg.what = FAILED;
			msg.obj = ERROR_CODE_IO;
			mHandler.sendMessage(msg);
			e.printStackTrace();
		}
	}

	/**
	 * ��ȡ������������������
	 * 
	 * @return
	 */
	abstract InputStream getRequestInputStream() throws IOException;

	private byte[] readInputStream(InputStream inputStream) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int len = -1;
		byte[] buff = new byte[1024];
		while ((len = inputStream.read(buff)) != -1) {
			bos.write(buff, 0, len);
		}
		bos.flush();
		bos.close();
		inputStream.close();
		return bos.toByteArray();
	}
	
	public HttpURLConnection getHttpURLConnection() {
		return this.mUrlConnection;
	}
}
