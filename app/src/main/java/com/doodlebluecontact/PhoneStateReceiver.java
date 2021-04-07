package com.doodlebluecontact;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.widget.PopupWindow;


public class PhoneStateReceiver extends BroadcastReceiver {
	PopupWindow popupWindow;
	@Override
	public void onReceive(Context context, Intent intent) {

		try {
			System.out.println("Receiver start");
			String state = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
			String incomingNumber = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);

			if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
				incomingNumber = incomingNumber.substring(3);
				SharedPreferences preferences = context.getSharedPreferences("login", 0);
				String strMobile = preferences.getString("mobile", null);

				if (PhoneNumberUtils.compare(strMobile, incomingNumber)) {

					Intent i = new Intent(context, IncomingCallActivity.class);
					i.putExtra("mobile",strMobile);
					i.putExtras(intent);
					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					context.startActivity(i);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}