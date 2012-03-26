package com.manNtel.struct;

import android.os.Parcel;
import android.os.Parcelable;

//게임시 필요한 정보

public class GameStruct implements Parcelable 
{
	public String 	key;
	public String 	name;
	public String 	playDate;
	public String 	part;
	
	public int 	count;
	public int 	gameNum;
	public int		level;
	public String 	playTime;
	public int		score;
	
	public float 	leftBal;
	public float	rightBal;
	public float 	clearValue;
	
	public static final Parcelable.Creator<GameStruct> CREATOR = new Parcelable.Creator<GameStruct>() 
	{
		@Override
		public GameStruct createFromParcel(Parcel in) 
		{
			return new GameStruct(in);
		}

		@Override
		public GameStruct[] newArray(int size) 
		{
			return new GameStruct[size];
		}
	};
	
	public GameStruct()
	{
		key = "NULL";
		name = "NULL";
		playDate = "NULL";
		part = "NULL";
				
		count = 0;
		gameNum = -1;
		level = -1;
		playTime = "NULL";
		score = 0;
		leftBal = -1;
		rightBal = -1;
		clearValue = -1;
	}
	
	public GameStruct(Parcel in)
	{
		readFromParcel(in);
	}
	
	public GameStruct(String _key, String _name)
	{
		key = _key;
		name = _name;
		playDate = "NULL";
		part = "NULL";
		
		count = 0;
		gameNum = -1;
		level = -1;
		playTime = "NULL";
		score = 0;
		leftBal = -1;
		rightBal = -1;
		clearValue = -1;
	}
	
	@Override
	public int describeContents() 
	{ return 0; }
	
	private void readFromParcel(Parcel in)
	{
		key = in.readString();
		name = in.readString();
		playDate = in.readString();
		part = in.readString();
		
		count = in.readInt();
		gameNum = in.readInt();
		level = in.readInt();
		playTime = in.readString();
		score = in.readInt();
		leftBal = in.readFloat();
		rightBal = in.readFloat();
		clearValue = in.readFloat();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) 
	{
		dest.writeString(key);
		dest.writeString(name);
		dest.writeString(playDate);
		dest.writeString(part);
		
		dest.writeInt(count);
		dest.writeInt(gameNum);
		dest.writeInt(level);
		dest.writeString(playTime);
		dest.writeInt(score);
		dest.writeFloat(leftBal);
		dest.writeFloat(rightBal);
		dest.writeFloat(clearValue);
	}
}
