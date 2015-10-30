package com.example.pouyakarimi.testappone.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.pouyakarimi.testappone.R;
import com.example.pouyakarimi.testappone.objects.User;

import java.util.List;

/**
 * Created by pouyakarimi on 10/27/15.
 */
public class UserArrayAdaptor extends ArrayAdapter<User> {

    private Context mContext;

    public UserArrayAdaptor(Context context, int rId, List<User> items) {
        super(context, rId, items);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.user_list_layout, parent, false);
        }

        TextView userName = (TextView) convertView.findViewById(R.id.userNameTextViewList);
        TextView userEmail = (TextView) convertView.findViewById(R.id.userEmailTextViewList);
        TextView userIsPrimary = (TextView) convertView.findViewById(R.id.userPrimaryTextViewList);
        userIsPrimary.setVisibility(View.INVISIBLE);

        if (userName != null && userEmail != null) {
            userName.setText(getItem(position).getName());
            userEmail.setText(getItem(position).getEmail());
            if (getItem(position).isItPrimary()) {
                userIsPrimary.setVisibility(View.VISIBLE);
            }
        }

        return convertView;
    }
}
