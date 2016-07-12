package com.example.hedgehog.pokemons;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by hedgehog on 12.07.2016.
 */
public class MyListFragment extends Fragment {

    GridView gridView;
    Activity activity;
    static final int NUM_COLUMNS = 3;
    ArrayList<Pokemon> pokemons;
    MyArrayAdapter adapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    public void setNewArrayList (ArrayList<Pokemon> arrayList){
        for (Pokemon p:arrayList){
            pokemons.add(p);
        }
        adapter.notifyDataSetChanged();

    }

    public void notifyList (){
        adapter.notifyDataSetChanged();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, null);
        gridView = (GridView) rootView.findViewById(R.id.gridView);
        gridView.setNumColumns(NUM_COLUMNS);
        pokemons = new ArrayList<>();



        adapter = new MyArrayAdapter(activity, R.id.gridView, pokemons );
        gridView.setAdapter(adapter);

        return rootView;
    }

    public class MyArrayAdapter extends ArrayAdapter<Pokemon> {
        Context context;
        ArrayList<Pokemon> arrayList;


        public MyArrayAdapter(Context context, int resource, ArrayList<Pokemon> objects) {
            super(context, resource, objects);
            arrayList =  objects;
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rootView = inflater.inflate(R.layout.item_layout, parent, false);
            ImageView ivPicture = (ImageView) rootView.findViewById(R.id.ivPicture);
            TextView tvName = (TextView) rootView.findViewById(R.id.tvName);
            TextView tvType = (TextView) rootView.findViewById(R.id.tvType);

            ivPicture.setImageBitmap(arrayList.get(position).picture);
            String fullName = arrayList.get(position).name + " #" + arrayList.get(position).id;
            tvName.setText(fullName);
            String [] types = arrayList.get(position).types;
            StringBuilder sb = new StringBuilder("");

            for (int i = 0; i < types.length; i++){
              sb.append(types[i]);
                if (i !=types.length-1){
                    sb.append(", ");
                }
               }

            tvType.setText(sb.toString());

            return rootView;
        }
    }


}
