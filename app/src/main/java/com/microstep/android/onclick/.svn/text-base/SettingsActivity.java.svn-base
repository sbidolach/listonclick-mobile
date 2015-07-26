package com.microstep.android.onclick;

import com.microstep.android.onclick.util.Language;
import com.microstep.android.onclick.util.Utils;
import com.microstep.onclick.R;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener, OnClickListener {

	private String listCurrency;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);		
		addPreferencesFromResource(R.xml.preferences);
		setContentView(R.layout.preferences_layout);
		Button back = (Button) this.findViewById(R.id.bn_back);
		back.setOnClickListener(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		System.out.println("listCurrency=" + listCurrency);
		getPrefs();
	}	

	private void getPrefs() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		listCurrency = prefs.getString("currencyPref", Utils.getDefaulCurrency(this));
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
	}

	public String getListCurrency() {
		return listCurrency;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.bn_back:
				finish();
				break;
		}
	}
}
