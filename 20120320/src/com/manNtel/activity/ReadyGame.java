package com.manNtel.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.manNtel_mid.R;
import com.manNtel.service.ProcessManager;
import com.manNtel.service.SharedDataService;
import com.manNtel.struct.GameStruct;
import com.manNtel.valueSetting.GameBalance;

public class ReadyGame extends Activity 
{
	GameStruct user;
	public CountDownTimer cdt = new CountDownTimer(4000,1000)
	{
		int i = 3;
		
		@Override
		public void onFinish()
		{
			TextView cnt = (TextView)findViewById(R.id.textCnt);
			cnt.setText(R.string.bal_stanceReady);
		}

		@Override
		public void onTick(long millisUntilFinished)
		{
			TextView cnt = (TextView)findViewById(R.id.textCnt);
			cnt.setText(Integer.toString(i));
			i--;
		}		
	};
	
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
        
        setContentView(R.layout.waitbalance);
        
        Bundle bundle = getIntent().getExtras();
        user = bundle.getParcelable("userInfo");        
        
        cdt.start();
        try{
        	Log.i("[PART]",user.part);
        }catch (NullPointerException e) 
        { 
        	SharedDataService sds = (SharedDataService)getApplicationContext();
        	user = sds.sharedGameInfo();
        	Log.i("part]","user null");
        	Log.i("[ReadyGame]", " " + user.part);
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
		switch (v.getId())
		{
		case R.id.btnPrev:
			finish();
			break;
		case R.id.btnNext:
			Intent intent = new Intent(this,GameBalance.class); 
    		intent.putExtra("userInfo", user);
    		startActivityForResult(intent,0);		
			break;
		case R.id.btnClose:	
			finish();
			break;
		}	
	}
}
