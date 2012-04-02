package com.manNtel.database;

//DB Manager용 상수 모음
import android.provider.BaseColumns;

public final class DBConstants implements BaseColumns
{
	public static final class DBCon implements BaseColumns
	{
		//Path & Name
		public static final String 	DBPATH = "/data/data/com.android.manNtel_mid/databases/";
		
		public static final String	DB_NAME = "manNtel.db";
		
		//Version
		public static final int 	DB_VERSION = 1;
		
		//Tables		
		public static final String 	USERINFO = "USERINFO";
		
		public static final String 	EVALRECORD = "EVALRECORD";
		public static final String 	GAMERECORD = "GAMERECORD";
		//USERINFO columns
		public static final String KEY = "key";
		
		public static final String ID = "id";
		public static final String NAME = "name";
		public static final String AGE = "age";
		public static final String BIRTH = "birth";
		public static final String SEX = "sex";
		public static final String PART = "part";
		public static final String LEFTBAL = "leftbal";
		public static final String RIGHTBAL = "rightbal";
		public static final String ANGLE = "angle";
		public static final String WEIGHT = "weight";
		public static final String ROLL = "roll";
		public static final String PITCH = "pitch";
		public static final String SLIDE = "slide";
		public static final String RECENTCON = "recentcon";
		
		//VALRECORD columns
		public static final String TIMES = "times";
		
		//GAMERECORD columns
		public static final String GAMENUM = "gamenum";
		
		public static final String PLAYDATE = "playdate";
		public static final String LEVEL = "level";
		public static final String PLAYTIME = "playtime";
		public static final String SCORE = "score";
		private DBCon() {}
		
	}
	
	private DBConstants(){}
}