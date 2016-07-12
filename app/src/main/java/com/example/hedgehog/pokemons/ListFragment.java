package com.example.hedgehog.pokemons;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;


/**
 * Created by hedgehog on 12.07.2016.
 */
public class ListFragment extends Fragment {

    GridView gridView;
    Activity activity;
    static final int NUM_COLUMNS = 3;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, null);
        gridView = (GridView) rootView.findViewById(R.id.gridView);
        gridView.setNumColumns(NUM_COLUMNS);
        return rootView;
    }


}
