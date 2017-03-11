package com.behave.behave.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.behave.behave.R;

import java.util.List;

/**
 * Created by IJM on 3/11/17.
 */

class CustomListViewAdapter extends ArrayAdapter<String> {


    public CustomListViewAdapter(@NonNull Context context, /*@LayoutRes*/ /*String[]*/ List<String> children) {
        super(context, R.layout.custom_row, children);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater myInflater = LayoutInflater.from(getContext());
        View CustomView = myInflater.inflate(R.layout.custom_row, parent, false);

        // Gets a reference to the image
        String singleChildItem = getItem(position);
        TextView myText = (TextView) CustomView.findViewById(R.id.tv_myText);
        ImageView myImage = (ImageView) CustomView.findViewById(R.id.iv_green_monster);

        myText.setText(singleChildItem);
        myImage.setImageResource(R.drawable.green_monster);

        return CustomView;
    }
}