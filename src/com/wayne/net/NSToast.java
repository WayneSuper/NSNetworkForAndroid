package com.wayne.net;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class NSToast {
	private static Toast mToast;
	private static ImageView imageView;
	private static TextView textView;

	public static final void _short(Context context, String msg) {
		createToast(context);
		mToast.setDuration(Toast.LENGTH_SHORT);
		textView.setText(msg);
		mToast.show();
	}

	public static final void _short(Context context, int msgRes) {
		createToast(context);
		mToast.setDuration(Toast.LENGTH_SHORT);
		textView.setText(msgRes);
		mToast.show();
	}

	public static final void _long(Context context, String msg) {
		createToast(context);
		mToast.setDuration(Toast.LENGTH_LONG);
		textView.setText(msg);
		mToast.show();
	}

	public static final void _long(Context context, int msgRes) {
		createToast(context);
		mToast.setDuration(Toast.LENGTH_LONG);
		textView.setText(msgRes);
		mToast.show();
	}

	public static final void show(Context context, String msg, int duration) {
		createToast(context);
		mToast.setDuration(duration);
		textView.setText(msg);
		mToast.show();
	}

	private static void createToast(Context context) {
		if (mToast != null) {
			return;
		}
		mToast = new Toast(context);
		View view = LayoutInflater.from(context).inflate(R.layout.layout_toast, null);
		imageView = (ImageView) view.findViewById(R.id.imageView);
		textView = (TextView) view.findViewById(R.id.textView);
		mToast.setGravity(Gravity.BOTTOM, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, context.getResources().getDisplayMetrics()));
		mToast.setView(view);
	}
	
	public static void cancel () {
		if(mToast!=null)
			mToast.cancel();
	}

}
