package com.example.chinmayee.mainactivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


public class PublicProfile extends AppCompatActivity {
    ImageView imageView1;
    RoundImage roundedImage;
    TextView mText1;
    TextView mText2;
    TextView mText3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_profile);
        Firebase.setAndroidContext(this);
        Firebase mFBRef = new Firebase("https://flickering-inferno-293.firebaseio.com/");

        imageView1 = (ImageView) findViewById(R.id.imageView1);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.profimg2);
        roundedImage = new RoundImage(bm);
        imageView1.setImageDrawable(roundedImage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mText1 = (TextView) findViewById(R.id.userName);
        mText2 = (TextView) findViewById(R.id.userLevel);
        mText3 = (TextView) findViewById(R.id.bio);

        // Attach an listener to read the data at our posts reference
        mFBRef.child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    // Haven't put image in here yet

                    // Set user name
                    String name = (String) postSnapshot.child("fname").getValue() + " " +
                            (String) postSnapshot.child("lname").getValue();
                    mText1.setText(name);

                    // Set user level
                    String level = "LEVEL " + (String) postSnapshot.child("level").getValue();
                    mText2.setText(level);

                    // Set user BIO
                    mText3.setText((String) postSnapshot.child("about").getValue());


                }
            }
                @Override
                public void onCancelled (FirebaseError firebaseError){
                    System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });


    }
}
