package com.manNtel.struct;

import android.os.Parcel;
import android.os.Parcelable;

public class UserInfoStruct implements Parcelable
{
	public String mKey;
	public String mID;
	public String mName;
	public String mBirth;
	
	public int mAge;
	public float mLeftBal;
	public float mRightBal;
	public float mAngle;
	public float mWeight;
	public int mMaxRoll;
	public int mMaxPitch;
	public int mMaxSlide;
	
	public String mSex;
	public String mPart;
	
	public String mRecent;
	
	public static final Parcelable.Creator<UserInfoStruct> CREATOR = new Parcelable.Creator<UserInfoStruct>() 
	{
		@Override
		public UserInfoStruct createFromParcel(Parcel in) 
		{
			return new UserInfoStruct(in);
		}

		@Override
		public UserInfoStruct[] newArray(int size) 
		{
			return new UserInfoStruct[size];
		}
	};
	
	public UserInfoStruct()
	{
		mKey = "NULL";
		mID = "NULL";
		mName = "NULL";
		mBirth = "NULL";
		
		mAge = -1;
		mLeftBal = -1;
		mRightBal = -1;
		mAngle = -1;
		mWeight = -1;
		mMaxRoll = -1;
		mMaxPitch = -1;
		mMaxSlide = -1;
		
		mSex = "NULL";
		mPart = "NULL";
		
		mRecent = "NULL";
	}
	
	public UserInfoStruct(Parcel in)
	{
		readFromParcel(in);
	}
	
	@Override
	public int describeContents() 
	{ return 0; }
	
//	public void printInfo()
//	{
//		Log.i("[KEY]",mKey);
//		Log.i("[NAME]",mName);
//		Log.i("[ID]",mID);
//		Log.i("[BIRTH]",mBirth);
//		Log.i("[AGE]",Integer.toString(mAge));
//		Log.i("[SEX]",mSex);
//		Log.i("[PART]",mPart);
//		Log.i("[WEIGHT]",Float.toString(mWeight));
//		Log.i("[ANGLE]",Float.toString(mAngle));
//		Log.i("[LEFT BAL]",Float.toString(mLeftBal));
//		Log.i("[RIGHT BAL]",Float.toString(mRightBal));
//		Log.i("[ROLLING]",Integer.toString(mMaxRoll));
//		Log.i("[PITCHING]",Integer.toString(mMaxPitch));
//		Log.i("[SLIDING]",Integer.toString(mMaxSlide));
//		Log.i("[RECENT]",mRecent);
//	}
	
	private void readFromParcel(Parcel in)
	{
		mKey = in.readString();
		mID = in.readString();
		mName = in.readString();
		mBirth = in.readString();
		
		mAge = in.readInt();
		mLeftBal = in.readFloat();
		mRightBal = in.readFloat();
		mAngle = in.readFloat();
		mWeight = in.readFloat();
		mMaxRoll = in.readInt();
		mMaxPitch = in.readInt();
		mMaxSlide = in.readInt();
		
		mSex = in.readString();
		mPart = in.readString();
		
		mRecent = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) 
	{
		dest.writeString(mKey);
        dest.writeString(mID);
        dest.writeString(mName);
        dest.writeString(mBirth);
        
        dest.writeInt(mAge);
        dest.writeFloat(mLeftBal);
        dest.writeFloat(mRightBal);
        dest.writeFloat(mAngle);
        dest.writeFloat(mWeight);
        dest.writeInt(mMaxRoll);
        dest.writeInt(mMaxPitch);
        dest.writeInt(mMaxSlide);
        
        dest.writeString(mSex);
        dest.writeString(mPart);
        
        dest.writeString(mRecent);
	}
	
}