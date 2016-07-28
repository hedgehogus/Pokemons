package com.example.hedgehog.pokemons;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by hedgehog on 17.07.2016.
 */
public class PokemonDetailFragment extends Fragment {
    boolean isVisible1 = true;
    View rootView;
    ImageView ivItem;
    TextView tvNameItem;
    TextView tvTypeValue;
    TextView tvAttackValue;
    TextView tvDefenceValue;
    TextView tvHPValue;
    TextView tvSPAttackValue;
    TextView tvSPDefenceValue;
    TextView tvSpeedValue;
    TextView tvWeightValue;
    TextView tvTotalMovesValue;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.pokemon_detail_layout, null);
        ivItem = (ImageView) rootView.findViewById(R.id.ivItem);
        tvNameItem = (TextView) rootView.findViewById(R.id.tvNameItem);
        tvTypeValue = (TextView) rootView.findViewById(R.id.tvTypeValue);
        tvAttackValue = (TextView) rootView.findViewById(R.id.tvAttackValue);
        tvDefenceValue = (TextView) rootView.findViewById(R.id.tvDefenceValue);
        tvHPValue = (TextView) rootView.findViewById(R.id.tvHPValue);
        tvSPAttackValue = (TextView) rootView.findViewById(R.id.tvSPAttackValue);
        tvSPDefenceValue = (TextView) rootView.findViewById(R.id.tvSPDefenceValue);
        tvSpeedValue = (TextView) rootView.findViewById(R.id.tvSpeedValue);
        tvWeightValue = (TextView) rootView.findViewById(R.id.tvWeightValue);
        tvTotalMovesValue = (TextView) rootView.findViewById(R.id.tvTotalMovesValue);
        return rootView;
    }

    public void setValues(Pokemon pokemon){
        ivItem.setImageBitmap(pokemon.picture);
        tvNameItem.setText(pokemon.name + " #" + pokemon.id);

        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < pokemon.types.length; i++){
            sb.append(pokemon.types[i]);
            if (i !=pokemon.types.length-1){
                sb.append(", ");
            }
        }
        tvTypeValue.setText(sb.toString());

        tvAttackValue.setText(Integer.toString(pokemon.attack));
        tvDefenceValue.setText(Integer.toString(pokemon.defence));
        tvHPValue.setText(Integer.toString(pokemon.hp));
        tvSPAttackValue.setText(Integer.toString(pokemon.spAttack));
        tvSPDefenceValue.setText(Integer.toString(pokemon.spDefence));
        tvSpeedValue.setText(Integer.toString(pokemon.speed));
        tvWeightValue.setText(Integer.toString(pokemon.weight));
        tvTotalMovesValue.setText(Integer.toString(pokemon.totalMoves));
    }

    public void setVisibility(boolean b){

        if (b){
            rootView.setVisibility(View.VISIBLE);
            isVisible1 = true;
        } else {
            rootView.setVisibility(View.INVISIBLE);
            isVisible1 = false;
        }
    }
}
