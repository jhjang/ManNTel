//메인메뉴 액티비티
//XML 사용여부 : O
package com.manNtel.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.manNtel_mid.R;
import com.manNtel.service.ProcessManager;
import com.manNtel.setting.SystemSetting;

public class Main extends Activity 
{
	//버튼 터치시	
	public static Activity app;
	private String originPass;

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
			SharedPreferences pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
			originPass = pref.getString("password", "null");
			if(originPass.equals("null")){
				new AlertDialog.Builder(app)
				.setTitle("알림")
				.setMessage(R.string.dlgSetPass)
				.setIcon(R.drawable.icon)
				.setPositiveButton(R.string.dlgGoSetPass, new DialogInterface.OnClickListener() {							
					@Override
					public void onClick(DialogInterface dialog, int which) {
						final LinearLayout linear = (LinearLayout)View.inflate(app, R.layout.passdlg, null);
						
						new AlertDialog.Builder(app)
						.setTitle("Set Password")
						.setIcon(R.drawable.icon)
						.setView(linear)
						.setPositiveButton("OK", new DialogInterface.OnClickListener() {				
							@Override
							public void onClick(DialogInterface dialog, int which) {
								EditText newPass = (EditText)linear.findViewById(R.id.edtNewPass);
								EditText confirmPass = (EditText)linear.findViewById(R.id.edtConfirmPass);
								
								if(newPass.getText().toString().equals(confirmPass.getText().toString())){
									SharedPreferences pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
									
									SharedPreferences.Editor editor = pref.edit();
									
									editor.putString("password", newPass.getText().toString());
									editor.commit();
									new AlertDialog.Builder(getWindow().getContext())
									.setTitle("알림")
									.setMessage(R.string.dlgSetPassOK)
									.setIcon(R.drawable.icon)
									.setCancelable(false)
									.setNegativeButton("Close", new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											// TODO Auto-generated method stub
											return;
										}
									})
									.show();									
								}
								else{
									new AlertDialog.Builder(getWindow().getContext())
									.setTitle("알림")
									.setMessage(R.string.dlgPassDeny)
									.setIcon(R.drawable.icon)
									.setCancelable(false)
									.setNegativeButton("Close", new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											// TODO Auto-generated method stub
											return;
										}
									})
									.show();
								}					
							}
						})
						.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								return;
							}
						})
						.show();												
					}
				})
				.setNegativeButton(R.string.dlgGoSetPassNo, new DialogInterface.OnClickListener() {							
					@Override
					public void onClick(DialogInterface dialog, int which) {}
				})
				.show();				
			}
			else{
				final LinearLayout linear = (LinearLayout)View.inflate(this, R.layout.getpassdlg, null);
				originPass = pref.getString("password", "null");
				new AlertDialog.Builder(this)
				.setTitle("System Setting (4 Characters)")
				.setIcon(R.drawable.icon)
				.setView(linear)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						EditText inputPass = (EditText)linear.findViewById(R.id.edtPass);
						if(originPass.equals(inputPass.getText().toString())){							
							startActivity(new Intent(Main.app, SystemSetting.class));						
						}
						else{
							new AlertDialog.Builder(app)
							.setTitle("알림")
							.setMessage(R.string.dlgPassDeny)
							.setIcon(R.drawable.icon)
							.setCancelable(false)
							.setNegativeButton("Close", new DialogInterface.OnClickListener() {							
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									return;
								}
							})
							.show();						
						}
					}				
				})
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {				
					@Override
					public void onClick(DialogInterface dialog, int which) { return; }
				})
				.show();
			}
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

		app = this;

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