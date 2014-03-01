package com.shan.yamba;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class StatusActivity extends Activity implements OnClickListener,
		TextWatcher, OnSharedPreferenceChangeListener {
	private static final String TAG = "StatusActivity";
	EditText editText;
	Button updateButton;
	Twitter twitter;
	TextView textCount;
	SharedPreferences prefs;//

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater(); //
		inflater.inflate(R.menu.menu, menu); //
		return true; //
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) { //
		case R.id.itemPrefs:
			startActivity(new Intent(this, PrefsActivity.class)); //
			break;
		}
		return true; //
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.status);

		editText = (EditText) findViewById(R.id.editText);
		updateButton = (Button) findViewById(R.id.buttonUpdate);

		updateButton.setOnClickListener(this);
		textCount = (TextView) findViewById(R.id.textCount);
		textCount.setText(Integer.toString(140));
		textCount.setTextColor(Color.GREEN);
		editText.addTextChangedListener(this);
		prefs = PreferenceManager.getDefaultSharedPreferences(this); //
		prefs.registerOnSharedPreferenceChangeListener(this);

		twitter = new Twitter("student", "password");
		twitter.setAPIRootUrl("http://yamba.shan.com/api");
	}

	private Twitter getTwitter() {
		if (twitter == null) { //
			String username, password, apiRoot;
			username = prefs.getString("username", ""); //
			password = prefs.getString("password", "");
			apiRoot = prefs.getString("apiRoot", "http://yamba.shan.com/api");
			// Connect to twitter.com
			twitter = new Twitter(username, password); //
			twitter.setAPIRootUrl(apiRoot); //
		}
		return twitter;
	}

	class PostToTwitter extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... statuses) {
			// TODO Auto-generated method stub
			try {
				winterwell.jtwitter.Status status = twitter
						.updateStatus(statuses[0]);
				return status.text;
			} catch (TwitterException e) {
				Log.e(TAG, e.toString());
				e.printStackTrace();
				return "Failed to post";
			}
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			Toast.makeText(StatusActivity.this, result, Toast.LENGTH_LONG);
		}

	}

	public void onClick(View v) {
		try {
			getTwitter().setStatus(editText.getText().toString());
		} catch (TwitterException e) {
			Log.d(TAG, "Twitter setStatus failed: " + e);
		}
	}

	// TextWatcher methods
	public void afterTextChanged(Editable statusText) { //
		int count = 140 - statusText.length(); //
		textCount.setText(Integer.toString(count));
		textCount.setTextColor(Color.GREEN); //
		if (count < 10)
			textCount.setTextColor(Color.YELLOW);
		if (count < 0)
			textCount.setTextColor(Color.RED);
	}

	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) { //
	}

	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) { 
		twitter = null;
	}
}
