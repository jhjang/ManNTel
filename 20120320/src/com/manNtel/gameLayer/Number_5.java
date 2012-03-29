package com.manNtel.gameLayer;

import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.opengl.CCTexture2D;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import com.android.manNtel_mid.R;
import com.manNtel.database.DatabaseManager;
import com.manNtel.service.SharedDataService;
import com.manNtel.struct.GameStruct;

public class Number_5 extends MoveFlower{

	private String mBalType = null;
	private String mSlideType = null;
	private float  mBalance = 0;
	private float  mSlide = 0;
	private boolean mSunAnimationFlag = false;
	private String mGuideMoreMsg = null;
	private String mGuideSlideStartMsg = null;

	private CCSprite sun = null;
	private boolean mWeightFlag = false;
	private boolean mSlideFlag = false;
	private int mDistance = -1;
	private int mUserSlide = -1;
	private float mUserBal = -1;
	private float mStartX = -1;
	private boolean mScoreFlag = false;	

	public Number_5(GameStruct user, Context context) {
		super(user, context);

		DatabaseManager dbm = new DatabaseManager(CCDirector.sharedDirector().getActivity());
		dbm.open();
		Cursor cursor = dbm.fetchItem(0, user.key);
		cursor.moveToFirst();
		mUserSlide = cursor.getInt(13);		
		cursor.close();
		dbm.close();

		SharedPreferences pref = CCDirector.sharedDirector().getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
		mUserSlide = mUserSlide * Integer.parseInt(pref.getString("gameOpt4", "80")) / 100;		

		//사용자 타입 및 이미지 포지션 설정
		if(user.part.equals("좌")){
			mBalType = "LL";
			mSlideType = "SL";
			sun = loadImage(POS_SUNSLIDE_LEFT,"game4/sun1");
			mGuideMoreMsg = CCDirector.theApp.getResources().getString(R.string.game1MovingLeft);
			mDistance = (int)(POS_END.x - POS_START.x);
			mStartX = POS_START.x;
			mUserBal = mUser.leftBal;
		}
		else{
			mBalType = "RL";
			mSlideType = "SR";
			sun = loadImage(POS_SUNSLIDE_RIGHT,"game4/sun1");
			mGuideMoreMsg = CCDirector.theApp.getResources().getString(R.string.game1MovingRight);
			mDistance = (int)(POS_START.x - POS_END.x);
			mStartX = POS_END.x;
			mUserBal = mUser.rightBal;
		}	

		mGuideSlideStartMsg = CCDirector.theApp.getResources().getString(R.string.game5SlideNow);

		//스프라이트 추가
		super.addChild(sun,VIEW_OBJ);

		//값 저장스케쥴 설정
		this.schedule("setInputValue");
		this.schedule("checkWeight");		
		this.schedule("checkSlide");
	}

	//값 저장 타이머 콜백 함수
	public void setInputValue(float dt){
		SharedDataService ds = (SharedDataService)CCDirector.sharedDirector().getActivity().getApplication();
		mBalance = ds.getDataService().getValue(mBalType);
		mSlide = ds.getDataService().getValue(mSlideType);		

		if(mSlide > mUserSlide)
			mSlide = mUserSlide;

		weightLabel.setString((int)(mBalance/mUser.clearValue*100)+"%");
	}	

	public void checkWeight(float dt){
		//이동이 가능한 무게 
		
		
		if(isCanMove(mBalType)){			
			guideLabel.setString(mGuideSlideStartMsg);

			mWeightFlag = true;			

			//애니메이션 설정
			if(mSunAnimationFlag)
				return;

			CCAnimation sunAnimation = CCAnimation.animation("sun");
			sunAnimation.addFrame("game4/sun2.png");
			sunAnimation.addFrame("game4/sun3.png");

			CCAnimate sunAnimate = CCAnimate.action(0.5f, sunAnimation, true);
			sun.runAction(CCRepeatForever.action(sunAnimate));
			mSunAnimationFlag = true;
			////////////////
		}
		//이동이 안될때 무게를 더 올려라.
		else{
			mWeightFlag = false;
			guideLabel.setString(mGuideMoreMsg);
			CCTexture2D texture = CCTextureCache.sharedTextureCache().addImage("game4/sun1.png");
			sun.stopAllActions();
			sun.setTexture(texture);	
			mSunAnimationFlag = false;
		}
	}

	public void checkSlide(float dt){		
		//무게가 다 안되면 바로 리턴
		if(!mWeightFlag || mSlideFlag){			
			return;
		}		
		
		
		//다음 이동 좌표 계산 후 이동  
		float nextFlowerX = mStartX + (mDistance * mSlide / mUserSlide);		
		flower.setPosition(nextFlowerX,flower.getPosition().y);
		
		//목적지에 도착하면.
		if(((mUser.part.equals("좌") && chkBound_Left(flower)) && mWeightFlag) || (mUser.part.equals("우") && chkBound_Right(flower) && mWeightFlag)){			
			mSlideFlag = true;			
			guideLabel.setString(mContext.getResources().getText(R.string.gameDecreaseWeight));
			if(!mScoreFlag){
				this.unschedule("checkWeight");
				this.unschedule("checkSlide");
				this.schedule("checkScore");
				mScoreFlag = true;
			}
		}	
	}
	
	public void checkScore(float dt){
		if(mBalance <= 0 && mSlide <=0){
			mScoreFlag = false;
			mWeightFlag = false;
			mSlideFlag = false;
			potCountLabel.setString(mContext.getResources().getText(R.string.gameMovedPot).toString()
					+ ++score + mContext.getResources().getText(R.string.gamePotCount).toString());
			this.unschedule("checkScore");
			this.schedule("checkWeight");
			this.schedule("checkSlide");
			return;
		}
			
	}
}