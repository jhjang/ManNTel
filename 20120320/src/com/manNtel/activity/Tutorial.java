package com.manNtel.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.manNtel_mid.R;
import com.manNtel.service.ProcessManager;
import com.manNtel.struct.GameStruct;

public class Tutorial extends Activity 
{
	GameStruct user;
	SoundPool pool;
	int stream;

	public void btnGame(View v)
	{
		pool.stop(stream);
		pool.release();
		switch(v.getId())
		{
		case R.id.btnStart :
			//°ÔÀÓ ½ÃÀÛ
			Intent intent = new Intent(this, Game.class);
			intent.putExtra("userInfo", user);
			startActivity(intent);
			break;
		case R.id.btnChoiceGame:
			//°ÔÀÓ ¼±ÅÃ
			Intent intent2 = new Intent(this,GameSelect.class);
			intent2.putExtra("userInfo", user);
			startActivity(intent2);
			break;
		}
	}

	public void btnMenu(View v)
	{
		pool.stop(stream);
		pool.release();
		
		switch(v.getId()){
		case R.id.btnGoMain :
			startActivity(new Intent(this, Main.class));
			break;

		case R.id.btnPrevPage : 
			finish();
			break;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tutorial);


		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);   

		Bundle bundle = getIntent().getExtras();
		user = bundle.getParcelable("userInfo");

		TextView tuto1 = (TextView)findViewById(R.id.txtTuto1);
		TextView tuto2 = (TextView)findViewById(R.id.txtTuto2);
		ImageView imgTuto = (ImageView)findViewById(R.id.imgTuto);


		TextView welcome = (TextView)findViewById(R.id.txtWelcome);
		welcome.setText(user.name);

		switch(user.gameNum)
		{
		case 1 : 
			tuto1.setText(R.string.tutGame1_1);
			if(user.part.equals("ÁÂ")){
				tuto2.setText(R.string.tutGame1_2_Left);
				imgTuto.setImageResource(R.drawable.guidegame1left);
			}

			else{
				tuto2.setText(R.string.tutGame1_2_Right);
				imgTuto.setImageResource(R.drawable.guidegame1right);
			}			

			break;
		case 2 : 
			tuto1.setText(R.string.tutGame2_1);
			if(user.part.equals("ÁÂ")){
				tuto2.setText(R.string.tutGame2_2_Left);
				imgTuto.setImageResource(R.drawable.guidegame2left);
			}

			else{
				tuto2.setText(R.string.tutGame2_2_Right);
				imgTuto.setImageResource(R.drawable.guidegame2right);
			}			
			break;
		case 3 : 
			tuto1.setText(R.string.tutGame3_1);
			if(user.part.equals("ÁÂ")){
				tuto2.setText(R.string.tutGame3_2_Left);
				imgTuto.setImageResource(R.drawable.guidegame3left);
			}			

			else{
				tuto2.setText(R.string.tutGame3_2_Right);
				imgTuto.setImageResource(R.drawable.guidegame3right);
			}
			break;
		case 4 : 
			tuto1.setText(R.string.tutGame4_1);
			if(user.part.equals("ÁÂ")){
				tuto2.setText(R.string.tutGame4_2_Left);
				imgTuto.setImageResource(R.drawable.guidegame4left);
			}				
			else{
				tuto2.setText(R.string.tutGame4_2_Right);
				imgTuto.setImageResource(R.drawable.guidegame4right);
			}			
			break;
		case 5 : 
			tuto1.setText(R.string.tutGame5_1);
			if(user.part.equals("ÁÂ")){
				tuto2.setText(R.string.tutGame5_2_Left);
				imgTuto.setImageResource(R.drawable.guidegame5left);
			}

			else{
				tuto2.setText(R.string.tutGame5_2_Right);
				imgTuto.setImageResource(R.drawable.guidegame5right);
			}		

			break;
		case 6 : 
			tuto1.setText(R.string.tutGame6_1);
			if(user.part.equals("ÁÂ")){
				tuto2.setText(R.string.tutGame6_2_Left);
				imgTuto.setImageResource(R.drawable.guidegame6left);
			}				
			else{
				tuto2.setText(R.string.tutGame6_2_Right);
				imgTuto.setImageResource(R.drawable.guidegame6right);
			}			
			break;
		}
		ProcessManager.getInstance().addActivity(this);

		//»ç¿îµå Ãß°¡
		pool = new SoundPool(1,AudioManager.STREAM_MUSIC,0);

		pool.setOnLoadCompleteListener(mListener);
		pool.load(this, R.raw.playing,1);
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

	@Override
	public void onDestroy(){
		super.onDestroy();
		ProcessManager.getInstance().deleteActivity(this);
	}
}
