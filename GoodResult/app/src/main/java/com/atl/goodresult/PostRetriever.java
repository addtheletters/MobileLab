package com.atl.goodresult;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben on 4/27/2015.
 */
public class PostRetriever {
    private static final String SUBREDDIT_NAME_PLACEHOLDER = "SUBREDDIT_NAME";
    private static final String AFTER_PLACEHOLDER = "AFTER";
    private final String URL_TEMPLATE = "http://www.reddit.com/r/" + SUBREDDIT_NAME_PLACEHOLDER + "/"
            + ".json"
            + "?after=" + AFTER_PLACEHOLDER;

    String subreddit;
    String url;
    String after;

    public PostRetriever(String subred){
        subreddit = subred;
        after = "";
        generateURL();
    }

    private void generateURL(){
        url = URL_TEMPLATE.replace(SUBREDDIT_NAME_PLACEHOLDER, subreddit);
        url = url.replace(AFTER_PLACEHOLDER, after);
    }

    List<Post> fetchPosts(){
        String raw = RemoteData.readContents(url);
        List<Post> list = new ArrayList<Post>();
        try {
            JSONObject data = new JSONObject(raw).getJSONObject("data");
            JSONArray children = data.getJSONArray("children");

            //Using this property we can fetch the next set of
            //posts from the same subreddit
            after=data.getString("after");

            for(int i = 0; i < children.length(); i++){
                JSONObject cur = children.getJSONObject(i).getJSONObject("data");
                System.out.println("JSON THING:---------");
                System.out.println(cur.toString(4));
                System.out.println("--------END JSON");
                Post p = new Post();
                p.title = cur.optString("title");
                p.url   = cur.optString("url");
                p.numComments   = cur.optInt("num_comments");
                p.points        = cur.optInt("score");
                p.author        = cur.optString("author");
                p.subreddit     = cur.optString("subreddit");
                p.permalink     = cur.optString("permalink");
                p.domain        = cur.optString("domain");
                p.id            = cur.optString("id");

                p.thumbnail     = cur.optString("thumbnail");
                p.upVotes       = cur.optInt("ups");
                p.downVotes     = cur.optInt("downs");

                p.is_self       = cur.optBoolean("is_self");
                if(p.is_self){
                    p.post_contents = cur.optString("selftext");
                }
                else{
                    p.post_contents = p.url;
                    //p.post_contents = cur.optString("media");
                }

                if( p.title != null && p.isNSFW() == false )
                    list.add(p);
            }
        }catch(Exception e){
            Log.e("fetchPosts()", e.toString());
        }
        return list;
    }

    List<Post> fetchMorePosts(){
        generateURL();
        return fetchPosts();
    }

}
