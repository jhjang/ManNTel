//사용자 추가
//XML O
package com.manNtel.activity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.android.manNtel_mid.R;
import com.manNtel.service.ProcessManager;
import com.manNtel.struct.UserInfoStruct;

public class AddUser extends Activity 
{
	InputMethodManager mImm;
	EditText mName, mID;
	Button mBirth, mSex, mPart;
	
	int mYear, mMonth, mDay, todayYear;
	String mSexOrigin, mPartOrigin;
	
	UserInfoStruct newUser;
	
    DialogInterface.OnClickListener pickSex = new DialogInterface.OnClickListener() 
    {
		@Override
		public void onClick(DialogInterface dialog, int which) 
		{
			if(which == DialogInterface.BUTTON1)	
				mSex.setText(R.string.dlgSexMale);			
			else
				mSex.setText(R.string.dlgSexFemale);					
		}
	};
    
    DialogInterface.OnClickListener dlgClose = new DialogInterface.OnClickListener() 
	{		
		@Override
		public void onClick(DialogInterface dialog, int which) 
		{}
	};
    
    DialogInterface.OnClickListener pickPart = new DialogInterface.OnClickListener() 
    {
		@Override
		public void onClick(DialogInterface dialog, int which) 
		{
			if(which == DialogInterface.BUTTON1)			
				mPart.setText(R.string.dlgPartLeft);
			else
				mPart.setText(R.string.dlgPartRight);						
		}
	};
	
	DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() 
    {

    	@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) 
    	{
    		mYear = year;
    		mMonth = monthOfYear;
    		mDay = dayOfMonth;
    		mBirth.setText(String.format("%d/%d/%d", mYear,mMonth+1,mDay));
    	}
    };
    
	
	public boolean chkErr()
    {
    	if(newUser.mName.length() == 0)
    	{
    		new AlertDialog.Builder(AddUser.this).setTitle(R.string.warning).setMessage(R.string.wrongName).setIcon(R.drawable.icon).setPositiveButton(R.string.dlgClose, dlgClose).show();
    		mName.requestFocus();
    		return false;
    	}    	
    	else if(newUser.mID.length() == 0)
    	{
    		new AlertDialog.Builder(AddUser.this).setTitle(R.string.warning).setMessage(R.string.wrongID).setIcon(R.drawable.icon).setPositiveButton(R.string.dlgClose, dlgClose).show();
    		mID.requestFocus();
    		return false;
    	}
    	else if(newUser.mSex.equals(mSexOrigin))
    	{
    		new AlertDialog.Builder(AddUser.this).setTitle(R.string.warning).setMessage(R.string.wrongSex).setIcon(R.drawable.icon).setPositiveButton(R.string.dlgClose, dlgClose).show();    		
    		return false;
    	}
    	else if(newUser.mPart.equals(mPartOrigin))
    	{
    		new AlertDialog.Builder(AddUser.this).setTitle(R.string.warning).setMessage(R.string.wrongPart).setIcon(R.drawable.icon).setPositiveButton(R.string.dlgClose, dlgClose).show();    		
    		return false;
    	}
    	
    	return true;
    }
    
    @Override    
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);   
        setContentView(R.layout.adduser);
            
        mImm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        mName = (EditText)findViewById(R.id.edtName);
        mID = (EditText)findViewById(R.id.edtID);
        
        mBirth = (Button)findViewById(R.id.btnBirth);
        mSex = (Button)findViewById(R.id.btnSex);
        mPart = (Button)findViewById(R.id.btnPart);
        
        mSexOrigin = mSex.getText().toString();
        mPartOrigin = mPart.getText().toString();
        
        Calendar cal = new GregorianCalendar();
        todayYear = cal.get(Calendar.YEAR);
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);
        
        newUser = new UserInfoStruct();
                
        mBirth.setText(String.format("%d/%d/%d", mYear,mMonth+1,mDay));
        
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
    	case R.id.btnBirth : 
    		new DatePickerDialog(AddUser.this,mDateSetListener,mYear,mMonth,mDay).show();
    		break;
    	
    	case R.id.btnSex :
    		new AlertDialog.Builder(this).setTitle(R.string.dlgSexTitle).setMessage(R.string.dlgSexContent).setPositiveButton(R.string.dlgSexMale, pickSex).setNegativeButton(R.string.dlgSexFemale,pickSex).show();
    		break;
    	
    	case R.id.btnPart:
    		new AlertDialog.Builder(this).setTitle(R.string.dlgPartTitle).setMessage(R.string.dlgPartContent).setPositiveButton(R.string.dlgPartLeft, pickPart).setNegativeButton(R.string.dlgPartRight,pickPart).show();
    		break;    	
    	case R.id.btnClose:
    		finish();
    		break;
    	case R.id.btnNext:
    		Update();
    		if(chkErr()==true)
    		{
    			Intent goPartChoice = new Intent(this,PartChoice.class); 
    			goPartChoice.putExtra("userInfo", newUser);
    			startActivity(goPartChoice);
    		}
    		break;
    	}    	
    }   
    
    public void Update()
    {
    	SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd-HHmmss");		
		Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());			
		
		newUser.mKey = sd.format(currentTimestamp);
		newUser.mID = mID.getText().toString();
		newUser.mName = mName.getText().toString();		
		
		newUser.mAge = todayYear - mYear + 1;    	
    	newUser.mBirth = mBirth.getText().toString();
		
    	newUser.mSex = mSex.getText().toString();
    	newUser.mPart = mPart.getText().toString();
    }
}