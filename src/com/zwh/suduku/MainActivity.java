package com.zwh.suduku;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends Activity implements ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v13.app.FragmentStatePagerAdapter}.
	 */ 
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(false);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}



	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}*/

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		String [] titles;
		int [] types;

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
			titles = getResources().getStringArray(R.array.types_name);
			types = getResources().getIntArray(R.array.types);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			String typeStr = String.valueOf(types[position]);
			return PlaceholderFragment.newInstance(typeStr);
		}

		@Override
		public int getCount() {
			return titles.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position];
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment 
	implements LoaderManager.LoaderCallbacks<Cursor> {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";
		private SimpleCursorAdapter mAdapter;
		private DataBaseHelper mDbHelper;
		private String mType;
		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(String type) {
			PlaceholderFragment fragment = new PlaceholderFragment(type);
			Bundle args = new Bundle();
			args.putString(ARG_SECTION_NUMBER, type);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment(){
		}
		
		@SuppressLint("ValidFragment")
		public PlaceholderFragment(String type){
			this.mType = type;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);

			ListView listView = (ListView) rootView.findViewById(R.id.list_view);

			if (savedInstanceState != null) {
				// Restore last state for checked position.
				mType = savedInstanceState.getString("mType", "901");
			}
			// For the cursor adapter, specify which columns go into which views
			String[] fromColumns = {"_id","data"};
			int[] toViews = {android.R.id.text1}; // The TextView in simple_list_item_1

			// Create an empty adapter we will use to display the loaded data.
			// We pass null for the cursor, then update it in onLoadFinished()
			mDbHelper = new DataBaseHelper(getActivity());
			
			Cursor cursor = mDbHelper.query( mType );
			
			mAdapter = new CursorAdapter(getActivity(), 
					android.R.layout.simple_list_item_1, cursor,
					fromColumns, toViews, 0);

			listView.setAdapter(mAdapter);
			listView.setOnItemClickListener(listViewItemClick);
			// Prepare the loader.  Either re-connect with an existing one,
			// or start a new one.
			//getLoaderManager().initLoader(0, null, this);
			return rootView;
		}

		@Override
		public void onSaveInstanceState(Bundle outState) {
			super.onSaveInstanceState(outState);
			outState.putString("mType", mType);
		}
		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			// TODO Auto-generated method stub
			int type[] = getResources().getIntArray(R.array.types);
			return new DBCursorLoader(getActivity(),  mType);
		}

		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
			// TODO Auto-generated method stub
			mAdapter.swapCursor(data);
		}

		@Override
		public void onLoaderReset(Loader<Cursor> loader) {
			// TODO Auto-generated method stub
			mAdapter.swapCursor(null);
		}
		private OnItemClickListener listViewItemClick= new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String dataStr = (String) view.getTag(R.id.data_base_data);
				String idStr = (String) view.getTag(R.id.data_base_id);

				Intent intent = new Intent(getActivity(), PlayActivity.class);
				intent.putExtra("data",  dataStr.toCharArray());
				intent.putExtra("id", idStr);
				startActivityForResult(intent, R.id.REQUEST_CODE);
			}
		};

		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			if ( resultCode == RESULT_OK ) {
				Log.e("zhengwenhui", "onActivityResult: "+ resultCode);
				//getLoaderManager().initLoader(0, null, this);
				Cursor cursor = mDbHelper.query( mType );
				mAdapter.changeCursor(cursor);
			}
		}
	}
}
