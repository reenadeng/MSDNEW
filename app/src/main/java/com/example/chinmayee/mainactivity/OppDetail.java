package com.example.chinmayee.mainactivity;

//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class OppDetail extends AppCompatActivity {
    ImageView image;
    //RoundImage roundedImage;
    private Firebase mFBRef ;
    TextView mText1;
    TextView mText2;
    TextView mText3;
    TextView mText4;
    TextView mText5;
    Integer oppID = 1002;
    ArrayList<Integer> imgs = new ArrayList<Integer>();
    ArrayList<String> dates = new ArrayList<String>();
    ArrayList<String> names = new ArrayList<String>();
    ArrayList<String> locs = new ArrayList<String>();
    ArrayList<Integer> pts = new ArrayList<Integer>();
    int[] icons= {R.drawable.hi, R.drawable.opp1, R.drawable.star,R.drawable.hi, R.drawable.opp1, R.drawable.star};
    LinearLayout scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opp_detail);
        Firebase.setAndroidContext(this);
        mFBRef = new Firebase("https://flickering-inferno-293.firebaseio.com/");
        //imageView1 = (ImageView) findViewById(R.id.imageView1);
        //Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.profimg2);
        //roundedImage = new RoundImage(bm);
        // imageView1.setImageDrawable(roundedImage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mText1 = (TextView) findViewById(R.id.oppDate);
        mText2 = (TextView) findViewById(R.id.oppName);
        mText3 = (TextView) findViewById(R.id.oppDes);
        mText4 = (TextView) findViewById(R.id.oppLoc);
        image = (ImageView) findViewById(R.id.image);
        mText5 = (TextView) findViewById(R.id.oppContact);

        // Attach an listener to read the data at our posts reference
        String oppFBID = "opportunity/" + oppID.toString();
        mFBRef.child(oppFBID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot postSnapshot) {

                String picSrc = (String) postSnapshot.child("pic").getValue();
                int id = getApplicationContext().getResources().getIdentifier(picSrc, "drawable", getApplicationContext().getPackageName());
                System.out.println("################ First" + id);
                image.setImageResource(id);

                mText1.setText((String) postSnapshot.child("start date").getValue());

                mText2.setText((String) postSnapshot.child("name").getValue());

                mText3.setText((String) postSnapshot.child("longDesc").getValue());

                mText4.setText((String) postSnapshot.child("location").getValue());

                mText5.setText((String) postSnapshot.child("contact").getValue());

                // Missing attendees in db

                // Generate the recommendation opportunity list
                rcmListGenerator(mFBRef);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

    }

    // Helper function. Must call it this way, else Firebase will complain

    private void rcmListGenerator(Firebase mFBRef){
        mFBRef.child("opportunity").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot msgSnapshot : dataSnapshot.getChildren()) {
                    //if (msgSnapshot.child("oppid").getValue() != oppID.toString()) {
                    //names.add(msgSnapshot.child("start date").getValue().toString());
                    String img = (String) msgSnapshot.child("pic").getValue();
                    int mid = getApplicationContext().getResources().getIdentifier(img, "drawable", getApplicationContext().getPackageName());
                    imgs.add(mid);
                    System.out.println("###########" + mid);

                    dates.add(msgSnapshot.child("start date").getValue().toString());

                    names.add(msgSnapshot.child("name").getValue().toString());

                    locs.add(msgSnapshot.child("location").getValue().toString());

                    Integer[] dimScore = new Integer[5];
                    int sumScore=0;
                    for (int j=1; j<6; j++) {
                        dimScore[j - 1] = Integer.parseInt((String) msgSnapshot.child("score").child("d" + j).getValue());
                        sumScore += dimScore[j - 1];
                    }
                    pts.add(sumScore);
                    //}

                }

                scrollView = (LinearLayout)findViewById(R.id.scrollView);
                for (int i = 0; i<dates.size(); i++){
                    //System.out.println("########### OK ###############");
                    LayoutInflater li = getLayoutInflater();
                    View v = li.inflate(R.layout.item, null);
                    ImageView imageView = (ImageView)v.findViewById(R.id.imgIcon);
                    TextView tDate = (TextView)v.findViewById(R.id.txtDate);
                    TextView tName = (TextView)v.findViewById(R.id.txtTitle);
                    TextView tLoc = (TextView)v.findViewById(R.id.txtLoc);
                    TextView tPTS = (TextView)v.findViewById(R.id.txtPTS);
                    imageView.setImageResource(imgs.get(i));
                    //imageView.setImageResource(icons[i]);
                    tDate.setText(dates.get(i));
                    tName.setText(names.get(i));
                    tLoc.setText(locs.get(i));
                    tPTS.setText(pts.get(i).toString());


            //final int finalI = i;
            /*v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imgResult.setImageResource(icons[finalI]);
                }
            });*/

            scrollView.addView(v);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void addMapFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        MapFragment fragment = new MapFragment();
        transaction.add(R.id.mapView, fragment);
        transaction.commit();
    }



}
