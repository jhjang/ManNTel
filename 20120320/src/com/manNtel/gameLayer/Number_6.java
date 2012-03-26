package com.manNtel.gameLayer;

import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.instant.CCHide;
import org.cocos2d.actions.instant.CCShow;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.opengl.CCTexture2D;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.manNtel.database.DatabaseManager;
import com.manNtel.struct.GameStruct;

public class Number_6 extends GrowFlower {

	float incValue = 0;
	private boolean waterDropAniRunFlag;

	public Number_6(GameStruct user, Context context) {		
		super(user, context);

		DatabaseManager dbm = new DatabaseManager(mContext);
		dbm.open();

		Cursor cursor = dbm.fetchItem(0, mUser.key);
		incValue = CLEAR_ROTATE_VALUE / cursor.getInt(9);

		Log.i("[GameFourLayer]",""+incValue);

		cursor.close();
		dbm.close();

		if(mUser.part.equals("��")){
			waterBucket = loadImage(POS_WATERBUCKETSIX_LEFT,"game6/can_left");
			waterDrop = loadImage(POS_WATERDROP_LEFT,"game6/water_left1");
			progressTimer.setPosition(POS_WATERPROGRESSSIX_LEFT);
		}
		else{
			waterBucket = loadImage(POS_WATERBUCKETSIX_RIGHT,"game6/can_right");
			waterDrop = loadImage(POS_WATERDROP_RIGHT,"game6/water_right1");
			progressTimer.setPosition(POS_WATERPROGRESSSIX_RIGHT);
		}
		
		

		super.addChild(waterDrop,VIEW_OBJ);
		super.addChild(waterBucket,VIEW_OBJ);


		this.schedule("increaseWeight");
		this.schedule("increaseAngle");

		Log.i("[GameFourLayer]","ClearValue : " + mUser.clearValue);					
	}	

	public void decreaseAngle(float dt)
	{
		waterBucket.setRotation(inputRoll * incValue);
		if(inputRoll <=0)
		{
			waterFlag = false;
		}
	}

	public void decreaseWeight(float dt)
	{
		if(mUser.part.equals("��"))
		{
			if(inputBal - mUser.leftBal <=0)
			{
				sunFlag = false;
				if(!waterFlag)
				{
					this.schedule("increaseWeight");
					this.schedule("increaseAngle");

					this.unschedule("decreaseWeight");
					this.unschedule("decreaseAngle");
				}
			}
			return;
		}
		else
		{
			if(inputBal - mUser.rightBal <=0)
			{
				sunFlag = false;
				if(!waterFlag)
				{
					this.schedule("increaseWeight");
					this.schedule("increaseAngle");

					this.unschedule("decreaseWeight");
					this.unschedule("decreaseAngle");
				}
			}
			return;
		}
	}

	//�Ѹ��� ���� ȿ��
	public void increaseAngle(float dt)
	{	
		CCTexture2D texture;

		if(inputRoll * incValue / CLEAR_ROTATE_VALUE * 100 < 0)
			progressTimer.setPercentage(0);
		progressTimer.setPercentage(inputRoll * incValue / CLEAR_ROTATE_VALUE * 100);

		//��ġ ���� �κ�
		
		//�޹�
		if(mUser.part.equals("��")){
			if(inputRoll * incValue > CLEAR_ROTATE_VALUE){		
				waterFlag = false;

				waterDrop.stopAllActions();
				waterDrop.runAction(CCHide.action());
				waterAniRunFlag = false;
				waterDropAniRunFlag = false;

				if(inputRoll == 0){
					waterBucket.setRotation(0);
				}
				else if(inputRoll * incValue >= CLEAR_ROTATE_VALUE){
					waterBucket.setRotation(inputRoll*incValue);
				}

			}
			else if(inputRoll*incValue <= CLEAR_ROTATE_VALUE){

				waterBucket.setRotation(CLEAR_ROTATE_VALUE);

				waterFlag = true;


				if(!waterDropAniRunFlag){
					CCAnimation waterAnimation = CCAnimation.animation("waterDrop");
					if(mUser.part.equals("��")){
						waterAnimation.addFrame("game6/water_left1.png");
						waterAnimation.addFrame("game6/water_left2.png");
					}
					else{
						waterAnimation.addFrame("game6/water_right1.png");
						waterAnimation.addFrame("game6/water_right2.png");
					}

					CCAnimate waterani = CCAnimate.action(0.5f,waterAnimation,false);

					waterDrop.runAction(CCShow.action());
					waterDrop.runAction(CCRepeatForever.action(waterani));
					waterDropAniRunFlag = true;
				}
			}	
		}
		//������
		else{
			if(inputRoll * incValue < CLEAR_ROTATE_VALUE){		
				waterFlag = false;

				waterDrop.stopAllActions();
				waterDrop.runAction(CCHide.action());
				waterAniRunFlag = false;
				waterDropAniRunFlag = false;

				if(inputRoll == 0){
					waterBucket.setRotation(0);
				}
				else if(inputRoll * incValue <= CLEAR_ROTATE_VALUE){
					waterBucket.setRotation(inputRoll * incValue);
				}

			}
			else if(inputRoll*incValue >= CLEAR_ROTATE_VALUE){

				waterBucket.setRotation(CLEAR_ROTATE_VALUE);

				waterFlag = true;

				if(!waterDropAniRunFlag){
					CCAnimation waterAnimation = CCAnimation.animation("waterDrop");
					waterAnimation.addFrame("game6/water_right1.png");
					waterAnimation.addFrame("game6/water_right2.png");

					CCAnimate waterani = CCAnimate.action(0.5f,waterAnimation,false);

					waterDrop.runAction(CCShow.action());
					waterDrop.runAction(CCRepeatForever.action(waterani));
					waterDropAniRunFlag = true;
				}
			}	
		}
	}

	//���Կ� ���� ȿ��
	public void increaseWeight(float dt)
	{		
		//���԰� 0�̰ų� 0���� ������ : ���Ը� �Ǿ�� 
		if(inputBal <= 0){			
			activeSunSmile(false);
			guideLabel.setString("���Ը� �Ǿ����");
			guideLabel.setString("���԰� ����");			
		}

		//���԰� 0���� ũ���� ��ǥġ���� ������
		else if(inputBal >= 0 && inputBal < mUser.clearValue)
		{
			activeSunSmile(false);
			// 1: ���Ը� �ø��� �ִ� ���ΰ�.
			if(!sunFlag)
			{
				guideLabel.setString("���Ը� �� �� �Ǿ����");											
			}
			// 2: ������ �ø��� ���ư��� ���ΰ�.
			else
			{
				guideLabel.setString("���Ը� �� �ٿ�������");
			}
		}

		else if(inputBal >= mUser.clearValue)
		{
			sunFlag = true;
			activeSunSmile(true);

			guideLabel.setString("�޴��� ���� �����߾�� ���� ������ �����ּ���");			
		}
		weightLabel.setString((int)(inputBal/mUser.clearValue*100)+"%");		

	}
}
