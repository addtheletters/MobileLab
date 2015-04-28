package com.atl.goodresult;

import android.os.Environment;
import android.util.Log;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Date;

/**
 * Created by Ben on 4/28/2015.
 */
public class Cacher {
    private static String directory = "/Android/data/com.atl.GoodResult/cache/";
    static {
        if(Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED)){
            directory = Environment.getExternalStorageDirectory() + directory;
            File f = new File(directory);
            f.mkdirs();
        }
    }

    static public String cacheIdentifier(String url){
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(url.getBytes());
            byte[] b        = digest.digest();
            BigInteger bi   = new BigInteger(b);
            return "mycache_" + bi.toString(16) + ".cac";
        } catch (Exception e) {
            Log.d("ERROR", e.toString());
            return null;
        }
    }

    private static boolean needsRefresh(long time){
        long now = new Date().getTime();
        long diff = now - time;
        // 5 minutes old is too old
        if( diff > 1000*60*5 ) {
            return true;
        }
        return false;
    }

    public static String urlDirectory(String url){
        return directory + "/" + cacheIdentifier(url);
    }

    public static byte[] read(String url){
        try{
            String file = urlDirectory(url);
            File f = new File(file);
            if(!f.exists() || f.length() < 1){
                return null;
            }
            if(f.exists() && needsRefresh(f.lastModified())){
                // if is too old, delete old information
                f.delete();
            }
            byte data[] = new byte[(int)f.length()];
            DataInputStream fis=new DataInputStream(
                    new FileInputStream(f));
            fis.readFully(data);
            fis.close();
            return data;
        }catch(Exception e) { return null; }
    }

    public static void write(String url, String data){
        try{
            String file     = urlDirectory(url);
            PrintWriter pw  = new PrintWriter(new FileWriter(file));
            pw.print(data);
            pw.close();
        } catch(Exception e) {
            System.out.println("failed write " + e);
        }
    }
}
