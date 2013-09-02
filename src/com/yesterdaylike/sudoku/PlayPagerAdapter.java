package com.yesterdaylike.sudoku;

import java.util.List;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

public class PlayPagerAdapter extends PagerAdapter {

	private int mCount;
	//界面列表
	private List<View> mViews;
	private OnInstantiateItemListener mOnInstantiateItemListener;

	public PlayPagerAdapter ( int count, List<View> views ) {
		this.mViews = views;
		Log.i("count", ""+count);
		mCount = count;
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		// TODO Auto-generated method stub
		View view = mViews.get( position % mViews.size() );
		( ( ViewPager ) container).removeView( view );
	}

	@Override
	public void finishUpdate(View container) {
		// TODO Auto-generated method stub
		//super.finishUpdate(container);
	}

	@Override
	public Object instantiateItem(View container, int position) {
		// TODO Auto-generated method stub
		int index = position % mViews.size();
		index %= mViews.size();
		if(BuildConfig.DEBUG){
			Log.i("instantiateItem", ""+container+" " + index+ " instantiateItem");
		}
		
		( ( ViewPager ) container).addView( mViews.get(index) );
		
		if( null != mOnInstantiateItemListener ){
			mOnInstantiateItemListener.OnInstantiateItem(index,position);
		}
		
		return mViews.get( index );
	}

	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return super.getItemPosition(object);
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
		// TODO Auto-generated method stub
		super.restoreState(state, loader);
	}

	@Override
	public Parcelable saveState() {
		// TODO Auto-generated method stub
		return super.saveState();
	}

	@Override
	public void startUpdate(View container) {
		// TODO Auto-generated method stub
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mCount;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}
	
	public void setOnInstantiateItemListener(OnInstantiateItemListener l) {
		mOnInstantiateItemListener = l;
    }
	
	public interface OnInstantiateItemListener {
        boolean OnInstantiateItem(int index, int position);
    }
}
