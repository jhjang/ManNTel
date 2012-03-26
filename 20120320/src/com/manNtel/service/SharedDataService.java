package com.manNtel.service;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.manNtel.service.DataService.DataBinder;
import com.manNtel.struct.GameStruct;

public class SharedDataService extends Application 
{
	private static DataService mService;	
	private static boolean mBound = false;
	private static ComponentName mComponent; 
	private static GameStruct sharedGameInfo;

	private ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName className,	IBinder service) {
			// We've bound to MyService, cast the IBinder and get MyService instance
			DataBinder binder = (DataBinder) service;
			mService = binder.getService();
			mBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;
		}
	};
	
	public void setSharedGameInfo(GameStruct _gameInfo){
		sharedGameInfo = _gameInfo;
	}
	
	public GameStruct sharedGameInfo(){
		return sharedGameInfo;
	}

	public boolean getBound()
	{
		return mBound;
	}

	public DataService getDataService()
	{
		return mService;
	}

	public void killProcess()
	{
		ActivityManager am = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
		am.restartPackage(getPackageName());
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		Intent intent = new Intent(this,DataService.class);
		mComponent = startService(intent);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}
	
	@Override
	public void onTerminate()
	{
		super.onTerminate();
		if (mBound) {
			unbindService(mConnection);
			mBound = false;
		}

		Intent i = new Intent();
		i.setComponent(mComponent);
		stopService(i);
	}

	public void stopService()
	{
		mService.stopService();
	}
}
