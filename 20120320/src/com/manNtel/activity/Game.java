package com.manNtel.activity;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;
import org.cocos2d.sound.SoundEngine;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.android.manNtel_mid.R;
import com.manNtel.gameLayer.Number_1;
import com.manNtel.gameLayer.Number_2;
import com.manNtel.gameLayer.Number_3;
import com.manNtel.gameLayer.Number_4;
import com.manNtel.gameLayer.Number_5;
import com.manNtel.gameLayer.Number_6;
import com.manNtel.service.ProcessManager;
import com.manNtel.struct.GameStruct;

public class Game extends Activity 
{
	public static Game app;
    private CCGLSurfaceView mGLSurfaceView;
    
    public static Context ctx;
    int displayWidth, displayHeight;
    
    GameStruct user;
    
    public static SoundEngine bg_sound = SoundEngine.sharedEngine();
    public static SoundEngine bt_sound = SoundEngine.sharedEngine();
    public static int soundCode = 0;
    
    @Override
	public void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
    	
    	Bundle bundle = getIntent().getExtras();
		user = bundle.getParcelable("userInfo");
    	
    	app = this;
    		
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    	
    	mGLSurfaceView = new CCGLSurfaceView(this);
    	setContentView(mGLSurfaceView);
    	
    	CCDirector.sharedDirector().attachInView(mGLSurfaceView);
    	CCDirector.sharedDirector().setLandscape(false);
    	CCDirector.sharedDirector().setDisplayFPS(false);
    	CCDirector.sharedDirector().setAnimationInterval(1.0f/60);
    	    	
    	CCScene scene = CCScene.node();
    	switch(user.gameNum)
    	{
    	case 1:
    		scene.addChild(new Number_1(user, this));    		
    		break;
    	case 2:
    		scene.addChild(new Number_2(user, this));
    		break;    		
    	case 3:
    		scene.addChild(new Number_3(user, this));
    		break;
    	case 4:
    		scene.addChild(new Number_4(user, this));
    		break;
    	case 5:
    		scene.addChild(new Number_5(user, this));
    		break;
    	case 6:
    		scene.addChild(new Number_6(user, this));
    		break;   		
    	}    	
    	
    	CCDirector.sharedDirector().runWithScene(scene);    	
    	ProcessManager.getInstance().addActivity(this);
    }
    
    
    @Override
	public void onDestroy()
    {
    	super.onDestroy();
    	
    	CCDirector.sharedDirector().end();
    	bg_sound.realesAllSounds();
    	ProcessManager.getInstance().deleteActivity(this);
    }
    
    @Override
	public boolean onKeyUp(int keyCode,KeyEvent event)
    {    	 	
    	if(keyCode == KeyEvent.KEYCODE_BACK)
    	{    	
    		finish();
    	}
    	return true;
    }
    
    
    @Override
	public void onPause()
    {
    	super.onPause();
    	
    	bg_sound.pauseSound();
    	
    	CCDirector.sharedDirector().onPause();
    }
    
    @Override
	public void onResume()
    {
    	super.onResume();
    	
    	if(soundCode == 0x7f040002){
    		bg_sound.playSound(app, 0x7f040002, true);
    	}
    	
    	CCDirector.sharedDirector().onResume();
    }
    
    @Override
	public void onStart()
    {
    	super.onStart();
    }
    
    public void showExit()
    {
    	AlertDialog.Builder builder = null;
    	
    	builder = new AlertDialog.Builder(this);
    	builder.setIcon(R.drawable.icon);
    	builder.setTitle("title");
    	builder.setMessage("Exit?");
    	builder.setCancelable(false);
    	builder.setPositiveButton("yes",new DialogInterface.OnClickListener() {
    		@Override
			public void onClick(DialogInterface arg0, int arg1)
    		{
    			finish();
    		}
    	});
    	builder.setNegativeButton("no",new DialogInterface.OnClickListener() {
    		@Override
			public void onClick(DialogInterface arg0, int arg1)
    		{}
    	});
    	builder.create().show();
    } 
}
