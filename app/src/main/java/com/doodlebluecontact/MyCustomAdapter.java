package com.doodlebluecontact;


import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class MyCustomAdapter extends ArrayAdapter {

	private List contactsInfoList;
	private Context context;

	public MyCustomAdapter(@NonNull Context context, int resource, @NonNull List objects) {
		super(context, resource, objects);
		this.contactsInfoList = objects;
		this.context = context;
	}

	private class ViewHolder {
		TextView displayName;
		TextView phoneNumber;
		CheckBox checkbox;
		LinearLayout llLinear;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.contact_info, null);

			holder = new ViewHolder();
			holder.displayName = (TextView) convertView.findViewById(R.id.displayName);
			holder.phoneNumber = (TextView) convertView.findViewById(R.id.phoneNumber);
			holder.checkbox    = (CheckBox) convertView.findViewById(R.id.checkBox);
			holder.llLinear    = (LinearLayout) convertView.findViewById(R.id.llrow);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		com.doodlebluecontact.ContactsInfo contactsInfo = (com.doodlebluecontact.ContactsInfo) contactsInfoList.get(position);
		holder.displayName.setText(contactsInfo.getDisplayName());
		holder.phoneNumber.setText(contactsInfo.getPhoneNumber());
		ViewHolder finalHolder = holder;
		holder.llLinear.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(finalHolder.checkbox.isChecked()){
					SharedPreferences preferencesimg = context.getSharedPreferences(
							"login", 0);
					SharedPreferences.Editor editorimg = preferencesimg.edit();
					editorimg.putString("mobile", contactsInfo.getPhoneNumber().replaceAll(" ",""));
					editorimg.apply();
				}else{
					SharedPreferences preferencesimg = context.getSharedPreferences(
							"login", 0);
					SharedPreferences.Editor editorimg = preferencesimg.edit();
					editorimg.putString("mobile", "");
					editorimg.apply();
				}
			}
		});


		return convertView;
	}
}
