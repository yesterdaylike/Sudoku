package com.zwh.suduku;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;

public class DBCursorLoader extends CursorLoader{

	private Context mContext;
	private DataBaseHelper mDbHelper;
	private String mType;
	public DBCursorLoader(Context context, String type) {
		super(context);
		mContext = context;
		mDbHelper = new DataBaseHelper(mContext);
		mType = type;
	}

	@Override
	protected Cursor onLoadInBackground() {
		Cursor cursor = mDbHelper.query(mType);
		return cursor;
	}
}
