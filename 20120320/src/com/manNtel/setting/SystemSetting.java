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
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.manNtel_mid.R;
import com.manNtel.service.ProcessManager;
import com.manNtel.service.SharedDataService;

public class SystemSetting extends Activity implements OnClickListener {
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

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
		case R.id.btnClose:
			finish();
			break;
		}		
	}
	
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
