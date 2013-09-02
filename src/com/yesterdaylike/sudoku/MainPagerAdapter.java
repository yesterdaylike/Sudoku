package com.yesterdaylike.sudoku;

import java.util.List;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class MainPagerAdapter extends PagerAdapter {

	//界面列表
	private List<View> mViews;

	public MainPagerAdapter ( List<View> views ) {
		this.mViews = views;
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		// TODO Auto-generated method stub
		( ( ViewPager ) container).removeView( mViews.get(position) );
	}

	@Override
	public void finishUpdate(View container) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object instantiateItem(View container, int position) {
		// TODO Auto-generated method stub
		( ( ViewPager ) container).addView( mViews.get(position), 0 );
		return mViews.get( position );
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
		if( null != mViews ){
			return mViews.size();
		}
		return 0;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}
}
