package com.example.quantifydemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class HomeFragmentAdapter extends ArrayAdapter<Tests> {

    private static final String TAG = "CustomListAdapter";
    private Context mContext;
    private int mResource;

    public HomeFragmentAdapter(Context context, int resource, ArrayList<Tests> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    //@NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.home_cards, parent, false);

        TextView subject = convertView.findViewById(R.id.subject);
        TextView techer = convertView.findViewById(R.id.teacher);
        subject.setText(getItem(position).getSubject());
        techer.setText(getItem(position).getTeacher());

        return convertView;
    }
}



