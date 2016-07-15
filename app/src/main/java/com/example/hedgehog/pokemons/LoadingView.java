package com.example.hedgehog.pokemons;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.os.AsyncTask;

/**
 * Created by hedgehog on 15.07.2016.
 */
public class LoadingView extends RelativeLayout {
    ImageView imageView1,imageView2,imageView3,imageView4,imageView5;
    Context context;
    Animation loadingAnimation1, loadingAnimation2, loadingAnimation3, loadingAnimation4, loadingAnimation5;

    public LoadingView(Context context) {
        super(context);

        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout rootView = (RelativeLayout) inflater.inflate(R.layout.animation_layout, this, true);
        imageView1 = (ImageView) rootView.findViewById(R.id.imageView1);
        imageView2 = (ImageView) rootView.findViewById(R.id.imageView2);
        imageView3 = (ImageView) rootView.findViewById(R.id.imageView3);
        imageView4 = (ImageView) rootView.findViewById(R.id.imageView4);
        imageView5 = (ImageView) rootView.findViewById(R.id.imageView5);
        imageView1.setVisibility(INVISIBLE);
        imageView2.setVisibility(INVISIBLE);
        imageView3.setVisibility(INVISIBLE);
        imageView4.setVisibility(INVISIBLE);
        imageView5.setVisibility(INVISIBLE);

    }

    public LoadingView( Context context, AttributeSet attr){
        super(context, attr);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout rootView = (RelativeLayout) inflater.inflate(R.layout.animation_layout, this, true);
        imageView1 = (ImageView) rootView.findViewById(R.id.imageView1);
        imageView2 = (ImageView) rootView.findViewById(R.id.imageView2);
        imageView3 = (ImageView) rootView.findViewById(R.id.imageView3);
        imageView4 = (ImageView) rootView.findViewById(R.id.imageView4);
        imageView5 = (ImageView) rootView.findViewById(R.id.imageView5);
        imageView1.setVisibility(INVISIBLE);
        imageView2.setVisibility(INVISIBLE);
        imageView3.setVisibility(INVISIBLE);
        imageView4.setVisibility(INVISIBLE);
        imageView5.setVisibility(INVISIBLE);


    }

    public void startAnimation() {
       
        loadingAnimation1 = AnimationUtils.loadAnimation(getContext(), R.anim.loading_anim);
        loadingAnimation2 = AnimationUtils.loadAnimation(getContext(), R.anim.loading_anim);
        loadingAnimation3 = AnimationUtils.loadAnimation(getContext(), R.anim.loading_anim);
        loadingAnimation4 = AnimationUtils.loadAnimation(getContext(), R.anim.loading_anim);
        loadingAnimation5 = AnimationUtils.loadAnimation(getContext(), R.anim.loading_anim);

        AsyncTask<Void,Integer,Void> at= new AnimaAsyncTask();
        at.execute();



    }

    private class AnimaAsyncTask extends AsyncTask<Void, Integer, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i = 1; i < 6; i ++){
               try {
                   Thread.sleep(200);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
               publishProgress(i);
           }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            switch (values[0]){
                case 1:
                    imageView5.setVisibility(VISIBLE);
                    imageView5.startAnimation(loadingAnimation4);
                    break;
                case 2:
                    imageView4.setVisibility(VISIBLE);
                    imageView4.startAnimation(loadingAnimation5);
                    break;
                case 3:
                    imageView1.setVisibility(VISIBLE);
                    imageView1.startAnimation(loadingAnimation1);
                    break;
                case 4:
                    imageView2.setVisibility(VISIBLE);
                    imageView2.startAnimation(loadingAnimation2);
                    break;

                case 5:
                    imageView3.setVisibility(VISIBLE);
                    imageView3.startAnimation(loadingAnimation3);
                    break;
            }
        }
    }



}
