package com.manNtel.evaluation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.manNtel_mid.R;
import com.manNtel.service.ProcessManager;
import com.manNtel.struct.EvalStruct;

public class EvalReadyRoll extends Activity {
	EvalStruct user = null;
	
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
        
        cdt.start();
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
			Bundle bundle = getIntent().getExtras();
	        user = bundle.getParcelable("userInfo"); 
	        Intent intent = new Intent(this,EvalRoll.class);
			intent.putExtra("userInfo", user);
			
			startActivityForResult(intent,0);break;
			
		case R.id.btnClose:
			finish();
			break;
		}	
	}	
}
