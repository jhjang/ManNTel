//XML 사용여부 : X

package com.manNtel.record;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.android.manNtel_mid.R;
import com.manNtel.common.GameRecordForm;
import com.manNtel.database.DatabaseManager;
import com.manNtel.struct.GameStruct;

public class GameRecord extends Activity 
{
	private static int ROW = 12;
	private static int COL = 6;

	GameRecordForm[][] cell = new GameRecordForm[ROW][COL];
	LinearLayout mainL = null;
	LinearLayout subL = null;
	TableLayout subT = null;

	GameStruct[] List;


	int pageNumber;


	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		super.onCreate(savedInstanceState);

		pageNumber = 0;

		init();

		drawTable();
		drawData();
		drawBtn();        

		mainL.setBackgroundResource(R.drawable.background);

		setContentView(mainL);
	}

	Button.OnClickListener mBtnClickListener = new Button.OnClickListener() 
	{

		@Override
		public void onClick(View v) 
		{
			switch(v.getId())
			{
			case 1:
				Log.i("[Touch]","Prev");
				if(pageNumber>0)
					pageNumber-=10;
				break;
			case 2:
				Log.i("[Touch]","Next");
				pageNumber+=10;

				break;
			case 3:
				finish();
				break;
			}
		}
	};

	public void drawBtn()
	{
		Button btnPrev = new Button(this);
		Button btnNext = new Button(this);
		Button btnClose = new Button(this);

		btnPrev.setId(1);
		btnNext.setId(2);
		btnClose.setId(3);


		LinearLayout.LayoutParams prevParam = new LinearLayout.LayoutParams(150,80);
		LinearLayout.LayoutParams nextParam = new LinearLayout.LayoutParams(150,80);
		LinearLayout.LayoutParams closeParam = new LinearLayout.LayoutParams(150,80);

		prevParam.setMargins(25, 40, 0, 0);
		nextParam.setMargins(5, 40, 0, 0);
		closeParam.setMargins(520, 40, 25, 0);

		btnPrev.setText(R.string.btnPrevPage);
		btnNext.setText(R.string.btnNextPage);
		btnClose.setText(R.string.btnExit);

		btnPrev.setTextSize(25);
		btnNext.setTextSize(25);
		btnClose.setTextSize(25);

		btnPrev.setOnClickListener(mBtnClickListener);
		btnNext.setOnClickListener(mBtnClickListener);
		btnClose.setOnClickListener(mBtnClickListener);		

		subL.addView(btnPrev,prevParam);
		subL.addView(btnNext,nextParam);
		subL.addView(btnClose,closeParam);

		mainL.addView(subL);
	}	
	public void drawData()
	{		
		getList();

		int i=0;
		while(((i<=10)&&(i<List.length)))
		{			
			cell[i+1][0].setText(List[i+pageNumber].playDate);
			cell[i+1][1].setText(String.valueOf(List[i+pageNumber].count));
			cell[i+1][2].setText(String.valueOf(List[i+pageNumber].gameNum));
			cell[i+1][3].setText(String.valueOf(List[i+pageNumber].level));
			cell[i+1][4].setText(List[i+pageNumber].playTime);
			cell[i+1][5].setText(String.valueOf(List[i+pageNumber].score));

			i++;
		}		
	}

	public void drawTable()
	{
		TableLayout.LayoutParams marginParam = new TableLayout.LayoutParams(100,LayoutParams.WRAP_CONTENT);
		TableLayout.LayoutParams dftParam = new TableLayout.LayoutParams(100,LayoutParams.WRAP_CONTENT);

		marginParam.setMargins(0, 30, 0, 0);

		for(int i=0;i<ROW;i++)
		{
			TableRow tr = new TableRow(this);		

			tr.setGravity(Gravity.CENTER);		

			for(int j=0;j<COL;j++)
			{
				tr.addView(cell[i][j]);
				if(i==0) 
				{
					cell[i][j].setBackgroundResource(R.drawable.titleshape);
				}
				else{
					cell[i][j].setBackgroundResource(R.drawable.cell_shape);
				}
				cell[i][j].setTextColor(Color.rgb(8, 55, 98));
				cell[i][j].setId(i);				
				cell[i][j].setTextSize(25);
			}		

			if(i==0)
				subT.addView(tr,marginParam);

			else			
				subT.addView(tr,dftParam);
		}
		mainL.addView(subT);
	}

	public void getList()
	{
		Cursor fromDB;
		DatabaseManager dbm = new DatabaseManager(this);

		dbm.open();
		Intent intent = getIntent();		
		fromDB = dbm.fetchItem(1,intent.getStringExtra("userKey"));
		List = new GameStruct[fromDB.getCount()];            

		Log.i("[getList]","start........");
		fromDB.moveToFirst();

		Log.i("[getList]","after move first");
		for(int i=0;i<fromDB.getCount();i++)
		{
			List[i] = new GameStruct(); 

			List[i].key = fromDB.getString(0);
			List[i].name = fromDB.getString(1);
			List[i].playDate = fromDB.getString(2);
			List[i].part = fromDB.getString(3);
			List[i].count = fromDB.getInt(4);
			List[i].gameNum = fromDB.getInt(5);
			List[i].level = fromDB.getInt(6);
			List[i].playTime = fromDB.getString(7);
			List[i].score = fromDB.getInt(8);

			fromDB.moveToNext();
		}
		fromDB.close();
		dbm.close();
	}

	public void init()
	{		 
		mainL = new LinearLayout(this);
		subL = new LinearLayout(this);
		subT = new TableLayout(this);

		mainL.setOrientation(LinearLayout.VERTICAL);
		subL.setOrientation(LinearLayout.HORIZONTAL);

		for(int i=0;i<ROW;i++)
		{
			for(int j=0;j<COL;j++)
			{
				cell[i][j] = new GameRecordForm(this);
			}
		}	

		//0번셀의 칼럼 명
		cell[0][0].setText(R.string.GameRecordDate);
		cell[0][1].setText(R.string.GameRecordTimes);
		cell[0][2].setText(R.string.GameRecordNum);
		cell[0][3].setText(R.string.GameRecordLevel);
		cell[0][4].setText(R.string.GameRecordPlayTime);
		cell[0][5].setText(R.string.GameRecordScore);

		//실제 DB에서 불러온 사용자 데이터는 cell[1][X]부터.
	}



	@Override
	public void onPause()
	{
		super.onPause();

	}

	@Override
	public void onStop()
	{
		super.onStop();

	}
}
