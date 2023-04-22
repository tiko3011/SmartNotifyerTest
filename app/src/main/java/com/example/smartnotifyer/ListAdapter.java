package com.example.smartnotifyer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<String>  {

    private ArrayList<String> name;
    private ArrayList<Long> time;

    public ListAdapter(Context context, ArrayList<String> name, ArrayList<Long> time) {
        super(context, R.layout.list_item); // R.layout.list_item is the layout resource for each list item
        this.name = name;
        this.time = time;
    }

    public int getCount() {
        return name.size();
    }

    @Override
    public String getItem(int position) {
        return name.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item, parent, false);

        TextView textView1 = rowView.findViewById(R.id.tvPackageName);
        TextView textView2 = rowView.findViewById(R.id.tvTotalTime);

        textView1.setText(name.get(position));
        textView2.setText(String.valueOf(time.get(position)));
        return rowView;
    }

    @Override
    public void notifyDataSetChanged(){


    }

}