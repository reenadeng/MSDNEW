package com.example.chinmayee.mainactivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    ImageView image;
    TextView mText1;
    TextView mText2;
    TextView mText3;
    TextView mText4;
    ProgressBar mBar0;
    ProgressBar mBar1;
    ProgressBar mBar2;
    ProgressBar mBar3;
    ProgressBar mBar4;
    ProgressBar mBar5;

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    private ArrayList<Integer> imgs = new ArrayList<Integer>();
    private ArrayList<String> dates = new ArrayList<String>();
    private ArrayList<String> names = new ArrayList<String>();
    private ArrayList<String> locs = new ArrayList<String>();
    private ArrayList<Integer> pts = new ArrayList<Integer>();
    private LinearLayout scrollView;
    private Firebase mFBRef ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);

        setupToolbar();
        mDrawerList = (ListView)findViewById(R.id.navList1);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout1);
        mActivityTitle = "MY INFO";

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mFBRef = new Firebase("https://flickering-inferno-293.firebaseio.com/");
        image = (ImageView) findViewById(R.id.imageView1);
        mText1 = (TextView) findViewById(R.id.userName);
        mText2 = (TextView) findViewById(R.id.bio);
        mText3 = (TextView) findViewById(R.id.levelNum);
        mText4 = (TextView) findViewById(R.id.levelScore);
        mBar0 = (ProgressBar) findViewById(R.id.progressBar);
        mBar1 = (ProgressBar) findViewById(R.id.d1);
        mBar2 = (ProgressBar) findViewById(R.id.d2);
        mBar3 = (ProgressBar) findViewById(R.id.d3);
        mBar4 = (ProgressBar) findViewById(R.id.d4);
        mBar5 = (ProgressBar) findViewById(R.id.d5);

        Drive myapp = (Drive) getApplication();
        String userId = myapp.getUserId();
        //String userId = "001722744";

        String searchUser = "user/" + userId;
        // Attach an listener to read the data at our posts reference
        mFBRef.child(searchUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot postSnapshot) {

                    // image
                    String picSrc = (String) postSnapshot.child("pic").getValue();
                    Picasso.with(image.getContext()).load(picSrc).transform(new CircleTransform())
                            .into(image);

                    // Set user name
                    String name = (String) postSnapshot.child("fname").getValue() + " " +
                            (String) postSnapshot.child("lname").getValue();
                    mText1.setText(name);

                    // Set user BIO
                    mText2.setText((String) postSnapshot.child("about").getValue());

                    // Set user level
                    String level = "LEVEL " + (String) postSnapshot.child("level").getValue();
                    mText3.setText(level);

                    // Set Total progress bar and score
                    int[] dimScore = new int[5];
                    int sumScore = 0;
                    for (int j = 1; j < 6; j++) {
                        dimScore[j - 1] = Integer.parseInt((String) postSnapshot.child("dimensions").child("d" + j).getValue());
                        sumScore += dimScore[j - 1];
                    }
                    int levelScore = 1000;

                    mBar0.setMax(levelScore);
                    mBar0.setProgress(sumScore);
                    mText4.setText(sumScore + "/"+levelScore);

                    // Update 5 progress bars
                    mBar1.setProgress(Integer.parseInt((String) postSnapshot.child("dimensions").child("d1").getValue()));
                    mBar2.setProgress(Integer.parseInt((String) postSnapshot.child("dimensions").child("d2").getValue()));
                    mBar3.setProgress(Integer.parseInt((String) postSnapshot.child("dimensions").child("d3").getValue()));
                    mBar4.setProgress(Integer.parseInt((String) postSnapshot.child("dimensions").child("d4").getValue()));
                    mBar5.setProgress(Integer.parseInt((String) postSnapshot.child("dimensions").child("d5").getValue()));

                // Generate the recommend opportunity list
                rcmListGenerator(mFBRef);

                }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

    }

    // Helper function. Must call it this way, else Firebase will complain
    private void rcmListGenerator(Firebase mFBRef) {
        mFBRef.child("opportunity").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot msgSnapshot : dataSnapshot.getChildren()) {

                    String img = (String) msgSnapshot.child("pic").getValue();
                    int mid = getApplicationContext().getResources().getIdentifier(img, "drawable", getApplicationContext().getPackageName());
                    imgs.add(mid);

                    dates.add(msgSnapshot.child("start date").getValue().toString());

                    names.add(msgSnapshot.child("name").getValue().toString());

                    locs.add(msgSnapshot.child("location").getValue().toString());

                    Integer[] dimScore = new Integer[5];
                    int sumScore = 0;
                    for (int j = 1; j < 6; j++) {
                        dimScore[j - 1] = Integer.parseInt((String) msgSnapshot.child("score").child("d" + j).getValue());
                        sumScore += dimScore[j - 1];
                    }
                    pts.add(sumScore);

                }

                scrollView = (LinearLayout) findViewById(R.id.scrollView);
                for (int i = 0; i < dates.size(); i++) {
                    View v = getLayoutInflater().inflate(R.layout.item, null);
                    ImageView imageView = (ImageView) v.findViewById(R.id.imgIcon);
                    TextView tDate = (TextView) v.findViewById(R.id.txtDate);
                    TextView tName = (TextView) v.findViewById(R.id.txtTitle);
                    TextView tLoc = (TextView) v.findViewById(R.id.txtLoc);
                    TextView tPTS = (TextView) v.findViewById(R.id.txtPTS);
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
                System.out.println("OppDetail failed: " + firebaseError.getMessage());
            }
        });
    }

    private void setupToolbar() {
        // TODO Auto-generated method stub
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarsdfs1);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);}
    }

    private void addDrawerItems() {
        String[] osArray = { "HOME", "MY INFO", "MY OPPORTUNITIES", "LOGOUT" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Toast.makeText(MyOppsActivity.this, "Time for an upgrade!"+position+" "+id, Toast.LENGTH_SHORT).show();
                openActivity(position);
            }
        });
    }

    /** Called when the user clicks the Edit button */
    public void goToEdit(View view) {
        Intent intent = new Intent(this, EditProfile.class);
        startActivity(intent);
    }

    protected void openActivity(int position) {

        mDrawerList.setItemChecked(position, true);
//		setTitle(listArray[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
        // BaseActivity.position = position; //Setting currently selected position in this field so that it will be available in our child activities.

        switch (position) {
            case 0:
                startActivity(new Intent(this, HomeActivity.class));
                break;
            case 1:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case 2:
                startActivity(new Intent(this, MyOppsActivity.class));
                break;
            case 3:
                //  startActivity(new Intent(this, Item3Activity.class));
                break;

            default:
                break;
        }

//		Toast.makeText(this, "Selected Item Position::"+position, Toast.LENGTH_LONG).show();
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {


            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("MENU");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }


            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle("MY INFO");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

}