package com.wayne.net.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.wayne.net.AsynchHttpClient;
import com.wayne.net.HttpMethod;
import com.wayne.net.NSToast;
import com.wayne.net.ParseCallback;
import com.wayne.net.R;
import com.wayne.net.RequestCallback;

public class MainActivity extends Activity {

	protected static final String tag = "MainActivity";
	private TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textView = (TextView) findViewById(R.id.textView);
		AsynchHttpClient.getInstance().request(HttpMethod.GET, "http://www.baidu.com", null, new ParseCallback() {
			public Object parse(String response) {
				return response;
			}
		}, new RequestCallback() {
			public void onSuccess(Object result) {
				Log.d(tag, (String)result);
				textView.setText((String)result);
			}
			public void onFailed(int errorCode) {
				// TODO Auto-generated method stub

			}
		});
		
		
	}
	
	public void onClick(View view) {
		NSToast._short(this, "点我干什么？草");
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		NSToast.cancel();
	}
}
