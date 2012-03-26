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
import android.util.Log;

import com.android.manNtel_mid.R;
import com.manNtel.service.SharedDataService;
import com.manNtel.struct.GameStruct;

public class Number_2 extends MoveFlower {

	CCSprite seed = null;
	CCLabel seedToFlowerCount = null;
	CCAnimation growUpAnimation = null;
	int count = 0;
	float value = 0;
	private boolean growUpAnimationFlag = false;
	public Number_2(GameStruct user, Context context) 
	{
		super(user, context);

		seed = loadImage(startPoint.getPosition(), "game2/seed");
		this.addChild(seed,VIEW_OBJ);
		
		flower.runAction(CCHide.action());

		seedToFlowerCount = loadLabel(POS_BACKGROUND, count + "��!", 30);
		seedToFlowerCount.runAction(CCHide.action());
		this.addChild(seedToFlowerCount,VIEW_BACK);

		potCountLabel.setString(mContext.getResources().getText(R.string.gameGrownFlower).toString()
				+ score + mContext.getResources().getText(R.string.gamePotCount).toString());
		
		growUpAnimation = CCAnimation.animation("growup");
		for(int i=1;i<=5;i++){
			growUpAnimation.addFrame(String.format("game2/growSeed_%d.png", i));
		}
		
		this.schedule("getInputValue");
		this.schedule("moveObj");
	}
	public void getInputValue(float dt)
	{
		SharedDataService ds = (SharedDataService)CCDirector.sharedDirector().getActivity().getApplication();
		
		if(mUser.part.equals("��"))
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
		if(mUser.part.equals("��"))
		{
			if(value-mUser.leftBal<=0)
			{
				this.schedule("moveObj");
				this.unschedule("isDecrease");

				//������ �ö󰡸� �ٽ� Ÿ�̸� ȣ�� �� �۾� �ʱ�ȭ
				seed.setPosition(startPoint.getPosition());
				CCTexture2D texture = CCTextureCache.sharedTextureCache().addImage("game2/seed.png");
				seed.setTexture(texture);
				
				count = 0;
				seedToFlowerCount.runAction(CCHide.action());
				arrow.runAction(CCShow.action());
			}
		}
		else
		{
			if(value-mUser.rightBal<=0)
			{
				this.schedule("moveObj");
				this.unschedule("isDecrease");
				//������ �ö󰡸� �ٽ� Ÿ�̸� ȣ�� �� �۾� �ʱ�ȭ
				seed.setPosition(startPoint.getPosition());
				CCTexture2D texture = CCTextureCache.sharedTextureCache().addImage("game2/seed.png");
				seed.setTexture(texture);						

				count = 0;
				seedToFlowerCount.runAction(CCHide.action());
				arrow.runAction(CCShow.action());
			}	
		}
	}

	public void moveObj(float dt)
	{
		//�޹�
		if(mUser.part.equals("��"))
		{
			//�����ϼ� �ֳ�? : ���� üũ
			if(isCanMove("LL"))
			{
				//�����ϼ� ������ �����̶�� �޽��� ���
				//���� ��ġ�� �ٿ��� ���� ���� �Լ� Ÿ�̸� ����.
				if(chkBound_Left(seed))
				{
					Log.e("[GameTwo]","Goal!");
					this.unschedule("moveObj");
					this.schedule("seedToFlower",1.0f);
					return;
				}
				//�ٿ�尡 �ƴϸ� �̵��Ѵ�. 
				else
				{
					guideLabel.setString(mContext.getResources().getText(R.string.gameMovingNow));
					seed.setPosition(seed.getPosition().x+moveValue,seed.getPosition().y);
				}

			}
			//�������̸� ���� �� ������� ���Ұ�
			else
			{
				//���԰� �ȽǷ� ������ �������.
				if(value <= 0)
					guideLabel.setString(mContext.getResources().getText(R.string.game1StartLeft));
				//���԰� �Ƿ� �ִµ� �������̸� �� �������.
				else
					guideLabel.setString(mContext.getResources().getText(R.string.game1MovingLeft));
			}
		}
		//������ 
		else
		{
			if(isCanMove("RL"))
			{
				//�����ϼ� ������ �����̶�� �޽��� ���
				//���� ��ġ�� �ٿ��� ���� ���� �Լ� Ÿ�̸� ����.
				if(chkBound_Right(seed))
				{
					Log.e("[GameTwo]","Goal!");
					this.unschedule("moveObj");
					this.schedule("seedToFlower",1.0f);
					return;
				}
				//�ٿ�尡 �ƴϸ� �̵��Ѵ�. 
				else
				{
					guideLabel.setString(mContext.getResources().getText(R.string.gameMovingNow));
					seed.setPosition(seed.getPosition().x+moveValue,seed.getPosition().y);
				}

			}
			//�������̸� ���� �� ������� ���Ұ�
			else
			{
				//���԰� �ȽǷ� ������ �������.
				if(value <= 0)
					guideLabel.setString(mContext.getResources().getText(R.string.game1StartRight));
				//���԰� �Ƿ� �ִµ� �������̸� �� �������.
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
		//�� �ǿ��� �޽��� ���
		guideLabel.setString(mContext.getResources().getText(R.string.game2SeedToFlower));
		seedToFlowerCount.runAction(CCShow.action());
		arrow.runAction(CCHide.action());

		SharedPreferences pref = mContext.getSharedPreferences("pref", Context.MODE_PRIVATE);
		targetCount = Integer.parseInt(pref.getString("gameOpt1", "5"));		
		
		Log.i("[Game2]",""+value);
		//���԰� ��ǥġ�� ũ�ų� ������?
		if(value >= mUser.clearValue && value >= mUser.clearValue)
		{			
			count++;
			CCAnimate action = CCAnimate.action(targetCount,growUpAnimation,false);			
			if(!growUpAnimationFlag ){
				growUpAnimationFlag = true;
				seed.runAction(action);
			}				
			
			seedToFlowerCount.setString(count + "��!!");
		}
		//�ƴϸ� ����ġ
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
			seed.stopAllActions();
			guideLabel.setString(mContext.getResources().getText(R.string.game2End));

			potCountLabel.setString(mContext.getResources().getText(R.string.gameGrownFlower).toString()
					+ ++score + mContext.getResources().getText(R.string.gamePotCount).toString());
			
			growUpAnimationFlag = false;			

			//���Ը� �ٽ� ����������
			this.unschedule("seedToFlower");
			this.schedule("isDecrease");
		}
	}
}