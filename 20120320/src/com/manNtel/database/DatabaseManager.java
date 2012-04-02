package com.manNtel.database;

//

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.manNtel.database.DBConstants.DBCon;
import com.manNtel.struct.EvalStruct;
import com.manNtel.struct.GameStruct;
import com.manNtel.struct.UserInfoStruct;

public class DatabaseManager
{
	public class DatabaseHelper extends SQLiteOpenHelper
	{		
		public DatabaseHelper(Context context) 
		{		
			super(context, DBCon.DB_NAME, null, DBCon.DB_VERSION);			
			Log.i("[MSG]", "DB Create");		 		
		}

		@Override
		public void onCreate(SQLiteDatabase db)
		{				
			db.execSQL("CREATE TABLE " + DBCon.USERINFO + " (" + DBCon.KEY + 		" VARCHAR(15) PRIMARY KEY, " 
					+ DBCon.ID + 		" text, " 
					+ DBCon.NAME + 		" text, "
					+ DBCon.BIRTH + 	" text, "
					+ DBCon.AGE + 		" Integer, "
					+ DBCon.SEX + 		" text, "
					+ DBCon.PART + 		" text, "
					+ DBCon.LEFTBAL +	" Integer, "
					+ DBCon.RIGHTBAL+	" Integer, "
					+ DBCon.ANGLE + 	" Integer, "
					+ DBCon.WEIGHT + 	" Integer, "
					+ DBCon.ROLL + 		" Integer, "
					+ DBCon.PITCH + 	" Integer, "
					+ DBCon.SLIDE + 	" Integer, "
					+ DBCon.RECENTCON + 	" text);");
			Log.i("[DatabaseManager]", "User List Table Created");
			
			db.execSQL("CREATE TABLE " + DBCon.GAMERECORD + " (idx Integer PRIMARY KEY AUTOINCREMENT, " 
					+ DBCon.KEY + 			" text, "
					+ DBCon.NAME + 			" text, "
					+ DBCon.PLAYDATE + 		" text, "
					+ DBCon.PART + 			" text, "
					+ DBCon.TIMES + 		" Integer, "
					+ DBCon.GAMENUM + 		" Integer, "
					+ DBCon.LEVEL + 		" Integer, "
					+ DBCon.PLAYTIME + 		" text, "
					+ DBCon.SCORE + 		" Integer);");			
			Log.i("[DatabaseManager]", "Game Record Table Created");
			
			db.execSQL("CREATE TABLE " + DBCon.EVALRECORD + " (idx Integer PRIMARY KEY AUTOINCREMENT, " 
					+ DBCon.KEY + 			" text, "					
					+ DBCon.PLAYDATE + 		" text, "
					+ DBCon.LEFTBAL + 		" float, "
					+ DBCon.RIGHTBAL + 		" float, "		
					+ DBCon.ANGLE +			" Integer, "
					+ DBCon.WEIGHT + 		" Integer, "
					+ DBCon.ROLL + 			" Integer, "
					+ DBCon.PITCH +	 		" Integer, "
					+ DBCon.SLIDE +		 	" Integer);");
			Log.i("[DatabaseManager]", "Eval Record Table Created");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			db.execSQL("DROP TABLE IF EXISTS " + DBCon.USERINFO);
			onCreate(db);
		}	

	}
	private DatabaseHelper mDbHelper;

	private SQLiteDatabase mDb;

	private final Context mContext;

	public DatabaseManager(Context ctx)
	{
		mContext = ctx;
	}

	public long addItem(EvalStruct newUser) 
	{
		ContentValues args = new ContentValues();
				
		args.put(DBCon.KEY, newUser.mKey);
		args.put(DBCon.PLAYDATE, newUser.mDate);
		args.put(DBCon.LEFTBAL, newUser.mLeftBal);
		args.put(DBCon.RIGHTBAL, newUser.mRightBal);
		args.put(DBCon.ANGLE, newUser.mAngle);
		args.put(DBCon.WEIGHT, newUser.mWeight);
		args.put(DBCon.ROLL, newUser.mRolling);
		args.put(DBCon.PITCH, newUser.mPitching);
		args.put(DBCon.SLIDE, newUser.mSliding);				

		return mDb.insert(DBCon.EVALRECORD, null, args);		
	}

	public long addItem(GameStruct userRec) 
	{
		ContentValues args = new ContentValues();

		args.put(DBCon.KEY, userRec.key);
		args.put(DBCon.NAME, userRec.name);
		args.put(DBCon.PLAYDATE, userRec.playDate);
		args.put(DBCon.PART, userRec.part);
		args.put(DBCon.TIMES, userRec.count);
		args.put(DBCon.GAMENUM, userRec.gameNum);
		args.put(DBCon.PLAYTIME, userRec.playTime);
		args.put(DBCon.LEVEL, userRec.level);
		args.put(DBCon.SCORE, userRec.score);

		return mDb.insert(DBCon.GAMERECORD, null, args);		
	}
	public long addItem(UserInfoStruct newUser) 
	{
		ContentValues args = new ContentValues();

		args.put(DBCon.KEY, newUser.mKey);
		args.put(DBCon.ID, newUser.mID);		
		args.put(DBCon.NAME, newUser.mName);
		args.put(DBCon.BIRTH, newUser.mBirth);
		args.put(DBCon.AGE, newUser.mAge);
		args.put(DBCon.SEX, newUser.mSex);
		args.put(DBCon.PART, newUser.mPart);
		args.put(DBCon.LEFTBAL, newUser.mLeftBal);
		args.put(DBCon.RIGHTBAL, newUser.mRightBal);
		args.put(DBCon.ANGLE, newUser.mAngle);
		args.put(DBCon.WEIGHT, newUser.mWeight);
		args.put(DBCon.ROLL, newUser.mMaxRoll);
		args.put(DBCon.PITCH, newUser.mMaxPitch);
		args.put(DBCon.SLIDE, newUser.mMaxSlide);        
		args.put(DBCon.RECENTCON, newUser.mRecent);        

		return mDb.insert(DBCon.USERINFO, null, args);		
	}

	public void close()
	{
		mDbHelper.close();
	}
	
	public void deleteItem(String rowID)
	{
		Log.i("[DELETE]","value of " + rowID);		
		String sql = "DELETE FROM " + DBCon.USERINFO + " WHERE " + DBCon.KEY + " = " +  "\"" + rowID + "\"" + ";";		
		
		mDb.execSQL(sql);		
	}

	public Cursor fetchAllItems(int flag)
	{
		//0 : 사용자 목록 1:게임이력 2:평가이력
		
		if(flag==0)
		{
			return mDb.query(DBCon.USERINFO, new String[] {DBCon.KEY, DBCon.ID, DBCon.NAME, DBCon.BIRTH, DBCon.AGE, DBCon.SEX,DBCon.PART, DBCon.LEFTBAL, DBCon.RIGHTBAL, 
				DBCon.ANGLE, DBCon.WEIGHT, DBCon.ROLL, DBCon.PITCH, DBCon.SLIDE, DBCon.RECENTCON}, 
				null,null,null,null,null);
		}
		else if(flag==1)
		{		
			return mDb.query(DBCon.GAMERECORD, new String[] {DBCon.KEY, DBCon.NAME, DBCon.PLAYDATE, DBCon.PART, DBCon.TIMES, DBCon.GAMENUM, DBCon.LEVEL, DBCon.PLAYTIME, DBCon.SCORE}, null, null, null, null, null);
		}
		else if(flag==2){
			return mDb.query(DBCon.EVALRECORD, new String[] {DBCon.KEY,DBCon.PLAYDATE, DBCon.LEFTBAL, DBCon.RIGHTBAL, DBCon.ANGLE, DBCon.WEIGHT, DBCon.ROLL, DBCon.PITCH, DBCon.SLIDE}, null, null, null, null, null);
		}
			return null;		
	}

	public Cursor fetchItem(int flag, String rowId) throws SQLException
	{
		Log.i("[DBM]","Key : " + rowId);
		Cursor mCursor = null;
		if(flag==0)
		{
			mCursor =		
				mDb.query(true, DBCon.USERINFO, new String[] {DBCon.KEY, DBCon.ID, DBCon.NAME, DBCon.BIRTH, DBCon.AGE, DBCon.SEX,DBCon.PART, DBCon.LEFTBAL, DBCon.RIGHTBAL,
						DBCon.ANGLE, DBCon.WEIGHT, DBCon.ROLL, DBCon.PITCH, DBCon.SLIDE, DBCon.RECENTCON},				
						DBCon.KEY + "=" + "\"" + rowId + "\"", null, null, null, null,null);			
		}
		
		
		else if(flag==1)
		{
			mCursor = 
				mDb.query(true, DBCon.GAMERECORD, new String[] {DBCon.KEY, DBCon.NAME, DBCon.PLAYDATE, DBCon.PART, DBCon.TIMES, DBCon.GAMENUM, DBCon.LEVEL, DBCon.PLAYTIME, DBCon.SCORE}, 
						DBCon.KEY + "=" + "\"" + rowId + "\"", null, null, null, DBCon.TIMES + " desc", null);
		}
		
		else if(flag==2)
		{
			mCursor = 
				mDb.query(true, DBCon.EVALRECORD, new String[] {"idx", DBCon.KEY, DBCon.PLAYDATE, DBCon.LEFTBAL, DBCon.RIGHTBAL, DBCon.ANGLE, DBCon.WEIGHT, DBCon.ROLL, DBCon.PITCH, DBCon.SLIDE}, 
						DBCon.KEY + "=" + "\"" + rowId + "\"", null, null, null, "idx" + " desc", null);
		}
		
		if (mCursor != null) 	
		{
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public DatabaseManager open() throws SQLException
	{
		mDbHelper = new DatabaseHelper(mContext);
		mDb = mDbHelper.getWritableDatabase();				

		return this;
	}

	public boolean updateItem(String rowId, String data) 
	{
		ContentValues args = new ContentValues();
     
		args.put(DBCon.RECENTCON, data);		

		return mDb.update(DBCon.USERINFO, args, DBCon.KEY + "=" + "\"" + rowId + "\"", null) > 0;
	}	
	
	public void exportToCsv(){
		Log.i("[DatabaseManager]","export start.....");
		
		
		
		Log.i("[DatabaseManager]","export end.....");
	}
}
