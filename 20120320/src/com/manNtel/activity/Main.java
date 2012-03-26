//메인메뉴 액티비티
//XML 사용여부 : O
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
    //버튼 터치시
    public void moveAct(View v)
    {
    	switch(v.getId())
    	{
    	case R.id.list :
    		Log.i("[moveAct]","환자 리스트");
        	startActivity(new Intent(this, UserList.class));
    		break;
    	
    	case R.id.addList :
    		Log.i("[moveAct]","신규 사용자 등록");
    		startActivity(new Intent(this, AddUser.class));
    		break;
    	
    	case R.id.setting :
    		Log.i("[moveAct]","시스템 설정");
    		startActivity(new Intent(this, SystemSetting.class));
    		break;    		
    	
    	case R.id.exit :
    		
    		Log.i("[moveAct]","시스템 종료");    		
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