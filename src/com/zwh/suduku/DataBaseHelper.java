package com.zwh.suduku;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

public class DataBaseHelper {
	static final String DATABASE_NAME = "sudoku.db";//保存的数据库文件名
	static final String SUDOKU_TABLE = "sudoku";
	static final String HISTORY_TABLE = "history";
	static final int VERSION = 1;
	private final Context mContext;
	private final int BUFFER_SIZE = 400000;
	public static final String PACKAGE_NAME = "com.zwh.suduku";
	private String dbFile;
	private SQLiteDatabase database;

	public DataBaseHelper(Context context) {
		mContext = context;
		dbFile = getDbFile();
		CreateDatabase();
	}

	public boolean CreateDatabase() {
		try {
			if (!(new File(dbFile).exists())) {
				//判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
				InputStream is = mContext.getResources().openRawResource( R.raw.sudoku ); //欲导入的数据库
				FileOutputStream fos = new FileOutputStream(dbFile);
				byte[] buffer = new byte[BUFFER_SIZE];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
			}
			return true;
		} catch (FileNotFoundException e) {
			Log.e("Database", "File not found");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("Database", "IO exception");
			e.printStackTrace();
		}
		return false;
	}

	private SQLiteDatabase openDatabase(){
		return SQLiteDatabase.openOrCreateDatabase( dbFile, null );
	}

	private String getDbFile(){
		StringBuilder stringBuilder = new StringBuilder("/data");
		stringBuilder.append(Environment.getDataDirectory().getAbsolutePath());
		stringBuilder.append("/");
		stringBuilder.append(PACKAGE_NAME);
		stringBuilder.append("/");
		stringBuilder.append(DataBaseHelper.DATABASE_NAME);

		return stringBuilder.toString();
	}

	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		try {
			db.execSQL("CREATE TABLE sudoku (_id integer NOT NULL PRIMARY KEY AUTOINCREMENT,status integer NOT NULL DEFAULT 0,degreeOfDifficulty integer DEFAULT 0,type integer DEFAULT 0,data varchar(81) NOT NULL)");
			db.execSQL("CREATE TABLE history (_id integer NOT NULL PRIMARY KEY AUTOINCREMENT,status integer NOT NULL DEFAULT 0,timeTakes varchar(8) DEFAULT 0,completionTime varchar(20),data varchar(81) NOT NULL)");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public long insertSudoku(){
		database = openDatabase();

		ContentValues values = new ContentValues();
		values.put("degreeOfDifficulty", "30");
		values.put("type", "A1");
		values.put("data", "060104210000000305301000000046605030");
		database.insert(SUDOKU_TABLE, null, values);

		long result = database.insert(SUDOKU_TABLE, null, values);
		database.close();
		return result;
	}
	
	public void complete(String id){
		database = openDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("status", 1);
		Log.i("complete", id);
		database.update(SUDOKU_TABLE, contentValues, "_id=?", new String[]{id});
	}

	public Cursor query(String type){
		database = openDatabase();
		Cursor cursor = database.query(SUDOKU_TABLE, null, "type=?", new String[]{type}, null, null, "degreeOfDifficulty", null);
		return cursor;
	}

	public Sudoku[] querySudoku(String type){
		database = openDatabase();
		Cursor cursor = database.query(SUDOKU_TABLE, null, "type=?", new String[]{type}, null, null, "degreeOfDifficulty", null);
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
		database = openDatabase();
		ContentValues values = new ContentValues();
		values.put("status", "1");
		values.put("data", "0987654321");
		long result = database.insert(HISTORY_TABLE, null, values);
		database.close();
		return result;
	}

	/*public Cursor queryHistory(){
		return database.query(HISTORY_TABLE, null, null, null, null, null, null, null);
	}*/

	public void closeDatabase(){
		if( null != database ){
			database.close();
		}
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion,
			int newVersion) {
		int version = oldVersion;

		if (version != VERSION) {
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
