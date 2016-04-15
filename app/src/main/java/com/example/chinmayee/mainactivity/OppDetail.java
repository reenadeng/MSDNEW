package com.example.chinmayee.mainactivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
    private ArrayAdapter<String> mAdapter;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opp_detail);
        Firebase.setAndroidContext(this);
        bundle = getIntent().getExtras();
        cBtnFlag = bundle.getBoolean("isComplete");
        starCounter = bundle.getBoolean("isSaved");
        oppID = getIntent().getExtras().getInt("oppId");
        mFBRef = new Firebase(new Drive().getFirebaseURL());
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

        //for menu
        setupToolbar();
        mDrawerList = (ListView)findViewById(R.id.navList3);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout3);
        mActivityTitle = "OPPORTUNITY DETAILS";
        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


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



    private void setupToolbar() {
        // TODO Auto-generated method stub
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
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
                Intent intent = new Intent(getApplicationContext(), Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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
                getSupportActionBar().setTitle("HOME");
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
