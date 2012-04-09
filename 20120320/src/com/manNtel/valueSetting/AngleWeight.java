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
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.manNtel_mid.R;
import com.manNtel.activity.Main;
import com.manNtel.service.ProcessManager;
import com.manNtel.service.SharedDataService;
import com.manNtel.struct.UserInfoStruct;

public class AngleWeight extends Activity 
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
			getAngleWeight();
		}

		@Override
		public void onTick(long millisUntilFinished)
		{
			TextView cnt = (TextView)findViewById(R.id.textCnt);
			cnt.setText(Integer.toString(i--));
			getAngleWeight();
		}		
	};

	//각도 / 하중 읽어오는 함수
	public void getAngleWeight()
	{
		TextView angle = (TextView)findViewById(R.id.textAngle);
		TextView weight = (TextView)findViewById(R.id.textWeight);

		//무게 소켓에서 읽어오는 부분 필요
		SharedDataService ds = (SharedDataService)getApplication();

		float valueAngle = ds.getDataService().getValue("TS");
		float valueWeight = -1;
		if(newUser.mPart.equals("좌"))
		{
			valueWeight = ds.getDataService().getValue("LL");
		}
		else
		{
			valueWeight = ds.getDataService().getValue("RL");
		}

//		angle.setText(Float.toString(valueAngle));
		angle.setText("사용안함");
		weight.setText(Float.toString(valueWeight));

		newUser.mAngle = valueAngle;
		newUser.mWeight = valueWeight;
	}

	@Override
	protected void onActivityResult(int requestCode,int resultCode, Intent data)
	{
		if(resultCode == RESULT_OK)
		{
			Intent intent = getIntent();
			finish();
			startActivity(intent);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.setangleweight);

		cdt.start();

		//디버그 모드 처리       
		SharedPreferences pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
		if(pref.getBoolean("isDebug", false)){
			LinearLayout debugLayout = (LinearLayout)findViewById(R.id.layoutDebug);
			debugLayout.setVisibility(View.VISIBLE);

			TimerTask debugTask = new TimerTask(){
				@Override
				public void run(){
					try{

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
		
		ProcessManager.getInstance().addActivity(this);
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		ProcessManager.getInstance().deleteActivity(this);
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
		case R.id.btnNext:
			Intent goSetRoll = new Intent(this,Roll.class);
			goSetRoll.putExtra("userInfo", newUser);			
			startActivityForResult(goSetRoll, 2);			
			break;
		case R.id.btnClose:			
			startActivity(new Intent(this,Main.class));
			break;
		}	
	}

}