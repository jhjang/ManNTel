package com.manNtel.gameLayer;

import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSpriteFrameCache;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.types.CGPoint;

import android.content.Context;
import com.android.manNtel_mid.R;
import com.manNtel.service.SharedDataService;
import com.manNtel.struct.GameStruct;

//게임 1
public class Number_1 extends MoveFlower {

	public Number_1(GameStruct user, Context context) 
	{
		super(user, context);
		mUser = user;

		this.schedule("moveObj");
	}
	
	public void moveObj(float dt)
	{		
		SharedDataService ds = (SharedDataService)CCDirector.sharedDirector().getActivity().getApplication();
		//왼쪽다리
		float value=0;
		if(mUser.part.equals("좌"))
		{
			value = ds.getDataService().getValue("LL");			
			if(isCanMove("LL") && !incScoreFlag)
			{
				if(chkBound_Left(flower))
				{
					incScoreFlag = true;
					guideLabel.setString(mContext.getResources().getText(R.string.gameDecreaseWeight));					
				}
				else 
				{
					guideLabel.setString(mContext.getResources().getText(R.string.gameMovingNow));
					flower.setPosition(CGPoint.ccp(flower.getPosition().x+moveValue, flower.getPosition().y));
				}					
			}			
			else 
			{
				if(incScoreFlag)
					guideLabel.setString(mContext.getResources().getText(R.string.gameDecreaseWeight));
				else if(value!=0)
					guideLabel.setString(mContext.getResources().getText(R.string.game1MovingLeft));
				else
					guideLabel.setString(mContext.getResources().getText(R.string.game1StartLeft));
			}
		}
		
		//오른쪽다리
		else 
		{			
			value = ds.getDataService().getValue("RL");			
			if(isCanMove("RL"))
			{
				if(chkBound_Right(flower))
				{
					incScoreFlag = true;
					guideLabel.setString(mContext.getResources().getText(R.string.gameDecreaseWeight));					
				}
				else 
				{
					guideLabel.setString(mContext.getResources().getText(R.string.gameMovingNow));
					flower.setPosition(CGPoint.ccp(flower.getPosition().x+moveValue, flower.getPosition().y));
				}

			}			
			else 
			{
				if(incScoreFlag)
					guideLabel.setString(mContext.getResources().getText(R.string.gameDecreaseWeight));
				else if(value!=0)
					guideLabel.setString(mContext.getResources().getText(R.string.game1MovingRight));
				else
					guideLabel.setString(mContext.getResources().getText(R.string.game1StartRight));
			}
		
		}		
		//점수 올라가는거
		if(incScoreFlag && (value-mUser.clearValue<0))
		{
			incScoreFlag = false;
			flower.setPosition(startPoint.getPosition());
			potCountLabel.setString(mContext.getResources().getText(R.string.gameMovedPot) + String.valueOf(++score) + mContext.getResources().getText(R.string.gamePotCount).toString());
		}
		
		weightLabel.setString((int)(value/mUser.clearValue*100)+"%");		
	}

	@Override
	public void onExit()
	{
		CCSpriteFrameCache.purgeSharedSpriteFrameCache();
		CCTextureCache.purgeSharedTextureCache();
		CCDirector.sharedDirector().purgeCachedData();
		CCDirector.sharedDirector().getSendCleanupToScene();
		this.unschedule("moveObj");
		super.onExit();		
	}
}