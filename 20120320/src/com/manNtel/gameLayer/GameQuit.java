package com.manNtel.gameLayer;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.manNtel_mid.R;
import com.manNtel.activity.Game;
import com.manNtel.activity.GameSelect;
import com.manNtel.activity.Main;
import com.manNtel.database.DatabaseManager;
import com.manNtel.service.ProcessManager;
import com.manNtel.struct.GameStruct;

public class GameQuit extends Activity {
	private GameStruct mUser;
	SoundPool pool;
	int stream;
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
    	
    	TextView txtGameType = (TextView)findViewById(R.id.txtGameQuit);
    	switch(mUser.gameNum){
    	
    	case 1:
    	case 5:
    		txtGameType.setText(R.string.gameQuitOne);
    		break;
    	case 2:
    	case 3:
    		txtGameType.setText(R.string.gameQuitTwo);
    		break;
    	case 4:
    	case 6:
    		txtGameType.setText(R.string.gameQuitFour);
    		break;
    	}
    	
    	DatabaseManager dbm = new DatabaseManager(this);
    	dbm.open();
    	dbm.addItem(mUser);
    	dbm.close();    	
    	
    	ProcessManager.getInstance().addActivity(this);
    	
		//사운드 추가
		pool = new SoundPool(1,AudioManager.STREAM_MUSIC,0);

		pool.setOnLoadCompleteListener(mListener);
		pool.load(this, R.raw.ending,1);
    }
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		ProcessManager.getInstance().deleteActivity(this);
	}
	
	SoundPool.OnLoadCompleteListener mListener =
			new SoundPool.OnLoadCompleteListener() {
		@Override
		public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
			if(status ==0){
				stream = soundPool.play(sampleId, 1, 1, 0, 1, 1);
			}
		}
	};
	
	public void onClick(View v){
		pool.stop(stream);
		pool.release();
		
		switch(v.getId()){
		case R.id.btnMain : 
			Intent intent = new Intent(this,Main.class);
			startActivity(intent);
			break;
		case R.id.btnShutdown :
			ProcessManager.getInstance().allEndActivity();
			System.exit(1);
			break;
		case R.id.btnReplay : 
			Intent intent3 = new Intent(this,Game.class);
			intent3.putExtra("userInfo", mUser);
			startActivity(intent3);
			break;
		case R.id.btnPlayOther: 
			Intent intent2 = new Intent(this,GameSelect.class);
			intent2.putExtra("userInfo", mUser);
			startActivity(intent2);
			break;
		}
	}
}
