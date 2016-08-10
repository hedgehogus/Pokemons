package com.example.hedgehog.pokemons;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;


/**
 * Created by hedgehog on 12.07.2016.
 */
public class MyListFragment extends Fragment implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener{

    GridView gridView;
    Activity activity;
    static final int NUM_COLUMNS = 2;

    ArrayList<Pokemon> pokemons = new ArrayList<>();
    ArrayList<Integer> visiblePositions
    MyArrayAdapter adapter;


    private int preLast;
    int totalItemCount = 0;
    int scrollState;


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
        //Log.d("asdf", "chosen   " + chosenTypes.toString());
        pokemons.clear();
        preLast = 0;
        if (chosenTypes.size() == 0) {
            for (Pokemon p : arrayList) {
                pokemons.add(p);
            }
        } else {
            for (Pokemon p : arrayList) {
                boolean isAcceptable = false;
                for (Type t : p.types) {
                    if (chosenTypes.contains(t)) {
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
        this.scrollState = scrollState;

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (scrollState != AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            if (gridView.getChildCount() != 0) {
                if (gridView.getLastVisiblePosition() == gridView.getAdapter().getCount() - 1 &&
                        gridView.getChildAt(gridView.getChildCount() - 1).getBottom() <= gridView.getHeight()) {
                   loadMore();
                }
            }
       } else {
          // Log.d("asdf", "totalItemCount " + totalItemCount);
            this.totalItemCount = totalItemCount;
       }


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
            if (totalItemCount < 7 && totalItemCount > 0){
                loadMore();
            }

            return rootView;
        }
    }

    private void loadMore(){
        if(!MainActivity.isLoadingNow) {
            MainActivity.at = new MainActivity.MyAsyncTask();
            MainActivity.at.execute(MainActivity.LIMIT);
            MainActivity.loadingView.setVisibility(View.VISIBLE);
            MainActivity.loadingView.isRunning = true;
            MainActivity.loadingView.startAnimation1();

        }

    }

    private class AsyncTaskPicrureLoading extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            int id = 1;
            URL pictureUrl = null;
            HttpURLConnection pictureConn = null;
            int responseCode2 = 0;
            try {
                pictureUrl = new URL(API.getPicture(id));
                pictureConn = (HttpURLConnection) pictureUrl.openConnection();
                pictureConn.setReadTimeout(100000 /* milliseconds */);
                pictureConn.setConnectTimeout(150000 /* milliseconds */);
                pictureConn.setRequestMethod("GET");
                pictureConn.setDoInput(true);
                pictureConn.connect();
                responseCode2 = pictureConn.getResponseCode();
            } catch (Exception e) {
               Log.d("asdf", "" + responseCode2);
            }

            InputStream pictureIS = null;
            try {
                pictureIS = pictureConn.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Bitmap bitmap = null;

            try {
                bitmap = BitmapFactory.decodeStream(pictureIS);
            } catch (Exception e) {
                bitmap = BitmapFactory.decodeResource(activity.getResources(),
                        R.drawable.default_picture);
            }

            pokemon.setPicture(bitmap);
            adapter.notifyDataSetChanged();
            if (pictureIS != null) {
                try {
                    pictureIS.close();
                } catch (IOException e) {
                    Log.d("asdf", e.getMessage());
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }


}
