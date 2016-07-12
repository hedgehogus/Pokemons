package com.example.hedgehog.pokemons;

/**
 * Created by hedgehog on 12.07.2016.
 */
public class API {



    private static final String K_BASE_URL  =          "https://pokeapi.co/";
    private static final String API_GET_LIST =         "api/v1/pokemon/";
    private static final String API_GET_PICTURE =      "/media/img/";



    public static String getPockemonList(int limit, int offset) {

        String urlStr = K_BASE_URL + API_GET_LIST + "?limit=" + limit + "&offset=" + offset;
        return urlStr;
    }

    public static String getPicture(int id ) {
        String urlStr = K_BASE_URL + API_GET_PICTURE + id + ".png";

        return urlStr;

    }

}
