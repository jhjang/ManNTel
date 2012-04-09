package com.manNtel.setting;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.manNtel_mid.R;
import com.manNtel.database.DatabaseManager;
import com.manNtel.service.ProcessManager;
import com.manNtel.service.SharedDataService;

public class SystemSetting extends Activity implements OnClickListener {
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);	

		setContentView(R.layout.systemsetmenu);
		
		Button btnDebug = (Button)findViewById(R.id.btnDebug);
		SharedPreferences pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
		String str = getResources().getText(R.string.btnDebug).toString();
		if(pref.getBoolean("isDebug", false)){
			btnDebug.setText(str + " [" + "ON" + "]");
		}
		else{
			btnDebug.setText(str + " [" + "OFF" + "]");
		}
		
		ProcessManager.getInstance().addActivity(this);
	}
	
	public void onDestory(){
		super.onDestroy();
		ProcessManager.getInstance().deleteActivity(this);
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.btnSensor :	
			startActivity(new Intent(this, SensorSetting.class));
			break;
		case R.id.btnRomLoad :			
			new AlertDialog.Builder(this).setTitle(R.string.dlgSysTitle).setMessage(R.string.dlgLoadContent).setPositiveButton(R.string.dlgYes, romLoad).setNegativeButton(R.string.dlgNo,romLoad).show();
			break;
		case R.id.btnRomSave :			
			new AlertDialog.Builder(this).setTitle(R.string.dlgSysTitle).setMessage(R.string.dlgSaveContent).setPositiveButton(R.string.dlgYes, romSave).setNegativeButton(R.string.dlgNo,romSave).show();
			break;
		case R.id.btnDebug:			
			new AlertDialog.Builder(this).setTitle(R.string.dlgSysTitle).setMessage(R.string.dlgDebugContent).setPositiveButton(R.string.dlgYes, debug).setNegativeButton(R.string.dlgNo,debug).show();
			break;
		case R.id.btnDB:			
			new AlertDialog.Builder(this).setTitle(R.string.dlgSysTitle).setMessage(R.string.dlgDBContent).setPositiveButton(R.string.dlgYes, DbExport).setNegativeButton(R.string.dlgNo,DbExport).show();
			break;
		case R.id.btnPass:
			final LinearLayout linear = (LinearLayout)View.inflate(this, R.layout.passdlg, null);
			
			new AlertDialog.Builder(this)
			.setTitle("Change Password (4 Characters)")
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
						.setMessage("암호를 확인 후 다시 입력하세요.")
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
			break;
		case R.id.btnClose:
			finish();
			break;
		}		
	}
	
	DialogInterface.OnClickListener DbExport = new DialogInterface.OnClickListener() 
	{
		@Override
		public void onClick(DialogInterface dialog, int which) 
		{
			if(which == DialogInterface.BUTTON1){
				DatabaseManager dbm = new DatabaseManager(getApplicationContext());
				dbm.open();
				dbm.exportToCsv();
				dbm.close();
			}									
		}
	};
	
	DialogInterface.OnClickListener romLoad = new DialogInterface.OnClickListener() 
	{
		@Override
		public void onClick(DialogInterface dialog, int which) 
		{
			if(which == DialogInterface.BUTTON1){
				SharedDataService ds = (SharedDataService)getApplication();
				ds.getDataService().sendMsg("LOAD");
				
				SharedPreferences pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = pref.edit();
				editor.putString("loadedPacket", ds.getDataService().getLoadedData());
				editor.putBoolean("loadPacketFlag", true);
				editor.commit();
			}									
		}
	};

	DialogInterface.OnClickListener romSave = new DialogInterface.OnClickListener() 
	{
		@Override
		public void onClick(DialogInterface dialog, int which) 
		{
			if(which == DialogInterface.BUTTON1){
				//To do : 서버로 save 패킷 전송 
				SharedPreferences pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
				Log.i("[Packet]",pref.getString("savePacket", "FF"));
				SharedDataService ds = (SharedDataService)getApplication();
				ds.getDataService().sendMsg(pref.getString("savePacket", "SAVE"));
				
			}						
															
		}
	};

	DialogInterface.OnClickListener debug = new DialogInterface.OnClickListener() 
	{
		@Override
		public void onClick(DialogInterface dialog, int which) 
		{
			SharedPreferences pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = pref.edit();
			Button btnDebug = (Button)findViewById(R.id.btnDebug);
			String str = getResources().getText(R.string.btnDebug).toString();
			
			if(which == DialogInterface.BUTTON1){
				btnDebug.setText(str+ " [" + "ON" + "]");				
				editor.putBoolean("isDebug", true);
				editor.commit();
			}							
			else{
				btnDebug.setText(str + " [" + "OFF" + "]");
				editor.putBoolean("isDebug", false);
				editor.commit();
			}			
		}
	};
}
