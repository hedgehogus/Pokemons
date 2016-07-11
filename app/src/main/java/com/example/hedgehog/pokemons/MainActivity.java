package com.example.hedgehog.pokemons;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    FrameLayout flListContainer, flItemContainer;
    boolean isPort;
    ListFragment listFragment;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flListContainer = (FrameLayout) findViewById(R.id.flListContainer);
        flItemContainer = (FrameLayout) findViewById(R.id.flItemContainer);

        if (flItemContainer == null){
            isPort = true;
        } else isPort = false;

        //Toast.makeText(this, (isPort? "port": "land"), Toast.LENGTH_LONG).show();

        listFragment = new ListFragment();
        fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.flListContainer, listFragment, "listFragment");
        fragmentTransaction.commit();


    }
}
