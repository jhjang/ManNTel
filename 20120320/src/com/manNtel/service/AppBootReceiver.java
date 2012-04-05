package com.manNtel.service;

import com.manNtel.activity.Main;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AppBootReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent i = new Intent(context, Main.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		context.startActivity(i);	
	}
}