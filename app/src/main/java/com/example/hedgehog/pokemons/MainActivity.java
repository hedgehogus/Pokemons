package com.example.hedgehog.pokemons;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    FrameLayout flListContainer, flItemContainer;
    boolean isPort;
    ListFragment listFragment;
    FragmentManager fragmentManager;
    static final int LIMIT = 18;
    static int offset = 0;


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

    static public class MyAsyncTask extends AsyncTask<Integer,Void,Integer> {

        @Override
        protected Integer doInBackground(Integer... params) {
            int limit = params [0];
            int offset = params [2];

            String responce = null;
            InputStream is = null;
            String myurl = API.getPockemonList(limit,offset);

            try {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
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
                    builder.setAttack(attack);

                    int defence = object.optInt("defense");
                    builder.setDefence(defence);

                    int hp = object.optInt("hp");
                    builder.setHP(hp);

                    int spAttack = object.optInt("sp_atk");
                    builder.setSPAttack(spAttack);

                    int spDefence = object.optInt("sp_def");
                    builder.setSPDefence(spDefence);

                    int speed = object.optInt("speed");
                    builder.setSpeed(speed);

                    int weight = object.optInt("weight");
                    builder.setWeight(weight);

                    JSONArray moves = object.optJSONArray("types");
                    int totalMoves = moves.length();
                    builder.setTotalMoves(totalMoves);

                    Pokemon pokemon = builder.build();



                }



            } catch (Exception e) {}


            return null;
        }
    }
}
