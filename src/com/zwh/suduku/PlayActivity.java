package com.zwh.suduku;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class PlayActivity extends Activity {

	private ReseauView mReseauView;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);
		mReseauView = (ReseauView) findViewById(R.id.reseau_view);
		
		int[] data;
		String idStr;
		
		if (savedInstanceState == null) {
			char[] ch = getIntent().getCharArrayExtra("data");
			idStr = getIntent().getStringExtra("id");

			data = new int[ ch.length ];
			for( int k = 0; k < ch.length; k++ ){
				data[k] = Integer.valueOf(String.valueOf(ch[k])).intValue(); 
			}
			
			mReseauView.setSudokuData(data);
			mReseauView.setSudokuId(idStr);
			mReseauView.buildSudokuTag();
		}else{
			data = savedInstanceState.getIntArray("sudoku_data");
			int [] histroy = savedInstanceState.getIntArray("sudoku_history");
			idStr = savedInstanceState.getString("sudoku_id");
			boolean[] tag = savedInstanceState.getBooleanArray("sudoku_tag");

			mReseauView.setSudokuData(data);
			mReseauView.rebuildActionStack(histroy);
			mReseauView.setSudokuId(idStr);
			mReseauView.setSudokuTag(tag);
		}

		mReseauView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					mReseauView.checkTouchPostion(event.getX(), event.getY());
					break;

				default:
					break;
				}

				return false;
			}
		});
	}

	public void onClickUndo( View view ){
		mReseauView.undo();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		int resultCode = RESULT_CANCELED;
		if( 0 == mReseauView.getBlankSize() ){
			resultCode = RESULT_OK;
		}

		Intent intent = new Intent();
		setResult(resultCode, intent);
		finish();
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		// Save the user's current game state
		savedInstanceState.putIntArray("sudoku_data", mReseauView.getSudokuData());
		savedInstanceState.putIntArray("sudoku_history", mReseauView.saveActionStack());
		savedInstanceState.putString("sudoku_id", mReseauView.getSudokuId());
		savedInstanceState.putBooleanArray("sudoku_tag", mReseauView.getSudokuTag());
		super.onSaveInstanceState(savedInstanceState);
	}
}
