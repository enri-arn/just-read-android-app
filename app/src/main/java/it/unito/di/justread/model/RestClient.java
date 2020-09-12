package it.unito.di.justread.model;


import org.apache.http.impl.client.DefaultHttpClient;

public class RestClient {

    private static DefaultHttpClient instance;

    public static DefaultHttpClient getInstance(){
        if (instance == null){
            instance = new DefaultHttpClient();
        }
        return instance;
    }
}
