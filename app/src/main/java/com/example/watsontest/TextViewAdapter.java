package com.example.watsontest;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TextViewAdapter extends BaseAdapter {
Context context;
ArrayList<TextView> list;

public TextViewAdapter(Context context){
    this.context = context;
    list = new ArrayList<TextView>();
}

public void setList(ArrayList<TextView> list){
    this.list = list;
}

public void add(TextView textView){
    list.add(textView);
}

public void clear(){
    list.clear();
}

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return list.get(position);
    }
}
