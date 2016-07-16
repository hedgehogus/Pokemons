package com.example.hedgehog.pokemons;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by hedgehog on 16.07.2016.
 */
public class AnimationImageView extends ImageView {
    public AnimationImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAnimationEnd() {
        super.onAnimationEnd();
        MainActivity.loadingView.startAnimation1();
    }
}
