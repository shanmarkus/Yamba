package com.shan.yamba;

import winterwell.jtwitter.Twitter;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.*;

public class YambaApplication extends Application implements
		OnSharedPreferenceChangeListener {

	private boolean serviceRunning;
	private static final String TAG = YambaApplication.class.getSimpleName();
	public Twitter twitter;
	private SharedPreferences prefs;

	@Override
	public void onCreate() {
		super.onCreate();
		this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
		this.prefs.registerOnSharedPreferenceChangeListener(this);
		Log.i(TAG, "onCreated");
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		Log.i(TAG, "onTerminated");
	}

	public synchronized Twitter getTwitter() { //
		if (this.twitter == null) {
			String username = this.prefs.getString("username", "");
			String password = this.prefs.getString("password", "");
			String apiRoot = prefs.getString("apiRoot",
					"http://yamba.shan.com/api");
			if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)
					&& !TextUtils.isEmpty(apiRoot)) {
				this.twitter = new Twitter(username, password);
				this.twitter.setAPIRootUrl(apiRoot);
			}
		}
		return this.twitter;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub

	}

	public boolean isServiceRunning() {
		return serviceRunning;
	}

	public void setServiceRunning(boolean serviceRunning) {
		this.serviceRunning = serviceRunning;
	}

}
