package com.shan.yamba;

import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.*;
import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import winterwell.jtwitter.TwitterList;

public class UpdaterService extends Service {
	static final String TAG = "UpdaterService";
	static final int DELAY = 60000;
	private boolean runFlag = false;
	private Updater updater;
	private YambaApplication yamba;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	public void onCreate() {
		super.onCreate();
		this.updater = new Updater();
		this.yamba = (YambaApplication) getApplication();
		Log.d(TAG, "onCreated");
	}

	public int onStartCommand(Intent intent, int flags, int startId) { //
		this.runFlag = true;
		super.onStartCommand(intent, flags, startId);
		this.updater.start();
		this.yamba.setServiceRunning(true);
		Log.d(TAG, "onStarted");

		return START_STICKY;
	}

	public void onDestroy() {
		this.updater.interrupt();
		this.runFlag = false;
		this.yamba.setServiceRunning(false);
		super.onDestroy();
		Log.d(TAG, "onDestroyed");
	}

	private class Updater extends Thread {
		// Have To Replace Twitter.Status to winterwell.jtwitter.Status
		List<winterwell.jtwitter.Status> timeline;

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
					try {
						timeline = yamba.getTwitter().getFriendsTimeline(); 
					} catch (TwitterException e) {
						Log.e(TAG, "Failed to connect to twitter service", e);
					}
					//looping to get the twittaahhhh
					for (winterwell.jtwitter.Status status : timeline) { //
						Log.d(TAG, String.format("%s: %s", status.user.name,
								status.text)); //
					}
					Log.d(TAG, "Updater ran");
					Thread.sleep(DELAY);
				} catch (InterruptedException e) { //
					updaterService.runFlag = false;
				}
			}
		}
	}
}
