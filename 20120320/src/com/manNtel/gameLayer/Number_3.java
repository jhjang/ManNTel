package com.manNtel.gameLayer;

import org.cocos2d.actions.instant.CCHide;
import org.cocos2d.actions.instant.CCShow;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
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

public class Number_3 extends MoveFlower {

	CCSprite seed = null;
	CCLabel seedToFlowerCount = null;
	private boolean growUpAnimationFlag = false;
	CCAnimation growUpAnimation = null;
	int count = 0;
	float value = 0;
	int inputAngle = 0;		
	int clearAngle;
	public Number_3(GameStruct user, Context context) 
	{
		super(user, context);

		seed = loadImage(startPoint.getPosition(), "game2/seed");
		this.addChild(seed,VIEW_OBJ);

		seedToFlowerCount = loadLabel(POS_BACKGROUND, count + "초!", 30);
		seedToFlowerCount.runAction(CCHide.action());
		this.addChild(seedToFlowerCount,VIEW_BACK);
		
		flower.runAction(CCHide.action());
		
		growUpAnimation = CCAnimation.animation("growup");		
		for(int i=1;i<=5;i++){
			growUpAnimation.addFrame(String.format("game2/growSeed_%d.png", i));
		}

		potCountLabel.setString(mContext.getResources().getText(R.string.gameGrownFlower).toString()
				+ score + mContext.getResources().getText(R.string.gamePotCount).toString());		
		
		DatabaseManager dbm = new DatabaseManager(CCDirector.sharedDirector().getActivity());
		dbm.open();
		Cursor cursor = dbm.fetchItem(0, user.key);
		cursor.moveToFirst();
		clearAngle = cursor.getInt(9);		
		cursor.close();
		dbm.close();				
		
		SharedPreferences pref = CCDirector.sharedDirector().getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
		clearAngle = clearAngle * Integer.parseInt(pref.getString("gameOpt3", "80")) / 100;
		
		this.schedule("getInputValue");
		this.schedule("moveObj");		
	}
	
	public void getInputValue(float dt)
	{
		SharedDataService ds = (SharedDataService)CCDirector.sharedDirector().getActivity().getApplication();
		
		inputAngle = (int) ds.getDataService().getValue("TL");
		if(mUser.part.equals("좌"))
		{
			value = ds.getDataService().getValue("LL");
		}
		else
		{
			value = ds.getDataService().getValue("RL");
		}		
	}

	public void isDecrease(float dt)
	{
		if(mUser.part.equals("좌"))
		{
			if(value-mUser.leftBal<=0)
			{
				//점수가 올라가면 다시 타이머 호출 및 작업 초기화		
				seed.stopAllActions();
				seed.setPosition(startPoint.getPosition());
				CCTexture2D texture = CCTextureCache.sharedTextureCache().addImage("game2/seed.png");
				seed.setTexture(texture);		
				
				this.schedule("moveObj");
				this.unschedule("isDecrease");

				count = 0;
				seedToFlowerCount.runAction(CCHide.action());
				arrow.runAction(CCShow.action());
			}
		}
		else
		{
			if(value-mUser.rightBal<=0)
			{
				//점수가 올라가면 다시 타이머 호출 및 작업 초기화
				seed.stopAllActions();
				seed.setPosition(startPoint.getPosition());
				CCTexture2D texture = CCTextureCache.sharedTextureCache().addImage("game2/seed.png");
				seed.setTexture(texture);
				
				this.schedule("moveObj");
				this.unschedule("isDecrease");

				count = 0;
				seedToFlowerCount.runAction(CCHide.action());
				arrow.runAction(CCShow.action());
			}	
		}
	}

	public void moveObj(float dt)
	{
		//왼발
		if(mUser.part.equals("좌"))
		{
			//움직일수 있나? : 무게 체크
			if(isCanMove("LL"))
			{
				//움직일수 있으면 움직이라고 메시지 출력
				//현재 위치가 바운드면 무게 유지 함수 타이머 가동.
				if(chkBound_Left(seed))
				{
					this.unschedule("moveObj");
					this.schedule("seedToFlower",1.0f);
					return;
				}
				//바운드가 아니면 이동한다. 
				else
				{
					guideLabel.setString(mContext.getResources().getText(R.string.gameMovingNow));
					seed.setPosition(seed.getPosition().x+moveValue,seed.getPosition().y);
				}

			}
			//못움직이면 무게 더 실으라고 말할것
			else
			{
				//무게가 안실려 있으면 실으라고.
				if(value <= 0)
					guideLabel.setString(mContext.getResources().getText(R.string.game1StartLeft));
				//무게가 실려 있는데 못움직이면 더 실으라고.
				else
					guideLabel.setString(mContext.getResources().getText(R.string.game1MovingLeft));
			}
		}
		//오른발 
		else
		{
			if(isCanMove("RL"))
			{
				//움직일수 있으면 움직이라고 메시지 출력
				//현재 위치가 바운드면 무게 유지 함수 타이머 가동.
				if(chkBound_Right(seed))
				{
					this.unschedule("moveObj");
					this.schedule("seedToFlower",1.0f);
					return;
				}
				//바운드가 아니면 이동한다. 
				else
				{
					guideLabel.setString(mContext.getResources().getText(R.string.gameMovingNow));
					seed.setPosition(seed.getPosition().x+moveValue,seed.getPosition().y);
				}

			}
			//못움직이면 무게 더 실으라고 말할것
			else
			{
				//무게가 안실려 있으면 실으라고.
				if(value <= 0)
					guideLabel.setString(mContext.getResources().getText(R.string.game1StartRight));
				//무게가 실려 있는데 못움직이면 더 실으라고.
				else
					guideLabel.setString(mContext.getResources().getText(R.string.game1MovingRight));
			}
		}
		weightLabel.setString((int)(value/mUser.clearValue*100)+"%");
	}
	public void seedToFlower(float dt)
	{
		weightLabel.setString((int)(value/mUser.clearValue*100)+"%");
		int targetCount;
		//꽃 피우라고 메시지 출력
		guideLabel.setString(mContext.getResources().getText(R.string.game3SeedToFlower));
		seedToFlowerCount.runAction(CCShow.action());
		arrow.runAction(CCHide.action());

		SharedPreferences pref = mContext.getSharedPreferences("pref", Context.MODE_PRIVATE);
		targetCount = Integer.parseInt(pref.getString("gameOpt2", "5"));

		//무게가 목표치와 크거나 같으면?
		if(inputAngle >= clearAngle && value >= mUser.clearValue)
		{			
			count++;
			CCAnimate action = CCAnimate.action(targetCount,growUpAnimation,false);			
			if(!growUpAnimationFlag ){
				growUpAnimationFlag = true;
				seed.runAction(action);
			}
			
			seedToFlowerCount.setString(count + "!");
		}
		//아니면 원위치
		else
		{
			seed.stopAllActions();
			guideLabel.setString(mContext.getResources().getText(R.string.game2SeedToFlowerFail));
			count = 0;
			seedToFlowerCount.runAction(CCHide.action());
			CCTexture2D texture = CCTextureCache.sharedTextureCache().addImage("game2/growSeed_1.png");
			seed.setTexture(texture);
			growUpAnimationFlag = false;
		}

		if(count == targetCount)
		{						
			guideLabel.setString(mContext.getResources().getText(R.string.game2End));

			potCountLabel.setString(mContext.getResources().getText(R.string.gameGrownFlower).toString()
					+ ++score + mContext.getResources().getText(R.string.gamePotCount).toString());
			
			growUpAnimationFlag = false;

			//무게를 다시 돌릴때까지
			this.unschedule("seedToFlower");
			this.schedule("isDecrease");
		}
	}
}