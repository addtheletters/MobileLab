package com.atl.goodresult;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class PostsFragment extends Fragment {ListView postsList;
    ArrayAdapter<Post> adapter;
    Handler handler;

    String subreddit;
    List<Post> posts;
    PostRetriever postRetriever;

    ProgressBar spinner;

    TextView noContentLabel;

    public PostsFragment(){
        handler=new Handler();
        posts=new ArrayList<Post>();
    }

    public static Fragment newInstance(String subreddit){
        PostsFragment pf=new PostsFragment();
        pf.subreddit=subreddit;
        pf.postRetriever=new PostRetriever(pf.subreddit);
        return pf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_posts
                , container
                , false);
        postsList   = (ListView)v.findViewById(R.id.posts_list);
        spinner     = (ProgressBar)v.findViewById(R.id.posts_load_progress);
        spinner.setVisibility(View.VISIBLE);
        noContentLabel = (TextView)v.findViewById(R.id.no_content_label);
        noContentLabel.setEnabled(false);
        noContentLabel.setVisibility(View.GONE);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
    }

    private void initialize(){
        if(posts.size()==0){
            // thread for network stuff
            new Thread(){
                public void run(){
                    posts.addAll(postRetriever.fetchPosts());
                    // adapter to work with UI
                    handler.post(new Runnable(){
                        public void run(){
                            createAdapter();
                        }
                    });
                }
            }.start();
        }else{
            createAdapter();
        }
    }

    private void createAdapter(){
        // if we're no longer on the activity thats bad
        if( getActivity() == null ) return;

        spinner.setVisibility(View.GONE);
        if(posts.size() == 0){
            noContentLabel.setVisibility(View.VISIBLE);
        }
        else {
            adapter = new ArrayAdapter<Post>(getActivity()
                    , R.layout.post_item
                    , posts) {
                @Override
                public View getView(int position,
                                    View convertView,
                                    ViewGroup parent) {

                    if (convertView == null) {
                        convertView = getActivity()
                                .getLayoutInflater()
                                .inflate(R.layout.post_item, null);
                    }

                    TextView postTitle;
                    postTitle = (TextView) convertView
                            .findViewById(R.id.post_title);

                    TextView postDetails;
                    postDetails = (TextView) convertView
                            .findViewById(R.id.post_details);

                    TextView postScore;
                    postScore = (TextView) convertView
                            .findViewById(R.id.post_score);

                    postTitle.setText(posts.get(position).title);
                    postDetails.setText(posts.get(position).getDetails());
                    postScore.setText(posts.get(position).getScore());
                    return convertView;
                }
            };
        }
        postsList.setAdapter(adapter);
        System.out.println("Loaded views for subreddit " + subreddit);
    }

}
