package com.ckr.multi_download;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created on 2019/7/20
 *
 * @author ckr
 */
public class OkHttpFactory {
	public static final int TIME_CONNECT = 10;
	public static final int TIME_READ = 15;
	public static final int TIME_WRITE = 15;
	private static OkHttpClient client;

	public static OkHttpClient createOkHttp() {
		if (client == null) {
			OkHttpClient.Builder builder = new OkHttpClient.Builder()
					.connectTimeout(TIME_CONNECT, TimeUnit.SECONDS)
					.readTimeout(TIME_READ, TimeUnit.SECONDS)
					.writeTimeout(TIME_WRITE, TimeUnit.SECONDS)
					.retryOnConnectionFailure(true);
			client = builder.build();
		}
		return client;
	}

}
