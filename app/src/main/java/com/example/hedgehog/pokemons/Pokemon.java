package com.example.hedgehog.pokemons;

import android.graphics.Bitmap;

/**
 * Created by hedgehog on 12.07.2016.
 */
public class Pokemon {

    int id;
    String name;
    String [] types;
    int attack;
    int defence;
    int hp;
    int spAttack;
    int spDefence;
    int speed;
    int weight;
    int totalMoves;
    Bitmap picture;

    private Pokemon(){}

    public static class PokeBuider {
        Pokemon pok = new Pokemon();

        public PokeBuider(int id, String name, String[] types){
            this.pok.id = id;
            this.pok.name = name;
            this.pok.types = types;
        }

        public PokeBuider setAttack(int attack){
            this.pok.attack = attack;
            return this;
        }
        public PokeBuider setDefence(int defence){
            this.pok.defence = defence;
            return this;
        }
        public PokeBuider setHP(int hp){
            this.pok.hp = hp;
            return this;
        }
        public PokeBuider setSPAttack(int spAttack){
            this.pok.spAttack = spAttack;
            return this;
        }
        public PokeBuider setSPDefence(int spDefence){
            this.pok.spDefence = spDefence;
            return this;
        }
        public PokeBuider setSpeed(int speed){
            this.pok.speed = speed;
            return this;
        }
        public PokeBuider setWeight(int weight){
            this.pok.weight = weight;
            return this;
        }
        public PokeBuider setTotalMoves(int totalMoves){
            this.pok.totalMoves = totalMoves;
            return this;
        }
        public PokeBuider setPicture(Bitmap picture){
            this.pok.picture = picture;
            return this;
        }

        public Pokemon build(){
            return this.pok;
        }


    }
}
