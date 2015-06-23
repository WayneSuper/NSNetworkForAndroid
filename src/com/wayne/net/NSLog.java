package com.wayne.net;

import android.util.Log;
/**
 * 日志打印类
 * @author Wayne
 *
 */
public class NSLog {
	private static boolean DEBUG = true;
	private static final String TAG = "NSLog";
	
	public static final void isDebug(boolean debug) {
		DEBUG = debug;
	}
	
	public static final void d(String tag,String msg) {
		if(DEBUG) {
			Log.d(tag, msg);
		}
	}
	
	public static final void d(String msg) {
		d(TAG,msg);
	}
	
	public static final void e(String tag,String msg) {
		if(DEBUG) {
			Log.e(tag, msg);
		}
	}
	
	public static final void e(String msg) {
		if(DEBUG) {
			e(TAG, msg);
		}
	}
}
