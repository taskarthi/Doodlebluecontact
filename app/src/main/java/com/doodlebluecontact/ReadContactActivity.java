package com.doodlebluecontact;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class ReadContactActivity extends AppCompatActivity {
	public static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;
	private static final int PERMISSION_REQUEST_READ_PHONE_STATE = 2;
	private static final int PERMISSION_REQUEST_READ_CALLLOG = 3;
	MyCustomAdapter dataAdapter = null;
	ListView listView;
	List<ContactsInfo> contactsInfoList;
	com.doodlebluecontact.PhoneStateReceiver phReceiver;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_readcontact);

		listView = (ListView) findViewById(R.id.lstContacts);
		listView.setAdapter(dataAdapter);

		phReceiver = new com.doodlebluecontact.PhoneStateReceiver();
		requestContactPermission();
		requestcalls();
		requestcallsLog();
	}

	private void getContacts(){

		String contactId = null;
		String displayName = null;
		contactsInfoList = new ArrayList<ContactsInfo>();
		Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
				if (hasPhoneNumber > 0) {

					ContactsInfo contactsInfo = new ContactsInfo();
					contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
					displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

					contactsInfo.setContactId(contactId);
					contactsInfo.setDisplayName(displayName);

					Cursor phoneCursor = getContentResolver().query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
							new String[]{contactId},
							null);

					if (phoneCursor.moveToNext()) {
						String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

						contactsInfo.setPhoneNumber(phoneNumber);
					}

					phoneCursor.close();

					contactsInfoList.add(contactsInfo);
				}
			}
		}
		cursor.close();

		dataAdapter = new MyCustomAdapter(com.doodlebluecontact.ReadContactActivity.this, R.layout.contact_info, contactsInfoList);
		listView.setAdapter(dataAdapter);
	}

	public void requestcalls(){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED) {
				String[] permissions = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE};
				requestPermissions(permissions, PERMISSION_REQUEST_READ_PHONE_STATE);
			}
		}
	}

	public void requestcallsLog(){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (ContextCompat.checkSelfPermission(com.doodlebluecontact.ReadContactActivity.this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(com.doodlebluecontact.ReadContactActivity.this, Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED) {
				ActivityCompat.requestPermissions(com.doodlebluecontact.ReadContactActivity.this,
						new String[]{Manifest.permission.READ_CALL_LOG, Manifest.permission.SYSTEM_ALERT_WINDOW},
						PERMISSION_REQUEST_READ_CALLLOG);
			}
		}
	}
	public void requestContactPermission() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
				if (ActivityCompat.shouldShowRequestPermissionRationale(this,
						Manifest.permission.READ_CONTACTS)) {
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("Read contacts access needed");
					builder.setPositiveButton(android.R.string.ok, null);
					builder.setMessage("Please enable access to contacts.");
					builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
						@TargetApi(Build.VERSION_CODES.M)
						@Override
						public void onDismiss(DialogInterface dialog) {
							requestPermissions(
									new String[]
											{Manifest.permission.READ_CONTACTS}
									, PERMISSIONS_REQUEST_READ_CONTACTS);
						}
					});
					builder.show();
				} else {
					ActivityCompat.requestPermissions(this,
							new String[]{Manifest.permission.READ_CONTACTS},
							PERMISSIONS_REQUEST_READ_CONTACTS);
				}
			} else {
				getContacts();
			}
		} else {
			getContacts();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
										   String permissions[], int[] grantResults) {
		switch (requestCode) {
			case PERMISSIONS_REQUEST_READ_CONTACTS: {
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					getContacts();
				} else {
					Toast.makeText(this, "You have disabled a contacts permission", Toast.LENGTH_LONG).show();
				}
				return;
			}
			case PERMISSION_REQUEST_READ_PHONE_STATE: {
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					Toast.makeText(this, "Permission granted: " + PERMISSION_REQUEST_READ_PHONE_STATE, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(this, "Permission NOT granted: " + PERMISSION_REQUEST_READ_PHONE_STATE, Toast.LENGTH_SHORT).show();
				}

				return;
			}
			case PERMISSION_REQUEST_READ_CALLLOG: {
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					Toast.makeText(this, "Permission granted: " + PERMISSION_REQUEST_READ_CALLLOG, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(this, "Permission NOT granted: " + PERMISSION_REQUEST_READ_CALLLOG, Toast.LENGTH_SHORT).show();
				}

				return;
			}
		}
	}
}
