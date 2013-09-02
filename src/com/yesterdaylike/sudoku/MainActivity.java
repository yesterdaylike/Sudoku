package com.yesterdaylike.sudoku;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity 
implements OnPageChangeListener, OnTouchListener{

	private ViewPager mViewPager;
	private MainPagerAdapter mAdapter;
	private List<View> mViews;
	private int mCurrentIndex = 0;
	private Button mPageNumberButton;
	private Animation mAnimation;

	private Panel helpPanel;
	private Panel feedBackPanel;

	private EditText messageEditText;
	private EditText subjectEditText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mViews = new ArrayList<View>();

		LayoutInflater layoutInflater = getLayoutInflater();

		View view = layoutInflater.inflate(R.layout.viewpager_item, null);
		view.setOnTouchListener(this);
		view.findViewById(R.id.ImageButton00).setOnTouchListener(this);
		view.findViewById(R.id.ImageButton01).setOnTouchListener(this);
		view.findViewById(R.id.ImageButton02).setOnTouchListener(this);
		view.findViewById(R.id.ImageButton03).setOnTouchListener(this);
		view.findViewById(R.id.ImageButton04).setOnTouchListener(this);
		view.findViewById(R.id.ImageButton05).setOnTouchListener(this);
		view.findViewById(R.id.ImageButton06).setOnTouchListener(this);
		view.findViewById(R.id.ImageButton07).setOnTouchListener(this);
		mViews.add( view );

		view = layoutInflater.inflate(R.layout.viewpager_item, null);
		view.setOnTouchListener(this);
		mViews.add( view );

		view = layoutInflater.inflate(R.layout.viewpager_item, null);
		view.setOnTouchListener(this);
		mViews.add( view );

		view = layoutInflater.inflate(R.layout.viewpager_item, null);
		view.setOnTouchListener(this);
		mViews.add( view );

		view = layoutInflater.inflate(R.layout.viewpager_item, null);
		view.setOnTouchListener(this);
		mViews.add( view );

		mViewPager = (ViewPager)findViewById(R.id.sudoku_type_viewpager);
		mAdapter = new MainPagerAdapter(mViews);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(this);

		mPageNumberButton = (Button) findViewById(R.id.page_number_button);
		mPageNumberButton.setText( String.valueOf( mCurrentIndex+1 ) );

		helpPanel = (Panel)findViewById(R.id.help_panel);
		feedBackPanel = (Panel)findViewById(R.id.feedback_panel);

		messageEditText = (EditText)findViewById(R.id.message);
		subjectEditText = (EditText)findViewById(R.id.subject);

		mAnimation = AnimationUtils.loadAnimation(this, R.anim.horizontal_scale);
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
		mPageNumberButton.startAnimation(mAnimation);
		mPageNumberButton.setText( String.valueOf( mCurrentIndex+1 ) );
	}

	public void onClick( View view ){
		if( BuildConfig.DEBUG ){
			Log.e("SuDoKu", ""+view.getId() );
		}
		switch (view.getId()) {
		case R.id.page_number_button:
			//页码
			mViewPager.setCurrentItem( (mCurrentIndex + 1) % mViews.size() );
			break;

		case R.id.help_button:
			//帮助
			if(feedBackPanel.isOpen()){
				feedBackPanel.onClick();
			}
			helpPanel.onClick();
			break;

		case R.id.feedback_button:
			//反馈
			if(helpPanel.isOpen()){
				helpPanel.onClick();
			}
			feedBackPanel.onClick();
			break;

		case R.id.feedback_send:
			//发送反馈
			String message = messageEditText.getText().toString();
			if( null != message && message.length()>0 ){
				if(BuildConfig.DEBUG){
					Log.i("FeedBack", message);
				}
				String subject = subjectEditText.getText().toString();
				Intent email = new Intent(android.content.Intent.ACTION_SEND);
				email.setType("plain/text");
				email.putExtra(android.content.Intent.EXTRA_EMAIL, 
						new String[]{"zhengwenhui@outlook.com"});   			 //设置邮件默认地址
				email.putExtra(android.content.Intent.EXTRA_SUBJECT, subject); 	 //设置邮件默认标题
				email.putExtra(android.content.Intent.EXTRA_TEXT, message); 	 //设置要默认发送的内容
				startActivity(Intent.createChooser(email, 
						getString(R.string.select_mail_software)));  			 //调用系统的邮件系统
			}

			messageEditText.setText("");
			subjectEditText.setText("");

			if(feedBackPanel.isOpen()){
				feedBackPanel.onClick();
			}
			break;

		case R.id.ImageButton00:
			if(feedBackPanel.isOpen()){
				feedBackPanel.onClick();
			}

			if(helpPanel.isOpen()){
				helpPanel.onClick();
			}

			Intent intent1 = new Intent(this, PlayActivity4.class);
			//intent.putExtra("type", "1");
			startActivity(intent1);
			overridePendingTransition(0, android.R.anim.fade_out); 
			break;

		case R.id.ImageButton01:
			if(feedBackPanel.isOpen()){
				feedBackPanel.onClick();
			}

			if(helpPanel.isOpen()){
				helpPanel.onClick();
			}

			Intent intent = new Intent(this, PlayActivity6.class);
			//intent.putExtra("type", "1");
			startActivity(intent);
			overridePendingTransition(0, android.R.anim.fade_out); 
			break;

		default:
			if(feedBackPanel.isOpen()){
				feedBackPanel.onClick();
			}

			if(helpPanel.isOpen()){
				helpPanel.onClick();
			}

			Intent intent2 = new Intent(this, PlayActivity9.class);
			//intent.putExtra("type", "1");
			startActivity(intent2);
			overridePendingTransition(0, android.R.anim.fade_out); 
			break;
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(feedBackPanel.isOpen()){
			feedBackPanel.onClick();
			return;
		}

		if(helpPanel.isOpen()){
			helpPanel.onClick();
			return;
		}
		super.onBackPressed();
	}

	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if(feedBackPanel.isOpen()){
			feedBackPanel.onClick();
		}

		if(helpPanel.isOpen()){
			helpPanel.onClick();
		}
		return false;
	}
}
