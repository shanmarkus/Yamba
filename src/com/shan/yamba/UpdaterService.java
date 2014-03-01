package com.shan.yamba;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.*;

public class UpdaterService extends Service {
	static final String TAG = "UpdaterService";

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreated");
	}

	public int onStartCommand(Intent intent, int flags, int startId) { //
		super.onStartCommand(intent, flags, startId);
		Log.d(TAG, "onStarted");
		return START_STICKY;
	}
	
	public void onDestroy(){
		super.onDestroy();
		Log.d(TAG, "onDestroy");
	}

}
