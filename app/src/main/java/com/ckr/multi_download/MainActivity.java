package com.ckr.multi_download;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
	private static final String TAG = "MainActivity";
	private static Toast mToast;

	public static void toastShort(String text) {
		if (mToast == null) {
			mToast = Toast.makeText(MainApplication.getContext(), "", Toast.LENGTH_SHORT);
		}
		if (TextUtils.isEmpty(text)) {
			return;
		}
		mToast.setText(text);
		mToast.show();
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}


	public void startDownload(View view) {
		toastShort("开始下载");
	}
}
