package com.zwh.suduku;

import java.util.ArrayList;
import java.util.Stack;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class ReseauView extends View{

	private Paint paintLight;
	private Paint paintText;

	private int colorLight = 0xffeee4da;
	private int colorDark = 0xffede0c8;
	private int colorConflict = 0x80f65e3b;
	private int colorDialog = 0xedffffff;

	private int colorTextDark = 0xff000000;
	private int colorTextLight = 0xffcc0000;

	private int SIZE = 9;
	private int[] posWidth;
	private int[] posHeight;

	private int currentWidth;
	private int currentHeight;

	private int radius;

	private float drawTextWidthAdjust;
	private float drawTextHeightAdjust;

	private final static int SHOWED = 0x80;
	private final static int SHOWING = 0x81;
	private final static int NOSHOWED = 0x82;
	private int showDialog = NOSHOWED;

	private int[] mData;
	private String mId;
	private boolean []mDataTag;

	private Context mContext;

	private Stack<Action> mHistoryStack = new Stack<Action>();
	private ArrayList<Integer> mConLocation = new ArrayList<Integer>(3);

	public ReseauView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.mContext = context;
		initPaint();
	}

	private void initPaint(){
		paintLight = new Paint(Paint.ANTI_ALIAS_FLAG );
		paintLight.setDither(true);
		//paint.setStyle( Paint.Style.STROKE );
		paintLight.setStyle(Paint.Style.FILL);

		paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
		paintText.setDither(true);
		paintText.setTextSize(24);
		paintText.setColor(Color.BLACK);
	}

	private void initPositionlist(){
		int width = getWidth();
		int height = getHeight();

		int diameter;
		int blankLeaving;

		int temp;

		posWidth = new int[SIZE+3];
		posHeight = new int[SIZE+3];

		if( width > height ){
			diameter = height / ( SIZE + 2 );
			blankLeaving = ( width - height ) >> 1;

			temp = 0;
			for(int i = 0; i <= SIZE + 2; i++){
				posWidth[i] = temp + blankLeaving;
				posHeight[i] = temp;
				temp += diameter;
			}
		}
		else{
			diameter = width / ( SIZE + 2 );
			blankLeaving = ( height - width ) >> 1;

			temp = 0;
			for(int i = 0; i <= ( SIZE + 2 ); i++){
				posWidth[i] = temp;
				posHeight[i] = temp + blankLeaving;
				temp += diameter;
			}
		}

		radius = diameter >> 1;

			drawTextWidthAdjust = radius - paintText.measureText("0") / 2;
			drawTextHeightAdjust = radius + ( paintText.descent() - paintText.ascent() ) / 4;
	}

	public void checkTouchPostion(float x, float y){
		int w,h;
		for ( h = 0; h <= ( SIZE + 2 ); h++) {
			if(y < posHeight[h]){
				h--;
				break;
			}
		}

		for ( w = 0; w <= ( SIZE + 2 ); w++) {
			if(x < posWidth[w]){
				w--;
				break;
			}
		}

		Log.i(VIEW_LOG_TAG, "("+w+","+h+")");

		if( showDialog == SHOWED ){
			if( isInDialog(w, h, currentWidth, currentHeight) ){
				showDialog = NOSHOWED;
				this.invalidate();
			}
			else if( isInReseau(w, h) ){
				showDialog = SHOWING;
				currentWidth = w;
				currentHeight = h;
				this.invalidate();
			}
			else{
				showDialog = NOSHOWED;
				this.invalidate();
			}
		}
		else if( isInReseau(w, h) && mDataTag[ h * 9 + w - 10 ]){
			showDialog = SHOWING;
			currentWidth = w;
			currentHeight = h;
			this.invalidate();
		}
	}

	private boolean isInReseau(int w, int h){
		if( w >= 1 && w <= SIZE && h >= 1 && h <= SIZE ){
			return true;
		}
		return false;
	}

	private boolean isInDialog(int w, int h, int centerW, int centerH){
		if( w >= centerW-1 && w <= centerW+1 && h >= centerH-1 && h <= centerH+1 ){
			int getValue = 3 * ( h - currentHeight ) + w - currentWidth + 5;
			int location = currentHeight * 9 + currentWidth - 10;
			isConflict(location, getValue);

			if (mConLocation.isEmpty()) {
				mHistoryStack.push(new Action(location, mData[location], getValue));
				mData[location] = getValue;
				if ( 0 == getBlankSize() ){
					Toast.makeText(mContext, R.string.success, Toast.LENGTH_SHORT).show();
					DataBaseHelper mDbHelper = new DataBaseHelper(mContext);
					mDbHelper.complete(mId);
					mDbHelper.closeDatabase();
					Log.e("isInDialog", "complete");
				}
			}

			return true;
		}
		return false;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if( null == posWidth ){
			initPositionlist();
		}

		paintLight.setColor(colorDialog);
		canvas.drawRect(
				posWidth[ 1 ] - 2,
				posHeight[ 1 ] - 2,
				posWidth[ 10 ],
				posHeight[ 10 ],
				paintLight);

		int color;
		int k, h, w;
		for (h = 1, k = 0; h <= SIZE; h++) {
			for (w = 1; w <= SIZE; w++, k++) {
				color = isDarkOrLight(w, h) ? colorDark : colorLight;
				paintLight.setColor(color);
				canvas.drawRect(posWidth[w], posHeight[h], posWidth[w+1]-2, posHeight[h+1]-2, paintLight);

				int cudata = mData[k];

				if( mData[k] != 0 ){
					color = mDataTag[k] ? colorTextLight : colorTextDark;
					paintText.setColor(color);

					canvas.drawText(String.valueOf(cudata),
							posWidth[w] + drawTextWidthAdjust, 
							posHeight[h] + drawTextHeightAdjust, 
							paintText);
				}
			}
		}

		paintLight.setColor(colorConflict);
		for (int location : mConLocation) {
			h = location / 9 + 1;
			w = location % 9 + 1;
			canvas.drawRect(posWidth[w], posHeight[h], posWidth[w+1]-2, posHeight[h+1]-2, paintLight);
		}
		mConLocation.clear();

		paintText.setColor(colorTextLight);
		switch (showDialog) {
		case SHOWING:
			paintLight.setColor(colorDialog);
			canvas.drawRect(
					posWidth[ currentWidth - 1 ] - 2,
					posHeight[ currentHeight - 1 ] - 2,
					posWidth[ currentWidth + 2 ],
					posHeight[ currentHeight + 2 ],
					paintLight);

			paintText.setColor(colorTextLight);
			for ( k = 1, h = currentHeight-1; h <= currentHeight+1; h++) {
				for ( w = currentWidth-1; w <= currentWidth+1; w++, k++) {
					//canvas.drawRect(posWidth[w], posHeight[h], posWidth[w+1]-2, posHeight[h+1]-2, paintLight);
					canvas.drawText(String.valueOf(k),
							posWidth[w] + drawTextWidthAdjust, 
							posHeight[h] + drawTextHeightAdjust,
							paintText);
				}
			}
			showDialog = SHOWED;
			break;

		default:
			break;
		}
		//getAnswer();
	}

	int getBlankSize(){
		int size = 0;
		for (int item : mData) {
			if(item == 0){
				size++;
			}
		}
		return size;
	}

	private boolean isConflict(int index, int value){
		int x = index / 9;
		int y = index % 9;

		int line = x*9;
		int row = y;
		int rect = x / 3 * 27 + y / 3 * 3;

		mConLocation.clear();

		for (int i = 0; i < 9; i++) {
			if( index != line && value == mData[line] ){
				mConLocation.add(line);
				break;
			}
			line++;
		}

		for (int i = 0; i < 9; i++) {
			if( index != row && value == mData[row] ){
				if(!mConLocation.contains(row)){
					mConLocation.add(row);
				}
				break;
			}
			row += 9;
		}

		for (int i = 0; i < 9; i++) {
			if(  index != rect && value == mData[rect] ){
				if(!mConLocation.contains(rect)){
					mConLocation.add(rect);
				}
				break;
			}

			if( i == 2 || i == 5 ){
				rect += 6;
			}
			rect++;
		}

		return !mConLocation.isEmpty();
	}

	private boolean isDarkOrLight(int w, int h){
		int rect = (w - 1)/ 3 * 3 + ( h - 1 ) / 3;
		return rect % 2 == 0;
	}

	void undo(){
		if( !mHistoryStack.empty() ){
			Action action = mHistoryStack.pop();
			mData[action.location] = action.oldValue;
			this.invalidate();
		}
	}

	private boolean getAnswer(){
		int forword = 1;
		int i;
		for ( i = 0; i < mDataTag.length && i >=0; i+=forword) {
			if( mDataTag[i] ){
				do {
					mData[i]++;
				} while ( mData[i]<=9 && isConflict(i, mData[i]) );

				if( mData[i]<=9 ){
					forword = 1;
				}
				else{
					forword = -1;
					mData[i] = 0;
				}
				Log.i(" ", "index: "+i+" , data: "+mData[i] +", forword: "+forword);
			}
		}
		return i >= mDataTag.length;
	}

	void setSudokuData( int []data ){
		this.mData = data;
	}

	int[] getSudokuData(){
		return this.mData;
	}

	void setSudokuId(String id){
		this.mId = id;
	}

	String getSudokuId(){
		return this.mId;
	}

	void setSudokuTag(boolean[] dataTag){
		this.mDataTag = dataTag;
	}

	boolean[] getSudokuTag(){
		return this.mDataTag;
	}

	void buildSudokuTag(){
		mDataTag = new boolean[mData.length];
		for (int i = 0; i < mDataTag.length; i++) {
			mDataTag[i] = mData[i]==0 ? true : false;
		}
	}

	void rebuildActionStack(int[] history){
		Action action = null;

		for (int j = 0; j < history.length; j++) {
			Log.i("rebuildActionStack", " "+j+": "+history[j]);
		}

		for (int i = history.length - 1; i >= 2; i -= 3) {
			action = new Action(history[i-2], history[i-1], history[i]);
			mHistoryStack.push(action);
		}
	}

	int[] saveActionStack(){
		int size = mHistoryStack.size();
		Log.i("saveActionStack", "capacity:¡¡"+ size);
		int []history = new int[ 3 * size ];

		Action action;
		int i = 0;

		while( !mHistoryStack.empty() ){
			action = mHistoryStack.pop();
			history[i++] = action.location;
			history[i++] = action.oldValue;
			history[i++] = action.value;
		}

		for (int j = 0; j < history.length; j++) {
			Log.i("saveActionStack", " "+j+": "+history[j]);
		}

		return history;
	}

	class Action{
		int location;
		int oldValue;
		int value;
		public Action(int location, int oldValue, int value) {
			// TODO Auto-generated constructor stub
			this.location = location;
			this.oldValue = oldValue;
			this.value = value;
		}
	}
}
