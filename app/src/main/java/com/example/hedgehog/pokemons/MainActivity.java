package com.example.hedgehog.pokemons;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Fade;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    public static final String NO_TYPES = "no types";
    static Activity thisActivity = null;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;

    FragmentManager fragmentManager;
    static PokemonDetailFragment detail;
    static MyListFragment listFragment;

    static FrameLayout flListContainer, flItemContainer;
    static LoadingView loadingView;
    static Button bLoad;
    static ListView mDrawerList;

    static AsyncTask<Integer,Void,Integer> at;
    static final int LIMIT = 18;

    static boolean isLoadingNow = false;
    static boolean isDataExists = false;
    static boolean isPort;

    static ArrayList<Pokemon> arrayList = new ArrayList<>();
    static ArrayList<Type> typesArray = new ArrayList<>();
    static ArrayAdapter<Type> adapter;
    static int currentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        thisActivity = this;

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,  R.string.app_name, R.string.app_name){
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(R.string.app_name);
            }
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(R.string.app_name);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        adapter = new MenuAdapter(this,R.layout.menu_item_layout, typesArray);

        if (typesArray.size() == 0) {
            typesArray.add(new Type(NO_TYPES));
        }
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(this);

        flListContainer = (FrameLayout) findViewById(R.id.flListContainer);
        flItemContainer = (FrameLayout) findViewById(R.id.flItemContainer);
        loadingView = (LoadingView) findViewById(R.id.animView);
        bLoad = (Button) findViewById(R.id.bLoad);
        flListContainer.setVisibility(View.GONE);
        if (listFragment == null) {
            listFragment = new MyListFragment();
        }
        if (detail == null) {
            detail = new PokemonDetailFragment();
        }
        fragmentManager = getFragmentManager();

        fragmentManager.beginTransaction().add(R.id.flListContainer, listFragment, "listFragment").commit();

        bLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                at = new MyAsyncTask();
                if (isNetworkExist(getApplicationContext()) && !isLoadingNow) {
                    at.execute(LIMIT);
                    loadingView.startAnimation1();
                    loadingView.setVisibility(View.VISIBLE);
                    loadingView.isRunning = true;
                    bLoad.setVisibility(View.GONE);
                    isDataExists = true;
                } else {
                    Toast.makeText(getApplicationContext(), "No access to a network! Try later", Toast.LENGTH_LONG).show();
                }
            }
        });
        if (flItemContainer == null){
            isPort = true;
        } else isPort = false;

        if (!isPort){
            fragmentManager.beginTransaction().add(R.id.flItemContainer,detail,"detail").commit();

        } else {
            fragmentManager.beginTransaction().add(R.id.flListContainer,detail,"detail").commit();
        }


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onBackPressed() {
        if (this.mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (isPort && detail.isVisible1) {
                detail.setVisibility(false);
                fragmentManager.beginTransaction().hide(detail).commit();
                fragmentManager.beginTransaction().show(listFragment).commit();

            } else {
                super.onBackPressed();
                typesArray.clear();
                arrayList.clear();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPort){
            detail.setVisibility(false);
            fragmentManager.beginTransaction().hide(detail).commit();
            fragmentManager.beginTransaction().show(listFragment).commit();
        }

        Fragment f = fragmentManager.findFragmentByTag("listFragment");
        if (f == null){
            fragmentManager.beginTransaction().add(R.id.flListContainer, listFragment, "listFragment").commit();
        }
        f = fragmentManager.findFragmentByTag("detail");
        if (f == null){
            if (!isPort){
                fragmentManager.beginTransaction().add(R.id.flItemContainer,detail,"detail").commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.flListContainer,detail,"detail").commit();
            }
        }
        listFragment.setNewArrayList(arrayList);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        fragmentManager.beginTransaction().remove(detail).commit();
        if (isDataExists) {
            outState.putInt("key", 1);
        }

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
         if (fragmentManager.findFragmentByTag("listFragment") == null) {
             fragmentManager.beginTransaction().add(R.id.flListContainer, listFragment, "listFragment").commit();
         }
         listFragment.setNewArrayList(arrayList);

        if (savedInstanceState.getInt("key", 0) == 1){
           bLoad.setVisibility(View.GONE);
            if (arrayList.size() > 1) {
              flListContainer.setVisibility(View.VISIBLE);
            }
            if (!isLoadingNow) {
                loadingView.setVisibility(View.GONE);
            } else {
                  loadingView.setVisibility(View.VISIBLE);
                  loadingView.isRunning = true;
                  loadingView.startAnimation1();
            }
            if (arrayList.size() != 0) {
                detail.setValues(arrayList.get(currentPosition));
            }
            fragmentManager.beginTransaction().show(listFragment).commit();
            if (isPort ){
                 detail.setVisibility(false);
                 fragmentManager.beginTransaction().hide(detail).commit();
            }

        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Type currentType = typesArray.get(position);
        if (currentType.nameOfType == NO_TYPES){
            for (Type t: typesArray){
                t.isChosenNow = false;
            }
            currentType.isChosenNow = false;
        } else {
            typesArray.get(0).isChosenNow = false;
            if (currentType.isChosenNow) {
                currentType.isChosenNow = false;
            } else {
                currentType.isChosenNow = true;
            }
        }
        adapter.notifyDataSetChanged();
        listFragment.setNewArrayList(arrayList);

    }

    static public class MyAsyncTask extends AsyncTask<Integer,Void,Integer> {

        @Override
        protected Integer doInBackground(Integer... params) {
            int limit = params [0];
            int tempOffset = arrayList.size();
            int resp = 1;
            Log.d("asdf", "limit: " + limit + "   arraySize:" + arrayList.size() + "    offset:" + tempOffset);

            isLoadingNow = true;
            String responce = null;
            InputStream is = null;
            String myurl = API.getPockemonList(limit,tempOffset);

            try {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(60000 /* milliseconds */);
                conn.setConnectTimeout(60000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();

                int responseCode = conn.getResponseCode();
                if (responseCode!=200){
                    Log.d("asdf", "" + responseCode);
                }

                is = conn.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();

                String read = br.readLine();
                while(read != null) {
                    sb.append(read);
                    read = br.readLine();
                }
                responce = sb.toString();
                isr.close();
                br.close();

                JSONObject jsonResponce = null;
                try {
                    jsonResponce = new JSONObject(responce);
                } catch (JSONException e) {
                    Log.d("asdf",e.getMessage());
                }

                JSONArray jsonObjects = jsonResponce.optJSONArray("objects");
                for (int i = 0; i < jsonObjects.length(); i++ ){
                    JSONObject object = jsonObjects.optJSONObject(i);
                    int id = object.optInt("pkdx_id");
                    String name = object.optString("name");
                    JSONArray jsonTypes = object.optJSONArray("types");
                    String [] types = new String[jsonTypes.length()];
                    for (int n = 0; n <jsonTypes.length(); n++){
                        JSONObject type = jsonTypes.optJSONObject(n);
                        types [n] = type.optString("name");
                    }
                    Pokemon.PokeBuilder builder = new Pokemon.PokeBuilder(id, name, types);

                    int attack = object.optInt("attack");
                    int defence = object.optInt("defense");
                    int hp = object.optInt("hp");
                    int spAttack = object.optInt("sp_atk");
                    int spDefence = object.optInt("sp_def");
                    int speed = object.optInt("speed");
                    int weight = object.optInt("weight");
                    JSONArray moves = object.optJSONArray("types");
                    int totalMoves = moves.length();
                    Pokemon pokemon = builder.setAttack(attack)
                            .setDefence(defence)
                            .setHP(hp)
                            .setSPAttack(spAttack)
                            .setSPDefence(spDefence)
                            .setSpeed(speed)
                            .setWeight(weight)
                            .setTotalMoves(totalMoves)
                            .build();

                    URL pictureUrl = new URL(API.getPicture(id));

                    HttpURLConnection pictureConn = (HttpURLConnection) pictureUrl.openConnection();
                    pictureConn.setReadTimeout(100000 );
                    pictureConn.setConnectTimeout(150000 );
                    pictureConn.setRequestMethod("GET");
                    pictureConn.setDoInput(true);
                    pictureConn.connect();
                    InputStream pictureIS = pictureConn.getInputStream();
                    Bitmap bitmap = null;

                    try {
                        bitmap = BitmapFactory.decodeStream(pictureIS);
                    } catch (Exception e) {
                        bitmap = BitmapFactory.decodeResource(thisActivity.getResources(),
                                R.drawable.default_picture);
                    }

                    pokemon.setPicture(bitmap);

                    arrayList.add(pokemon);

                    if (pictureIS != null) {
                        try {
                            pictureIS.close();
                        } catch (IOException e) {
                            Log.d("asdf", e.getMessage());
                        }
                    }

                    if (arrayList.size() > 100){
                        for (int r = 0; r < arrayList.size(); r++){
                            if (!arrayList.get(r).isVisibleNow){
                                arrayList.get(r).picture = null;
                            }
                        }
                     }
                }

            } catch (Exception e) {
                Log.d("asdf", "" + e.getMessage());
            }
            finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        Log.d("asdf", e.getMessage());
                    }
                }
            }
            return resp;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            loadingView.stopAnimation();
            loadingView.setVisibility(View.GONE);
            if (arrayList.size() > 0) {
                listFragment.setNewArrayList(arrayList);
                flListContainer.setVisibility(View.VISIBLE);
            }
            isLoadingNow = false;

            defineTypes();

            if (arrayList.size() == 0){
                isDataExists = false;
                bLoad.setVisibility(View.VISIBLE);
                Toast.makeText(thisActivity, "Try later", Toast.LENGTH_SHORT ).show();
            }
        }
    }

    public static void defineTypes(){
        for (Pokemon p: arrayList){
            for (Type t: p.types){
                Type temp = t;
                if (!typesArray.contains(temp)){
                    typesArray.add(temp);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    public static boolean isNetworkExist (Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean result = false;

        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!= null) {
            result = result || connectivityManager.getNetworkInfo(connectivityManager.TYPE_MOBILE).isConnected();
        } else {
            result = result || true;
        }
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!= null) {
            result = result || connectivityManager.getNetworkInfo(connectivityManager.TYPE_WIFI).isConnected();
        } else {
            result = result || true;
        }
        return result;

    }
}
