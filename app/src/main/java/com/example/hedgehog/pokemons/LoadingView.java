package com.example.hedgehog.pokemons;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;


/**
 * Created by hedgehog on 15.07.2016.
 */
public class LoadingView extends RelativeLayout {
    ImageView imageView1,imageView2,imageView3,imageView4,imageView5;
    Context context;
    Animation loadingAnimation1, loadingAnimation2, loadingAnimation3, loadingAnimation4, loadingAnimation5;
    public boolean isRunning = false;




    public void stopAnimation() {
        isRunning = false;
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
        loadingAnimation1 = AnimationUtils.loadAnimation(getContext(), R.anim.loading_anim);
        loadingAnimation2 = AnimationUtils.loadAnimation(getContext(), R.anim.loading2);
        loadingAnimation3 = AnimationUtils.loadAnimation(getContext(), R.anim.loading3);
        loadingAnimation4 = AnimationUtils.loadAnimation(getContext(), R.anim.loading4);
        loadingAnimation5 = AnimationUtils.loadAnimation(getContext(), R.anim.loading5);
        loadingAnimation5.setFillAfter(true);
        loadingAnimation4.setFillAfter(true);
        loadingAnimation3.setFillAfter(true);
        loadingAnimation2.setFillAfter(true);
        loadingAnimation1.setFillAfter(true);

        isRunning = true;



    }

    public void startAnimation1() {
        imageView5.startAnimation(loadingAnimation1);
        imageView4.startAnimation(loadingAnimation2);
        imageView1.startAnimation(loadingAnimation3);
        imageView2.startAnimation(loadingAnimation4);
        imageView3.startAnimation(loadingAnimation5);
    }







}




