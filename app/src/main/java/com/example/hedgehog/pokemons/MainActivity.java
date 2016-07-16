package com.example.hedgehog.pokemons;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

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

public class MainActivity extends AppCompatActivity {
    static FrameLayout flListContainer, flItemContainer;
    static boolean isPort;
    static MyListFragment listFragment;
    FragmentManager fragmentManager;
    static final int LIMIT = 18;
    static int offset = 0;
    static LoadingView loadingView;
    static AsyncTask<Integer,Void,Integer> at;
    static ArrayList<Pokemon> arrayList= new ArrayList<>();
    static boolean isLoadingNow = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flListContainer = (FrameLayout) findViewById(R.id.flListContainer);
        flItemContainer = (FrameLayout) findViewById(R.id.flItemContainer);
        loadingView = (LoadingView) findViewById(R.id.animView);

        if (flItemContainer == null){
            isPort = true;
        } else isPort = false;

        //Toast.makeText(this, (isPort? "port": "land"), Toast.LENGTH_LONG).show();

        flListContainer.setVisibility(View.GONE);

        listFragment = new MyListFragment();
        fragmentManager = getFragmentManager();



        at = new MyAsyncTask();

        if (isNetworkExist(this) && !isLoadingNow){
           at.execute(LIMIT, offset);
           loadingView.startAnimation1();

        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.flListContainer, listFragment, "listFragment");
        fragmentTransaction.commit();

    }





    static public class MyAsyncTask extends AsyncTask<Integer,Void,Integer> {

        @Override
        protected Integer doInBackground(Integer... params) {
            int limit = params [0];
            int tempOffset = params [1];
            offset = offset + LIMIT;
            isLoadingNow = true;
            Log.d("asdf", "" + limit + " " + tempOffset);

            String responce = null;
            InputStream is = null;
            String myurl = API.getPockemonList(limit,tempOffset);

            try {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(100000 /* milliseconds */);
                conn.setConnectTimeout(150000 /* milliseconds */);
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
                    pictureConn.setReadTimeout(100000 /* milliseconds */);
                    pictureConn.setConnectTimeout(150000 /* milliseconds */);
                    pictureConn.setRequestMethod("GET");
                    pictureConn.setDoInput(true);
                    pictureConn.connect();
                    int responseCode2 = conn.getResponseCode();

                    InputStream pictureIS = pictureConn.getInputStream();

                    Bitmap bitmap = null;

                       bitmap = BitmapFactory.decodeStream(pictureIS);

                    pokemon.setPicture(bitmap);

                    arrayList.add(pokemon);

                    if (pictureIS != null) {
                        try {
                            pictureIS.close();
                        } catch (IOException e) {
                            Log.d("asdf", e.getMessage());
                        }
                    }
                }

            } catch (Exception e) {
                Log.d("asdf", e.getMessage());
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



            return 1;
        }

        @Override
        protected void onPostExecute(Integer integer) {
           // super.onPostExecute(integer);
            listFragment.setNewArrayList(arrayList);
            isLoadingNow = false;
            loadingView.stopAnimation();
            loadingView.setVisibility(View.GONE);
            flListContainer.setVisibility(View.VISIBLE);


        }
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
