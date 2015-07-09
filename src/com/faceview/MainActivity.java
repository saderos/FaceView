package com.faceview;

import com.example.facepageactivity.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;


public class MainActivity extends Activity {
	FaceView faceView;
	ImageView imgView;
	EditText editView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment_activity_publish);

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
						| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		faceView = (FaceView) findViewById(R.id.face_view);
		imgView = (ImageView) findViewById(R.id.commentPublish_ivAddFace);
		editView = (EditText) findViewById(R.id.commentPublish_etContent);
		
		faceView.setEdit(editView);
		faceView.setBtnView(imgView);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
