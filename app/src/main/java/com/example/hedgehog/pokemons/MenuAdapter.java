package com.example.hedgehog.pokemons;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hedgehog on 07.08.2016.
 */
public class MenuAdapter extends ArrayAdapter<Type> {

    Context context;
    ArrayList<Type> arrayList;


    public MenuAdapter(Context context, int resource, ArrayList<Type> objects) {
        super(context, resource, objects);
        arrayList = objects;
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.menu_item_layout, parent, false);
        TextView textView = (TextView) rootView.findViewById(R.id.tvMenuType);

        Type t = arrayList.get(position);
        textView.setText(t.nameOfType);

        if (t.isChosenNow){
            rootView.setBackgroundColor(context.getResources().getColor(R.color.color3));
        }

        return rootView;
    }
}
