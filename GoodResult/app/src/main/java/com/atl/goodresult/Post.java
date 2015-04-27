package com.atl.goodresult;

/**
 * Created by Ben on 4/27/2015.
 */
public class Post {

    String subreddit;
    String title;
    String author;
    int points;
    int numComments;
    String permalink;
    String url;
    String domain;
    String id;

    String getDetails(){
        String details = author
                + " posted and got "
                + points
                + " points and "
                + numComments
                + " replies.";
        return details;
    }

    String getTitle(){
        return title;
    }

    String getScore(){
        return Integer.toString(points);
    }
}