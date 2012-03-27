//XML 사용여부 : X

package com.manNtel.record;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.android.manNtel_mid.R;
import com.manNtel.common.EvalRecordForm;
import com.manNtel.database.DatabaseManager;
import com.manNtel.struct.EvalStruct;

public class EvalRecord extends Activity 
{
	private final static int ROW = 11;
	private final static int COL = 10;
	
	private int pageNumber = 0;

	EvalRecordForm[][] cell = new EvalRecordForm[ROW][COL];
	private EvalStruct[] List;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		super.onCreate(savedInstanceState);                

		setContentView(R.layout.test);

		drawTable();
		drawData();
		drawBtn();		
	}

	private void drawTable(){

		TableLayout ListTL = (TableLayout)findViewById(R.id.listLayout);

		for(int i=0;i<ROW;i++)
		{
			TableRow tr = new TableRow(this);		

			tr.setGravity(Gravity.CENTER);			

			for(int j=0;j<COL;j++)
			{
				cell[i][j] = new EvalRecordForm(this);
				switch (j){
				case 0 :
					cell[i][j].setWidth(150);
					break;
				case 1 :
					cell[i][j].setWidth(80);
					break;
				case 2 :				
				case 3 : 
					cell[i][j].setWidth(120);
					break;
				case 4 : 
					cell[i][j].setWidth(100);
					break;
				case 5 :				
				case 6 :					
				case 7 :					
				case 8 : 
					cell[i][j].setWidth(70);
					break;
				case 9 : 
					cell[i][j].setWidth(120);
					break;					
				}
				tr.addView(cell[i][j]);
				cell[i][j].setGravity(Gravity.CENTER);
				cell[i][j].setBackgroundResource(R.drawable.cell_shape);
				cell[i][j].setTextColor(Color.rgb(8, 55, 98));
			}
			ListTL.addView(tr);
		}
	}
	private void drawBtn()
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
		
		prevParam.setMargins(0, 20, 0, 0);
		nextParam.setMargins(5, 20, 0, 0);
		closeParam.setMargins(513, 20, 0, 0);
		
		btnPrev.setText(R.string.btnPrevPage);
		btnNext.setText(R.string.btnNextPage);
		btnClose.setText(R.string.btnExit);
		
		btnPrev.setTextSize(25);
		btnNext.setTextSize(25);
		btnClose.setTextSize(25);
				
		btnPrev.setOnClickListener(mBtnClickListener);
		btnNext.setOnClickListener(mBtnClickListener);
		btnClose.setOnClickListener(mBtnClickListener);		
		
		LinearLayout btnLayout = (LinearLayout)findViewById(R.id.btnLayout);
		btnLayout.addView(btnPrev,prevParam);
		btnLayout.addView(btnNext,nextParam);
		btnLayout.addView(btnClose,closeParam);		
	}
	
	private void getList()
	{
		Cursor fromDB;
		DatabaseManager dbm = new DatabaseManager(this);
		
		dbm.open();
		Intent intent = getIntent();		
        fromDB = dbm.fetchItem(2,intent.getStringExtra("userKey"));
        List = new EvalStruct[fromDB.getCount()];            
		
		fromDB.moveToFirst();
		
		for(int i=0;i<fromDB.getCount();i++)
		{
			List[i] = new EvalStruct(getIntent().getStringExtra("userKey")); 
			
			List[i].mCount = fromDB.getInt(0);
			List[i].mKey = fromDB.getString(1);
			List[i].mDate = fromDB.getString(2);
			List[i].mLeftBal = fromDB.getFloat(3);
			List[i].mRightBal = fromDB.getFloat(4);
			List[i].mAngle = fromDB.getInt(5);
			List[i].mWeight = fromDB.getInt(6);
			List[i].mRolling = fromDB.getInt(7);
			List[i].mPitching = fromDB.getInt(8);
			List[i].mSliding = fromDB.getInt(9);
			
			fromDB.moveToNext();
		}
		fromDB.close();
		dbm.close();
	}
	
	private void drawData(){
		getList();
		
		int i=0;
		while(((i<=10)&&(i<List.length)))
		{			
			cell[i][0].setText(List[i+pageNumber].mDate);
			cell[i][1].setText(String.valueOf(List[i+pageNumber].mCount));
			cell[i][2].setText(String.valueOf(List[i+pageNumber].mLeftBal));
			cell[i][3].setText(String.valueOf(List[i+pageNumber].mRightBal));
			cell[i][4].setText(String.valueOf(List[i+pageNumber].mLeftBal + List[i+pageNumber].mRightBal));
			cell[i][5].setText(String.valueOf(List[i+pageNumber].mAngle));
			cell[i][6].setText(String.valueOf(List[i+pageNumber].mWeight));
			cell[i][7].setText(String.valueOf(List[i+pageNumber].mRolling));
			cell[i][8].setText(String.valueOf(List[i+pageNumber].mPitching));
			cell[i][9].setText(String.valueOf(List[i+pageNumber].mSliding));
			
			i++;
		}
	}
	
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
        		
        		break;
        	case 3:
        		finish();
        		break;
        	}
        }
	};
}
