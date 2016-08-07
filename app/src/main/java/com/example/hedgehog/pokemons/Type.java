package com.example.hedgehog.pokemons;

/**
 * Created by hedgehog on 07.08.2016.
 */
public class Type {
    public boolean isChosenNow = false;
    String nameOfType;

    public Type (String nameOfType){
        this.nameOfType = nameOfType;
    }

    @Override
    public boolean equals(Object other){
        if (this == other) return true;
        if (!(other instanceof Type)) return false;
        final Type that = (Type) other;
        return this.nameOfType.equals(that.nameOfType);
    }

    public int hashCode(){
        return this.nameOfType.hashCode();
    }
}
