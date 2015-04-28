package com.atl.goodresult;

import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

/**
 * Created by Ben on 4/27/2015.
 */
public class RemoteData {

    private static final int TIMEOUT = 30000;

    // returns a connection to the url givenx
    public static HttpURLConnection getConnection(String url){
        System.out.println("URL: "+url);
        HttpURLConnection hcon = null;
        try {
            hcon = (HttpURLConnection)new URL(url).openConnection();
            hcon.setReadTimeout(TIMEOUT); // Timeout at 30 seconds
            hcon.setRequestProperty("User-Agent", "GoodResult_1");
        } catch (MalformedURLException e) {
            Log.e("getConnection()",
                    "Invalid URL: "+e.toString());
        } catch (IOException e) {
            Log.e("getConnection()",
                    "Could not connect: "+e.toString());
        }
        return hcon;
    }

    // takes what's at a url and returns as string
    public static String readContents(String url){
        byte[] cacheread = Cacher.read(url);
        String cached = null;
        if(cacheread != null) {
            cached = new String(cacheread);
            cacheread = null;
        }
        if(cached!=null) {
            Log.d("MSG","Using cache for "+url);
            return cached;
        }

        HttpURLConnection hcon = getConnection(url);
        if(hcon == null){
            return null;
        }
        try{
            StringBuffer sb = new StringBuffer(8192);
            String tmp = "";
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            hcon.getInputStream()
                    )
            );
            while( (tmp = br.readLine()) != null)
                sb.append(tmp).append("\n");
            br.close();

            Cacher.write(url, sb.toString());

            return sb.toString();
        }catch(IOException e){
            Log.d("READ FAILED", e.toString());
            return null;
        }
    }

}
