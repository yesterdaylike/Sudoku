package com.zwh.suduku;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.View;
import android.widget.SimpleCursorAdapter;

public class CursorAdapter extends SimpleCursorAdapter {

	public CursorAdapter(Context context, int layout, Cursor c, String[] from,
			int[] to, int flags) {
		super(context, layout, c, from, to, flags);
		// TODO Auto-generated constructor stub
	}


	@Override  
	public void bindView(View view, Context context, Cursor cursor) {
		String data = cursor.getString(cursor.getColumnIndex("data"));
		String id = cursor.getString(cursor.getColumnIndex("_id"));
		int status = cursor.getInt(cursor.getColumnIndex("status"));
		
		view.setTag(R.id.data_base_data, data);
		view.setTag(R.id.data_base_id, id);
		
		if(status == 1){
			view.setBackgroundColor(0x0f0000ff);
		}
		else{
			view.setBackgroundColor(Color.WHITE);
		}
		
		super.bindView(view, context, cursor);
	}
}
