package com.shan.yamba;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.*;

public class UpdaterService extends Service {
	static final String TAG = "UpdaterService";
	static final int DELAY = 60000;
	private boolean runFlag = false;
	private Updater updater;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	public void onCreate() {
		super.onCreate();
		this.updater = new Updater();
		Log.d(TAG, "onCreated");
	}

	public int onStartCommand(Intent intent, int flags, int startId) { //
		this.runFlag = true;
		super.onStartCommand(intent, flags, startId);
		this.updater.start();
		Log.d(TAG, "onStarted");
		return START_STICKY;
	}

	public void onDestroy() {
		this.updater.interrupt();
		this.runFlag = false;
		super.onDestroy();
		Log.d(TAG, "onDestroyed");
	}

	private class Updater extends Thread {
		public Updater() {
			super("UpdaterService-Updater");
		}

		@Override
		public void run() { //
			UpdaterService updaterService = UpdaterService.this;
			while (updaterService.runFlag) {
				Log.d(TAG, "Updater running");
				try {
					// Some work goes here...
					Log.d(TAG, "Updater ran");
					Thread.sleep(DELAY);
				} catch (InterruptedException e) { //
					updaterService.runFlag = false;
				}
			}
		}
	}
}
