package com.example.pouyakarimi.testappone.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.pouyakarimi.testappone.R;
import com.example.pouyakarimi.testappone.objects.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pouyakarimi on 10/15/15.
 */
public class NoteArrayAdapter extends ArrayAdapter<Note> {

    private Context mContext;

    public NoteArrayAdapter(Context context, int rId,List<Note> items) {
        super(context,rId,items);
        mContext = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater ();
            convertView = inflater.inflate(R.layout.note_list_layout,parent,false);
        }

        TextView tv = (TextView)convertView.findViewById(R.id.noteText);
        if(tv != null)
            tv.setText(getItem(position).getText());



        return convertView;
    }

}
