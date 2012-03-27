//ȯ�� ����
//XML ��� ���� O

package com.manNtel.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.manNtel_mid.R;
import com.manNtel.record.EvalRecord;
import com.manNtel.record.GameRecord;
import com.manNtel.service.ProcessManager;
import com.manNtel.struct.UserInfoStruct;

public class UserInfo extends Activity 
{
	UserInfoStruct userInfo;
		
	public void _onClick(View v)
	{
		switch(v.getId())
		{
		case R.id.btnTest :
			//�� �̷� ����
			Intent evalRecord = new Intent(this,EvalRecord.class);
			evalRecord.putExtra("userKey", userInfo.mKey);
			startActivity(evalRecord);
			break;
		case R.id.btnGame :
			//���� �̷� ����
			Intent gameRecord = new Intent(this,GameRecord.class);
			gameRecord.putExtra("userKey", userInfo.mKey);
			startActivity(gameRecord);
			break;
		case R.id.btnOK :
			//���� �Ϸ�
			Intent goLogin = new Intent(this,Login.class);
			goLogin.putExtra("userKey", userInfo.mKey);
			goLogin.putExtra("userName", userInfo.mName);
			goLogin.putExtra("userPart", userInfo.mPart);
			startActivity(goLogin);
			break;
			
		case R.id.btnOtherUser :
			//�ٸ� ����� ����
			Intent goUserList = new Intent(this,UserList.class);
			goUserList.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(goUserList);
			break;
		case R.id.btnMain :
			//����ȭ��
			startActivity(new Intent(this,Main.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			break;
		case R.id.btnExit :
			//�ý��� ����
			break;
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
    {	
        super.onCreate(savedInstanceState);
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);   
        
        Bundle bundle = getIntent().getExtras();
        userInfo = bundle.getParcelable("userInfo");
        
        setContentView(R.layout.userinfo);
        
        TextView ID = (TextView)findViewById(R.id.userid);
        ID.setText(userInfo.mID);
        
        TextView age = (TextView)findViewById(R.id.userAge);
        age.setText(Integer.toString(userInfo.mAge));
        
        TextView name = (TextView)findViewById(R.id.name);
        name.setText(userInfo.mName);
        
        TextView sex = (TextView)findViewById(R.id.sex);
        sex.setText(userInfo.mSex);
        
        TextView part = (TextView)findViewById(R.id.part);
        part.setText(userInfo.mPart);
        
        TextView leftBal = (TextView)findViewById(R.id.balLeft);
        leftBal.setText(Float.toString(userInfo.mLeftBal));
        
        TextView rightBal = (TextView)findViewById(R.id.balRight);
        rightBal.setText(Float.toString(userInfo.mRightBal));
        
        TextView angle = (TextView)findViewById(R.id.maxAngle);
        angle.setText(Float.toString(userInfo.mAngle));
        
        TextView weight = (TextView)findViewById(R.id.maxWeight);
        weight.setText(Float.toString(userInfo.mWeight));
    
        ProcessManager.getInstance().addActivity(this);
    }
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		ProcessManager.getInstance().deleteActivity(this);
	}
}
