package com.manNtel.service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class DataService extends Service
{
	public class DataBinder extends Binder
	{
		public DataService getService()
		{
			return DataService.this;
		}
	}
	private final IBinder mBinder = new DataBinder();
	private Socket mSocket = null;

	private String mData[] = null;
	private String mFullData = null;

	private boolean mRunning;
	private String mLoadedData;

	private Runnable mRunnable = new Runnable()
	{
		@Override
		public void run()
		{
			if(mRunning)
			{
				String splitData[] = receiveData().split("\\n");
				mFullData = splitData[0];
				if(splitData[0].contains("LOAD")){
					mLoadedData = splitData[0];		
					Log.i("[DataService]","Loaded Data : " + mLoadedData);
				} else{
					setData(splitData[0]);
				}

				new Thread(mRunnable).start();
			}
		}
	};	

	public float getValue(String type)
	{		
		float foundValue = -1;		

		for(int i=1; i<mData.length;i+=3)
		{
			if(mData[i].equals(type))
			{
				foundValue = Float.parseFloat(mData[i+2]);
				break;
			}			
		}
		return foundValue;
	}

	public String getDigital(String type)
	{		
		String foundValue = "NULL";		

		for(int i=1; i<mData.length;i+=3)
		{
			if(mData[i].equals(type))
			{
				foundValue = mData[i+1];
				break;
			}			
		}
		
		if(type.equals("PS")){
			int tmp = Integer.parseInt(foundValue) + 26;
			foundValue = String.valueOf(tmp);
		}
		
		return foundValue;
	}



	@Override
	public IBinder onBind(Intent intent)
	{
		return mBinder;
	}

	@Override
	public void onCreate()
	{
		Log.e("[DataService]","Service Created");
		super.onCreate();

		try {
			mSocket = new Socket("localhost",2580);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		mRunning = false;		
	}

	@Override
	public void onDestroy()
	{
		mRunning = false;
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId)
	{
		Log.e("[DataService]","Service Started");

		super.onStart(intent, startId);

		if(!mRunning)
		{
			new Thread(mRunnable).start();
			mRunning = true;
		}
	}

	private String receiveData()
	{
		byte buffer[] = new byte[1024];
		int readByte = -1;
		InputStream is;
		try {
			is = mSocket.getInputStream();
			readByte = is.read(buffer);				
		} catch (IOException e) 
		{
			e.printStackTrace();
		} 
		if(readByte >= 0)
			return new String(buffer,0,1024);
		else
			return null;
	}

	private void setData(String _data)
	{
		mData = _data.split(",");		
		
	}

	public void stopService()
	{	
		mRunning = false;
		stopSelf();
	}
	
	public String getLoadedData(){
		return mLoadedData;
	}

	public String getFullData(){
		return mFullData;
	}

	public void sendMsg(String msg) {
		Log.i("[DataService]","Rcv Msg : " + msg);
		try {
			if(!msg.contains("LOAD")){
				msg = "SAVE," + msg;
			}
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream())),true);
			out.println(msg);
			Log.i("[DataService]","Sended Msg : " + msg);
		} catch (IOException e) {			
			e.printStackTrace();
		}		
	}	
}