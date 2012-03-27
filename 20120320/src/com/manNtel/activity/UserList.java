//환자 리스트 액티비티 
//XML 사용여부 : X

/* 과업
 * 텍스트뷰 리스너 달것.
 * 데이터 베이스 적재 저장.
 * 개인정보 조회 뷰로 이동.
 * 선택 완료 뷰로 이동. 
 */
package com.manNtel.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.manNtel_mid.R;
import com.manNtel.common.UserListForm;
import com.manNtel.database.DatabaseManager;
import com.manNtel.service.ProcessManager;
import com.manNtel.struct.UserInfoStruct;

public class UserList extends Activity 
{
	private static int ROW = 11;
	private static int COL = 4;

	UserListForm[][] cell = new UserListForm[ROW][COL];
	LinearLayout mainL = null;
	LinearLayout subL = null;
	TableLayout subT = null;

	UserInfoStruct[] userList;
	Cursor fromDB;
	DatabaseManager dbm;
	int selIndex;
	int pageNumber;
	boolean selFlag;

	TextView.OnClickListener mTextViewClickListener = new TextView.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			selIndex = pageNumber + v.getId();
			selIndex--;
			selFlag = true;

			changeColor(v.getId());			
		}
	};	

	Button.OnClickListener mBtnClickListener = new Button.OnClickListener() 
	{

		@Override
		public void onClick(View v) 
		{
			switch(v.getId())
			{
			case 1:
				if(pageNumber>0)
					pageNumber-=10;
				break;
			case 2:
				pageNumber+=10;
				if(userList.length>=10)
				{
					//
				}
				break;
			case 3:
				if(selFlag && selIndex<userList.length)
				{
					Intent goUserInfo = new Intent(UserList.this,UserInfo.class);
					goUserInfo.putExtra("userInfo", userList[selIndex]);
					startActivity(goUserInfo);
				}        			
				else
				{
					new AlertDialog.Builder(UserList.this)
					.setTitle(R.string.notification)
					.setMessage(R.string.msgSelectUser)
					.setIcon(R.drawable.icon)
					.setNeutralButton(R.string.dlgClose,new DialogInterface.OnClickListener(){ @Override
					public void onClick(DialogInterface dialog, int whichButton) {}}).show();
				}
				break;
			case 4:
				if(selFlag && selIndex<userList.length)
				{
					Intent goLogin = new Intent(UserList.this,Login.class);					
					goLogin.putExtra("userKey", userList[selIndex].mKey);
					goLogin.putExtra("userName", userList[selIndex].mName);
					goLogin.putExtra("userPart", userList[selIndex].mPart);
					startActivity(goLogin);
				}
				else
				{
					new AlertDialog.Builder(UserList.this)
					.setTitle(R.string.notification)
					.setMessage(R.string.msgSelectUser)
					.setIcon(R.drawable.icon)
					.setNeutralButton(R.string.dlgClose,new DialogInterface.OnClickListener(){ @Override
					public void onClick(DialogInterface dialog, int whichButton) {}}).show();
				}
				break;
			case 5:
				startActivity(new Intent(UserList.this,Main.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
				break;
			case 6:
				if(selFlag && selIndex<userList.length)
				{
					dbm.deleteItem(userList[selIndex].mKey);        		
					Intent intent = getIntent();
					finish();
					startActivity(intent);
				}				
				break;
			}
		}
	};
	private void changeColor(int index)
	{
		for(int i=1;i<ROW;i++)
		{
			if(i==index)
			{
				for(int j=0;j<COL;j++)
				{
					cell[i][j].setBackgroundResource(R.drawable.selectedcell);
				}
			}				
			else
			{
				for(int j=0;j<COL;j++)
				{					
					cell[i][j].setBackgroundResource(R.drawable.cell_shape);
				}
			}
		}
	}	
	public void drawBtn()
	{
		Button btnPrev = new Button(this);
		Button btnNext = new Button(this);
		Button btnInfo = new Button(this);
		Button btnOK = new Button(this);
		Button btnEnd = new Button(this);
		Button btnDelete = new Button(this);

		btnPrev.setId(1);
		btnNext.setId(2);
		btnInfo.setId(3);
		btnOK.setId(4);
		btnEnd.setId(5);
		btnDelete.setId(6);

		btnPrev.setTextSize(25);
		btnNext.setTextSize(25);
		btnInfo.setTextSize(25);
		btnOK.setTextSize(25);
		btnEnd.setTextSize(25);
		btnDelete.setTextSize(25);

		btnPrev.setTextColor(Color.rgb(8, 37, 62));
		btnNext.setTextColor(Color.rgb(8, 37, 62));
		btnInfo.setTextColor(Color.rgb(8, 37, 62));
		btnOK.setTextColor(Color.rgb(8, 37, 62));
		btnEnd.setTextColor(Color.rgb(8, 37, 62));
		btnDelete.setTextColor(Color.rgb(8, 37, 62));

		LinearLayout.LayoutParams prevParam = new LinearLayout.LayoutParams(100,80);
		prevParam.setMargins(28, 60, 0, 0);		

		LinearLayout.LayoutParams nextParam = new LinearLayout.LayoutParams(100,80);
		nextParam.setMargins(5, 60, 0, 0);

		LinearLayout.LayoutParams infoParam = new LinearLayout.LayoutParams(211,80);
		infoParam.setMargins(37, 60, 0, 0);

		LinearLayout.LayoutParams delParam = new LinearLayout.LayoutParams(211,80);
		delParam.setMargins(5, 60, 0, 0);

		LinearLayout.LayoutParams okParam = new LinearLayout.LayoutParams(129,80);
		okParam.setMargins(37, 60, 0, 0);

		LinearLayout.LayoutParams endParam = new LinearLayout.LayoutParams(129,80);
		endParam.setMargins(5, 60, 0, 0);

		btnPrev.setText(R.string.btnPrev);
		btnNext.setText(R.string.btnNext);
		btnInfo.setText(R.string.btnGetUserInfo);
		btnOK.setText(R.string.btnSelectOK);
		btnEnd.setText(R.string.btnExit);
		btnDelete.setText(R.string.btnDelete);

		btnPrev.setOnClickListener(mBtnClickListener);
		btnNext.setOnClickListener(mBtnClickListener);
		btnInfo.setOnClickListener(mBtnClickListener);
		btnOK.setOnClickListener(mBtnClickListener);
		btnEnd.setOnClickListener(mBtnClickListener);
		btnDelete.setOnClickListener(mBtnClickListener);	

		subL.addView(btnPrev,prevParam);
		subL.addView(btnNext,nextParam);
		subL.addView(btnInfo,infoParam);
		subL.addView(btnDelete,delParam);
		subL.addView(btnOK,okParam);
		subL.addView(btnEnd,endParam);		


		mainL.addView(subL);
	}	
	public void drawData()
	{		
		getUserList();

		int i=0;
		while(((i<=10)&&(i<userList.length)))
		{

			cell[i+1][0].setText(userList[i+pageNumber].mName);
			cell[i+1][1].setText(userList[i+pageNumber].mID);
			cell[i+1][2].setText(userList[i+pageNumber].mBirth);
			cell[i+1][3].setText(userList[i+pageNumber].mRecent);

			i++;
		}		
	}
	public void drawTable()
	{
		TableLayout.LayoutParams marginParam = new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		TableLayout.LayoutParams dftParam = new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);

		marginParam.setMargins(0, 30, 0, 0);

		for(int i=0;i<ROW;i++)
		{
			TableRow tr = new TableRow(this);		

			tr.setGravity(Gravity.CENTER);		

			for(int j=0;j<COL;j++)
			{
				tr.addView(cell[i][j]);
				cell[i][j].setTextSize(25);
				cell[i][j].setId(i);
				if(i==0){
					cell[i][j].setBackgroundResource(R.drawable.titleshape);
				}
				else {
					cell[i][j].setBackgroundResource(R.drawable.cell_shape);
				}
				cell[i][j].setTextColor(Color.rgb(8, 37, 62));
				cell[i][j].setOnClickListener(mTextViewClickListener);
			}		

			if(i==0)
				subT.addView(tr,marginParam);

			else			
				subT.addView(tr,dftParam);
		}
		mainL.addView(subT);

		drawData();
	}

	public void getUserList()
	{
		fromDB = dbm.fetchAllItems(0);
		userList = new UserInfoStruct[fromDB.getCount()];            

		fromDB.moveToFirst();

		for(int i=0;i<fromDB.getCount();i++)
		{
			userList[i] = new UserInfoStruct(); 

			userList[i].mKey = fromDB.getString(0);
			userList[i].mID = fromDB.getString(1);
			userList[i].mName = fromDB.getString(2);
			userList[i].mBirth = fromDB.getString(3);
			userList[i].mAge = fromDB.getInt(4);
			userList[i].mSex = fromDB.getString(5);
			userList[i].mPart = fromDB.getString(6);
			userList[i].mLeftBal = fromDB.getInt(7);
			userList[i].mRightBal = fromDB.getInt(8);
			userList[i].mAngle = fromDB.getInt(9);
			userList[i].mWeight = fromDB.getInt(10);
			userList[i].mMaxRoll = fromDB.getInt(11);
			userList[i].mMaxPitch = fromDB.getInt(12);
			userList[i].mMaxSlide = fromDB.getInt(13);
			userList[i].mRecent = fromDB.getString(14);

			fromDB.moveToNext();
		}
		fromDB.close();
	}

	public void init()
	{		 
		mainL = new LinearLayout(this);
		subL = new LinearLayout(this);
		subL.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.FILL_PARENT));
		subL.setGravity(Gravity.BOTTOM);
		subT = new TableLayout(this);

		mainL.setOrientation(LinearLayout.VERTICAL);
		subL.setOrientation(LinearLayout.HORIZONTAL);

		for(int i=0;i<ROW;i++)
		{
			cell[i][0] = new UserListForm(this);
			cell[i][1] = new UserListForm(this);
			cell[i][2] = new UserListForm(this);
			cell[i][3] = new UserListForm(this);
		}	

		//0번셀의 칼럼 명
		cell[0][0].setText(R.string.list_userName);
		cell[0][1].setText(R.string.list_userID);
		cell[0][2].setText(R.string.list_userBirth);
		cell[0][3].setText(R.string.list_userRecentCon);

		//실제 DB에서 불러온 사용자 데이터는 cell[1][X]부터.
	}

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);   
		super.onCreate(savedInstanceState);

		dbm = new DatabaseManager(this);
		dbm.open();

		selIndex = 0;
		pageNumber = 0;
		selFlag = false;       

		init();

		drawTable();
		drawBtn();
		

		setContentView(mainL);
		mainL.setBackgroundResource(R.drawable.pattern_bg);
		ProcessManager.getInstance().addActivity(this);
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		ProcessManager.getInstance().deleteActivity(this);
	}

	@Override
	public void onPause()
	{
		super.onPause();
		dbm.close();
	}

	@Override
	public void onStop()
	{
		super.onStop();
		dbm.close();
	}
}
