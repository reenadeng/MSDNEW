package com.example.chinmayee.mainactivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 *
 * Chinmayee Nitin Vaidya, Bhumitra Nagar, Swapnil Mahajan, Xinyan Deng
 * This is the activity that shows other user's PROFILE page.
 *
 */
public class PublicProfile extends AppCompatActivity {
    ImageView image;
    TextView mText1;
    TextView mText2;
    TextView mText3;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_profile);
        Firebase.setAndroidContext(this);
        userID = getIntent().getExtras().getString("otherUserId");
        Firebase mFBRef = new Firebase(new Drive().getFirebaseURL());
        image = (ImageView) findViewById(R.id.imageView1);
        mText1 = (TextView) findViewById(R.id.userName);
        mText2 = (TextView) findViewById(R.id.userLevel);
        mText3 = (TextView) findViewById(R.id.bio);

        // Attach an listener to read the data at our posts reference
        String userFBID = "user/" + userID.toString();
        mFBRef.child(userFBID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot postSnapshot) {
               // Image
                String picSrc = (String) postSnapshot.child("pic").getValue();
                if (picSrc.equals("profimg2")) {
                    Picasso.with(image.getContext()).load(R.drawable.profimg2).transform(new CircleTransform())
                            .into(image);
                } else if (picSrc.equals("profileimg1")) {
                    Picasso.with(image.getContext()).load(R.drawable.profileimg1).transform(new CircleTransform())
                            .into(image);
                } else { // If picSrc isn't an url, then can't use Picasso
                    Picasso.with(image.getContext()).load(picSrc).transform(new CircleTransform())
                            .into(image);
                }

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
                @Override
                public void onCancelled (FirebaseError firebaseError){
                    System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });


    }
}
