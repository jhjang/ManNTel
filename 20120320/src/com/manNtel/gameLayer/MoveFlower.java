package com.manNtel.gameLayer;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.interval.CCFadeIn;
import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.events.CCTouchDispatcher;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItemLabel;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrameCache;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.ccColor3B;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.MotionEvent;

import com.android.manNtel_mid.R;
import com.manNtel.activity.Game;
import com.manNtel.activity.LevelSelect;
import com.manNtel.activity.Main;
import com.manNtel.service.ProcessManager;
import com.manNtel.service.SharedDataService;
import com.manNtel.struct.GameStruct;

//일단 왼쪽만.

public class MoveFlower extends CCLayer implements ControlState 
{
	CCSprite background = null;
	CCSprite startPoint = null;
	CCSprite endPoint = null;
	CCSprite flower = null;
	CCSprite potLabelBack = null;
	CCSprite playTimeBack = null;
	CCSprite arrow = null;

	CCLabel guideLabel = null;
	CCLabel potCountLabel = null;
	CCLabel playTimeLabel = null;
	CCLabel weightLabel = null;

	boolean pauseFlag;
	boolean incScoreFlag;

	int score;
	int moveValue;

	Context mContext;
	GameStruct mUser;

	//디버그 
	CCLabel debugLabel_1 = null;
	CCLabel debugLabel_2 = null;
	CCLabel debugLabel_3 = null;

	String debugStr_1;
	String debugStr_2;
	String debugStr_3;	

	private long currentTime, startTime;	 

	public MoveFlower(GameStruct user, Context context){
		super(); 
		this.setIsTouchEnabled(true);

		score = 0;
		mContext = context;
		mUser = user;
		pauseFlag = false;

		String msg;

		startTime = System.currentTimeMillis() - (currentTime - startTime);

		background = loadImage(POS_BACKGROUND, "common/background");
		
		arrow = loadImage(POS_BACKGROUND,"common/arrow");
		if(user.part.equals("좌"))
		{			
			startPoint = loadImage(POS_START,"common/endpoint");
			endPoint = loadImage(POS_END,"common/endpoint");
			flower = loadImage(POS_START,"game1/flower1");
			msg = context.getResources().getText(R.string.game1StartLeft).toString();
			moveValue = -3;
		}

		else
		{
			startPoint = loadImage(POS_END,"common/endpoint");
			endPoint = loadImage(POS_START,"common/endpoint");
			flower = loadImage(POS_END,"game1/flower1");
			msg = context.getResources().getText(R.string.game1StartRight).toString();
			arrow.setFlipX(true);
			moveValue = 5;
		}

		startPoint.setOpacity(70);
		endPoint.setOpacity(70);

		this.addChild(background,VIEW_BACK);
		this.addChild(startPoint,VIEW_BACK);		
		this.addChild(endPoint,VIEW_BACK);
		this.addChild(flower,VIEW_OBJ);
		this.addChild(arrow,VIEW_BACK);

		arrow.runAction(CCRepeatForever.action(CCSequence.actions(CCFadeIn.action(0.5f),CCFadeOut.action(0.5f))));

		guideLabel = loadLabel(POS_GUIDELABEL, msg, 30);
		guideLabel.runAction(CCRepeatForever.action(CCSequence.actions(CCFadeIn.action(0.5f),CCFadeOut.action(0.5f))));
		this.addChild(guideLabel,VIEW_BACK);

		msg = context.getResources().getText(R.string.gameMovedPot) + "0" + context.getResources().getText(R.string.gamePotCount).toString();
		potCountLabel = loadLabel(POS_SCORE,msg,30);

		this.addChild(potCountLabel,VIEW_BACK);

		msg = context.getResources().getText(R.string.gamePlayTime).toString();
		playTimeLabel = loadLabel(POS_PLAYTIME,msg,30);
		this.addChild(playTimeLabel,VIEW_BACK);

		weightLabel = loadLabel(POS_WEIGHTLABEL,0+"%",30);

		setMenu();

		this.addChild(weightLabel,VIEW_BACK);		

		this.schedule("updateTime");	

		//디버깅 셋
		SharedPreferences pref = CCDirector.theApp.getSharedPreferences("pref", Context.MODE_PRIVATE);
		if(pref.getBoolean("isDebug", false)){
			debugLabel_1 = CCLabel.makeLabel(" ", "DroidSans", 15);
			debugLabel_1.setColor(ccColor3B.ccGREEN);
			debugLabel_1.setAnchorPoint(CGPoint.ccp(0, 0));
			debugLabel_1.setPosition(CGPoint.ccp(10, 0));

			debugLabel_2 = CCLabel.makeLabel(" ", "DroidSans", 15);
			debugLabel_2.setColor(ccColor3B.ccGREEN);
			debugLabel_2.setAnchorPoint(CGPoint.ccp(0, 0));
			debugLabel_2.setPosition(CGPoint.ccp(10, 15));

			debugLabel_3 = CCLabel.makeLabel(" ", "DroidSans", 15);
			debugLabel_3.setColor(ccColor3B.ccGREEN);
			debugLabel_3.setAnchorPoint(CGPoint.ccp(0, 0));
			debugLabel_3.setPosition(CGPoint.ccp(10, 30));

			this.addChild(debugLabel_1,VIEW_OBJ);
			this.addChild(debugLabel_2,VIEW_OBJ);
			this.addChild(debugLabel_3,VIEW_OBJ);

			debugStr_1 = " ";
			debugStr_2 = " ";
			debugStr_3 = " ";
			this.schedule("debug",1.0f);
		}
	}

	public void debug(float dt){
		//디버그 모드 처리		

		SharedDataService ds = (SharedDataService)CCDirector.theApp.getApplication();

		SimpleDateFormat sd = new SimpleDateFormat("yyyy MM dd-HH:mm:ss");		
		Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());

		debugStr_1 = debugStr_2;
		debugStr_2 = debugStr_3;
		debugStr_3 = sd.format(currentTimestamp) + " : " + ds.getDataService().getFullData();

		debugLabel_1.setString(debugStr_3);
		debugLabel_2.setString(debugStr_2);
		debugLabel_3.setString(debugStr_1);
	}

	public boolean ccTochesBegan(MotionEvent event){
		int x = (int)event.getX();
		int y = (int)event.getY();

		CGPoint touchLocation = CGPoint.ccp(x,y);
		touchLocation = CCDirector.sharedDirector().convertToGL(touchLocation);

		return CCTouchDispatcher.kEventHandled;
	}

	protected boolean chkBound_Left(CCSprite sprite)
	{
		if(endPoint.getPosition().x>=sprite.getPosition().x)
		{			
			return true;
		}		
		return false;
	}

	protected boolean chkBound_Right(CCSprite sprite)
	{
		if(endPoint.getPosition().x<=sprite.getPosition().x)
		{			
			return true;
		}		
		return false;
	}

	protected CCMenuItemLabel drawMenu(String labelName,CGPoint pos,String callBack)
	{			
		CCLabel label = CCLabel.makeLabel(labelName, "DroidSans", 18);
		CCSprite sprite = loadImage(pos, "common/menu");		
		sprite.setAnchorPoint(CGPoint.ccp(0.5f, 0.5f));
		this.addChild(sprite,VIEW_BACK);
		CCMenuItemLabel menuItem = CCMenuItemLabel.item(label,this,callBack);
		menuItem.setAnchorPoint(0.5f,0.5f);
		menuItem.setPosition(pos);
		menuItem.setColor(ccColor3B.ccc3(8, 37, 62));		

		return menuItem;
	}

	public boolean isCanMove(String flag)
	{
		SharedDataService ds = (SharedDataService)CCDirector.sharedDirector().getActivity().getApplication();
		return ds.getDataService().getValue(flag) >= mUser.clearValue;

	}

	protected CCSprite loadImage(CGPoint pos, String source)
	{
		CCSprite img = CCSprite.sprite(source+".png");

		img.setAnchorPoint(0.5f,0.5f);
		img.setPosition(pos);

		return img;
	}	

	protected CCLabel loadLabel(CGPoint pos, String value, int fontSize)
	{
		CCLabel label = CCLabel.makeLabel(value, "DroidSans", fontSize);
		label.setAnchorPoint(0.5f,0.5f);
		label.setPosition(pos);
		label.setColor(ccColor3B.ccBLACK);

		return label;		
	}

	public void moveBack(Object sender)
	{		
		//		ds.endFlag = true;
		Game.bt_sound.playEffect(Game.app, R.raw.button);
		CCDirector.theApp.finish();
	}		

	public void moveMain(Object sender)
	{
		//메인화면으로 이동		

		Game.bt_sound.playEffect(Game.app, R.raw.button);
		this.removeAllChildren(true);
		this.removeFromParentAndCleanup(true);

		CCSpriteFrameCache.purgeSharedSpriteFrameCache();
		CCTextureCache.purgeSharedTextureCache();
		CCDirector.sharedDirector().purgeCachedData();
		CCDirector.sharedDirector().getSendCleanupToScene();

		Intent intent = new Intent(CCDirector.theApp,Main.class);
		CCDirector.theApp.startActivity(intent);		
	}

	@Override
	public void onEnter()
	{
		Game.bg_sound.playSound(Game.app, 0x7f040002, true);
		Game.soundCode = 0x7f040002;
		super.onEnter();	
	}	

	public void setEndGame(Object sender)
	{
		Game.bt_sound.playEffect(Game.app, R.raw.button);
		//게임 종료
		
		mUser.score = score;
		mUser.count++;		

		Calendar calendar = Calendar.getInstance( );  // 현재 날짜/시간 등의 각종 정보 얻기

		mUser.playDate = calendar.get(Calendar.YEAR) + "." + (calendar.get(Calendar.MONDAY)+1) + "." + calendar.get(Calendar.DAY_OF_MONTH);

		String playTime = "00 : 00 : 00";
		currentTime = System.currentTimeMillis();

		long millis = currentTime - startTime;
		int seconds = (int)(millis / 1000);
		int minutes = seconds / 60;
		int hour = minutes / 60;

		seconds = seconds % 60;
		minutes = minutes % 60;
		millis = (millis / 10) % 100;

		playTime = String.format("%02d:%02d:%02d", hour, minutes, seconds);		
		mUser.playTime = playTime;		

		this.removeAllChildren(true);
		this.removeFromParentAndCleanup(true);

		CCSpriteFrameCache.purgeSharedSpriteFrameCache();
		CCTextureCache.purgeSharedTextureCache();
		CCDirector.sharedDirector().purgeCachedData();
		CCDirector.sharedDirector().getSendCleanupToScene();

		Intent intent = new Intent(CCDirector.theApp,GameQuit.class);
		intent.putExtra("userInfo", mUser);

		Game.bt_sound.realesAllSounds();
		CCDirector.theApp.startActivity(intent);
	}

	public void setLevel(Object sender)
	{
		Game.bt_sound.playEffect(Game.app, R.raw.button);
		//난이도 선택

		this.removeAllChildren(true);
		this.removeFromParentAndCleanup(true);

		CCSpriteFrameCache.purgeSharedSpriteFrameCache();
		CCTextureCache.purgeSharedTextureCache();
		CCDirector.sharedDirector().purgeCachedData();
		CCDirector.sharedDirector().getSendCleanupToScene();

		Intent intent = new Intent(CCDirector.theApp,LevelSelect.class);
		intent.putExtra("userInfo", mUser);
		CCDirector.theApp.startActivity(intent);
	}	

	protected void setMenu()
	{
		String menuRes = mContext.getResources().getText(R.string.menuMain).toString();

		CCMenu menu = CCMenu.menu();
		menu.addChild(drawMenu(menuRes,POS_MENUMAIN,"moveMain"));		

		menuRes = mContext.getResources().getText(R.string.menuSysOff).toString();
		menu.addChild(drawMenu(menuRes,POS_MENUSHUTDOWN,"sysShutdown"));

		menuRes = mContext.getResources().getText(R.string.menuBack).toString();
		menu.addChild(drawMenu(menuRes,POS_MENUBACK,"moveBack"));

		menuRes = mContext.getResources().getText(R.string.menuPause).toString();
		menu.addChild(drawMenu(menuRes,POS_MENUPAUSE,"setPause"));

		menuRes = mContext.getResources().getText(R.string.menuSetLevel).toString();
		menu.addChild(drawMenu(menuRes,POS_MENUSETLEVEL,"setLevel"));

		menuRes = mContext.getResources().getText(R.string.menuEndGame).toString();
		menu.addChild(drawMenu(menuRes,POS_MENUENDGAME,"setEndGame"));

		menu.setAnchorPoint(0.5f,0.5f);
		menu.setPosition(0,0);


		this.addChild(menu,VIEW_OBJ);
	}

	public void setPause(Object sender)
	{
		Game.bt_sound.playEffect(Game.app, R.raw.button);
		if(pauseFlag)
		{
			Game.app.onPause();
			pauseFlag = false;

			return;
		}
		pauseFlag = true;
		Game.app.onResume();		
	}

	public void sysShutdown(Object sender)
	{
		Game.bt_sound.playEffect(Game.app, R.raw.button);
		ProcessManager.getInstance().allEndActivity();
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	public void updateTime(float dt)
	{		
		String Display_string = "00 : 00 : 00";
		currentTime = System.currentTimeMillis();

		long millis = currentTime - startTime;
		int seconds = (int)(millis / 1000);
		int minutes = seconds / 60;
		int hour = minutes / 60;

		seconds = seconds % 60;
		minutes = minutes % 60;
		millis = (millis / 10) % 100;

		Display_string = String.format("%02d:%02d:%02d", hour, minutes, seconds);		

		playTimeLabel.setString(mContext.getResources().getText(R.string.gamePlayTime).toString() + 
				" " + Display_string);	
	}
}