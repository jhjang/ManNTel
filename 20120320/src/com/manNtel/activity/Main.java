//���θ޴� ��Ƽ��Ƽ
//XML ��뿩�� : O
package com.manNtel.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.android.manNtel_mid.R;
import com.manNtel.service.ProcessManager;
import com.manNtel.setting.SystemSetting;

public class Main extends Activity 
{
    //��ư ��ġ��
    public void moveAct(View v)
    {
    	switch(v.getId())
    	{
    	case R.id.list :
    		Log.i("[moveAct]","ȯ�� ����Ʈ");
        	startActivity(new Intent(this, UserList.class));
    		break;
    	
    	case R.id.addList :
    		Log.i("[moveAct]","�ű� ����� ���");
    		startActivity(new Intent(this, AddUser.class));
    		break;
    	
    	case R.id.setting :
    		Log.i("[moveAct]","�ý��� ����");
    		startActivity(new Intent(this, SystemSetting.class));
    		break;    		
    	
    	case R.id.exit :
    		
    		Log.i("[moveAct]","�ý��� ����");    		
    		ProcessManager.getInstance().allEndActivity();    		
    		android.os.Process.killProcess(android.os.Process.myPid());
    		
    		break;    		
    	}
    }    
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        ProcessManager.getInstance().addActivity(this);
        
        setContentView(R.layout.main);            
    }
    
    @Override
    public void onDestroy(){
    	super.onDestroy();
    	ProcessManager.getInstance().deleteActivity(this);
    }
}