package com.example.chinmayee.mainactivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class OppDetail extends AppCompatActivity {
    private ImageView image;
    private Firebase mFBRef ;
    private TextView mText1;
    private TextView mText2;
    private TextView mText3;
    private TextView mText4;
    private TextView mText5;
    private Button mButton;
    private Integer oppID;
    private ImageButton starBtn;
    private ArrayList<Integer> imgs = new ArrayList<Integer>();
    private ArrayList<String> dates = new ArrayList<String>();
    private ArrayList<String> names = new ArrayList<String>();
    private ArrayList<String> locs = new ArrayList<String>();
    private ArrayList<Integer> pts = new ArrayList<Integer>();
    private ArrayList<String> recIds = new ArrayList<String>();
    private LinearLayout scrollView;
    private Boolean isBefore = false;
    private Boolean cBtnFlag;
    private String edate;
    private LinearLayout uScrollView;
    private ImageView mUserImage;
    private TextView u1Name;
    private ArrayList<PublicUser> btnList = new ArrayList<PublicUser>();
    private Bundle bundle;
    private boolean starCounter = false;
    private String userId;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opp_detail);
        Firebase.setAndroidContext(this);
        bundle = getIntent().getExtras();
        cBtnFlag = bundle.getBoolean("isComplete");
        starCounter = bundle.getBoolean("isSaved");
        oppID = getIntent().getExtras().getInt("oppId");
        mFBRef = new Firebase("https://flickering-inferno-293.firebaseio.com/");
        mText1 = (TextView) findViewById(R.id.oppDate);
        mText2 = (TextView) findViewById(R.id.oppName);
        mText3 = (TextView) findViewById(R.id.oppDes);
        mText4 = (TextView) findViewById(R.id.oppLoc);
        image = (ImageView) findViewById(R.id.image);
        mText5 = (TextView) findViewById(R.id.oppContact);
        mButton = (Button) findViewById(R.id.mButton);
        starBtn = (ImageButton) findViewById(R.id.favorite_button);
        Drive myapp = (Drive) getApplication();
        userId= myapp.getUserId();

        // set star button
        if (!starCounter) {
            starBtn.setImageResource(android.R.drawable.btn_star_big_off);
        } else {
            starBtn.setImageResource(android.R.drawable.btn_star_big_on);
        }

        starBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            if (!starCounter) {
                starBtn.setImageResource(android.R.drawable.btn_star_big_on);
                Toast.makeText(getApplicationContext(), "Added to favorite.", Toast.LENGTH_LONG).show();
                starCounter = true;
            } else {
                starBtn.setImageResource(android.R.drawable.btn_star_big_off);
                Toast.makeText(getApplicationContext(), "Removed from favorite.", Toast.LENGTH_LONG).show();
                starCounter = false;
            }
            }

        });

        // Attach an listener to read the data at our posts reference
        String oppFBID = "opportunity/" + oppID.toString();
        mFBRef.child(oppFBID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot postSnapshot) {

                // Fetch data from db and set it
                String picSrc = (String) postSnapshot.child("pic").getValue();
                int id = getApplicationContext().getResources().getIdentifier(picSrc, "drawable", getApplicationContext().getPackageName());
                image.setImageResource(id);
                mText1.setText((String) postSnapshot.child("start date").getValue());
                mText2.setText((String) postSnapshot.child("name").getValue());
                mText3.setText((String) postSnapshot.child("longDesc").getValue());
                mText4.setText((String) postSnapshot.child("location").getValue());
                mText5.setText((String) postSnapshot.child("contact").getValue());
                edate = (String) postSnapshot.child("end date").getValue();

                // Check date/time
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy, h:mm a");
                String curT = new SimpleDateFormat("MM/dd/yyyy, h:mm a").format(Calendar.getInstance().getTime());

                try {
                    if (sdf.parse(edate).before(sdf.parse(curT))) {
                        isBefore = true;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // Set the Register/Complete button
                mButton.setTransformationMethod(null);
                if (cBtnFlag) { // Change the buttonText into completed
                    mButton.setText("MARK COMPLETE");
                    // Change the button color
                    mButton.setBackgroundColor(0xFFFF0000);
                }

                mButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        alertDialog = new AlertDialog.Builder(OppDetail.this).create();
                    // When button first got click
                    if (!cBtnFlag) { // Change the buttonText into completed
                        mButton.setText("MARK COMPLETE");
                        // Change the button color
                        mButton.setBackgroundColor(0xFFFF0000);
                        cBtnFlag = true;
                        alertDialog.setTitle("SUCCESS");
                        alertDialog.setMessage("You have successfully registered !!!");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    } else if (!isBefore) { // Click it before the end Date
                        alertDialog.setTitle("WAIT !");
                        alertDialog.setMessage("Opportunity has not yet ended !!!");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    } else { // Click it after the end Date
                        mButton.setVisibility(View.GONE);
                        AlertDialog alertDialog = new AlertDialog.Builder(OppDetail.this).create();
                        alertDialog.setTitle("SUCCESS");
                        alertDialog.setMessage("You have successfully finished !!!");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                    }
            });

                // Attendees list. Must call it this way, else Firebase will complain.
                usersListGenerator(mFBRef);

                // Recommendation opportunity list. Must call it this way, else Firebase will complain.
                rcmListGenerator(mFBRef);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("OppDetail failed: " + firebaseError.getMessage());
            }
        });
    }

    // Helper function. Generate the attendees list
    private void usersListGenerator(Firebase mFBRef){
        mFBRef.child("user/").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot msgSnapshot : dataSnapshot.getChildren()) {
                    // Fetch data from db
                    PublicUser pu = new PublicUser(msgSnapshot.getKey().toString(),
                            msgSnapshot.child("fname").getValue().toString(),
                            msgSnapshot.child("lname").getValue().toString(),
                            msgSnapshot.child("pic").getValue().toString());
                    btnList.add(pu);
                }

                // Set it
                uScrollView = (LinearLayout) findViewById(R.id.attendeesList);

                for (int i = 0; i < btnList.size(); i++) {
                    final PublicUser p = btnList.get(i);
                    if (p.getId().equals(userId)) continue;
                    View v = getLayoutInflater().inflate(R.layout.attendees_item, null);

                    mUserImage  = (ImageView) v.findViewById(R.id.user1);
                    String picSrc = (String) btnList.get(i).getPic();
                    if (picSrc.equals("profimg2")) {
                        Picasso.with(mUserImage.getContext()).load(R.drawable.profimg2).transform(new CircleTransform()).into(mUserImage);
                    } else if (picSrc.equals("profileimg1")) {
                        Picasso.with(mUserImage.getContext()).load(R.drawable.profileimg1).transform(new CircleTransform()).into(mUserImage);
                    } else { // If picSrc isn't an url, then can't use Picasso
                        Picasso.with(mUserImage.getContext()).load(picSrc).transform(new CircleTransform())
                                .into(mUserImage);
                    }

                    u1Name = (TextView) v.findViewById(R.id.user1Name);
                    u1Name.setText(p.getFname() + " " + p.getLname());
                    v.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Intent myIntent = new Intent(OppDetail.this, PublicProfile.class);
                            Bundle b = new Bundle();
                            b.putString("otherUserId", p.getId());
                            myIntent.putExtras(b); //Put your id to your next Intent
                            startActivity(myIntent);
                        }
                    });
                    uScrollView.addView(v);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("#########OppDetail failed: " + firebaseError.getMessage());
            }
        });
    }

    // Helper function. Generate the recommendation opportunity list
    private void rcmListGenerator(Firebase mFBRef){
        mFBRef.child("opportunity").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot msgSnapshot : dataSnapshot.getChildren()) {
                    // Fetch and set
                    String img = (String) msgSnapshot.child("pic").getValue();
                    int mid = getApplicationContext().getResources().getIdentifier(img, "drawable", getApplicationContext().getPackageName());
                    imgs.add(mid);
                    dates.add(msgSnapshot.child("start date").getValue().toString());
                    names.add(msgSnapshot.child("name").getValue().toString());
                    locs.add(msgSnapshot.child("location").getValue().toString());
                    recIds.add(msgSnapshot.child("oppId").getValue().toString());

                    Integer[] dimScore = new Integer[5];
                    int sumScore=0;
                    for (int j=1; j<6; j++) {
                        dimScore[j - 1] = Integer.parseInt((String) msgSnapshot.child("score").child("d" + j).getValue());
                        sumScore += dimScore[j - 1];
                    }
                    pts.add(sumScore);

                }

                scrollView = (LinearLayout)findViewById(R.id.scrollView);
                for (int i = 0; i<dates.size(); i++){
                    final int passID = Integer.parseInt(recIds.get(i));
                    if (passID == oppID) continue;
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

                    v.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View arg0) {
                        Intent myIntent = new Intent(OppDetail.this, OppDetail.class);
                        Bundle b = new Bundle();
                        b.putInt("oppId", passID); //Your id
                        b.putString("userLevel", bundle.getString("userLevel"));
                        b.putBoolean("isComplete", false);
                        myIntent.putExtras(b); //Put your id to your next Intent
                        startActivity(myIntent);
                        }
                    });
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
