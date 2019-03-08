package com.example.watsontest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ImageTextAdapter extends BaseAdapter {
    Context context;
    List<ImageText> list;
    LayoutInflater inflater;

    public ImageTextAdapter(Context context){
        this.context = context;
        list = new ArrayList<ImageText>();
        inflater = LayoutInflater.from(context);
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageText current = list.get(position);

            if(current.left){
                convertView = inflater.inflate(R.layout.adapter_image_text_left, parent, false);
                ImageView image = convertView.findViewById(R.id.imageView2);
                image.setImageResource(R.drawable.a2);
                TextView text = convertView.findViewById(R.id.textView2);
                text.setText(list.get(position).text);
            }else {
                convertView = inflater.inflate(R.layout.adapter_image_text, parent, false);
                ImageView image = convertView.findViewById(R.id.imageView);
                image.setImageResource(R.drawable.a1);
                TextView text = convertView.findViewById(R.id.textView);
                text.setText(list.get(position).text);
            }


        return convertView;
    }

    public void add(ImageText element){
        list.add(element);
    }

    public void clear(){
        list.clear();
    }
}
