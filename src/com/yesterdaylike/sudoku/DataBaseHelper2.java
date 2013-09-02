package com.yesterdaylike.sudoku;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper2 extends SQLiteOpenHelper {

	static final String DATABASE_NAME = "sudoku.db";
	static final String SUDOKU_TABLE = "sudoku";
	static final String HISTORY_TABLE = "history";
	static final int VERSION = 1;
	private final Context mContext;

	private SQLiteDatabase database = null;

	public DataBaseHelper2(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
		mContext = context;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		try {
			db.execSQL("CREATE TABLE sudoku (_id integer NOT NULL PRIMARY KEY AUTOINCREMENT,degreeOfDifficulty integer DEFAULT 0,type integer DEFAULT 0,data varchar(81) NOT NULL)");
			db.execSQL("CREATE TABLE history (_id integer NOT NULL PRIMARY KEY AUTOINCREMENT,status integer NOT NULL DEFAULT 0,timeTakes varchar(8) DEFAULT 0,completionTime varchar(20),data varchar(81) NOT NULL)");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	/*
	060104 210000 000305 301000 000046 605030
	500001 601302 050010 000000 006100 010020
	600050 040016 001000 030200 006040 000003
	200001 006400 010020 600004 001200 050060
	042000 600010 000301 301000 020003 000420
	006000 000024 100000 000041 020400 010300*/

	public long insertSudoku(){
		database = getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("degreeOfDifficulty", "30");
		values.put("type", "A1");
		values.put("data", "060104210000000305301000000046605030");
		database.insert(SUDOKU_TABLE, null, values);

		values = new ContentValues();
		values.put("degreeOfDifficulty", "25");
		values.put("type", "A1");
		values.put("data", "500001601302050010000000006100010020");
		database.insert(SUDOKU_TABLE, null, values);

		values = new ContentValues();
		values.put("degreeOfDifficulty", "31");
		values.put("type", "A1");
		values.put("data", "600050040016001000030200006040000003");
		database.insert(SUDOKU_TABLE, null, values);

		values = new ContentValues();
		values.put("degreeOfDifficulty", "29");
		values.put("type", "A1");
		values.put("data", "200001006400010020600004001200050060");
		database.insert(SUDOKU_TABLE, null, values);

		values = new ContentValues();
		values.put("degreeOfDifficulty", "27");
		values.put("type", "A1");
		values.put("data", "042000600010000301301000020003000420");
		database.insert(SUDOKU_TABLE, null, values);

		values = new ContentValues();
		values.put("degreeOfDifficulty", "26");
		values.put("type", "A1");
		values.put("data", "006000000024100000000041020400010300");

		long result = database.insert(SUDOKU_TABLE, null, values);
		database.close();
		return result;
	}

	/*public Cursor querySudoku(){
		if( null == database ){
			database = getWritableDatabase();
		}
		return database.query(SUDOKU_TABLE, null, null, null, null, null, null, null);
	}*/

	public Sudoku[] querySudoku(String type){
		database = getWritableDatabase();
		//Cursor cursor = database.query(SUDOKU_TABLE, null, "type=?", new String[]{type}, null, null, "degreeOfDifficulty", null);
		Cursor cursor = database.query(SUDOKU_TABLE, null, null, null, null, null, "degreeOfDifficulty", null);

		int count = cursor.getCount();
		Sudoku[] sudokus = null;
		Sudoku sudoku;
		Log.i("zheng", " querySudoku "+count);
		
		if( count > 0 ){
			sudokus = new Sudoku[count];
			for( int i = 0; i < count; i++ ){
				cursor.moveToPosition(i);
				Log.i("zheng", " "+cursor.getInt(0)+" "+cursor.getInt(1)+" "+cursor.getInt(2)+" "+cursor.getString(3));
				sudoku = new Sudoku();
				sudoku._id = cursor.getInt(0);
				sudoku.degreeOfDifficulty = cursor.getInt(1);

				char[] ch = cursor.getString(3).toCharArray();
				sudoku.data = new int[ ch.length ];
				for( int k = 0; k < ch.length; k++ ){
					sudoku.data[k] = Integer.valueOf(String.valueOf(ch[k])).intValue(); 
				}
				sudokus[i] = sudoku;
			}
		}
		cursor.close();
		database.close();
		return sudokus;
	}

	public long insertHistory(){
		if( null == database ){
			database = getWritableDatabase();
		}
		ContentValues values = new ContentValues();
		values.put("status", "1");
		values.put("data", "0987654321");
		return database.insert(HISTORY_TABLE, null, values);
	}

	public Cursor queryHistory(){
		if( null == database ){
			database = getWritableDatabase();
		}
		return database.query(HISTORY_TABLE, null, null, null, null, null, null, null);
	}

	public void close(){
		if( null != database ){
			database.close();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion,
			int newVersion) {
		int version = oldVersion;

		if (version != VERSION) {
			Log.w("SQLiteOpenHelper", "Destroying all old data.");
			db.execSQL("DROP TABLE IF EXISTS " + SUDOKU_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + HISTORY_TABLE);
			onCreate(db);
		}
	}

	public class Sudoku{
		int _id;
		int degreeOfDifficulty;
		int[] data;
	};
}
