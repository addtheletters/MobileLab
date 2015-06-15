package test.dhlanm.nonpermanence;

import android.app.Activity;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.List;

/**
 * Created by Ben on 6/11/2015.
 */
public class FirebaseInterfacer {

    final static String GRID_PATH = "grid";

    Firebase rootRef;

    public FirebaseInterfacer(String rootURL, Activity context){
        Firebase.setAndroidContext(context);
        rootRef = new Firebase(rootURL);
    }

    private String getGridRef(int row, int col){
        return GRID_PATH + "/" + row + "/" + col;
    }

    public void saveTile(Tile t){
        Firebase gridPosRef = rootRef.child(getGridRef(t.getPos().x, t.getPos().y));
        gridPosRef.push().setValue(Stroke.getFBStrokesObject(t.getStrokes()));
        /*Query qref = rootRef.child(getGridRef(1, 1)).orderByKey().limitToLast(1);
        qref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                System.out.println("retrieved tile " + dataSnapshot.toString());
                MainActivity.testSetStrokes(dataSnapshot);
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });*/
    }

    public void retrieveTile(int row, int col, int tileNum){
        Query qref = rootRef.child(getGridRef(row, col)).orderByKey().limitToLast(1);
        qref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                System.out.println("retrieved tile " + dataSnapshot.toString());
                MainActivity.testSetStrokes(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

}
