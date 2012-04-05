package com.manNtel.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.manNtel_mid.R;
import com.manNtel.service.ProcessManager;
import com.manNtel.struct.GameStruct;

public class LevelSelect extends Activity 
{
	GameStruct user;
	public void btnLevelSel(View v)
	{		
		SharedPreferences pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
		String fromPrefValue = null;
				
		switch(v.getId())
		{
		case R.id.btnLevel1:
			user.level = 1;
			fromPrefValue = pref.getString("levelOpt1", "40");			
			break;			
		case R.id.btnLevel2:
			user.level = 2;			
			fromPrefValue = pref.getString("levelOpt2", "50");
			break;		
		case R.id.btnLevel3:
			user.level = 3;			
			fromPrefValue = pref.getString("levelOpt3", "60");
			break;		
		case R.id.btnLevel4:
			user.level = 4;	
			fromPrefValue = pref.getString("levelOpt4", "70");
			break;		
		case R.id.btnLevel5:
			user.level = 5;			
			fromPrefValue = pref.getString("levelOpt5", "80");
			break;		
		}
		float clearValue = Float.parseFloat(fromPrefValue);
		
		user.clearValue = (user.leftBal + user.rightBal) * (clearValue/100);  
		
		Intent intent = new Intent(this, Tutorial.class);
		intent.putExtra("userInfo", user);		
		
		startActivity(intent);
	}
	
	public void btnMenu(View v)
	{
		switch(v.getId())
		{
		case R.id.btnGoMain:
			//메인화면
			startActivity(new Intent(this,Main.class));
			break;
		case R.id.btnShutdown:
			//시스템 종료
			break;
		case R.id.btnOtherUser:
			startActivity(new Intent(this,UserList.class));
			break;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);   
        setContentView(R.layout.levelselect);
        
        Bundle bundle = getIntent().getExtras();
        user = bundle.getParcelable("userInfo");
        
        TextView welcome = (TextView)findViewById(R.id.txtWelcome);
        welcome.setText(user.name);  
        
        ProcessManager.getInstance().addActivity(this);
    }
	@Override
	public void onDestroy(){
		super.onDestroy();
		ProcessManager.getInstance().deleteActivity(this);
	}
}
