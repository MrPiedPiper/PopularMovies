package com.fancystachestudios.popularmovies.popularmovies.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by napuk on 5/20/2018.
 */

public class NetworkUtils {

    public static URI buildUriFromUrl(URL url){
        URI returnUri = null;
        try{
            returnUri = url.toURI();
            } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return returnUri;
    }

    public static JSONObject stringToJson (String string){
        JSONObject returnJsonObject = null;
        try{
            returnJsonObject = new JSONObject(string);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnJsonObject;
    }

    public static JSONObject getJsonFromUrl(URL url) throws IOException {
        JSONObject returnJson;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();

        returnJson = stringToJson(response.body().string());

        return returnJson;
    }
}
