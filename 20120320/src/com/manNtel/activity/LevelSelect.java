package com.manNtel.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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
		Log.i("[LevelSelect]","Clear Value : " + user.clearValue);
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
        
        switch(user.level)
        {
        case 1 : 
        	Button bt1 = (Button)findViewById(R.id.btnLevel1);
        	bt1.setBackgroundResource(R.drawable.btn_on);
        	break;
        case 2 : 
        	Button bt2 = (Button)findViewById(R.id.btnLevel2);
        	bt2.setBackgroundResource(R.drawable.btn_on);
        	break;
        case 3 : 
        	Button bt3 = (Button)findViewById(R.id.btnLevel3);
        	bt3.setBackgroundResource(R.drawable.btn_on);
        	break;
        case 4 : 
        	Button bt4 = (Button)findViewById(R.id.btnLevel4);
        	bt4.setBackgroundResource(R.drawable.btn_on);
        	break;
        case 5 : 
        	Button bt5 = (Button)findViewById(R.id.btnLevel5);
        	bt5.setBackgroundResource(R.drawable.btn_on);
        	break;
        }
        
        ProcessManager.getInstance().addActivity(this);
    }
	@Override
	public void onDestroy(){
		super.onDestroy();
		ProcessManager.getInstance().deleteActivity(this);
	}
}
