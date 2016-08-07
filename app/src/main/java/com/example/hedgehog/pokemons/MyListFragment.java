package com.example.hedgehog.pokemons;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by hedgehog on 12.07.2016.
 */
public class MyListFragment extends Fragment implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener{

    GridView gridView;
    Activity activity;
    static final int NUM_COLUMNS = 2;

    ArrayList<Pokemon> pokemons = new ArrayList<>();
    MyArrayAdapter adapter;



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    public void setNewArrayList (ArrayList<Pokemon> arrayList){
        ArrayList<Type> chosenTypes = new ArrayList<>();
        for (Type type: MainActivity.typesArray){
            if (type.isChosenNow == true){
                chosenTypes.add(type);
            }
        }
        Log.d("asdf", "chosen   " + chosenTypes.toString());
        pokemons.clear();
        if (chosenTypes.size() == 0) {
            for (Pokemon p : arrayList) {
                pokemons.add(p);
            }
        } else {
            for (Pokemon p : arrayList) {
                boolean isAcceptable = false;
                for (Type t : p.types) {
                    Log.d ("asdf", t.nameOfType);
                    if (chosenTypes.contains(t)) {
                        Log.d ("asdf", t.nameOfType);
                        isAcceptable = true;
                        break;
                    }
                }
                if (isAcceptable) {
                    pokemons.add(p);
                }
            }
        }
        adapter.notifyDataSetChanged();

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, null);
        gridView = (GridView) rootView.findViewById(R.id.gridView);
        gridView.setNumColumns(NUM_COLUMNS);
        adapter = new MyArrayAdapter(activity, R.id.gridView, pokemons);
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //gridView.setTranscriptMode(GridView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        gridView.setOnItemClickListener(this);
        gridView.setOnScrollListener(this);
        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FragmentManager fragmentManager = getFragmentManager();
        PokemonDetailFragment detailFragment= (PokemonDetailFragment) fragmentManager.findFragmentByTag("detail");
        Fragment listFragment = fragmentManager.findFragmentByTag("listFragment");
        detailFragment.setValues(pokemons.get(position));
        MainActivity.currentPosition = position;
        if (MainActivity.isPort){
            detailFragment.setVisibility(true);
            fragmentManager.beginTransaction().show(detailFragment).commit();
            fragmentManager.beginTransaction().hide(listFragment).commit();
        }

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

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
            View rootView = null;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rootView = inflater.inflate(R.layout.item_layout, parent, false);
            } else{
                rootView = convertView;
            }
            ImageView ivPicture = (ImageView) rootView.findViewById(R.id.ivPicture);
            TextView tvName = (TextView) rootView.findViewById(R.id.tvName);
            TextView tvType = (TextView) rootView.findViewById(R.id.tvType);

            ivPicture.setImageBitmap(arrayList.get(position).picture);
            String fullName = arrayList.get(position).name + " #" + arrayList.get(position).id;
            tvName.setText(fullName);
            Type [] types = arrayList.get(position).types;
            StringBuilder sb = new StringBuilder("");

            for (int i = 0; i < types.length; i++){
              sb.append(types[i].nameOfType);
                if (i !=types.length-1){
                    sb.append(", ");
                }
               }

            tvType.setText(sb.toString());
            if (position == arrayList.size()-1){
                loadMore();
            }

            return rootView;
        }
    }

    private void loadMore(){
        if(!MainActivity.isLoadingNow) {
            MainActivity.at = new MainActivity.MyAsyncTask();
            MainActivity.at.execute(MainActivity.LIMIT, MainActivity.offset);
            MainActivity.loadingView.setVisibility(View.VISIBLE);
            MainActivity.loadingView.isRunning = true;
            MainActivity.loadingView.startAnimation1();

        }

    }


}
