package com.ckr.multi_download;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {
	private static final String TAG = "MainActivity";
	private static Toast mToast;
	private ImageView girlImage;
	private static InnerHandler mHandler;
	private OkHttpClient okHttp;
	public static final int DOWNLOADING = 0;
	public static final int INIT = -1;
	private int state = INIT;

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
		girlImage = findViewById(R.id.girlImage);
		mHandler = new InnerHandler(new WeakReference<MainActivity>(this));
	}


	public void startDownload(View view) {
		toastShort("开始下载");
		downloadImage();
	}

	private void resetState() {
		state = INIT;
	}

	private void downloadImage() {
		if (state == DOWNLOADING) {
			toastShort("下载中...");
			return;
		}
		state = DOWNLOADING;
		if (okHttp == null) {
			okHttp = OkHttpFactory.createOkHttp();
		}
		Request request = new Request.Builder()
				.url("https://github.com/ckrgithub/PageRecyclerView/blob/master/screenRecorder/qq.png")
				.build();
		okHttp.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(@NotNull Call call, @NotNull IOException e) {
				Log.d(TAG, "onFailure: ");
				resetState();
				mHandler.sendEmptyMessage(InnerHandler.FAIL);
			}

			@Override
			public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
				Log.d(TAG, "onResponse: ");
				resetState();
				ResponseBody body = response.body();
				int code = response.code();
				if (body != null) {
					byte[] bytes = body.bytes();
//					String string = body.string();
					MediaType mediaType = body.contentType();
					String type = mediaType.type();
					long l = body.contentLength();
					Log.d(TAG, "onResponse: code=" + code +/* ",string=" + string +*/ ",type=" + type + ",length:" + l);
					Message message = mHandler.obtainMessage();
					message.obj = bytes;
					message.what = InnerHandler.SUCCESS;
					mHandler.sendMessage(message);
				}
			}
		});
	}

	private static class InnerHandler extends Handler {
		public static final int FAIL = 0;
		public static final int SUCCESS = 1;
		private WeakReference<MainActivity> weakReference;

		public InnerHandler(WeakReference<MainActivity> weakReference) {
			super(Looper.getMainLooper());
			this.weakReference = weakReference;
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Log.d(TAG, "handleMessage: ");
			MainActivity activity = weakReference.get();
			if (activity == null) {
				return;
			}
			switch (msg.what) {
				case SUCCESS:
					byte[] bytes = (byte[]) msg.obj;
					Log.d(TAG, "handleMessage: bytes=" + bytes);
					if (bytes != null) {
						Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
						Log.d(TAG, "handleMessage: length=" + bytes.length + ",bitmap=" + bitmap);
						activity.girlImage.setImageBitmap(bitmap);
					}
					break;
				case FAIL:
					toastShort("加载失败");
					break;
			}
		}
	}
}
