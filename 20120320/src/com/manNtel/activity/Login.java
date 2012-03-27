package com.manNtel.activity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.manNtel_mid.R;
import com.manNtel.database.DatabaseManager;
import com.manNtel.service.ProcessManager;
import com.manNtel.service.SharedDataService;
import com.manNtel.setting.GameSetting;
import com.manNtel.setting.SystemSetting;
import com.manNtel.struct.GameStruct;
import com.manNtel.struct.UserInfoStruct;

public class Login extends Activity 
{
	GameStruct user;
	DatabaseManager dbm;
	
	public void btnTouch(View v)
	{
		SharedDataService sds = (SharedDataService)getApplicationContext();
		sds.setSharedGameInfo(user);		
		
		switch(v.getId())
		{
		case R.id.btnGoMain:
			//����ȭ�� �̵� 
			startActivity(new Intent(this, Main.class));
			break;
			
		case R.id.btnOtherUser:
			//����� ����
			startActivity(new Intent(this, UserList.class));
			break;
			
		case R.id.btnSelGame:
			//���� ����
			Intent intent = new Intent(this, ReadyGame.class);
			intent.putExtra("userInfo", user);
			startActivity(intent);
			break;
			
		case R.id.btnValUser:
			//����� ��
			Intent goEval = new Intent(this, ReadyEval.class);
			goEval.putExtra("userKey", user.key);
			goEval.putExtra("userPart", user.part);
			startActivity(goEval);
			break;
		
		case R.id.btnUserInfo:
			//����� ����
			Intent goInfo = new Intent(this,UserInfo.class);
			goInfo.putExtra("userInfo", getUserInfo());
			startActivity(goInfo);			
			break;
			
		case R.id.btnSetting:
			//ȯ�� ����
			Intent goGameSet = new Intent(this,GameSetting.class);
			startActivity(goGameSet);
			break;
			
		case R.id.btnSysSetting:
			Intent goSys = new Intent(this,SystemSetting.class);
			startActivity(goSys);
			//�ý��� ����
			break;
			
		case R.id.btnShutdown:
		case R.id.btnShutdown2:
			ProcessManager.getInstance().allEndActivity();
			System.exit(0);
			//�ý��� ����
			break;			
		}
	}
	
	public void getRecc(Cursor c, String name)
	{
		//���� �ֽ� ������ ������
		c.moveToLast();
		
		user = new GameStruct();
		
		user.key = c.getString(0);
		user.name = c.getString(1);
		user.playDate = c.getString(2);
		user.part = c.getString(3);
		user.count = c.getInt(4);
		user.gameNum = c.getInt(5);
		user.level = c.getInt(6);
		user.playTime = c.getString(7);
		user.score = c.getInt(8);	
	}
	
	public UserInfoStruct getUserInfo()
	{
		UserInfoStruct uis = new UserInfoStruct();
		dbm.open();
		Cursor c = dbm.fetchItem(0, user.key);		
		        
		c.moveToFirst();
	
		uis.mKey = c.getString(0);
		uis.mID = c.getString(1);
		uis.mName = c.getString(2);
		uis.mBirth = c.getString(3);
		uis.mAge = c.getInt(4);
		uis.mSex = c.getString(5);
		uis.mPart = c.getString(6);
		uis.mLeftBal = c.getInt(7);
		uis.mRightBal = c.getInt(8);
		uis.mAngle = c.getInt(9);
		uis.mWeight = c.getInt(10);
		uis.mMaxRoll = c.getInt(11);
		uis.mMaxPitch = c.getInt(12);
		uis.mMaxSlide = c.getInt(13);
		uis.mRecent = c.getString(14);

		c.close();
		dbm.close();
		
		return uis;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);   
        setContentView(R.layout.afterlogin);
        
        Intent intent = getIntent();        
        dbm = new DatabaseManager(this);
        dbm.open();        
        
        
        Cursor gameRec = dbm.fetchItem(1, intent.getStringExtra("userKey"));
        
        
        
        //�ش� ����� ���� �̷��� ������
        if(gameRec.getCount()==0)
        {
        	user = new GameStruct(intent.getStringExtra("userKey"),intent.getStringExtra("userName"));
        }
        else        	
        {
        	getRecc(gameRec,intent.getStringExtra("userName"));
        	gameRec.close();
        }
        
        user.part = intent.getStringExtra("userPart");
        
        SimpleDateFormat sd = new SimpleDateFormat("yyyy.MM.dd. HH:mm");		
		Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());			
		
		String tmp = sd.format(currentTimestamp);
        dbm.updateItem(user.key, tmp);
        TextView welcome = (TextView)findViewById(R.id.txtWelcome);
        welcome.setText(user.name);
        dbm.close();
        
        ProcessManager.getInstance().addActivity(this);
        
    }
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		ProcessManager.getInstance().deleteActivity(this);
	}
}
