package com.manNtel.gameLayer;

import org.cocos2d.types.CGPoint;

public interface ControlState 
{
	int 
		VIEW_Z = 0,
		VIEW_BACK = 1,
		VIEW_OBJ = 2,
		VIEW_EFFECT = 3;
		
	boolean TEST_STATE = true;
	
	CGPoint POS_BACKGROUND = CGPoint.make(512,384);
	
	CGPoint POS_START = CGPoint.make(820, 380);
	CGPoint POS_END = CGPoint.make(195,380);
	CGPoint POS_ARROW = CGPoint.make(512, 380);
	
	CGPoint POS_SCORE = CGPoint.make(175, 140);
	CGPoint POS_PLAYTIME = CGPoint.make(212,100);
	
	CGPoint POS_GUIDELABEL = CGPoint.make(512,660);
	CGPoint POS_WEIGHTLABEL = CGPoint.make(512,150);
	
	CGPoint POS_MENUMAIN = CGPoint.make(670,713);
	CGPoint POS_MENUSHUTDOWN = CGPoint.make(790,713);
	CGPoint POS_MENUBACK = CGPoint.make(910,713);
	CGPoint POS_MENUPAUSE = CGPoint.make(670,120);
	CGPoint POS_MENUSETLEVEL = CGPoint.make(790, 120);
	CGPoint POS_MENUENDGAME = CGPoint.make(910,120);
		
	CGPoint POS_SEED = CGPoint.make(512,300);
	
	CGPoint POS_SUN_LEFT = CGPoint.make(260, 560);
	CGPoint POS_SUN_RIGHT = CGPoint.make(740, 560);	
	
	CGPoint POS_WEIGHTLABELWATER_LEFT = CGPoint.make(390,550);
	CGPoint POS_WEIGHTLABELWATER_RIGHT = CGPoint.make(870,550);
		
	CGPoint POS_WATERBUCKET_LEFT = CGPoint.make(590, 540);
	CGPoint POS_WATERBUCKET_RIGHT = CGPoint.make(380, 540);
	
	CGPoint POS_WATERBUCKETSIX_LEFT = CGPoint.make(670, 540);
	CGPoint POS_WATERBUCKETSIX_RIGHT = CGPoint.make(350, 545);
	
	CGPoint POS_WATERDROP_LEFT = CGPoint.make(515,470);
	CGPoint POS_WATERDROP_RIGHT = CGPoint.make(505,470);
		
	CGPoint POS_WATERPROGRESS_LEFT = CGPoint.make(710, 540);
	CGPoint POS_WATERPROGRESS_RIGHT = CGPoint.make(310, 540);
	
	CGPoint POS_WATERPROGRESSSIX_LEFT = CGPoint.make(790, 540);
	CGPoint POS_WATERPROGRESSSIX_RIGHT = CGPoint.make(230, 540);
	
	CGPoint POS_GUIDELABELWATER = CGPoint.make(300,650);
	
	CGPoint POS_SUNSLIDE_LEFT = CGPoint.make(100,570);
	CGPoint POS_SUNSLIDE_RIGHT = CGPoint.make(924,570);
		
}
