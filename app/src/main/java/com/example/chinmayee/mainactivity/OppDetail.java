package com.example.chinmayee.mainactivity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class OppDetail extends AppCompatActivity {
    ImageView image;
    private Firebase mFBRef ;
    TextView mText1;
    TextView mText2;
    TextView mText3;
    TextView mText4;
    TextView mText5;
    Button mButton;
    Integer oppID = 1006;
    private ArrayList<Integer> imgs = new ArrayList<Integer>();
    private ArrayList<String> dates = new ArrayList<String>();
    private ArrayList<String> names = new ArrayList<String>();
    private ArrayList<String> locs = new ArrayList<String>();
    private ArrayList<Integer> pts = new ArrayList<Integer>();
    private LinearLayout scrollView;
    private Boolean isBefore = false;
    private Boolean cBtnFlag = false;
    private String edate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opp_detail);
        Firebase.setAndroidContext(this);
        mFBRef = new Firebase("https://flickering-inferno-293.firebaseio.com/");
        mText1 = (TextView) findViewById(R.id.oppDate);
        mText2 = (TextView) findViewById(R.id.oppName);
        mText3 = (TextView) findViewById(R.id.oppDes);
        mText4 = (TextView) findViewById(R.id.oppLoc);
        image = (ImageView) findViewById(R.id.image);
        mText5 = (TextView) findViewById(R.id.oppContact);
        mButton = (Button) findViewById(R.id.mButton);

        // Attach an listener to read the data at our posts reference
        //!!! Need a way to get the opp ID
        String oppFBID = "opportunity/" + oppID.toString();
        mFBRef.child(oppFBID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot postSnapshot) {

                String picSrc = (String) postSnapshot.child("pic").getValue();
                int id = getApplicationContext().getResources().getIdentifier(picSrc,
                        "drawable", getApplicationContext().getPackageName());
                image.setImageResource(id);

                mText1.setText((String) postSnapshot.child("start date").getValue());

                mText2.setText((String) postSnapshot.child("name").getValue());

                mText3.setText((String) postSnapshot.child("longDesc").getValue());

                mText4.setText((String) postSnapshot.child("location").getValue());

                mText5.setText((String) postSnapshot.child("contact").getValue());

                edate = (String) postSnapshot.child("end date").getValue();

                // Set the button
                mButton.setTransformationMethod(null);
                mButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy, h:mm a");
                        String curT = new SimpleDateFormat("MM/dd/yyyy, h:mm a")
                                .format(Calendar.getInstance().getTime());

                        try {
                            if (sdf.parse(edate).before(sdf.parse(curT))) {
                                isBefore = true;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        // When button first got click
                        if (!cBtnFlag) {
                            // Change the buttonText into completed
                            mButton.setText("COMPLETED");
                            // Change the button color
                            mButton.setBackgroundColor(0xFFFF0000);
                            cBtnFlag = true;
                        } else if (!isBefore) { // Click it before the end Date
                            // Have a Dialog instead of a disable the button
                            System.out.println("############# Can't click now!!!!!!! ###########");
                        } else { // Click it after the end Date
                            mButton.setVisibility(View.GONE);

                        }
                    }
            });

                // Missing attendees in db

                // Generate the recommendation opportunity list
                rcmListGenerator(mFBRef);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("OppDetail failed: " + firebaseError.getMessage());
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
                    View v = getLayoutInflater().inflate(R.layout.item, null);
                    ImageView imageView = (ImageView)v.findViewById(R.id.imgIcon);
                    TextView tDate = (TextView)v.findViewById(R.id.txtDate);
                    TextView tName = (TextView)v.findViewById(R.id.txtTitle);
                    TextView tLoc = (TextView)v.findViewById(R.id.txtLoc);
                    TextView tPTS = (TextView)v.findViewById(R.id.txtPTS);
                    imageView.setImageResource(imgs.get(i));
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
                System.out.println("#########OppDetail failed: " + firebaseError.getMessage());
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
