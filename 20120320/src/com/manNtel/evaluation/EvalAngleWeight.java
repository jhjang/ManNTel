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
import com.manNtel.activity.Main;
import com.manNtel.service.ProcessManager;
import com.manNtel.service.SharedDataService;
import com.manNtel.struct.EvalStruct;

public class EvalAngleWeight extends Activity 
{
	EvalStruct user = null;
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

	//���� / ���� �о���� �Լ�
	public void getAngleWeight()
	{
		TextView angle = (TextView)findViewById(R.id.textAngle);
		TextView weight = (TextView)findViewById(R.id.textWeight);

		//���� ���Ͽ��� �о���� �κ� �ʿ�
		SharedDataService ds = (SharedDataService)getApplication();

		int valueAngle = (int)ds.getDataService().getValue("TL");
		int valueWeight = -1;

		if(user.mPart.equals("��"))
		{
			valueWeight = (int)ds.getDataService().getValue("LL");
		}
		else
		{
			valueWeight = (int)ds.getDataService().getValue("RL");
		}	

		angle.setText(Float.toString(valueAngle));
		weight.setText(Float.toString(valueWeight));

		user.mAngle = valueAngle;
		user.mWeight = valueWeight;
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

		setContentView(R.layout.setangleweight);

		Bundle bundle = getIntent().getExtras();
		user = bundle.getParcelable("userInfo");

		Log.i("[Data]",user.mPart);

		cdt.start();

		//����� ��� ó��       
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

			Intent goSetRoll = new Intent(this,EvalReadyRoll.class);
			goSetRoll.putExtra("userInfo", user);			
			startActivityForResult(goSetRoll, 2);			
			break;
		case R.id.btnClose:			
			startActivity(new Intent(this,Main.class));
			break;
		}	
	}
}