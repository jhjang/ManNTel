package com.manNtel.setting;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.app.TabActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.manNtel_mid.R;
import com.manNtel.service.ProcessManager;
import com.manNtel.service.SharedDataService;

public class SensorSetting extends TabActivity {
	private static final int TYPE = 0;
	private static final int FACTOR = 1;
	private static final int VALUE = 2;

	private static final int PACKET_LENGTH = 43;

	private Timer mTimer = null;
	private Handler handler = new Handler();

	private String[][] packet = null;

	private TabHost mTab;
	private String mData;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		mTab = getTabHost();
		super.onCreate(savedInstanceState);

		LayoutInflater inflater = LayoutInflater.from(this);
		inflater.inflate(R.layout.sensorsetting, mTab.getTabContentView(), true);

		mTab.addTab(mTab.newTabSpec("tag").setIndicator("Left Load").setContent(R.id.sensorLeftLoad));        
		mTab.addTab(mTab.newTabSpec("tag").setIndicator("Right Load").setContent(R.id.sensorRightLoad));
		//		mTab.addTab(mTab.newTabSpec("tag").setIndicator("Tilt").setContent(R.id.sensorTilt));
		//		mTab.addTab(mTab.newTabSpec("tag").setIndicator("Roll").setContent(R.id.sensorRoll));
		//		mTab.addTab(mTab.newTabSpec("tag").setIndicator("Pitch").setContent(R.id.sensorPitch));
		mTab.addTab(mTab.newTabSpec("tag").setIndicator("Slide").setContent(R.id.sensorSlide));

		packet = new String[PACKET_LENGTH][3];
		for(int i=0;i<PACKET_LENGTH;i++){     	

			if(i>=0 && i<15){
				packet[i][TYPE]="LL";
				packet[i][VALUE]=String.valueOf(i*10);
			}
			else if(i>=15 && i<30){
				packet[i][TYPE]="RL";
				packet[i][VALUE]=String.valueOf(i*10-150);
			}
			else if(i>=30 && i<36){
				packet[i][TYPE]="SL";
				packet[i][VALUE]=String.valueOf(-1);
			}
			else if(i==30){
				packet[i][TYPE]="SC";
				packet[i][VALUE]=String.valueOf(0);
			}
			else if(i>=36){
				packet[i][TYPE]="SR";
				packet[i][VALUE]=String.valueOf(1);
			}			

			packet[i][FACTOR]="FFFF";
		}

		SharedPreferences pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
		mData = pref.getString("savePacket", "null");

		if(pref.getBoolean("loadPacketFlag", false)){
			mData = pref.getString("loadedPacket", "null");
			SharedPreferences.Editor editor = pref.edit();
			editor.putBoolean("loadPacketFlag", false);

			if(!mData.equals("null")){
				String tmp[] = mData.split("LOAD,");
				mData = tmp[1];
			}

			editor.putString("savePacket", mData);

			editor.commit();
		}		

		initValue();

		//디버그 모드 처리				
		TimerTask debugTask = new TimerTask(){
			@Override
			public void run(){
				try{

					handler.post(new Runnable() {
						@Override
						public void run(){
							TextView debug1 = (TextView)findViewById(R.id.txtDebug1);
							TextView debug2 = (TextView)findViewById(R.id.txtDebug2);
							TextView debug3 = (TextView)findViewById(R.id.txtDebug3);                				        				

							debug1.setText(debug2.getText().toString());
							debug2.setText(debug3.getText().toString());
							SharedDataService ds = (SharedDataService)getApplication();

							SimpleDateFormat sd = new SimpleDateFormat("yyyy MM dd-HH:mm:ss");		
							Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());      				 

							debug3.setText(sd.format(currentTimestamp) + "  :  " + ds.getDataService().getFullData());
						}
					});        				        					       				
				} catch(Exception e) { e.printStackTrace(); }
			}
		};        	
		mTimer = new Timer();
		mTimer.schedule(debugTask, 0, 1000);	

		ProcessManager.getInstance().addActivity(this);
	}	 

	@Override
	public void onDestroy(){
		super.onDestroy();
		ProcessManager.getInstance().deleteActivity(this);
	}

	//onCreate에서 호출 
	private void initValue(){
		if(mData.equals("null")){
			Toast.makeText(this, "저장된 데이터 없음", 3);
			return;
		}
		String[] tmp2 = mData.split("\\n");
		String[] tmp = tmp2[0].split(",");		
		String value;		
		int realValue;

		for(int i=0;i<tmp.length;i++){
			if(tmp[i].equals("LL")){
				value = tmp[i+2];
				realValue = Integer.parseInt(value);

				switch(realValue){
				case 0 :
					TextView leftText0 = (TextView)findViewById(R.id.txtSensorLeft0);
					leftText0.setText(tmp[i+1]);
					break;
				case 10 :
					TextView leftText1 = (TextView)findViewById(R.id.txtSensorLeft1);
					leftText1.setText(tmp[i+1]);
					break;
				case 20 :
					TextView leftText2 = (TextView)findViewById(R.id.txtSensorLeft2);
					leftText2.setText(tmp[i+1]);
					break;
				case 30 :
					TextView leftText3 = (TextView)findViewById(R.id.txtSensorLeft3);
					leftText3.setText(tmp[i+1]);
					break;
				case 40 :
					TextView leftText4 = (TextView)findViewById(R.id.txtSensorLeft4);
					leftText4.setText(tmp[i+1]);
					break;
				case 50 :
					TextView leftText5 = (TextView)findViewById(R.id.txtSensorLeft5);
					leftText5.setText(tmp[i+1]);
					break;
				case 60 :
					TextView leftText6 = (TextView)findViewById(R.id.txtSensorLeft6);
					leftText6.setText(tmp[i+1]);
					break;
				case 70 :
					TextView leftText7 = (TextView)findViewById(R.id.txtSensorLeft7);
					leftText7.setText(tmp[i+1]);
					break;
				case 80 :
					TextView leftText8 = (TextView)findViewById(R.id.txtSensorLeft8);
					leftText8.setText(tmp[i+1]);
					break;
				case 90 :
					TextView leftText9 = (TextView)findViewById(R.id.txtSensorLeft9);
					leftText9.setText(tmp[i+1]);
					break;
				case 100 :
					TextView leftText10 = (TextView)findViewById(R.id.txtSensorLeft10);
					leftText10.setText(tmp[i+1]);
					break;
				case 110 :
					TextView leftText11 = (TextView)findViewById(R.id.txtSensorLeft11);
					leftText11.setText(tmp[i+1]);
					break;
				case 120 :
					TextView leftText12 = (TextView)findViewById(R.id.txtSensorLeft12);
					leftText12.setText(tmp[i+1]);
					break;
				case 130 :
					TextView leftText13 = (TextView)findViewById(R.id.txtSensorLeft13);
					leftText13.setText(tmp[i+1]);
					break;
				case 140 :
					TextView leftText14 = (TextView)findViewById(R.id.txtSensorLeft14);
					leftText14.setText(tmp[i+1]);
					break;					
				}				
			}
			else if(tmp[i].equals(("RL"))){
				value = tmp[i+2];
				realValue = Integer.parseInt(value);

				switch(realValue){
				case 0 :
					TextView RightText0 = (TextView)findViewById(R.id.txtSensorRight0);
					RightText0.setText(tmp[i+1]);
					break;
				case 10 :
					TextView RightText1 = (TextView)findViewById(R.id.txtSensorRight1);
					RightText1.setText(tmp[i+1]);
					break;
				case 20 :
					TextView RightText2 = (TextView)findViewById(R.id.txtSensorRight2);
					RightText2.setText(tmp[i+1]);
					break;
				case 30 :
					TextView RightText3 = (TextView)findViewById(R.id.txtSensorRight3);
					RightText3.setText(tmp[i+1]);
					break;
				case 40 :
					TextView RightText4 = (TextView)findViewById(R.id.txtSensorRight4);
					RightText4.setText(tmp[i+1]);
					break;
				case 50 :
					TextView RightText5 = (TextView)findViewById(R.id.txtSensorRight5);
					RightText5.setText(tmp[i+1]);
					break;
				case 60 :
					TextView RightText6 = (TextView)findViewById(R.id.txtSensorRight6);
					RightText6.setText(tmp[i+1]);
					break;
				case 70 :
					TextView RightText7 = (TextView)findViewById(R.id.txtSensorRight7);
					RightText7.setText(tmp[i+1]);
					break;
				case 80 :
					TextView RightText8 = (TextView)findViewById(R.id.txtSensorRight8);
					RightText8.setText(tmp[i+1]);
					break;
				case 90 :
					TextView RightText9 = (TextView)findViewById(R.id.txtSensorRight9);
					RightText9.setText(tmp[i+1]);
					break;
				case 100 :
					TextView RightText10 = (TextView)findViewById(R.id.txtSensorRight10);
					RightText10.setText(tmp[i+1]);
					break;
				case 110 :
					TextView RightText11 = (TextView)findViewById(R.id.txtSensorRight11);
					RightText11.setText(tmp[i+1]);
					break;
				case 120 :
					TextView RightText12 = (TextView)findViewById(R.id.txtSensorRight12);
					RightText12.setText(tmp[i+1]);
					break;
				case 130 :
					TextView RightText13 = (TextView)findViewById(R.id.txtSensorRight13);
					RightText13.setText(tmp[i+1]);
					break;
				case 140 :
					TextView RightText14 = (TextView)findViewById(R.id.txtSensorRight14);
					RightText14.setText(tmp[i+1]);
					break;					
				}
			}
			
			else if(tmp[i].equals(("SL"))|| tmp[i].equals("SC")){
				value = tmp[i+2];
				value.replaceAll(" ", " ");
				value.replaceAll("\\n"," ");				

				realValue = Integer.parseInt(value);

				switch(realValue){
				case 30 : 
					TextView SlideText0 = (TextView)findViewById(R.id.txtSensorSlide0);
					SlideText0.setText(tmp[i+1]);
					break;
				case 25 : 
					TextView SlideText1 = (TextView)findViewById(R.id.txtSensorSlide1);
					SlideText1.setText(tmp[i+1]);
					break;
				case 20 : 
					TextView SlideText2 = (TextView)findViewById(R.id.txtSensorSlide2);
					SlideText2.setText(tmp[i+1]);
					break;
				case 15 : 
					TextView SlideText3 = (TextView)findViewById(R.id.txtSensorSlide3);
					SlideText3.setText(tmp[i+1]);
					break;
				case 10 : 
					TextView SlideText4 = (TextView)findViewById(R.id.txtSensorSlide4);
					SlideText4.setText(tmp[i+1]);
					break;
				case 5 : 
					TextView SlideText5 = (TextView)findViewById(R.id.txtSensorSlide5);
					SlideText5.setText(tmp[i+1]);
					break;
				case 0 : 
					TextView SlideText6 = (TextView)findViewById(R.id.txtSensorSlide6);
					SlideText6.setText(tmp[i+1]);
					break;							
				}
			}			
			else if(tmp[i].equals(("SR"))){
				value = tmp[i+2];
				value.replaceAll(" ", " ");
				value.replaceAll("\\n"," ");				

				realValue = Integer.parseInt(value);

				switch(realValue){
				case 30 : 
					TextView SlideText7 = (TextView)findViewById(R.id.txtSensorSlide7);
					SlideText7.setText(tmp[i+1]);
					break;
				case 25 : 
					TextView SlideText8 = (TextView)findViewById(R.id.txtSensorSlide8);
					SlideText8.setText(tmp[i+1]);
					break;
				case 20 : 
					TextView SlideText9 = (TextView)findViewById(R.id.txtSensorSlide9);
					SlideText9.setText(tmp[i+1]);
					break;
				case 15 : 
					TextView SlideText10 = (TextView)findViewById(R.id.txtSensorSlide10);
					SlideText10.setText(tmp[i+1]);
					break;
				case 10 : 
					TextView SlideText11 = (TextView)findViewById(R.id.txtSensorSlide11);
					SlideText11.setText(tmp[i+1]);
					break;
				case 5 : 
					TextView SlideText12 = (TextView)findViewById(R.id.txtSensorSlide12);
					SlideText12.setText(tmp[i+1]);
					break;				
				}
			}	
		}
	}

	private void makePacket(){

		TextView leftText0 = (TextView)findViewById(R.id.txtSensorLeft0);
		packet[0][TYPE] = "LL"; packet[0][FACTOR] = leftText0.getText().toString() ; packet[0][VALUE] = String.valueOf(0);

		TextView leftText1 = (TextView)findViewById(R.id.txtSensorLeft1);
		packet[1][TYPE] = "LL"; packet[1][FACTOR] = leftText1.getText().toString() ; packet[1][VALUE] = String.valueOf(10);

		TextView leftText2 = (TextView)findViewById(R.id.txtSensorLeft2);
		packet[2][TYPE] = "LL"; packet[2][FACTOR] = leftText2.getText().toString() ; packet[2][VALUE] = String.valueOf(20);

		TextView leftText3 = (TextView)findViewById(R.id.txtSensorLeft3);
		packet[3][TYPE] = "LL"; packet[3][FACTOR] = leftText3.getText().toString() ; packet[3][VALUE] = String.valueOf(30);

		TextView leftText4 = (TextView)findViewById(R.id.txtSensorLeft4);
		packet[4][TYPE] = "LL"; packet[4][FACTOR] = leftText4.getText().toString() ; packet[4][VALUE] = String.valueOf(40);

		TextView leftText5 = (TextView)findViewById(R.id.txtSensorLeft5);
		packet[5][TYPE] = "LL"; packet[5][FACTOR] = leftText5.getText().toString() ; packet[5][VALUE] = String.valueOf(50);

		TextView leftText6 = (TextView)findViewById(R.id.txtSensorLeft6);
		packet[6][TYPE] = "LL"; packet[6][FACTOR] = leftText6.getText().toString() ; packet[6][VALUE] = String.valueOf(60);

		TextView leftText7 = (TextView)findViewById(R.id.txtSensorLeft7);
		packet[7][TYPE] = "LL"; packet[7][FACTOR] = leftText7.getText().toString() ; packet[7][VALUE] = String.valueOf(70);

		TextView leftText8 = (TextView)findViewById(R.id.txtSensorLeft8);
		packet[8][TYPE] = "LL"; packet[8][FACTOR] = leftText8.getText().toString() ; packet[8][VALUE] = String.valueOf(80);

		TextView leftText9 = (TextView)findViewById(R.id.txtSensorLeft9);
		packet[9][TYPE] = "LL"; packet[9][FACTOR] = leftText9.getText().toString() ; packet[9][VALUE] = String.valueOf(90);

		TextView leftText10 = (TextView)findViewById(R.id.txtSensorLeft10);
		packet[10][TYPE] = "LL"; packet[10][FACTOR] = leftText10.getText().toString() ; packet[10][VALUE] = String.valueOf(100);

		TextView leftText11 = (TextView)findViewById(R.id.txtSensorLeft11);
		packet[11][TYPE] = "LL"; packet[11][FACTOR] = leftText11.getText().toString() ; packet[11][VALUE] = String.valueOf(110);

		TextView leftText12 = (TextView)findViewById(R.id.txtSensorLeft12);
		packet[12][TYPE] = "LL"; packet[12][FACTOR] = leftText12.getText().toString() ; packet[12][VALUE] = String.valueOf(120);

		TextView leftText13 = (TextView)findViewById(R.id.txtSensorLeft13);
		packet[13][TYPE] = "LL"; packet[13][FACTOR] = leftText13.getText().toString() ; packet[13][VALUE] = String.valueOf(130);

		TextView leftText14 = (TextView)findViewById(R.id.txtSensorLeft14);
		packet[14][TYPE] = "LL"; packet[14][FACTOR] = leftText14.getText().toString() ; packet[14][VALUE] = String.valueOf(140);

		TextView RightText0 = (TextView)findViewById(R.id.txtSensorRight0);
		packet[15][TYPE] = "RL"; packet[15][FACTOR] = RightText0.getText().toString() ; packet[15][VALUE] = String.valueOf(0);

		TextView RightText1 = (TextView)findViewById(R.id.txtSensorRight1);
		packet[16][TYPE] = "RL"; packet[16][FACTOR] = RightText1.getText().toString() ; packet[16][VALUE] = String.valueOf(10);

		TextView RightText2 = (TextView)findViewById(R.id.txtSensorRight2);
		packet[17][TYPE] = "RL"; packet[17][FACTOR] = RightText2.getText().toString() ; packet[17][VALUE] = String.valueOf(20);

		TextView RightText3 = (TextView)findViewById(R.id.txtSensorRight3);
		packet[18][TYPE] = "RL"; packet[18][FACTOR] = RightText3.getText().toString() ; packet[18][VALUE] = String.valueOf(30);

		TextView RightText4 = (TextView)findViewById(R.id.txtSensorRight4);
		packet[19][TYPE] = "RL"; packet[19][FACTOR] = RightText4.getText().toString() ; packet[19][VALUE] = String.valueOf(40);

		TextView RightText5 = (TextView)findViewById(R.id.txtSensorRight5);
		packet[20][TYPE] = "RL"; packet[20][FACTOR] = RightText5.getText().toString() ; packet[20][VALUE] = String.valueOf(50);

		TextView RightText6 = (TextView)findViewById(R.id.txtSensorRight6);
		packet[21][TYPE] = "RL"; packet[21][FACTOR] = RightText6.getText().toString() ; packet[21][VALUE] = String.valueOf(60);

		TextView RightText7 = (TextView)findViewById(R.id.txtSensorRight7);
		packet[22][TYPE] = "RL"; packet[22][FACTOR] = RightText7.getText().toString() ; packet[22][VALUE] = String.valueOf(70);

		TextView RightText8 = (TextView)findViewById(R.id.txtSensorRight8);
		packet[23][TYPE] = "RL"; packet[23][FACTOR] = RightText8.getText().toString() ; packet[23][VALUE] = String.valueOf(80);

		TextView RightText9 = (TextView)findViewById(R.id.txtSensorRight9);
		packet[24][TYPE] = "RL"; packet[24][FACTOR] = RightText9.getText().toString() ; packet[24][VALUE] = String.valueOf(90);

		TextView RightText10 = (TextView)findViewById(R.id.txtSensorRight10);
		packet[25][TYPE] = "RL"; packet[25][FACTOR] = RightText10.getText().toString() ; packet[25][VALUE] = String.valueOf(100);

		TextView RightText11 = (TextView)findViewById(R.id.txtSensorRight11);
		packet[26][TYPE] = "RL"; packet[26][FACTOR] = RightText11.getText().toString() ; packet[26][VALUE] = String.valueOf(110);

		TextView RightText12 = (TextView)findViewById(R.id.txtSensorRight12);
		packet[27][TYPE] = "RL"; packet[27][FACTOR] = RightText12.getText().toString() ; packet[27][VALUE] = String.valueOf(120);

		TextView RightText13 = (TextView)findViewById(R.id.txtSensorRight13);
		packet[28][TYPE] = "RL"; packet[28][FACTOR] = RightText13.getText().toString() ; packet[28][VALUE] = String.valueOf(130);

		TextView RightText14 = (TextView)findViewById(R.id.txtSensorRight14);
		packet[29][TYPE] = "RL"; packet[29][FACTOR] = RightText14.getText().toString() ; packet[29][VALUE] = String.valueOf(140);

		TextView SlideText0 = (TextView)findViewById(R.id.txtSensorSlide0);
		packet[30][TYPE] = "SL"; packet[30][FACTOR] = SlideText0.getText().toString() ; packet[30][VALUE] = String.valueOf(30);

		TextView SlideText1 = (TextView)findViewById(R.id.txtSensorSlide1);
		packet[31][TYPE] = "SL"; packet[31][FACTOR] = SlideText1.getText().toString() ; packet[31][VALUE] = String.valueOf(25);

		TextView SlideText2 = (TextView)findViewById(R.id.txtSensorSlide2);
		packet[32][TYPE] = "SL"; packet[32][FACTOR] = SlideText2.getText().toString() ; packet[32][VALUE] = String.valueOf(20);

		TextView SlideText3 = (TextView)findViewById(R.id.txtSensorSlide3);
		packet[33][TYPE] = "SL"; packet[33][FACTOR] = SlideText3.getText().toString() ; packet[33][VALUE] = String.valueOf(15);

		TextView SlideText4 = (TextView)findViewById(R.id.txtSensorSlide4);
		packet[34][TYPE] = "SL"; packet[34][FACTOR] = SlideText4.getText().toString() ; packet[34][VALUE] = String.valueOf(10);

		TextView SlideText5 = (TextView)findViewById(R.id.txtSensorSlide5);
		packet[35][TYPE] = "SL"; packet[35][FACTOR] = SlideText5.getText().toString() ; packet[35][VALUE] = String.valueOf(5);

		TextView SlideText6 = (TextView)findViewById(R.id.txtSensorSlide6);
		packet[36][TYPE] = "SC"; packet[36][FACTOR] = SlideText6.getText().toString() ; packet[36][VALUE] = String.valueOf(0);

		TextView SlideText7 = (TextView)findViewById(R.id.txtSensorSlide7);
		packet[37][TYPE] = "SR"; packet[37][FACTOR] = SlideText7.getText().toString() ; packet[37][VALUE] = String.valueOf(5);

		TextView SlideText8 = (TextView)findViewById(R.id.txtSensorSlide8);
		packet[38][TYPE] = "SR"; packet[38][FACTOR] = SlideText8.getText().toString() ; packet[38][VALUE] = String.valueOf(10);

		TextView SlideText9 = (TextView)findViewById(R.id.txtSensorSlide9);
		packet[39][TYPE] = "SR"; packet[39][FACTOR] = SlideText9.getText().toString() ; packet[39][VALUE] = String.valueOf(15);

		TextView SlideText10 = (TextView)findViewById(R.id.txtSensorSlide10);
		packet[40][TYPE] = "SR"; packet[40][FACTOR] = SlideText10.getText().toString() ; packet[40][VALUE] = String.valueOf(20);

		TextView SlideText11 = (TextView)findViewById(R.id.txtSensorSlide11);
		packet[41][TYPE] = "SR"; packet[41][FACTOR] = SlideText11.getText().toString() ; packet[41][VALUE] = String.valueOf(25);

		TextView SlideText12 = (TextView)findViewById(R.id.txtSensorSlide12);
		packet[42][TYPE] = "SR"; packet[42][FACTOR] = SlideText12.getText().toString() ; packet[42][VALUE] = String.valueOf(30);

		String str = "";
		for(int i=0;i<PACKET_LENGTH-2;i++){
			if(packet[i][FACTOR].equals("null") || packet[i][FACTOR].equals(" ") || packet[i][FACTOR].equals("NULL")){
				packet[i][FACTOR] = "FF";
			}
			for(int j=0;j<3;j++){				
				str = str + packet[i][j] + ",";				
			}			
		}
		if(packet[PACKET_LENGTH-1][FACTOR].equals("null") || packet[PACKET_LENGTH-1][FACTOR].equals(" ") || packet[PACKET_LENGTH-1][FACTOR].equals("NULL")){
			packet[PACKET_LENGTH-1][FACTOR] = "FF";
		}
		str = str + packet[PACKET_LENGTH-1][TYPE] + ",";
		str = str + packet[PACKET_LENGTH-1][FACTOR] + ",";
		str = str + packet[PACKET_LENGTH-1][VALUE] + "\n";		

		SharedPreferences pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString("savePacket", str);
		editor.commit();
		Toast.makeText(this, "Saved", 3).show();
		Log.i("[Packet]",str);
	}

	public void onClick(View v){
		SharedDataService ds = (SharedDataService)getApplication();		
		switch(v.getId()){
		case R.id.btnSensorLeft0 :	 			
			TextView leftText0 = (TextView)findViewById(R.id.txtSensorLeft0);
			leftText0.setText( ds.getDataService().getDigital("LL"));
			packet[0][TYPE] = "LL"; packet[0][FACTOR] = leftText0.getText().toString() ; packet[0][VALUE] = String.valueOf(0);
			break;
		case R.id.btnSensorLeft1 : 
			TextView leftText1 = (TextView)findViewById(R.id.txtSensorLeft1);
			leftText1.setText( ds.getDataService().getDigital("LL"));
			packet[1][TYPE] = "LL"; packet[1][FACTOR] = leftText1.getText().toString() ; packet[1][VALUE] = String.valueOf(10);
			break;
		case R.id.btnSensorLeft2 : 
			TextView leftText2 = (TextView)findViewById(R.id.txtSensorLeft2);
			leftText2.setText( ds.getDataService().getDigital("LL"));
			packet[2][TYPE] = "LL"; packet[2][FACTOR] = leftText2.getText().toString() ; packet[2][VALUE] = String.valueOf(20);
			break;
		case R.id.btnSensorLeft3 :
			TextView leftText3 = (TextView)findViewById(R.id.txtSensorLeft3);
			leftText3.setText( ds.getDataService().getDigital("LL"));
			packet[3][TYPE] = "LL"; packet[3][FACTOR] = leftText3.getText().toString() ; packet[3][VALUE] = String.valueOf(30);
			break;
		case R.id.btnSensorLeft4 : 
			TextView leftText4 = (TextView)findViewById(R.id.txtSensorLeft4);
			leftText4.setText( ds.getDataService().getDigital("LL"));
			packet[4][TYPE] = "LL"; packet[4][FACTOR] = leftText4.getText().toString() ; packet[4][VALUE] = String.valueOf(40);
			break;
		case R.id.btnSensorLeft5 : 
			TextView leftText5 = (TextView)findViewById(R.id.txtSensorLeft5);
			leftText5.setText( ds.getDataService().getDigital("LL"));
			packet[5][TYPE] = "LL"; packet[5][FACTOR] = leftText5.getText().toString() ; packet[5][VALUE] = String.valueOf(50);
			break;
		case R.id.btnSensorLeft6 : 
			TextView leftText6 = (TextView)findViewById(R.id.txtSensorLeft6);
			leftText6.setText( ds.getDataService().getDigital("LL"));
			packet[6][TYPE] = "LL"; packet[6][FACTOR] = leftText6.getText().toString() ; packet[6][VALUE] = String.valueOf(60);
			break;
		case R.id.btnSensorLeft7 : 
			TextView leftText7 = (TextView)findViewById(R.id.txtSensorLeft7);
			leftText7.setText( ds.getDataService().getDigital("LL"));
			packet[7][TYPE] = "LL"; packet[7][FACTOR] = leftText7.getText().toString() ; packet[7][VALUE] = String.valueOf(70);
			break;
		case R.id.btnSensorLeft8 : 
			TextView leftText8 = (TextView)findViewById(R.id.txtSensorLeft8);
			leftText8.setText( ds.getDataService().getDigital("LL"));
			packet[8][TYPE] = "LL"; packet[8][FACTOR] = leftText8.getText().toString() ; packet[8][VALUE] = String.valueOf(80);
			break;
		case R.id.btnSensorLeft9 : 
			TextView leftText9 = (TextView)findViewById(R.id.txtSensorLeft9);
			leftText9.setText( ds.getDataService().getDigital("LL"));
			packet[9][TYPE] = "LL"; packet[9][FACTOR] = leftText9.getText().toString() ; packet[9][VALUE] = String.valueOf(90);
			break;
		case R.id.btnSensorLeft10 : 
			TextView leftText10 = (TextView)findViewById(R.id.txtSensorLeft10);
			leftText10.setText( ds.getDataService().getDigital("LL"));
			packet[10][TYPE] = "LL"; packet[10][FACTOR] = leftText10.getText().toString() ; packet[10][VALUE] = String.valueOf(100);
			break;
		case R.id.btnSensorLeft11 : 
			TextView leftText11 = (TextView)findViewById(R.id.txtSensorLeft11);
			leftText11.setText( ds.getDataService().getDigital("LL"));
			packet[11][TYPE] = "LL"; packet[11][FACTOR] = leftText11.getText().toString() ; packet[11][VALUE] = String.valueOf(110);
			break;
		case R.id.btnSensorLeft12 : 
			TextView leftText12 = (TextView)findViewById(R.id.txtSensorLeft12);
			leftText12.setText( ds.getDataService().getDigital("LL"));
			packet[12][TYPE] = "LL"; packet[12][FACTOR] = leftText12.getText().toString() ; packet[12][VALUE] = String.valueOf(120);
			break;
		case R.id.btnSensorLeft13 :
			TextView leftText13 = (TextView)findViewById(R.id.txtSensorLeft13);
			leftText13.setText( ds.getDataService().getDigital("LL"));
			packet[13][TYPE] = "LL"; packet[13][FACTOR] = leftText13.getText().toString() ; packet[13][VALUE] = String.valueOf(130);
			break;
		case R.id.btnSensorLeft14 : 
			TextView leftText14 = (TextView)findViewById(R.id.txtSensorLeft14);
			leftText14.setText( ds.getDataService().getDigital("LL"));
			packet[14][TYPE] = "LL"; packet[14][FACTOR] = leftText14.getText().toString() ; packet[14][VALUE] = String.valueOf(140);
			break;
		case R.id.btnSensorRight0 :	 			
			TextView RightText0 = (TextView)findViewById(R.id.txtSensorRight0);
			RightText0.setText( ds.getDataService().getDigital("RL"));
			packet[15][TYPE] = "RL"; packet[15][FACTOR] = RightText0.getText().toString() ; packet[15][VALUE] = String.valueOf(0);
			break;
		case R.id.btnSensorRight1 : 
			TextView RightText1 = (TextView)findViewById(R.id.txtSensorRight1);
			RightText1.setText( ds.getDataService().getDigital("RL"));
			packet[16][TYPE] = "RL"; packet[16][FACTOR] = RightText1.getText().toString() ; packet[16][VALUE] = String.valueOf(10);
			break;
		case R.id.btnSensorRight2 : 
			TextView RightText2 = (TextView)findViewById(R.id.txtSensorRight2);
			RightText2.setText( ds.getDataService().getDigital("RL"));
			packet[17][TYPE] = "RL"; packet[17][FACTOR] = RightText2.getText().toString() ; packet[17][VALUE] = String.valueOf(20);
			break;
		case R.id.btnSensorRight3 :
			TextView RightText3 = (TextView)findViewById(R.id.txtSensorRight3);
			RightText3.setText( ds.getDataService().getDigital("RL"));
			packet[18][TYPE] = "RL"; packet[18][FACTOR] = RightText3.getText().toString() ; packet[18][VALUE] = String.valueOf(30);
			break;
		case R.id.btnSensorRight4 : 
			TextView RightText4 = (TextView)findViewById(R.id.txtSensorRight4);
			RightText4.setText( ds.getDataService().getDigital("RL"));
			packet[19][TYPE] = "RL"; packet[19][FACTOR] = RightText4.getText().toString() ; packet[19][VALUE] = String.valueOf(40);
			break;
		case R.id.btnSensorRight5 : 
			TextView RightText5 = (TextView)findViewById(R.id.txtSensorRight5);
			RightText5.setText( ds.getDataService().getDigital("RL"));
			packet[20][TYPE] = "RL"; packet[20][FACTOR] = RightText5.getText().toString() ; packet[20][VALUE] = String.valueOf(50);
			break;
		case R.id.btnSensorRight6 : 
			TextView RightText6 = (TextView)findViewById(R.id.txtSensorRight6);
			RightText6.setText( ds.getDataService().getDigital("RL"));
			packet[21][TYPE] = "RL"; packet[21][FACTOR] = RightText6.getText().toString() ; packet[21][VALUE] = String.valueOf(60);
			break;
		case R.id.btnSensorRight7 : 
			TextView RightText7 = (TextView)findViewById(R.id.txtSensorRight7);
			RightText7.setText( ds.getDataService().getDigital("RL"));
			packet[22][TYPE] = "RL"; packet[22][FACTOR] = RightText7.getText().toString() ; packet[22][VALUE] = String.valueOf(70);
			break;
		case R.id.btnSensorRight8 : 
			TextView RightText8 = (TextView)findViewById(R.id.txtSensorRight8);
			RightText8.setText( ds.getDataService().getDigital("RL"));
			packet[23][TYPE] = "RL"; packet[23][FACTOR] = RightText8.getText().toString() ; packet[23][VALUE] = String.valueOf(80);
			break;
		case R.id.btnSensorRight9 : 
			TextView RightText9 = (TextView)findViewById(R.id.txtSensorRight9);
			RightText9.setText( ds.getDataService().getDigital("RL"));
			packet[24][TYPE] = "RL"; packet[24][FACTOR] = RightText9.getText().toString() ; packet[24][VALUE] = String.valueOf(90);
			break;
		case R.id.btnSensorRight10 : 
			TextView RightText10 = (TextView)findViewById(R.id.txtSensorRight10);
			RightText10.setText( ds.getDataService().getDigital("RL"));
			packet[25][TYPE] = "RL"; packet[25][FACTOR] = RightText10.getText().toString() ; packet[25][VALUE] = String.valueOf(100);
			break;
		case R.id.btnSensorRight11 : 
			TextView RightText11 = (TextView)findViewById(R.id.txtSensorRight11);
			RightText11.setText( ds.getDataService().getDigital("RL"));
			packet[26][TYPE] = "RL"; packet[26][FACTOR] = RightText11.getText().toString() ; packet[26][VALUE] = String.valueOf(110);
			break;
		case R.id.btnSensorRight12 : 
			TextView RightText12 = (TextView)findViewById(R.id.txtSensorRight12);
			RightText12.setText( ds.getDataService().getDigital("RL"));
			packet[27][TYPE] = "RL"; packet[27][FACTOR] = RightText12.getText().toString() ; packet[27][VALUE] = String.valueOf(120);
			break;
		case R.id.btnSensorRight13 :
			TextView RightText13 = (TextView)findViewById(R.id.txtSensorRight13);
			RightText13.setText( ds.getDataService().getDigital("RL"));
			packet[28][TYPE] = "RL"; packet[28][FACTOR] = RightText13.getText().toString() ; packet[28][VALUE] = String.valueOf(130);
			break;
		case R.id.btnSensorRight14 : 
			TextView RightText14 = (TextView)findViewById(R.id.txtSensorRight14);
			RightText14.setText( ds.getDataService().getDigital("RL"));
			packet[29][TYPE] = "RL"; packet[29][FACTOR] = RightText14.getText().toString() ; packet[29][VALUE] = String.valueOf(140);
			break;
		
		case R.id.btnSensorSlide0 :	 			
			TextView SlideText0 = (TextView)findViewById(R.id.txtSensorSlide0);
			SlideText0.setText( ds.getDataService().getDigital("SL"));
			packet[30][TYPE] = "SL"; packet[30][FACTOR] = SlideText0.getText().toString() ; packet[30][VALUE] = String.valueOf(30);
			break;
		case R.id.btnSensorSlide1 : 
			TextView SlideText1 = (TextView)findViewById(R.id.txtSensorSlide1);
			SlideText1.setText( ds.getDataService().getDigital("SL"));
			packet[31][TYPE] = "SL"; packet[31][FACTOR] = SlideText1.getText().toString() ; packet[31][VALUE] = String.valueOf(25);
			break;
		case R.id.btnSensorSlide2 : 
			TextView SlideText2 = (TextView)findViewById(R.id.txtSensorSlide2);
			SlideText2.setText( ds.getDataService().getDigital("SL"));
			packet[32][TYPE] = "SL"; packet[32][FACTOR] = SlideText2.getText().toString() ; packet[32][VALUE] = String.valueOf(20);
			break;
		case R.id.btnSensorSlide3 :
			TextView SlideText3 = (TextView)findViewById(R.id.txtSensorSlide3);
			SlideText3.setText( ds.getDataService().getDigital("SL"));
			packet[33][TYPE] = "SL"; packet[33][FACTOR] = SlideText3.getText().toString() ; packet[33][VALUE] = String.valueOf(15);
			break;
		case R.id.btnSensorSlide4 : 
			TextView SlideText4 = (TextView)findViewById(R.id.txtSensorSlide4);
			SlideText4.setText( ds.getDataService().getDigital("SL"));
			packet[34][TYPE] = "SL"; packet[34][FACTOR] = SlideText4.getText().toString() ; packet[34][VALUE] = String.valueOf(10);
			break;
		case R.id.btnSensorSlide5 : 
			TextView SlideText5 = (TextView)findViewById(R.id.txtSensorSlide5);
			SlideText5.setText( ds.getDataService().getDigital("SL"));
			packet[35][TYPE] = "SL"; packet[35][FACTOR] = SlideText5.getText().toString() ; packet[35][VALUE] = String.valueOf(5);
			break;
		case R.id.btnSensorSlide6 : 
			TextView SlideText6 = (TextView)findViewById(R.id.txtSensorSlide6);
			SlideText6.setText( ds.getDataService().getDigital("SC"));
			packet[36][TYPE] = "SC"; packet[36][FACTOR] = SlideText6.getText().toString() ; packet[36][VALUE] = String.valueOf(0);
			break;
		case R.id.btnSensorSlide7 : 
			TextView SlideText7 = (TextView)findViewById(R.id.txtSensorSlide7);
			SlideText7.setText( ds.getDataService().getDigital("SR"));
			packet[37][TYPE] = "SR"; packet[37][FACTOR] = SlideText7.getText().toString() ; packet[37][VALUE] = String.valueOf(5);
			break;
		case R.id.btnSensorSlide8 : 
			TextView SlideText8 = (TextView)findViewById(R.id.txtSensorSlide8);
			SlideText8.setText( ds.getDataService().getDigital("SR"));
			packet[38][TYPE] = "SR"; packet[38][FACTOR] = SlideText8.getText().toString() ; packet[38][VALUE] = String.valueOf(10);
			break;
		case R.id.btnSensorSlide9 : 
			TextView SlideText9 = (TextView)findViewById(R.id.txtSensorSlide9);
			SlideText9.setText( ds.getDataService().getDigital("SR"));
			packet[39][TYPE] = "SR"; packet[39][FACTOR] = SlideText9.getText().toString() ; packet[39][VALUE] = String.valueOf(15);
			break;
		case R.id.btnSensorSlide10 : 
			TextView SlideText10 = (TextView)findViewById(R.id.txtSensorSlide10);
			SlideText10.setText( ds.getDataService().getDigital("SR"));
			packet[40][TYPE] = "SR"; packet[40][FACTOR] = SlideText10.getText().toString() ; packet[40][VALUE] = String.valueOf(20);
			break;
		case R.id.btnSensorSlide11 : 
			TextView SlideText11 = (TextView)findViewById(R.id.txtSensorSlide11);
			SlideText11.setText( ds.getDataService().getDigital("SR"));
			packet[41][TYPE] = "SR"; packet[41][FACTOR] = SlideText11.getText().toString() ; packet[41][VALUE] = String.valueOf(25);
			break;
		case R.id.btnSensorSlide12 : 
			TextView SlideText12 = (TextView)findViewById(R.id.txtSensorSlide12);
			SlideText12.setText( ds.getDataService().getDigital("SR"));
			packet[42][TYPE] = "SR"; packet[42][FACTOR] = SlideText12.getText().toString() ; packet[42][VALUE] = String.valueOf(30);
			break;

		case R.id.btnSave : 
			makePacket();

			break;
		case R.id.btnClose : 
			if(mTimer !=null){
				mTimer.cancel();
				mTimer.purge();
			}
			finish();
		}
	}

}
