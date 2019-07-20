package com.ckr.multi_download;

import android.app.Application;
import android.content.Context;

/**
 * Created on 2019/7/20
 *
 * @author ckr
 */
public class MainApplication extends Application {

	private static MainApplication mApplication;

	public static Context getContext() {
		return mApplication.getApplicationContext();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mApplication = this;
	}
}
