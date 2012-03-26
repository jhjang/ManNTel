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
import com.manNtel.service.SharedDataService;

public class SensorSetting extends TabActivity {
	private static final int TYPE = 0;
	private static final int FACTOR = 1;
	private static final int VALUE = 2;

	private static final int PACKET_TYPE = 88;

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
		mTab.addTab(mTab.newTabSpec("tag").setIndicator("Tilt").setContent(R.id.sensorTilt));
		mTab.addTab(mTab.newTabSpec("tag").setIndicator("Roll").setContent(R.id.sensorRoll));
		mTab.addTab(mTab.newTabSpec("tag").setIndicator("Pitch").setContent(R.id.sensorPitch));
		mTab.addTab(mTab.newTabSpec("tag").setIndicator("Slide").setContent(R.id.sensorSlide));

		//0~14 : LL, 15~29 : RR, 30~44 : TS, 45~59 : RS, 60~74 : PS, 75~89 : SL, SR 
		packet = new String[PACKET_TYPE][3];
		for(int i=0;i<88;i++){     	

			if(i>=0 && i<15){
				packet[i][TYPE]="LL";
				packet[i][VALUE]=String.valueOf(i*10);
			}
			else if(i>=15 && i<30){
				packet[i][TYPE]="RL";
				packet[i][VALUE]=String.valueOf(i*10-150);
			}
			else if(i>=30 && i<45){
				packet[i][TYPE]="TL";					
			}
			else if(i>=45 && i<60){
				packet[i][TYPE]="RS";
			}
			else if(i>=60 && i<75){
				packet[i][TYPE]="PS";
			}
			else if(i>=75 && i<88){
				packet[i][TYPE]="SL";
			}
			packet[i][FACTOR]="FFFF";
		}
		SharedPreferences pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
		mData = pref.getString("savePacket", "null");

		if(pref.getBoolean("loadPacketFlag", false)){
			mData = pref.getString("loadedPacket", "null");
			Log.i("[Sensor]","Loaded");
			Log.i("[Sensor]","Before : " + mData);
			SharedPreferences.Editor editor = pref.edit();
			editor.putBoolean("loadPacketFlag", false);

			if(!mData.equals("null")){
				String tmp[] = mData.split("LOAD,");
				mData = tmp[1];
			}

			Log.i("[Sensor]","After : " + mData);
			editor.putString("savePacket", mData);

			editor.commit();
		}		

		Log.i("[Sensor]","Final : " + pref.getString("savePacket", "null"));

		initValue();

		//디버그 모드 처리				
		Log.i("[Sensor]","Debug On");

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
	}	 

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
			else if(tmp[i].equals(("TL"))){
				value = tmp[i+2];
				realValue = Integer.parseInt(value);

				switch(realValue){
				case -70 :
					TextView TiltText0 = (TextView)findViewById(R.id.txtSensorTilt0);
					TiltText0.setText(tmp[i+1]);
					break;
				case -60 :
					TextView TiltText1 = (TextView)findViewById(R.id.txtSensorTilt1);
					TiltText1.setText(tmp[i+1]);
					break;
				case -50 :
					TextView TiltText2 = (TextView)findViewById(R.id.txtSensorTilt2);
					TiltText2.setText(tmp[i+1]);
					break;
				case -40 :
					TextView TiltText3 = (TextView)findViewById(R.id.txtSensorTilt3);
					TiltText3.setText(tmp[i+1]);
					break;
				case -30 :
					TextView TiltText4 = (TextView)findViewById(R.id.txtSensorTilt4);
					TiltText4.setText(tmp[i+1]);
					break;
				case -20 :
					TextView TiltText5 = (TextView)findViewById(R.id.txtSensorTilt5);
					TiltText5.setText(tmp[i+1]);
					break;
				case -10 :
					TextView TiltText6 = (TextView)findViewById(R.id.txtSensorTilt6);
					TiltText6.setText(tmp[i+1]);
					break;
				case 0 :
					TextView TiltText7 = (TextView)findViewById(R.id.txtSensorTilt7);
					TiltText7.setText(tmp[i+1]);
					break;
				case 10 :
					TextView TiltText8 = (TextView)findViewById(R.id.txtSensorTilt8);
					TiltText8.setText(tmp[i+1]);
					break;
				case 20 :
					TextView TiltText9 = (TextView)findViewById(R.id.txtSensorTilt9);
					TiltText9.setText(tmp[i+1]);
					break;
				case 30 :
					TextView TiltText10 = (TextView)findViewById(R.id.txtSensorTilt10);
					TiltText10.setText(tmp[i+1]);
					break;
				case 40 :
					TextView TiltText11 = (TextView)findViewById(R.id.txtSensorTilt11);
					TiltText11.setText(tmp[i+1]);
					break;
				case 50 :
					TextView TiltText12 = (TextView)findViewById(R.id.txtSensorTilt12);
					TiltText12.setText(tmp[i+1]);
					break;
				case 60 :
					TextView TiltText13 = (TextView)findViewById(R.id.txtSensorTilt13);
					TiltText13.setText(tmp[i+1]);
					break;
				case 70 :
					TextView TiltText14 = (TextView)findViewById(R.id.txtSensorTilt14);
					TiltText14.setText(tmp[i+1]);
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
			else if(tmp[i].equals(("PS"))){
				value = tmp[i+2];
				realValue = Integer.parseInt(value);

				switch(realValue){
				case -70 :
					TextView PitchText0 = (TextView)findViewById(R.id.txtSensorPitch0);
					PitchText0.setText(tmp[i+1]);
					break;
				case -60 :
					TextView PitchText1 = (TextView)findViewById(R.id.txtSensorPitch1);
					PitchText1.setText(tmp[i+1]);
					break;
				case -50 :
					TextView PitchText2 = (TextView)findViewById(R.id.txtSensorPitch2);
					PitchText2.setText(tmp[i+1]);
					break;
				case -40 :
					TextView PitchText3 = (TextView)findViewById(R.id.txtSensorPitch3);
					PitchText3.setText(tmp[i+1]);
					break;
				case -30 :
					TextView PitchText4 = (TextView)findViewById(R.id.txtSensorPitch4);
					PitchText4.setText(tmp[i+1]);
					break;
				case -20 :
					TextView PitchText5 = (TextView)findViewById(R.id.txtSensorPitch5);
					PitchText5.setText(tmp[i+1]);
					break;
				case -10 :
					TextView PitchText6 = (TextView)findViewById(R.id.txtSensorPitch6);
					PitchText6.setText(tmp[i+1]);
					break;
				case 0 :
					TextView PitchText7 = (TextView)findViewById(R.id.txtSensorPitch7);
					PitchText7.setText(tmp[i+1]);
					break;
				case 10 :
					TextView PitchText8 = (TextView)findViewById(R.id.txtSensorPitch8);
					PitchText8.setText(tmp[i+1]);
					break;
				case 20 :
					TextView PitchText9 = (TextView)findViewById(R.id.txtSensorPitch9);
					PitchText9.setText(tmp[i+1]);
					break;
				case 30 :
					TextView PitchText10 = (TextView)findViewById(R.id.txtSensorPitch10);
					PitchText10.setText(tmp[i+1]);
					break;
				case 40 :
					TextView PitchText11 = (TextView)findViewById(R.id.txtSensorPitch11);
					PitchText11.setText(tmp[i+1]);
					break;
				case 50 :
					TextView PitchText12 = (TextView)findViewById(R.id.txtSensorPitch12);
					PitchText12.setText(tmp[i+1]);
					break;
				case 60 :
					TextView PitchText13 = (TextView)findViewById(R.id.txtSensorPitch13);
					PitchText13.setText(tmp[i+1]);
					break;
				case 70 :
					TextView PitchText14 = (TextView)findViewById(R.id.txtSensorPitch14);
					PitchText14.setText(tmp[i+1]);
					break;					
				}				
			}
			else if(tmp[i].equals(("RS"))){
				value = tmp[i+2];
				realValue = Integer.parseInt(value);

				switch(realValue){
				case -70 :
					TextView RollText0 = (TextView)findViewById(R.id.txtSensorRoll0);
					RollText0.setText(tmp[i+1]);
					break;
				case -60 :
					TextView RollText1 = (TextView)findViewById(R.id.txtSensorRoll1);
					RollText1.setText(tmp[i+1]);
					break;
				case -50 :
					TextView RollText2 = (TextView)findViewById(R.id.txtSensorRoll2);
					RollText2.setText(tmp[i+1]);
					break;
				case -40 :
					TextView RollText3 = (TextView)findViewById(R.id.txtSensorRoll3);
					RollText3.setText(tmp[i+1]);
					break;
				case -30 :
					TextView RollText4 = (TextView)findViewById(R.id.txtSensorRoll4);
					RollText4.setText(tmp[i+1]);
					break;
				case -20 :
					TextView RollText5 = (TextView)findViewById(R.id.txtSensorRoll5);
					RollText5.setText(tmp[i+1]);
					break;
				case -10 :
					TextView RollText6 = (TextView)findViewById(R.id.txtSensorRoll6);
					RollText6.setText(tmp[i+1]);
					break;
				case 0 :
					TextView RollText7 = (TextView)findViewById(R.id.txtSensorRoll7);
					RollText7.setText(tmp[i+1]);
					break;
				case 10 :
					TextView RollText8 = (TextView)findViewById(R.id.txtSensorRoll8);
					RollText8.setText(tmp[i+1]);
					break;
				case 20 :
					TextView RollText9 = (TextView)findViewById(R.id.txtSensorRoll9);
					RollText9.setText(tmp[i+1]);
					break;
				case 30 :
					TextView RollText10 = (TextView)findViewById(R.id.txtSensorRoll10);
					RollText10.setText(tmp[i+1]);
					break;
				case 40 :
					TextView RollText11 = (TextView)findViewById(R.id.txtSensorRoll11);
					RollText11.setText(tmp[i+1]);
					break;
				case 50 :
					TextView RollText12 = (TextView)findViewById(R.id.txtSensorRoll12);
					RollText12.setText(tmp[i+1]);
					break;
				case 60 :
					TextView RollText13 = (TextView)findViewById(R.id.txtSensorRoll13);
					RollText13.setText(tmp[i+1]);
					break;
				case 70 :
					TextView RollText14 = (TextView)findViewById(R.id.txtSensorRoll14);
					RollText14.setText(tmp[i+1]);
					break;					
				}
			}
			else if(tmp[i].equals(("SL"))|| tmp[i].equals("SC")){
				value = tmp[i+2];
				Log.i("[Sensor Value]", "SensorValue : " + value);
				value.replaceAll(" ", " ");
				value.replaceAll("\\n"," ");				

				realValue = Integer.parseInt(value);
				Log.i("[Sensor Value]", "RealValue : " + realValue);

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
				Log.i("[Sensor Value]", "SensorValue : " + value);
				value.replaceAll(" ", " ");
				value.replaceAll("\\n"," ");				

				realValue = Integer.parseInt(value);
				Log.i("[Sensor Value]", "RealValue : " + realValue);

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

		TextView TiltText0 = (TextView)findViewById(R.id.txtSensorTilt0);
		packet[30][TYPE] = "TL"; packet[30][FACTOR] = TiltText0.getText().toString() ; packet[30][VALUE] = String.valueOf(-70);

		TextView TiltText1 = (TextView)findViewById(R.id.txtSensorTilt1);
		packet[31][TYPE] = "TL"; packet[31][FACTOR] = TiltText1.getText().toString() ; packet[31][VALUE] = String.valueOf(-60);

		TextView TiltText2 = (TextView)findViewById(R.id.txtSensorTilt2);
		packet[32][TYPE] = "TL"; packet[32][FACTOR] = TiltText2.getText().toString() ; packet[32][VALUE] = String.valueOf(-50);

		TextView TiltText3 = (TextView)findViewById(R.id.txtSensorTilt3);
		packet[33][TYPE] = "TL"; packet[33][FACTOR] = TiltText3.getText().toString() ; packet[33][VALUE] = String.valueOf(-40);

		TextView TiltText4 = (TextView)findViewById(R.id.txtSensorTilt4);
		packet[34][TYPE] = "TL"; packet[34][FACTOR] = TiltText4.getText().toString() ; packet[34][VALUE] = String.valueOf(-30);

		TextView TiltText5 = (TextView)findViewById(R.id.txtSensorTilt5);
		packet[35][TYPE] = "TL"; packet[35][FACTOR] = TiltText5.getText().toString() ; packet[35][VALUE] = String.valueOf(-20);

		TextView TiltText6 = (TextView)findViewById(R.id.txtSensorTilt6);
		packet[36][TYPE] = "TL"; packet[36][FACTOR] = TiltText6.getText().toString() ; packet[36][VALUE] = String.valueOf(-10);

		TextView TiltText7 = (TextView)findViewById(R.id.txtSensorTilt7);
		packet[37][TYPE] = "TL"; packet[37][FACTOR] = TiltText7.getText().toString() ; packet[37][VALUE] = String.valueOf(0);

		TextView TiltText8 = (TextView)findViewById(R.id.txtSensorTilt8);
		packet[38][TYPE] = "TL"; packet[38][FACTOR] = TiltText8.getText().toString() ; packet[38][VALUE] = String.valueOf(10);

		TextView TiltText9 = (TextView)findViewById(R.id.txtSensorTilt9);
		packet[39][TYPE] = "TL"; packet[39][FACTOR] = TiltText9.getText().toString() ; packet[39][VALUE] = String.valueOf(20);

		TextView TiltText10 = (TextView)findViewById(R.id.txtSensorTilt10);
		packet[40][TYPE] = "TL"; packet[40][FACTOR] = TiltText10.getText().toString() ; packet[40][VALUE] = String.valueOf(30);

		TextView TiltText11 = (TextView)findViewById(R.id.txtSensorTilt11);
		packet[41][TYPE] = "TL"; packet[41][FACTOR] = TiltText11.getText().toString() ; packet[41][VALUE] = String.valueOf(40);

		TextView TiltText12 = (TextView)findViewById(R.id.txtSensorTilt12);
		packet[42][TYPE] = "TL"; packet[42][FACTOR] = TiltText12.getText().toString() ; packet[42][VALUE] = String.valueOf(50);

		TextView TiltText13 = (TextView)findViewById(R.id.txtSensorTilt13);
		packet[43][TYPE] = "TL"; packet[43][FACTOR] = TiltText13.getText().toString() ; packet[43][VALUE] = String.valueOf(60);

		TextView TiltText14 = (TextView)findViewById(R.id.txtSensorTilt14);
		packet[44][TYPE] = "TL"; packet[44][FACTOR] = TiltText14.getText().toString() ; packet[44][VALUE] = String.valueOf(70);

		TextView RollText0 = (TextView)findViewById(R.id.txtSensorRoll0);
		packet[45][TYPE] = "RS"; packet[45][FACTOR] = RollText0.getText().toString() ; packet[45][VALUE] = String.valueOf(-70);

		TextView RollText1 = (TextView)findViewById(R.id.txtSensorRoll1);
		packet[46][TYPE] = "RS"; packet[46][FACTOR] = RollText1.getText().toString() ; packet[46][VALUE] = String.valueOf(-60);

		TextView RollText2 = (TextView)findViewById(R.id.txtSensorRoll2);
		packet[47][TYPE] = "RS"; packet[47][FACTOR] = RollText2.getText().toString() ; packet[47][VALUE] = String.valueOf(-50);

		TextView RollText3 = (TextView)findViewById(R.id.txtSensorRoll3);
		packet[48][TYPE] = "RS"; packet[48][FACTOR] = RollText3.getText().toString() ; packet[48][VALUE] = String.valueOf(-40);

		TextView RollText4 = (TextView)findViewById(R.id.txtSensorRoll4);
		packet[49][TYPE] = "RS"; packet[49][FACTOR] = RollText4.getText().toString() ; packet[49][VALUE] = String.valueOf(-30);

		TextView RollText5 = (TextView)findViewById(R.id.txtSensorRoll5);
		packet[50][TYPE] = "RS"; packet[50][FACTOR] = RollText5.getText().toString() ; packet[50][VALUE] = String.valueOf(-20);

		TextView RollText6 = (TextView)findViewById(R.id.txtSensorRoll6);
		packet[51][TYPE] = "RS"; packet[51][FACTOR] = RollText6.getText().toString() ; packet[51][VALUE] = String.valueOf(-10);

		TextView RollText7 = (TextView)findViewById(R.id.txtSensorRoll7);
		packet[52][TYPE] = "RS"; packet[52][FACTOR] = RollText7.getText().toString() ; packet[52][VALUE] = String.valueOf(0);

		TextView RollText8 = (TextView)findViewById(R.id.txtSensorRoll8);
		packet[53][TYPE] = "RS"; packet[53][FACTOR] = RollText8.getText().toString() ; packet[53][VALUE] = String.valueOf(10);

		TextView RollText9 = (TextView)findViewById(R.id.txtSensorRoll9);
		packet[54][TYPE] = "RS"; packet[54][FACTOR] = RollText9.getText().toString() ; packet[54][VALUE] = String.valueOf(20);

		TextView RollText10 = (TextView)findViewById(R.id.txtSensorRoll10);
		packet[55][TYPE] = "RS"; packet[55][FACTOR] = RollText10.getText().toString() ; packet[55][VALUE] = String.valueOf(30);

		TextView RollText11 = (TextView)findViewById(R.id.txtSensorRoll11);
		packet[56][TYPE] = "RS"; packet[56][FACTOR] = RollText11.getText().toString() ; packet[56][VALUE] = String.valueOf(40);

		TextView RollText12 = (TextView)findViewById(R.id.txtSensorRoll12);
		packet[57][TYPE] = "RS"; packet[57][FACTOR] = RollText12.getText().toString() ; packet[57][VALUE] = String.valueOf(50);

		TextView RollText13 = (TextView)findViewById(R.id.txtSensorRoll13);
		packet[58][TYPE] = "RS"; packet[58][FACTOR] = RollText13.getText().toString() ; packet[58][VALUE] = String.valueOf(60);

		TextView RollText14 = (TextView)findViewById(R.id.txtSensorRoll14);
		packet[59][TYPE] = "RS"; packet[59][FACTOR] = RollText14.getText().toString() ; packet[59][VALUE] = String.valueOf(70);

		TextView PitchText0 = (TextView)findViewById(R.id.txtSensorPitch0);
		packet[60][TYPE] = "PS"; packet[60][FACTOR] = PitchText0.getText().toString() ; packet[60][VALUE] = String.valueOf(-70);

		TextView PitchText1 = (TextView)findViewById(R.id.txtSensorPitch1);
		packet[61][TYPE] = "PS"; packet[61][FACTOR] = PitchText1.getText().toString() ; packet[61][VALUE] = String.valueOf(-60);

		TextView PitchText2 = (TextView)findViewById(R.id.txtSensorPitch2);
		packet[62][TYPE] = "PS"; packet[62][FACTOR] = PitchText2.getText().toString() ; packet[62][VALUE] = String.valueOf(-50);

		TextView PitchText3 = (TextView)findViewById(R.id.txtSensorPitch3);
		packet[63][TYPE] = "PS"; packet[63][FACTOR] = PitchText3.getText().toString() ; packet[63][VALUE] = String.valueOf(-40);

		TextView PitchText4 = (TextView)findViewById(R.id.txtSensorPitch4);
		packet[64][TYPE] = "PS"; packet[64][FACTOR] = PitchText4.getText().toString() ; packet[64][VALUE] = String.valueOf(-30);

		TextView PitchText5 = (TextView)findViewById(R.id.txtSensorPitch5);
		packet[65][TYPE] = "PS"; packet[65][FACTOR] = PitchText5.getText().toString() ; packet[65][VALUE] = String.valueOf(-20);

		TextView PitchText6 = (TextView)findViewById(R.id.txtSensorPitch6);
		packet[66][TYPE] = "PS"; packet[66][FACTOR] = PitchText6.getText().toString() ; packet[66][VALUE] = String.valueOf(-10);

		TextView PitchText7 = (TextView)findViewById(R.id.txtSensorPitch7);
		packet[67][TYPE] = "PS"; packet[67][FACTOR] = PitchText7.getText().toString() ; packet[67][VALUE] = String.valueOf(0);

		TextView PitchText8 = (TextView)findViewById(R.id.txtSensorPitch8);
		packet[68][TYPE] = "PS"; packet[68][FACTOR] = PitchText8.getText().toString() ; packet[68][VALUE] = String.valueOf(10);

		TextView PitchText9 = (TextView)findViewById(R.id.txtSensorPitch9);
		packet[69][TYPE] = "PS"; packet[69][FACTOR] = PitchText9.getText().toString() ; packet[69][VALUE] = String.valueOf(20);

		TextView PitchText10 = (TextView)findViewById(R.id.txtSensorPitch10);
		packet[70][TYPE] = "PS"; packet[70][FACTOR] = PitchText10.getText().toString() ; packet[70][VALUE] = String.valueOf(30);

		TextView PitchText11 = (TextView)findViewById(R.id.txtSensorPitch11);
		packet[71][TYPE] = "PS"; packet[71][FACTOR] = PitchText11.getText().toString() ; packet[71][VALUE] = String.valueOf(40);

		TextView PitchText12 = (TextView)findViewById(R.id.txtSensorPitch12);
		packet[72][TYPE] = "PS"; packet[72][FACTOR] = PitchText12.getText().toString() ; packet[72][VALUE] = String.valueOf(50);

		TextView PitchText13 = (TextView)findViewById(R.id.txtSensorPitch13);
		packet[73][TYPE] = "PS"; packet[73][FACTOR] = PitchText13.getText().toString() ; packet[73][VALUE] = String.valueOf(60);

		TextView PitchText14 = (TextView)findViewById(R.id.txtSensorPitch14);
		packet[74][TYPE] = "PS"; packet[74][FACTOR] = PitchText14.getText().toString() ; packet[74][VALUE] = String.valueOf(70);

		TextView SlideText0 = (TextView)findViewById(R.id.txtSensorSlide0);
		packet[75][TYPE] = "SL"; packet[75][FACTOR] = SlideText0.getText().toString() ; packet[75][VALUE] = String.valueOf(30);

		TextView SlideText1 = (TextView)findViewById(R.id.txtSensorSlide1);
		packet[76][TYPE] = "SL"; packet[76][FACTOR] = SlideText1.getText().toString() ; packet[76][VALUE] = String.valueOf(25);

		TextView SlideText2 = (TextView)findViewById(R.id.txtSensorSlide2);
		packet[77][TYPE] = "SL"; packet[77][FACTOR] = SlideText2.getText().toString() ; packet[77][VALUE] = String.valueOf(20);

		TextView SlideText3 = (TextView)findViewById(R.id.txtSensorSlide3);
		packet[78][TYPE] = "SL"; packet[78][FACTOR] = SlideText3.getText().toString() ; packet[78][VALUE] = String.valueOf(15);

		TextView SlideText4 = (TextView)findViewById(R.id.txtSensorSlide4);
		packet[79][TYPE] = "SL"; packet[79][FACTOR] = SlideText4.getText().toString() ; packet[79][VALUE] = String.valueOf(10);

		TextView SlideText5 = (TextView)findViewById(R.id.txtSensorSlide5);
		packet[80][TYPE] = "SL"; packet[80][FACTOR] = SlideText5.getText().toString() ; packet[80][VALUE] = String.valueOf(5);

		TextView SlideText6 = (TextView)findViewById(R.id.txtSensorSlide6);
		packet[81][TYPE] = "SC"; packet[81][FACTOR] = SlideText6.getText().toString() ; packet[81][VALUE] = String.valueOf(0);

		TextView SlideText7 = (TextView)findViewById(R.id.txtSensorSlide7);
		packet[82][TYPE] = "SR"; packet[82][FACTOR] = SlideText7.getText().toString() ; packet[82][VALUE] = String.valueOf(5);

		TextView SlideText8 = (TextView)findViewById(R.id.txtSensorSlide8);
		packet[83][TYPE] = "SR"; packet[83][FACTOR] = SlideText8.getText().toString() ; packet[83][VALUE] = String.valueOf(10);

		TextView SlideText9 = (TextView)findViewById(R.id.txtSensorSlide9);
		packet[84][TYPE] = "SR"; packet[84][FACTOR] = SlideText9.getText().toString() ; packet[84][VALUE] = String.valueOf(15);

		TextView SlideText10 = (TextView)findViewById(R.id.txtSensorSlide10);
		packet[85][TYPE] = "SR"; packet[85][FACTOR] = SlideText10.getText().toString() ; packet[85][VALUE] = String.valueOf(20);

		TextView SlideText11 = (TextView)findViewById(R.id.txtSensorSlide11);
		packet[86][TYPE] = "SR"; packet[86][FACTOR] = SlideText11.getText().toString() ; packet[86][VALUE] = String.valueOf(25);

		TextView SlideText12 = (TextView)findViewById(R.id.txtSensorSlide12);
		packet[87][TYPE] = "SR"; packet[87][FACTOR] = SlideText12.getText().toString() ; packet[87][VALUE] = String.valueOf(30);

		String str = "";
		for(int i=0;i<87;i++){
			if(packet[i][FACTOR].equals("null") || packet[i][FACTOR].equals(" ") || packet[i][FACTOR].equals("NULL")){
				packet[i][FACTOR] = "FF";
			}
			for(int j=0;j<3;j++){				
				str = str + packet[i][j] + ",";				
			}			
		}
		if(packet[87][FACTOR].equals("null") || packet[87][FACTOR].equals(" ") || packet[87][FACTOR].equals("NULL")){
			packet[87][FACTOR] = "FF";
		}
		str = str + packet[87][TYPE] + ",";
		str = str + packet[87][FACTOR] + ",";
		str = str + packet[87][VALUE] + "\n";		

		SharedPreferences pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString("savePacket", str);
		editor.commit();
		Log.i("[Save]","Packet saved : " + str);
		Toast.makeText(this, "저장되었습니다.", 3).show();
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
		case R.id.btnSensorTilt0 :	 			
			TextView TiltText0 = (TextView)findViewById(R.id.txtSensorTilt0);
			TiltText0.setText( ds.getDataService().getDigital("TL"));
			packet[30][TYPE] = "TL"; packet[30][FACTOR] = TiltText0.getText().toString() ; packet[30][VALUE] = String.valueOf(-70);
			break;
		case R.id.btnSensorTilt1 : 
			TextView TiltText1 = (TextView)findViewById(R.id.txtSensorTilt1);
			TiltText1.setText( ds.getDataService().getDigital("TL"));
			packet[31][TYPE] = "TL"; packet[31][FACTOR] = TiltText1.getText().toString() ; packet[31][VALUE] = String.valueOf(-60);
			break;
		case R.id.btnSensorTilt2 : 
			TextView TiltText2 = (TextView)findViewById(R.id.txtSensorTilt2);
			TiltText2.setText( ds.getDataService().getDigital("TL"));
			packet[32][TYPE] = "TL"; packet[32][FACTOR] = TiltText2.getText().toString() ; packet[32][VALUE] = String.valueOf(-50);
			break;
		case R.id.btnSensorTilt3 :
			TextView TiltText3 = (TextView)findViewById(R.id.txtSensorTilt3);
			TiltText3.setText( ds.getDataService().getDigital("TL"));
			packet[33][TYPE] = "TL"; packet[33][FACTOR] = TiltText3.getText().toString() ; packet[33][VALUE] = String.valueOf(-40);
			break;
		case R.id.btnSensorTilt4 : 
			TextView TiltText4 = (TextView)findViewById(R.id.txtSensorTilt4);
			TiltText4.setText( ds.getDataService().getDigital("TL"));
			packet[34][TYPE] = "TL"; packet[34][FACTOR] = TiltText4.getText().toString() ; packet[34][VALUE] = String.valueOf(-30);
			break;
		case R.id.btnSensorTilt5 : 
			TextView TiltText5 = (TextView)findViewById(R.id.txtSensorTilt5);
			TiltText5.setText( ds.getDataService().getDigital("TL"));
			packet[35][TYPE] = "TL"; packet[35][FACTOR] = TiltText5.getText().toString() ; packet[35][VALUE] = String.valueOf(-20);
			break;
		case R.id.btnSensorTilt6 : 
			TextView TiltText6 = (TextView)findViewById(R.id.txtSensorTilt6);
			TiltText6.setText( ds.getDataService().getDigital("TL"));
			packet[36][TYPE] = "TL"; packet[36][FACTOR] = TiltText6.getText().toString() ; packet[36][VALUE] = String.valueOf(-10);
			break;
		case R.id.btnSensorTilt7 : 
			TextView TiltText7 = (TextView)findViewById(R.id.txtSensorTilt7);
			TiltText7.setText( ds.getDataService().getDigital("TL"));
			packet[37][TYPE] = "TL"; packet[37][FACTOR] = TiltText7.getText().toString() ; packet[37][VALUE] = String.valueOf(0);
			break;
		case R.id.btnSensorTilt8 : 
			TextView TiltText8 = (TextView)findViewById(R.id.txtSensorTilt8);
			TiltText8.setText( ds.getDataService().getDigital("TL"));
			packet[38][TYPE] = "TL"; packet[38][FACTOR] = TiltText8.getText().toString() ; packet[38][VALUE] = String.valueOf(10);
			break;
		case R.id.btnSensorTilt9 : 
			TextView TiltText9 = (TextView)findViewById(R.id.txtSensorTilt9);
			TiltText9.setText( ds.getDataService().getDigital("TL"));
			packet[39][TYPE] = "TL"; packet[39][FACTOR] = TiltText9.getText().toString() ; packet[39][VALUE] = String.valueOf(20);
			break;
		case R.id.btnSensorTilt10 : 
			TextView TiltText10 = (TextView)findViewById(R.id.txtSensorTilt10);
			TiltText10.setText( ds.getDataService().getDigital("TL"));
			packet[40][TYPE] = "TL"; packet[40][FACTOR] = TiltText10.getText().toString() ; packet[40][VALUE] = String.valueOf(30);
			break;
		case R.id.btnSensorTilt11 : 
			TextView TiltText11 = (TextView)findViewById(R.id.txtSensorTilt11);
			TiltText11.setText( ds.getDataService().getDigital("TL"));
			packet[41][TYPE] = "TL"; packet[41][FACTOR] = TiltText11.getText().toString() ; packet[41][VALUE] = String.valueOf(40);
			break;
		case R.id.btnSensorTilt12 : 
			TextView TiltText12 = (TextView)findViewById(R.id.txtSensorTilt12);
			TiltText12.setText( ds.getDataService().getDigital("TL"));
			packet[42][TYPE] = "TL"; packet[42][FACTOR] = TiltText12.getText().toString() ; packet[42][VALUE] = String.valueOf(50);
			break;
		case R.id.btnSensorTilt13 :
			TextView TiltText13 = (TextView)findViewById(R.id.txtSensorTilt13);
			TiltText13.setText( ds.getDataService().getDigital("TL"));
			packet[43][TYPE] = "TL"; packet[43][FACTOR] = TiltText13.getText().toString() ; packet[43][VALUE] = String.valueOf(60);
			break;
		case R.id.btnSensorTilt14 : 
			TextView TiltText14 = (TextView)findViewById(R.id.txtSensorTilt14);
			TiltText14.setText( ds.getDataService().getDigital("TL"));
			packet[44][TYPE] = "TL"; packet[44][FACTOR] = TiltText14.getText().toString() ; packet[44][VALUE] = String.valueOf(70);
			break;
		case R.id.btnSensorRoll0 :	 			
			TextView RollText0 = (TextView)findViewById(R.id.txtSensorRoll0);
			RollText0.setText( ds.getDataService().getDigital("RS"));
			packet[45][TYPE] = "RS"; packet[45][FACTOR] = RollText0.getText().toString() ; packet[45][VALUE] = String.valueOf(-70);
			break;
		case R.id.btnSensorRoll1 : 
			TextView RollText1 = (TextView)findViewById(R.id.txtSensorRoll1);
			RollText1.setText( ds.getDataService().getDigital("RS"));
			packet[46][TYPE] = "RS"; packet[46][FACTOR] = RollText1.getText().toString() ; packet[46][VALUE] = String.valueOf(-60);
			break;
		case R.id.btnSensorRoll2 : 
			TextView RollText2 = (TextView)findViewById(R.id.txtSensorRoll2);
			RollText2.setText( ds.getDataService().getDigital("RS"));
			packet[47][TYPE] = "RS"; packet[47][FACTOR] = RollText2.getText().toString() ; packet[47][VALUE] = String.valueOf(-50);
			break;
		case R.id.btnSensorRoll3 :
			TextView RollText3 = (TextView)findViewById(R.id.txtSensorRoll3);
			RollText3.setText( ds.getDataService().getDigital("RS"));
			packet[48][TYPE] = "RS"; packet[48][FACTOR] = RollText3.getText().toString() ; packet[48][VALUE] = String.valueOf(-40);
			break;
		case R.id.btnSensorRoll4 : 
			TextView RollText4 = (TextView)findViewById(R.id.txtSensorRoll4);
			RollText4.setText( ds.getDataService().getDigital("RS"));
			packet[49][TYPE] = "RS"; packet[49][FACTOR] = RollText4.getText().toString() ; packet[49][VALUE] = String.valueOf(-30);
			break;
		case R.id.btnSensorRoll5 : 
			TextView RollText5 = (TextView)findViewById(R.id.txtSensorRoll5);
			RollText5.setText( ds.getDataService().getDigital("RS"));
			packet[50][TYPE] = "RS"; packet[50][FACTOR] = RollText5.getText().toString() ; packet[50][VALUE] = String.valueOf(-20);
			break;
		case R.id.btnSensorRoll6 : 
			TextView RollText6 = (TextView)findViewById(R.id.txtSensorRoll6);
			RollText6.setText( ds.getDataService().getDigital("RS"));
			packet[51][TYPE] = "RS"; packet[51][FACTOR] = RollText6.getText().toString() ; packet[51][VALUE] = String.valueOf(-10);
			break;
		case R.id.btnSensorRoll7 : 
			TextView RollText7 = (TextView)findViewById(R.id.txtSensorRoll7);
			RollText7.setText( ds.getDataService().getDigital("RS"));
			packet[52][TYPE] = "RS"; packet[52][FACTOR] = RollText7.getText().toString() ; packet[52][VALUE] = String.valueOf(0);
			break;
		case R.id.btnSensorRoll8 : 
			TextView RollText8 = (TextView)findViewById(R.id.txtSensorRoll8);
			RollText8.setText( ds.getDataService().getDigital("RS"));
			packet[53][TYPE] = "RS"; packet[53][FACTOR] = RollText8.getText().toString() ; packet[53][VALUE] = String.valueOf(10);
			break;
		case R.id.btnSensorRoll9 : 
			TextView RollText9 = (TextView)findViewById(R.id.txtSensorRoll9);
			RollText9.setText( ds.getDataService().getDigital("RS"));
			packet[54][TYPE] = "RS"; packet[54][FACTOR] = RollText9.getText().toString() ; packet[54][VALUE] = String.valueOf(20);
			break;
		case R.id.btnSensorRoll10 : 
			TextView RollText10 = (TextView)findViewById(R.id.txtSensorRoll10);
			RollText10.setText( ds.getDataService().getDigital("RS"));
			packet[55][TYPE] = "RS"; packet[55][FACTOR] = RollText10.getText().toString() ; packet[55][VALUE] = String.valueOf(30);
			break;
		case R.id.btnSensorRoll11 : 
			TextView RollText11 = (TextView)findViewById(R.id.txtSensorRoll11);
			RollText11.setText( ds.getDataService().getDigital("RS"));
			packet[56][TYPE] = "RS"; packet[56][FACTOR] = RollText11.getText().toString() ; packet[56][VALUE] = String.valueOf(40);
			break;
		case R.id.btnSensorRoll12 : 
			TextView RollText12 = (TextView)findViewById(R.id.txtSensorRoll12);
			RollText12.setText( ds.getDataService().getDigital("RS"));
			packet[57][TYPE] = "RS"; packet[57][FACTOR] = RollText12.getText().toString() ; packet[57][VALUE] = String.valueOf(50);
			break;
		case R.id.btnSensorRoll13 :
			TextView RollText13 = (TextView)findViewById(R.id.txtSensorRoll13);
			RollText13.setText( ds.getDataService().getDigital("RS"));
			packet[58][TYPE] = "RS"; packet[58][FACTOR] = RollText13.getText().toString() ; packet[58][VALUE] = String.valueOf(60);
			break;
		case R.id.btnSensorRoll14 : 
			TextView RollText14 = (TextView)findViewById(R.id.txtSensorRoll14);
			RollText14.setText( ds.getDataService().getDigital("RS"));
			packet[59][TYPE] = "RS"; packet[59][FACTOR] = RollText14.getText().toString() ; packet[59][VALUE] = String.valueOf(70);
			break;

		case R.id.btnSensorPitch0 :	 			
			TextView PitchText0 = (TextView)findViewById(R.id.txtSensorPitch0);
			PitchText0.setText( ds.getDataService().getDigital("PS"));
			packet[60][TYPE] = "PS"; packet[60][FACTOR] = PitchText0.getText().toString() ; packet[60][VALUE] = String.valueOf(-70);
			break;
		case R.id.btnSensorPitch1 : 
			TextView PitchText1 = (TextView)findViewById(R.id.txtSensorPitch1);
			PitchText1.setText( ds.getDataService().getDigital("PS"));
			packet[61][TYPE] = "PS"; packet[61][FACTOR] = PitchText1.getText().toString() ; packet[61][VALUE] = String.valueOf(-60);
			break;
		case R.id.btnSensorPitch2 : 
			TextView PitchText2 = (TextView)findViewById(R.id.txtSensorPitch2);
			PitchText2.setText( ds.getDataService().getDigital("PS"));
			packet[62][TYPE] = "PS"; packet[62][FACTOR] = PitchText2.getText().toString() ; packet[62][VALUE] = String.valueOf(-50);
			break;
		case R.id.btnSensorPitch3 :
			TextView PitchText3 = (TextView)findViewById(R.id.txtSensorPitch3);
			PitchText3.setText( ds.getDataService().getDigital("PS"));
			packet[63][TYPE] = "PS"; packet[63][FACTOR] = PitchText3.getText().toString() ; packet[63][VALUE] = String.valueOf(-40);
			break;
		case R.id.btnSensorPitch4 : 
			TextView PitchText4 = (TextView)findViewById(R.id.txtSensorPitch4);
			PitchText4.setText( ds.getDataService().getDigital("PS"));
			packet[64][TYPE] = "PS"; packet[64][FACTOR] = PitchText4.getText().toString() ; packet[64][VALUE] = String.valueOf(-30);
			break;
		case R.id.btnSensorPitch5 : 
			TextView PitchText5 = (TextView)findViewById(R.id.txtSensorPitch5);
			PitchText5.setText( ds.getDataService().getDigital("PS"));
			packet[65][TYPE] = "PS"; packet[65][FACTOR] = PitchText5.getText().toString() ; packet[65][VALUE] = String.valueOf(-20);
			break;
		case R.id.btnSensorPitch6 : 
			TextView PitchText6 = (TextView)findViewById(R.id.txtSensorPitch6);
			PitchText6.setText( ds.getDataService().getDigital("PS"));
			packet[66][TYPE] = "PS"; packet[66][FACTOR] = PitchText6.getText().toString() ; packet[66][VALUE] = String.valueOf(-10);
			break;
		case R.id.btnSensorPitch7 : 
			TextView PitchText7 = (TextView)findViewById(R.id.txtSensorPitch7);
			PitchText7.setText( ds.getDataService().getDigital("PS"));
			packet[67][TYPE] = "PS"; packet[67][FACTOR] = PitchText7.getText().toString() ; packet[67][VALUE] = String.valueOf(0);
			break;
		case R.id.btnSensorPitch8 : 
			TextView PitchText8 = (TextView)findViewById(R.id.txtSensorPitch8);
			PitchText8.setText( ds.getDataService().getDigital("PS"));
			packet[68][TYPE] = "PS"; packet[68][FACTOR] = PitchText8.getText().toString() ; packet[68][VALUE] = String.valueOf(10);
			break;
		case R.id.btnSensorPitch9 : 
			TextView PitchText9 = (TextView)findViewById(R.id.txtSensorPitch9);
			PitchText9.setText( ds.getDataService().getDigital("PS"));
			packet[69][TYPE] = "PS"; packet[69][FACTOR] = PitchText9.getText().toString() ; packet[69][VALUE] = String.valueOf(20);
			break;
		case R.id.btnSensorPitch10 : 
			TextView PitchText10 = (TextView)findViewById(R.id.txtSensorPitch10);
			PitchText10.setText( ds.getDataService().getDigital("PS"));
			packet[70][TYPE] = "PS"; packet[70][FACTOR] = PitchText10.getText().toString() ; packet[70][VALUE] = String.valueOf(30);
			break;
		case R.id.btnSensorPitch11 : 
			TextView PitchText11 = (TextView)findViewById(R.id.txtSensorPitch11);
			PitchText11.setText( ds.getDataService().getDigital("PS"));
			packet[71][TYPE] = "PS"; packet[71][FACTOR] = PitchText11.getText().toString() ; packet[71][VALUE] = String.valueOf(40);
			break;
		case R.id.btnSensorPitch12 : 
			TextView PitchText12 = (TextView)findViewById(R.id.txtSensorPitch12);
			PitchText12.setText( ds.getDataService().getDigital("PS"));
			packet[72][TYPE] = "PS"; packet[72][FACTOR] = PitchText12.getText().toString() ; packet[72][VALUE] = String.valueOf(50);
			break;
		case R.id.btnSensorPitch13 :
			TextView PitchText13 = (TextView)findViewById(R.id.txtSensorPitch13);
			PitchText13.setText( ds.getDataService().getDigital("PS"));
			packet[73][TYPE] = "PS"; packet[73][FACTOR] = PitchText13.getText().toString() ; packet[73][VALUE] = String.valueOf(60);
			break;
		case R.id.btnSensorPitch14 : 
			TextView PitchText14 = (TextView)findViewById(R.id.txtSensorPitch14);
			PitchText14.setText( ds.getDataService().getDigital("PS"));
			packet[74][TYPE] = "PS"; packet[74][FACTOR] = PitchText14.getText().toString() ; packet[74][VALUE] = String.valueOf(70);
			break;

		case R.id.btnSensorSlide0 :	 			
			TextView SlideText0 = (TextView)findViewById(R.id.txtSensorSlide0);
			SlideText0.setText( ds.getDataService().getDigital("SL"));
			packet[75][TYPE] = "SL"; packet[75][FACTOR] = SlideText0.getText().toString() ; packet[75][VALUE] = String.valueOf(30);
			break;
		case R.id.btnSensorSlide1 : 
			TextView SlideText1 = (TextView)findViewById(R.id.txtSensorSlide1);
			SlideText1.setText( ds.getDataService().getDigital("SL"));
			packet[76][TYPE] = "SL"; packet[76][FACTOR] = SlideText1.getText().toString() ; packet[76][VALUE] = String.valueOf(25);
			break;
		case R.id.btnSensorSlide2 : 
			TextView SlideText2 = (TextView)findViewById(R.id.txtSensorSlide2);
			SlideText2.setText( ds.getDataService().getDigital("SL"));
			packet[77][TYPE] = "SL"; packet[77][FACTOR] = SlideText2.getText().toString() ; packet[77][VALUE] = String.valueOf(20);
			break;
		case R.id.btnSensorSlide3 :
			TextView SlideText3 = (TextView)findViewById(R.id.txtSensorSlide3);
			SlideText3.setText( ds.getDataService().getDigital("SL"));
			packet[78][TYPE] = "SL"; packet[78][FACTOR] = SlideText3.getText().toString() ; packet[78][VALUE] = String.valueOf(15);
			break;
		case R.id.btnSensorSlide4 : 
			TextView SlideText4 = (TextView)findViewById(R.id.txtSensorSlide4);
			SlideText4.setText( ds.getDataService().getDigital("SL"));
			packet[79][TYPE] = "SL"; packet[79][FACTOR] = SlideText4.getText().toString() ; packet[79][VALUE] = String.valueOf(10);
			break;
		case R.id.btnSensorSlide5 : 
			TextView SlideText5 = (TextView)findViewById(R.id.txtSensorSlide5);
			SlideText5.setText( ds.getDataService().getDigital("SL"));
			packet[80][TYPE] = "SL"; packet[80][FACTOR] = SlideText5.getText().toString() ; packet[80][VALUE] = String.valueOf(5);
			break;
		case R.id.btnSensorSlide6 : 
			TextView SlideText6 = (TextView)findViewById(R.id.txtSensorSlide6);
			SlideText6.setText( ds.getDataService().getDigital("SC"));
			packet[81][TYPE] = "SC"; packet[81][FACTOR] = SlideText6.getText().toString() ; packet[81][VALUE] = String.valueOf(0);
			break;
		case R.id.btnSensorSlide7 : 
			TextView SlideText7 = (TextView)findViewById(R.id.txtSensorSlide7);
			SlideText7.setText( ds.getDataService().getDigital("SR"));
			packet[82][TYPE] = "SR"; packet[82][FACTOR] = SlideText7.getText().toString() ; packet[82][VALUE] = String.valueOf(5);
			break;
		case R.id.btnSensorSlide8 : 
			TextView SlideText8 = (TextView)findViewById(R.id.txtSensorSlide8);
			SlideText8.setText( ds.getDataService().getDigital("SR"));
			packet[83][TYPE] = "SR"; packet[83][FACTOR] = SlideText8.getText().toString() ; packet[83][VALUE] = String.valueOf(10);
			break;
		case R.id.btnSensorSlide9 : 
			TextView SlideText9 = (TextView)findViewById(R.id.txtSensorSlide9);
			SlideText9.setText( ds.getDataService().getDigital("SR"));
			packet[84][TYPE] = "SR"; packet[84][FACTOR] = SlideText9.getText().toString() ; packet[84][VALUE] = String.valueOf(15);
			break;
		case R.id.btnSensorSlide10 : 
			TextView SlideText10 = (TextView)findViewById(R.id.txtSensorSlide10);
			SlideText10.setText( ds.getDataService().getDigital("SR"));
			packet[85][TYPE] = "SR"; packet[85][FACTOR] = SlideText10.getText().toString() ; packet[85][VALUE] = String.valueOf(20);
			break;
		case R.id.btnSensorSlide11 : 
			TextView SlideText11 = (TextView)findViewById(R.id.txtSensorSlide11);
			SlideText11.setText( ds.getDataService().getDigital("SR"));
			packet[86][TYPE] = "SR"; packet[86][FACTOR] = SlideText11.getText().toString() ; packet[86][VALUE] = String.valueOf(25);
			break;
		case R.id.btnSensorSlide12 : 
			TextView SlideText12 = (TextView)findViewById(R.id.txtSensorSlide12);
			SlideText12.setText( ds.getDataService().getDigital("SR"));
			packet[87][TYPE] = "SR"; packet[87][FACTOR] = SlideText12.getText().toString() ; packet[87][VALUE] = String.valueOf(30);
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
