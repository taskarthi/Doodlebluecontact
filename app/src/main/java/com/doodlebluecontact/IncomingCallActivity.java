package com.doodlebluecontact;


import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class IncomingCallActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		try {

			getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);


			setContentView(R.layout.popup);

			super.onCreate(savedInstanceState);

			String strMobile = getIntent().getExtras().getString("mobile");

			TextView txtName = (TextView) findViewById(R.id.nametxt);
			TextView txtMobile = (TextView) findViewById(R.id.mobiletxt);
			txtMobile.setText("Mobile Number :"+strMobile);
		} catch (Exception e) {
			Log.d("Exception", e.toString());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}