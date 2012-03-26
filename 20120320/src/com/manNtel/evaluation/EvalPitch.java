package com.manNtel.evaluation;

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
import com.manNtel.service.SharedDataService;
import com.manNtel.struct.EvalStruct;

public class EvalPitch extends Activity 
{
	EvalStruct user;
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
			getPitching();
		}
		
		@Override
		public void onTick(long millisUntilFinished)
		{
			TextView cnt = (TextView)findViewById(R.id.textCnt);
			cnt.setText(Integer.toString(i--));
			getPitching();
		}		
	};
	
	//��Ī �о���� �Լ�
	public void getPitching()
	{
		TextView pitching = (TextView)findViewById(R.id.textPitching);
				
		//���� ���Ͽ��� �о���� �κ� �ʿ�
		SharedDataService ds = (SharedDataService)getApplication();
		
		int valuePitch = (int)ds.getDataService().getValue("PS");

			
		
		pitching.setText(Integer.toString(valuePitch));
		
		user.mPitching = valuePitch;
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
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);		
		
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.setpitching);
        
        cdt.start();
        
      //����� ��� ó��       
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
        user = bundle.getParcelable("userInfo");        
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
			Log.i("[BtnTouch","next");
			Intent goSlide = new Intent(this,EvalSlide.class);
			goSlide.putExtra("userInfo", user);
			startActivityForResult(goSlide, 3);
			break;
		case R.id.btnClose:
			Log.i("[BtnTouch","close");
			break;
		}	
	}
}