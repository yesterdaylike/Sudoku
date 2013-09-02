package com.yesterdaylike.sudoku;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yesterdaylike.sudoku.DataBaseHelper.Sudoku;
import com.yesterdaylike.sudoku.PlayPagerAdapter.OnInstantiateItemListener;

/**
 * @author zheng
 */
public class PlayActivity4 extends Activity
implements OnPageChangeListener, OnTouchListener, OnInstantiateItemListener{
	private static final int squareheight = 2;
	private static final int squarewidth = 2;
	private static final int squarelength = 4;
	private static final int count = 16;

	private MyViewPager mViewPager;
	private PlayPagerAdapter mAdapter;
	private View titleBarLayout;
	private View downBarLayout;
	private Panel helpPanel;
	private RatingBar ratingBar;
	private TextView sudokuId;
	private View mClickView;
	
	private TextView timerTextView;
	private TextView closingTimeTextView;
	
	private int mClickNumber;
	private int width;
	private int height;

	private boolean mPlaying = false;
	private Sudoku[] mSudokus;
	private DataBaseHelper mDbHelper;
	private List<View> mSudokuViewList;
	private List<TextView[]> mSquareViewList;
	private int []mSquareViewIds;

	private TextView []mSquaresViews;
	private int mCurrentIndex = 0;
	private int[] mData;

	private Dialog mDialog;
	private int []mDialogViewIds;
	private TextView []mDialogViews;
	
	private int recLen = 0;
 
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        public void run() {
            recLen++;
            //txtView.setText("" + recLen);
            timerTextView.setText("" + recLen);
            handler.postDelayed(this, 1000);
        }
    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);

		mDbHelper = new DataBaseHelper(this);
		mSudokus = mDbHelper.querySudoku("40");
		/*if( mSudokus==null || mSudokus.length == 0 ){
			mDbHelper.insertSudoku();
			mSudokus = mDbHelper.querySudoku("A1");
		}*/

		titleBarLayout = findViewById(R.id.title_bar_layout);
		downBarLayout = findViewById(R.id.down_bar_layout);
		helpPanel = (Panel)findViewById(R.id.help_panel);
		ratingBar = (RatingBar)findViewById(R.id.difficulty_ratingbar);
		sudokuId = (TextView)findViewById(R.id.sudoku_id_textview);
		
		timerTextView = (TextView)findViewById(R.id.timer_textview);
		closingTimeTextView = (TextView)findViewById(R.id.closing_time_textview);

		mSudokuViewList = new ArrayList<View>();
		mSquareViewList = new ArrayList<TextView[]>();

		LayoutInflater layoutInflater = getLayoutInflater();

		View view1 = layoutInflater.inflate(R.layout.sudoku_item4, null);
		View view2 = layoutInflater.inflate(R.layout.sudoku_item4, null);
		View view3 = layoutInflater.inflate(R.layout.sudoku_item4, null);
		View view4 = layoutInflater.inflate(R.layout.sudoku_item4, null);

		mSquareViewIds = new int[count];
		mSquareViewIds[0] = R.id.Button0;
		mSquareViewIds[1] = R.id.Button1;
		mSquareViewIds[2] = R.id.Button2;
		mSquareViewIds[3] = R.id.Button3;
		mSquareViewIds[4] = R.id.Button4;
		mSquareViewIds[5] = R.id.Button5;
		mSquareViewIds[6] = R.id.Button6;
		mSquareViewIds[7] = R.id.Button7;
		mSquareViewIds[8] = R.id.Button8;
		mSquareViewIds[9] = R.id.Button9;
		mSquareViewIds[10] = R.id.Button10;
		mSquareViewIds[11] = R.id.Button11;
		mSquareViewIds[12] = R.id.Button12;
		mSquareViewIds[13] = R.id.Button13;
		mSquareViewIds[14] = R.id.Button14;
		mSquareViewIds[15] = R.id.Button15;

		mSudokuViewList.add( view1 );
		mSudokuViewList.add( view2 );
		mSudokuViewList.add( view3 );
		mSudokuViewList.add( view4 );

		for (View view : mSudokuViewList) {
			mSquaresViews = new TextView[count];
			for(int i = 0; i < count; i++){
				mSquaresViews[i] = ( TextView )view.findViewById(mSquareViewIds[i]);
				mSquaresViews[i].setTag(R.id.number, i);
			}
			mSquareViewList.add(mSquaresViews);
		}

		mViewPager = (MyViewPager)findViewById(R.id.sudoku_viewpager);
		mAdapter = new PlayPagerAdapter(mSudokus.length, mSudokuViewList);
		mAdapter.setOnInstantiateItemListener(this);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(this);
		mViewPager.setOnTouchListener(this);
		onPageSelected( 0 );

		//对话框
		mDialog = new Dialog(this, R.style.transparent_dialog);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = getLayoutInflater().inflate(R.layout.dialog4, null);
		mDialog.setContentView(view);

		mDialogViewIds = new int[squarelength];
		mDialogViewIds[0] = R.id.DialogButton0;
		mDialogViewIds[1] = R.id.DialogButton1;
		mDialogViewIds[2] = R.id.DialogButton2;
		mDialogViewIds[3] = R.id.DialogButton3;

		mDialogViews = new TextView[squarelength];
		for(int i = 0; i < squarelength; i++){
			mDialogViews[i] = ( TextView )view.findViewById(mDialogViewIds[i]);
			mDialogViews[i].setTag(R.id.number, i);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Display display = getWindowManager().getDefaultDisplay();
		width = display.getWidth(); 
		height = display.getHeight();
		super.onResume();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(helpPanel.isOpen()){
			helpPanel.onClick();
			return;
		}

		if(mPlaying){
			mPlaying = false;
			hideSystemUi(mPlaying);
			mViewPager.setPlaying(mPlaying);
			handler.removeCallbacks(runnable);
			closingTimeTextView.setText(Calendar.getInstance().getTime().toString());
			return;
		}
		super.onBackPressed();
	}

	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
	}

	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
	}

	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		mCurrentIndex = arg0;
		float rating = mSudokus[arg0].degreeOfDifficulty;
		ratingBar.setRating( rating );
		sudokuId.setText( String.valueOf( mSudokus[arg0]._id ) );
		mSquaresViews = mSquareViewList.get( arg0 % mSquareViewList.size() );
		mData = mSudokus[arg0].data;
		
		timerTextView.setText("");
		closingTimeTextView.setText("");
		
		Log.e("onPageSelected", ""+arg0+" "+rating+" "+String.valueOf(mSudokus[arg0].data));
	}

	public void onClick( View view ){
		switch (view.getId()) {

		case R.id.timer_button:
			//开始计时
			mPlaying = true;
			hideSystemUi(mPlaying);
			mViewPager.setPlaying(mPlaying);
			recLen = 0;
			handler.postDelayed(runnable, 1000);
			break;

		case R.id.help_button:
			helpPanel.onClick();
			break;

		default:
			if(mPlaying){
				boolean blank = (Boolean) view.getTag(R.id.blank);
				if(blank){
					mClickView = view;
					mClickNumber = (Integer) mClickView.getTag(R.id.number);

					boolean[] have = getHave();
					boolean b;
					for (int i = 0; i < squarelength; i++ ) {
						b = have[i];
						mDialogViews[i].setTag( R.id.blank, b );
						if(b){
							mDialogViews[i].setTextColor(Color.WHITE);
						}
						else{
							mDialogViews[i].setTextColor(Color.BLUE);
						}
					}

					int [] xy =  getXY(mClickView);
					WindowManager.LayoutParams lp=mDialog.getWindow().getAttributes();
					//lp.alpha=0.5f;
					lp.x = xy[0];
					lp.y = xy[1];
					mDialog.getWindow().setAttributes(lp);
					mDialog.show();
				}
			}
			else{
				Toast.makeText(this, "点击左下角按钮开始", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

	public void onDialogClick( View view ){
		boolean blank = (Boolean) view.getTag(R.id.blank);
		if( !blank ){
			int value = (Integer) view.getTag(R.id.number);
			value++;
			((TextView)mClickView).setText( String.valueOf(value) );
			mData[mClickNumber] = value;

			int i;
			for(i = 0; i < count && mData[i] != 0; i++){

			}

			if( i >= count ){
				Toast.makeText(this, "success", Toast.LENGTH_LONG).show();
				onBackPressed();
			}

			mDialog.dismiss();
		}
	}

	/**
	 * 是否隐藏状态栏和标题栏（即是否全屏）。
	 * @param hide
	 */
	private void hideSystemUi(boolean hide) {
		Window window = getWindow();
		if (hide) {
			titleBarLayout.setVisibility(View.GONE);
			downBarLayout.setVisibility(View.GONE);
			window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		} else {
			window.setFlags(0, WindowManager.LayoutParams.FLAG_FULLSCREEN);
			titleBarLayout.setVisibility(View.VISIBLE);
			downBarLayout.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 计算view中心位置与屏幕中心的偏移
	 * @param view
	 * @return
	 */
	public int[] getXY( View view ){
		int x = view.getLeft() + view.getRight();
		int y = view.getTop() + view.getBottom();

		int []xy = new int[2];
		xy[0] = (x - width) >> 1;
					xy[1] = (y - height) >> 1;

			return xy;
	}

	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if(helpPanel.isOpen()){
			helpPanel.onClick();
			return true;
		}

		if( mPlaying ){
			//不允许viewpager滑动
			return true;
		}
		return false;
	}

	public boolean OnInstantiateItem(int index, int position) {
		// TODO Auto-generated method stub
		int[] values = mSudokus[position].data;
		mSquaresViews = mSquareViewList.get(index);
		for(int i = 0; i < mSquaresViews.length; i++){

			int x = i % squarelength;
			int y = i / squarelength;
			if( ( x < 2 && y < 2 ) || ( x > 1 && y > 1) ){
				mSquaresViews[i].setBackgroundResource(R.drawable.blue_light);
			}
			else{
				mSquaresViews[i].setBackgroundResource(R.drawable.blue_dark);
			}

			if( values[i] == 0 ){
				mSquaresViews[i].setTextColor(Color.BLUE);
				mSquaresViews[i].setText( "" );
				mSquaresViews[i].setTag(R.id.blank, true);
			}
			else{
				mSquaresViews[i].setTextColor(Color.WHITE);
				mSquaresViews[i].setText( String.valueOf( values[i] ) );
				mSquaresViews[i].setTag(R.id.blank, false);
			}
		}
		return false;
	}

	private boolean[] getHave(){
		int x = mClickNumber % squarelength;
		int y = mClickNumber / squarelength;

		int xi = x / squarewidth * squarewidth;
		int yi = y / squareheight * squareheight;

		boolean []have = new boolean[squarelength];
		for(int i = 0; i < squarelength; i++){
			have[i] = false;
		}

		for(int i = 0, line = y * squarelength, row = x, block = yi * squarelength + xi; 
				i < squarelength; 
				i++, line++, row += squarelength, block++){
			int value = mData[line];
			if( value != 0 ){
				have[value-1] = true;
			}

			value = mData[row];
			if( value != 0 ){
				have[value-1] = true;
			}

			if( i == 2 ){
				block+=i;
			}
			value = mData[block];
			if( value != 0 ){
				have[value-1] = true;
			}
		}
		return have;
	}
}
