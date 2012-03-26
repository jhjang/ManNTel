package com.manNtel.gameLayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.manNtel_mid.R;
import com.manNtel.activity.Game;
import com.manNtel.activity.GameSelect;
import com.manNtel.activity.Main;
import com.manNtel.database.DatabaseManager;
import com.manNtel.struct.GameStruct;

public class GameQuit extends Activity {
	private GameStruct mUser;
	@Override
	public void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
    	
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    	
    	Bundle bundle = getIntent().getExtras();
		mUser = bundle.getParcelable("userInfo");
   	
    	setContentView(R.layout.gamequit);
    	
    	TextView txtName = (TextView)findViewById(R.id.txtName);    	
    	txtName.setText(mUser.name);
    	
    	TextView txtPlayTime = (TextView)findViewById(R.id.txtPlayTime);
    	txtPlayTime.setText(mUser.playTime);
    	
    	TextView txtScore = (TextView)findViewById(R.id.txtScore);
    	txtScore.setText(String.valueOf(mUser.score));  	
    	
    	DatabaseManager dbm = new DatabaseManager(this);
    	dbm.open();
    	dbm.addItem(mUser);
    	Log.i("[GameQuit]","DataSaved");
    	dbm.close();    	
    }
	
	public void onClick(View v){
		switch(v.getId()){
		case R.id.btnMain : 
			Log.i("[GameEnd]","Main");
			Intent intent = new Intent(this,Main.class);
			startActivity(intent);
			break;
		case R.id.btnShutdown :
			Log.i("[GameEnd]","shut");
			break;
		case R.id.btnReplay : 
			Log.i("[GameEnd]","rE");
			Intent intent3 = new Intent(this,Game.class);
			intent3.putExtra("userInfo", mUser);
			startActivity(intent3);
			break;
		case R.id.btnPlayOther: 
			Log.i("[GameEnd]","Ohter");
			Intent intent2 = new Intent(this,GameSelect.class);
			intent2.putExtra("userInfo", mUser);
			startActivity(intent2);
			break;
		}
	}
}
