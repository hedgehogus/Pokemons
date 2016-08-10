package com.example.hedgehog.pokemons;

import android.graphics.Bitmap;

/**
 * Created by hedgehog on 12.07.2016.
 */
public class Pokemon {

    final int id;
    final String name;
    final Type [] types;
    final int attack;
    final int defence;
    final int hp;
    final int spAttack;
    final int spDefence;
    final int speed;
    final int weight;
    final int totalMoves;
    Bitmap picture;
    boolean isVisibleNow = true;

    private Pokemon(int id, String name, Type[] types, int attack, int defence, int hp, int spAttack, int spDefence, int speed, int weight, int totalMoves){
        this.id = id;
        this.name = name;
        this.types = types;
        this.attack = attack;
        this.defence = defence;
        this.hp = hp;
        this.spAttack = spAttack;
        this.spDefence = spDefence;
        this.speed = speed;
        this.weight = weight;
        this.totalMoves = totalMoves;
    }

    public void setPicture(Bitmap picture){
        this.picture = picture;

    }

    public static class PokeBuilder {
        int builderId;
        String builderName;
        Type [] builderTypes;
        int attack = 0;
        int defence = 0;
        int hp = 0;
        int spAttack = 0;
        int spDefence = 0;
        int speed = 0;
        int weight = 0;
        int totalMoves = 0;




        public PokeBuilder(int id, String name, String[] typesStr){
            this.builderId = id;
            this.builderName = name;
            Type [] types = new Type[typesStr.length];
            for (int i = 0; i < typesStr.length; i++){
                types[i] = new Type(typesStr[i]);
            }
            this.builderTypes = types;
        }

        public PokeBuilder setAttack(int attack){
            this.attack = attack;
            return this;
        }
        public PokeBuilder setDefence(int defence){
            this.defence = defence;
            return this;
        }
        public PokeBuilder setHP(int hp){
            this.hp = hp;
            return this;
        }
        public PokeBuilder setSPAttack(int spAttack){
            this.spAttack = spAttack;
            return this;
        }
        public PokeBuilder setSPDefence(int spDefence){
            this.spDefence = spDefence;
            return this;
        }
        public PokeBuilder setSpeed(int speed){
            this.speed = speed;
            return this;
        }
        public PokeBuilder setWeight(int weight){
            this.weight = weight;
            return this;
        }
        public PokeBuilder setTotalMoves(int totalMoves){
            this.totalMoves = totalMoves;
            return this;
        }


        public Pokemon build(){
            Pokemon pok = new Pokemon(builderId, builderName, builderTypes, attack, defence, hp, spAttack, spDefence, speed, weight, totalMoves);
            return pok;
        }


    }
}
