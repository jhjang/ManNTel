package com.manNtel.valueSetting;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.manNtel_mid.R;
import com.manNtel.activity.Main;
import com.manNtel.database.DatabaseManager;
import com.manNtel.service.SharedDataService;
import com.manNtel.struct.UserInfoStruct;

public class Slide extends Activity 
{
	UserInfoStruct newUser;
	Timer mTimer = null;
	Handler handler = new Handler();
	public CountDownTimer cdt = new CountDownTimer(6000,1000)
	{
		int i = 5;

		@Override
		public void onFinish()
		{
			TextView cnt = (TextView)findViewById(R.id.textCnt);
			cnt.setText(R.string.valComplete);
			getSliding();
		}

		@Override
		public void onTick(long millisUntilFinished)
		{
			TextView cnt = (TextView)findViewById(R.id.textCnt);
			cnt.setText(Integer.toString(i--));
			getSliding();
		}		
	};

	//피칭 읽어오는 함수
	public void getSliding()
	{
		TextView sliding = (TextView)findViewById(R.id.textSliding);
		int valueSlide = -1;	
		//무게 소켓에서 읽어오는 부분 필요
		SharedDataService ds = (SharedDataService)getApplication();
		if(newUser.mPart.equals("좌")){
			valueSlide= (int)ds.getDataService().getValue("SL");
		}
		else
			valueSlide= (int)ds.getDataService().getValue("SR");

		sliding.setText(Integer.toString(valueSlide));

		newUser.mMaxSlide = valueSlide;

		newUser.printInfo();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.setsliding);

		cdt.start();

		//디버그 모드 처리       
		SharedPreferences pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
		if(pref.getBoolean("isDebug", false)){
			LinearLayout debugLayout = (LinearLayout)findViewById(R.id.layoutDebug);
			debugLayout.setVisibility(View.VISIBLE);
			Log.i("[Balance]","Debug On");

			TimerTask debugTask = new TimerTask(){
				@Override
				public void run(){
					try{
						Log.i("[Balance]","Debugging");

						handler.post(new Runnable() {
							@Override
							public void run(){
								TextView debug1 = (TextView)findViewById(R.id.txtDebug1);
								TextView debug2 = (TextView)findViewById(R.id.txtDebug2);
								TextView debug3 = (TextView)findViewById(R.id.txtDebug3);                				        				

								debug1.setText(debug2.getText().toString());
								debug2.setText(debug3.getText().toString());
								SharedDataService ds = (SharedDataService)getApplication();

								SimpleDateFormat sd = new SimpleDateFormat("yyyy MM dd-HH:mm:ss");		
								Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());      				 

								debug3.setText(sd.format(currentTimestamp) + "  :  " + ds.getDataService().getFullData());
							}
						});        				        				       				
					} catch(Exception e) { e.printStackTrace(); }
				}
			};        	
			mTimer = new Timer();
			mTimer.schedule(debugTask, 0, 1000);        	
		}           

		Bundle bundle = getIntent().getExtras();
		newUser = bundle.getParcelable("userInfo");        
	}

	public void popup(View v)
	{
		if(mTimer !=null){
			mTimer.cancel();
			mTimer.purge();
		}
		switch (v.getId())
		{
		case R.id.btnPrev:
			Intent intent = new Intent();
			setResult(RESULT_OK,intent);
			finish();			
			break;
		case R.id.btnOK:
			Log.i("[BtnTouch]","OK");
			//최근 접속시간을 ID생성시간으로 설정
			SimpleDateFormat sd = new SimpleDateFormat("yyyy.MM.dd. HH:mm");	
			Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());			

			newUser.mRecent = sd.format(currentTimestamp);
			//사용자 DB에 추가
			DatabaseManager dbm = new DatabaseManager(this);
			dbm.open();
			dbm.addItem(newUser);
			dbm.close();
			Intent goMain = new Intent(this,Main.class);
			goMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(goMain);	

			break;
		case R.id.btnClose:
			Log.i("[BtnTouch","close");
			startActivity(new Intent(this,Main.class));
			break;
		}	
	}
}
