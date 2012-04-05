package com.manNtel.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.manNtel_mid.R;
import com.manNtel.service.ProcessManager;
import com.manNtel.struct.GameStruct;

public class GameSelect extends Activity 
{
	GameStruct user;

	public void btnGameSel(View v)
	{
		switch(v.getId())
		{
		case R.id.btnGame1:
			user.gameNum = 1;			
			break;			
		case R.id.btnGame2:
			user.gameNum = 2;
			break;
		case R.id.btnGame3:
			user.gameNum = 3;
			break;
		case R.id.btnGame4:
			user.gameNum = 4;
			break;
		case R.id.btnGame5:
			user.gameNum = 5;
			break;
		case R.id.btnGame6:
			user.gameNum = 6;
			break;	
		}
		Intent intent = new Intent(this, LevelSelect.class);
		intent.putExtra("userInfo", user);
		startActivity(intent);
	}
	
	public void btnMenu(View v)
	{
		switch(v.getId())
		{
		case R.id.btnGoMain:
			//����ȭ��
			startActivity(new Intent(this,Main.class));
			break;
		case R.id.btnShutdown:
			//�ý��� ����
			ProcessManager.getInstance().allEndActivity();
			android.os.Process.killProcess(android.os.Process.myPid());
			break;
		case R.id.btnPrevPage:
			Intent intent = new Intent(this,Login.class);
			intent.putExtra("userKey", user.key);
			intent.putExtra("userName", user.name);
					
			startActivity(intent);
			
			break;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);   
		setContentView(R.layout.gameselect);

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