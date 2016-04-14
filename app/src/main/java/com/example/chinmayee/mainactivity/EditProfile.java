package com.example.chinmayee.mainactivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.squareup.picasso.Picasso;

public class EditProfile extends AppCompatActivity {
    ImageView image;
    String userPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        image = (ImageView) findViewById(R.id.imageView1);
        Drive myapp = (Drive) getApplication();
        userPic= myapp.getPic();

        if (userPic.equals("profimg2")) {
            Picasso.with(image.getContext()).load(R.drawable.profimg2).transform(new CircleTransform()).into(image);
        } else if (userPic.equals("profileimg1")) {
            Picasso.with(image.getContext()).load(R.drawable.profileimg1).transform(new CircleTransform()).into(image);
        } else { // If picSrc isn't an url, then can't use Picasso
            Picasso.with(image.getContext()).load(userPic).transform(new CircleTransform())
                    .into(image);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Spinner staticSpinner = (Spinner) findViewById(R.id.static_spinner);


        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(this, R.array.visible_array,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        staticSpinner.setAdapter(staticAdapter);
    }

}
