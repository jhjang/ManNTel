package com.manNtel.setting;

import android.app.TabActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.TextView;

import com.android.manNtel_mid.R;

public class GameSetting extends TabActivity 
{	
	TabHost mTab;
	SeekBar mSeekBar;
	TextView mSeekValue;
	EditText edtGameOpt1,edtGameOpt2,edtGameOpt3,edtGameOpt4,edtGameOpt5,edtGameOpt6;
	EditText edtLevelOpt1,edtLevelOpt2,edtLevelOpt3,edtLevelOpt4,edtLevelOpt5;
	public void btnEvent(View v)
	{
		switch(v.getId())
		{
		case R.id.btnSave : 
			updatePref();
			break;
			
		case R.id.btnClose :
			finish();
			break;
		}
	}
	
	@Override    
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        mTab = getTabHost();
        
        SharedPreferences pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
                       
        LayoutInflater inflater = LayoutInflater.from(this);
        inflater.inflate(R.layout.gamesetting, mTab.getTabContentView(), true);
        
        mTab.addTab(mTab.newTabSpec("tag").setIndicator("게임 설정").setContent(R.id.setGameLayout));        
        mTab.addTab(mTab.newTabSpec("tag").setIndicator("난이도 설정").setContent(R.id.setLevelLayout));
        mTab.addTab(mTab.newTabSpec("tag").setIndicator("Sliding 완급 조절").setContent(R.id.setSlideLayout));
        
        mSeekBar = (SeekBar)findViewById(R.id.slideSeekBar);
        mSeekValue = (TextView)findViewById(R.id.slideSeekValue);
        
        mSeekValue.setText("Value : " + mSeekBar.getProgress());
        
        //게임 2 
        edtGameOpt1 = (EditText)findViewById(R.id.edtSetGame1);
        edtGameOpt1.setText(pref.getString("gameOpt1", "5"));
        
        //게임 3 
        edtGameOpt2 = (EditText)findViewById(R.id.edtSetGame2);
        edtGameOpt2.setText(pref.getString("gameOpt2", "5"));
        
        //게임 3
        edtGameOpt3 = (EditText)findViewById(R.id.edtSetGame3);
        edtGameOpt3.setText(pref.getString("gameOpt3", "80"));
        
        //게임 4
        edtGameOpt4 = (EditText)findViewById(R.id.edtSetGame4);
        edtGameOpt4.setText(pref.getString("gameOpt4", "80"));
        
        //게임 5
        edtGameOpt5 = (EditText)findViewById(R.id.edtSetGame5);
        edtGameOpt5.setText(pref.getString("gameOpt5", "80"));
        
        //게임 6
        edtGameOpt6 = (EditText)findViewById(R.id.edtSetGame6);
        edtGameOpt6.setText(pref.getString("gameOpt6", "80"));
        
        
        edtLevelOpt1 = (EditText)findViewById(R.id.edtSetLevel1);
        edtLevelOpt1.setText(pref.getString("levelOpt1", "40"));
        
        edtLevelOpt2 = (EditText)findViewById(R.id.edtSetLevel2);
        edtLevelOpt2.setText(pref.getString("levelOpt2", "50"));
        
        edtLevelOpt3 = (EditText)findViewById(R.id.edtSetLevel3);
        edtLevelOpt3.setText(pref.getString("levelOpt3", "60"));
        
        edtLevelOpt4 = (EditText)findViewById(R.id.edtSetLevel4);
        edtLevelOpt4.setText(pref.getString("levelOpt4", "70"));
        
        edtLevelOpt5 = (EditText)findViewById(R.id.edtSetLevel5);
        edtLevelOpt5.setText(pref.getString("levelOpt5", "80"));

        
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) 
			{
				mSeekValue.setText("Value : " + progress);
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {	}
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
		});
    }
	
	private void updatePref()
	{
		SharedPreferences pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		
		editor.putString("gameOpt1", edtGameOpt1.getText().toString());
		editor.putString("gameOpt2", edtGameOpt2.getText().toString());
		editor.putString("gameOpt3", edtGameOpt3.getText().toString());
		editor.putString("gameOpt4", edtGameOpt4.getText().toString());
		editor.putString("gameOpt5", edtGameOpt5.getText().toString());
		editor.putString("gameOpt6", edtGameOpt6.getText().toString());
		
		editor.putString("levelOpt1", edtLevelOpt1.getText().toString());
		editor.putString("levelOpt2", edtLevelOpt2.getText().toString());
		editor.putString("levelOpt3", edtLevelOpt3.getText().toString());
		editor.putString("levelOpt4", edtLevelOpt4.getText().toString());
		editor.putString("levelOpt5", edtLevelOpt5.getText().toString());
				
		editor.commit();
		
		//슬라이드 값 업데이트 구현
	}
	
	
}