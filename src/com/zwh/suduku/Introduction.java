package com.zwh.suduku;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class Introduction extends Activity{

	TextView messageTextView;
	Button leftButton;
	Button rightButton;
	SeekBar seekBar;
	ImageView imageView;

	String[] introductionStr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_introduction);

		leftButton = (Button) findViewById(R.id.left_button);
		rightButton = (Button) findViewById(R.id.right_button);
		messageTextView = (TextView) findViewById(R.id.textview);
		seekBar = (SeekBar) findViewById(R.id.seekbar);
		imageView = (ImageView) findViewById(R.id.imageview);

		imageView.setImageResource(R.drawable.ic_launcher);

		introductionStr = getResources().getStringArray(R.array.introduction_message);
		messageTextView.setText(introductionStr[0]);

		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				Log.i("onProgressChanged", ""+progress);
			}
		});
	}

	public void onLeftButtonClick(View view){
		int progress = seekBar.getProgress();
		seekBar.setProgress( progress - 1 );

	}

	public void onRightButtonClick(View view){
		int progress = seekBar.getProgress();
		seekBar.setProgress( progress + 1 );
	}
}
