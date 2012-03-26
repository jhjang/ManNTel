package com.manNtel.struct;

import android.os.Parcel;
import android.os.Parcelable;

public class EvalStruct implements Parcelable
{
	public String 	mKey;
	public String 	mDate;
	public String 	mPart;
	public int		mCount;
	public float	mLeftBal;
	public float	mRightBal;
	public int		mWeight;
	public int	mAngle;
	public int		mMaxWeight;
	public int	 	mRolling;
	public int		mPitching;
	public int		mSliding;

	public static final Parcelable.Creator<EvalStruct> CREATOR = new Parcelable.Creator<EvalStruct>() 
	{
		@Override
		public EvalStruct createFromParcel(Parcel in) 
		{
			return new EvalStruct(in);
		}

		@Override
		public EvalStruct[] newArray(int size) 
		{
			return new EvalStruct[size];
		}
	};

	EvalStruct()
	{
		mKey = "NULL";
		mDate = "NULL";
		mPart = "NULL";
		mCount = 0;
		mLeftBal = -1;
		mRightBal = -1;
		mWeight = -1;
		mAngle = -1;
		mMaxWeight = -1;
		mRolling = -1;
		mPitching = -1;
		mSliding = -1;
	}
	
	public EvalStruct(Parcel in)
	{
		readFromParcel(in);
	}
	
	public EvalStruct(String Key)
	{
		mKey = Key;
		mDate = "NULL";
		mPart = "NULL";
		mCount = 0;
		mLeftBal = -1;
		mRightBal = -1;
		mWeight = -1;
		mAngle = -1;
		mMaxWeight = -1;
		mRolling = -1;
		mPitching = -1;
		mSliding = -1;
	}

	@Override
	public int describeContents() 
	{ return 0; }	

	private void readFromParcel(Parcel in)
	{
		mKey = in.readString();
		mDate = in.readString();
		mPart = in.readString();
		mCount = in.readInt();
		mLeftBal = in.readFloat();
		mRightBal = in.readFloat();
		mWeight = in.readInt();
		mAngle = in.readInt();
		mMaxWeight = in.readInt();
		mRolling = in.readInt();
		mPitching = in.readInt();
		mSliding = in.readInt();
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) 
	{
		dest.writeString(mKey);
		dest.writeString(mDate);
		dest.writeString(mPart);
		dest.writeInt(mCount);
		dest.writeFloat(mLeftBal);
		dest.writeFloat(mRightBal);
		dest.writeInt(mWeight);
		dest.writeInt(mAngle);
		dest.writeInt(mMaxWeight);
		dest.writeInt(mRolling);
		dest.writeInt(mPitching);
		dest.writeInt(mSliding);
	}
}
