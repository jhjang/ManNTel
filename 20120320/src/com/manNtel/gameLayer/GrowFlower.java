package com.manNtel.gameLayer;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.cocos2d.actions.CCProgressTimer;
import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCFadeIn;
import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.events.CCTouchDispatcher;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItemLabel;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrameCache;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.opengl.CCTexture2D;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.ccColor3B;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.MotionEvent;

import com.android.manNtel_mid.R;
import com.manNtel.activity.LevelSelect;
import com.manNtel.activity.Main;
import com.manNtel.service.ProcessManager;
import com.manNtel.service.SharedDataService;
import com.manNtel.struct.GameStruct;

public class GrowFlower extends CCLayer implements ControlState {
	protected CCSprite background = null;
	protected CCSprite seed = null;		
	protected CCSprite sun = null;
	protected CCSprite waterDrop = null;	
	protected CCSprite waterBucket = null;

	CCLabel guideLabel = null;
	CCLabel potCountLabel = null;
	CCLabel playTimeLabel = null;
	CCLabel weightLabel = null;

	protected static float CLEAR_ROTATE_VALUE = 0;
	Context mContext = null;
	private boolean pauseFlag = false;

	protected int inputBal = -1;
	protected int inputRoll = -1;
	protected int inputPitch = -1;
	protected int score = 0;

	protected boolean sunFlag = false;
	protected boolean waterFlag = false;
	protected boolean waterAniRunFlag = false;
	protected boolean sunAniRunFlag = false;	
	protected boolean scoreFlag = false;
	protected CCProgressTimer progressTimer = null;	

	GameStruct mUser;
	private long currentTime;
	private long startTime;	

	//디버그 
	CCLabel debugLabel_1 = null;
	CCLabel debugLabel_2 = null;
	CCLabel debugLabel_3 = null;
	
	String debugStr_1;
	String debugStr_2;
	String debugStr_3;	

	public GrowFlower(GameStruct user, Context context){
		super();
		this.setIsTouchEnabled(true);		

		progressTimer = CCProgressTimer.progress("common/progress.png");
		progressTimer.setType(CCProgressTimer.kCCProgressTimerTypeRadialCW);
		progressTimer.setPercentage(0);				

		startTime = System.currentTimeMillis() - (currentTime - startTime);

		mContext = context;
		mUser = user;
		//이미지 불러오기
		//배경
		background = loadImage(POS_BACKGROUND,"common/background");
		this.addChild(background,VIEW_BACK);

		if(mUser.part.equals("좌")){
			weightLabel = loadLabel(POS_WEIGHTLABELWATER_LEFT,0+"%",30);		
			sun = loadImage(POS_SUN_LEFT,"game4/sun1");
			progressTimer.setPosition(POS_WATERPROGRESS_LEFT);
			CLEAR_ROTATE_VALUE = -45;
		}
		
		
		else{
			weightLabel = loadLabel(POS_WEIGHTLABELWATER_RIGHT,0+"%",30);
			progressTimer.setPosition(POS_WATERPROGRESS_RIGHT);
			sun = loadImage(POS_SUN_RIGHT,"game4/sun1");
			CLEAR_ROTATE_VALUE = 45;
		}

		this.addChild(sun,VIEW_OBJ);
		this.addChild(progressTimer,VIEW_OBJ);

		//모종
		seed = loadImage(POS_SEED,"game4/seed_1");
		this.addChild(seed,VIEW_OBJ);

		//메뉴 불러오기
		setMenu();

		//라벨 불러오기
		String msg;
		msg = context.getResources().getText(R.string.game1StartLeft).toString();
		guideLabel = loadLabel(POS_GUIDELABEL, msg, 30);
		guideLabel.runAction(CCRepeatForever.action(CCSequence.actions(CCFadeIn.action(0.5f),CCFadeOut.action(0.5f))));
		this.addChild(guideLabel,VIEW_BACK);

		msg = context.getResources().getText(R.string.gameMovedPot) + "" + score + context.getResources().getText(R.string.gamePotCount).toString();
		potCountLabel = loadLabel(POS_SCORE,msg,30);
		this.addChild(potCountLabel,VIEW_BACK);

		msg = context.getResources().getText(R.string.gamePlayTime).toString();
		playTimeLabel = loadLabel(POS_PLAYTIME,msg,30);
		this.addChild(playTimeLabel,VIEW_BACK);			
		this.addChild(weightLabel,VIEW_OBJ);			

		this.schedule("updateTime");
		this.schedule("getInputValue");
		this.schedule("setAnimation");

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

	protected void activeSunSmile(boolean flag)
	{
		if(flag){						
			sunFlag = true;
		}
		else{						
			sunFlag = false;
		}			
	}

	protected void activeWater(boolean flag){
		if(flag){
			waterFlag = true;
		}
		else{
			waterFlag = false;
		}
	}

	public boolean ccTochesBegan(MotionEvent event){
		int x = (int)event.getX();
		int y = (int)event.getY();

		CGPoint touchLocation = CGPoint.ccp(x,y);
		touchLocation = CCDirector.sharedDirector().convertToGL(touchLocation);

		return CCTouchDispatcher.kEventHandled;
	}

	//메뉴 그리기
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

	public void getInputValue(float dt)
	{
		SharedDataService ds = (SharedDataService)CCDirector.sharedDirector().getActivity().getApplication();
		inputRoll = (int) ds.getDataService().getValue("RS");
		inputPitch = (int) ds.getDataService().getValue("PS");

		if(mUser.part.equals("좌"))
		{
			inputBal = (int) ds.getDataService().getValue("LL");
		}
		else
		{
			inputBal = (int) ds.getDataService().getValue("RL");
		}		
	}

	//이미지 불러오기
	protected CCSprite loadImage(CGPoint pos, String source)
	{
		CCSprite img = CCSprite.sprite(source+".png");

		img.setAnchorPoint(0.5f,0.5f);
		img.setPosition(pos);

		return img;
	}

	//라벨 그리기
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

		CCDirector.theApp.finish();
	}

	public void moveMain(Object sender)
	{
		//메인화면으로 이동
		//		ds.endFlag = true;
		Intent intent = new Intent(mContext,Main.class);
		mContext.startActivity(intent);
	}

	public void setAnimation(float dt){
		CCAnimation sunAnimation = CCAnimation.animation("sun");
		CCAnimation flowerAnimation = CCAnimation.animation("flower");
		CCAnimation seedAnimation = CCAnimation.animation("seed");

		//무게 o 각도 o
		if(sunFlag && waterFlag){			
			if(!sunAniRunFlag){
				sunAnimation.addFrame("game4/sun2.png");
				sunAnimation.addFrame("game4/sun3.png");
				CCAnimate sunAnimate = CCAnimate.action(0.5f, sunAnimation, false);

				sun.runAction(CCRepeatForever.action(sunAnimate));
				sunAniRunFlag = true;
			}
			if(!waterAniRunFlag){
				flowerAnimation.addFrame("game4/flower1.png");
				flowerAnimation.addFrame("game4/flower2.png");							
				CCAnimate flowerAnimate = CCAnimate.action(0.5f, flowerAnimation, false);			

				seed.runAction(CCRepeatForever.action(flowerAnimate));
				waterAniRunFlag = true;				
			}
			if(!scoreFlag){
				potCountLabel.setString(mContext.getResources().getText(R.string.gameMovedPot) + "" + ++score + mContext.getResources().getText(R.string.gamePotCount).toString());
				scoreFlag = true;
			}
			guideLabel.setString(mContext.getResources().getText(R.string.gameDecreaseWeight));

		}
		//무게 o 각도 x
		else if(sunFlag && !waterFlag ){
			if(!sunAniRunFlag){
				sunAnimation.addFrame("game4/sun2.png");
				sunAnimation.addFrame("game4/sun3.png");

				CCAnimate sunAnimate = CCAnimate.action(0.5f, sunAnimation, false);

				sun.runAction(CCRepeatForever.action(sunAnimate));
				sunAniRunFlag = true;
			}
		}

		//무게 x 각도 o
		else if(!sunFlag && waterFlag){
			if(!waterAniRunFlag){
				seedAnimation.addFrame("game4/seed_1.png");
				seedAnimation.addFrame("game4/seed_2.png");
				CCAnimate seedAnimate = CCAnimate.action(0.5f,seedAnimation,false);

				seed.runAction(CCRepeatForever.action(seedAnimate));
				waterAniRunFlag = true;
			}			
		}

		//무게 x 각도 x
		else{
			seed.stopAllActions();
			sun.stopAllActions();

			scoreFlag = false;
			waterAniRunFlag = false;
			sunAniRunFlag = false;			

			CCTexture2D texture = CCTextureCache.sharedTextureCache().addImage("game4/sun1.png");
			CCTexture2D seedTexture = CCTextureCache.sharedTextureCache().addImage("game4/seed_1.png");

			sun.setTexture(texture);
			seed.setTexture(seedTexture);
		}
	}

	public void setEndGame(Object sender)
	{
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

		CCDirector.theApp.startActivity(intent);
	}

	public void setLevel(Object sender)
	{
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

	//메뉴 불러오기
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
	//메뉴 콜백 함수
	public void setPause(Object sender)
	{
		if(pauseFlag)
		{
			CCDirector.sharedDirector().pause();
			pauseFlag = false;
			return;
		}
		pauseFlag = true;
		CCDirector.sharedDirector().resume();		
	}

	public void sysShutdown(Object sender)
	{
		ProcessManager.getInstance().allEndActivity();
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	//플레이타임 업데이트3
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
