//����� ��Ͻ� ���� ���� ��Ƽ��Ƽ

package com.manNtel.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;

import com.android.manNtel_mid.R;
import com.manNtel.service.ProcessManager;
import com.manNtel.struct.UserInfoStruct;

public class PartChoice extends Activity 
{
	ImageView mLeft = null;
	ImageView mRight = null;
	UserInfoStruct newUser = null;		
	
	ImageView.OnClickListener partTouch = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{			
			switch(v.getId())
			{
			case R.id.partleft : 
				mLeft.setImageResource(R.drawable.part_3);
				mRight.setImageResource(R.drawable.part_2);
				
				newUser.mPart = "��";
				break;
			
			case R.id.partright : 
				mLeft.setImageResource(R.drawable.part_1);
				mRight.setImageResource(R.drawable.part_4);
				
				newUser.mPart = "��";
				break;				
			}
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
    {	
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);   
        setContentView(R.layout.legchoice);
        
        mLeft = (ImageView)findViewById(R.id.partleft);
        mRight = (ImageView)findViewById(R.id.partright);
        
        mLeft.setOnClickListener(partTouch);
        mRight.setOnClickListener(partTouch);
        
        Bundle bundle = getIntent().getExtras();
        newUser = bundle.getParcelable("userInfo");        
        
        ProcessManager.getInstance().addActivity(this);
    }
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		ProcessManager.getInstance().deleteActivity(this);
	}
	
	public void popup(View v)
	{
		switch(v.getId())
		{
		case R.id.btnPrev:
			finish();
			break;
		case R.id.btnNext:
			
			Intent goWaitBal = new Intent(this,ReadyAdd.class); 
    		goWaitBal.putExtra("userInfo", newUser);
    		startActivity(goWaitBal);
    		
			break;
		case R.id.btnClose:
			finish();
			break;		
		}
	}
}
