package com.atl.goodresult;

/**
 * Created by Ben on 6/1/2015.
 */
public class CommentRetriever {
    private static final String SUBREDDIT_NAME_PLACEHOLDER = "SUBREDDIT_NAME";
    private static final String COMMENT_ID_PLACEHOLDER = "COMMENT_ID";

    private final String URL_TEMPLATE = "http://www.reddit.com/r/" + SUBREDDIT_NAME_PLACEHOLDER
            + "/comments/" + COMMENT_ID_PLACEHOLDER
            + ".json";

    String subreddit;
    String commentID;

    public CommentRetriever(String subred, String comment){
        subreddit = subred;
        commentID = comment;
    }



}
