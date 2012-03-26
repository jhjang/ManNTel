package com.manNtel.common;


import android.content.Context;
import android.view.Gravity;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.android.manNtel_mid.R;

public class EvalRecordForm extends TextView 
{
	public EvalRecordForm(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.setText(" ");
		this.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		this.setBackgroundResource(R.drawable.cell_shape);
		this.setGravity(Gravity.CENTER);
		this.setPadding(5, 5, 5, 5);		
		this.setTextSize(20);
		

	}
	

	
	
}