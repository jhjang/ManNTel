//���θ޴� ��Ƽ��Ƽ
//XML ��뿩�� : O
package com.manNtel.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
        	startActivity(new Intent(this, UserList.class));
    		break;
    	
    	case R.id.addList :
    		startActivity(new Intent(this, AddUser.class));
    		break;
    	
    	case R.id.setting :
    		startActivity(new Intent(this, SystemSetting.class));
    		break;    		
    	
    	case R.id.exit :
    		ProcessManager.getInstance().allEndActivity();    		
    		System.exit(1);    		
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