package com.example.chinmayee.mainactivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class HomeActivity extends AppCompatActivity {
    TabLayout tabLayout;
    private Firebase myFirebaseRef;
    private SearchView searchView;
    private  Button buttonWorkshop;
    private  Button buttonTalk;
    private  Button buttonCoop;
    private  Button buttonVolunteer;
    private  Button buttonHackAThon;
    private  Button buttonClubMem;
    private ProgressBar progressBar;
    private String userLevel;
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Firebase.setAndroidContext(this);
        myFirebaseRef = new Firebase(new Drive().getFirebaseURL());
        setupTablayout();
        setupSearch();
        setupButtonsAction();
               setupToolbar();
        mDrawerList = (ListView)findViewById(R.id.navList2);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout2);
        mActivityTitle = "HOME";
        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


    }

    private void setupToolbar() {
        // TODO Auto-generated method stub
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarsdfs2);
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
    protected void onStop() {
        //
        System.out.println("Stopped activity HomeActivity");
        super.onStop();
    }

    @Override
    protected void onPause() {
        System.out.println("Paused activity HomeActivity");
        super.onPause();
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

    //new end

    private void setupButtonsAction(){
        buttonWorkshop = (Button) findViewById(R.id.workshop);
        buttonWorkshop.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                Intent myIntent = new Intent(HomeActivity.this, OpportunityActivity.class);
                Bundle b = new Bundle();
                b.putString("filter", "workshop"); //Your id
                b.putString("userLevel", userLevel);
                myIntent.putExtras(b); //Put your id to your next Intent
                startActivity(myIntent);
            }
        });

        buttonTalk = (Button) findViewById(R.id.talk);
        buttonTalk.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                Intent myIntent = new Intent(HomeActivity.this, OpportunityActivity.class);
                Bundle b = new Bundle();
                b.putString("filter", "talk"); //Your id
                b.putString("userLevel", userLevel);
                myIntent.putExtras(b); //Put your id to your next Intent
                startActivity(myIntent);
            }
        });

        buttonCoop = (Button) findViewById(R.id.coop);
        buttonCoop.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                Intent myIntent = new Intent(HomeActivity.this,OpportunityActivity.class);
                Bundle b = new Bundle();
                b.putString("filter", "coop"); //Your id
                b.putString("userLevel", userLevel);
                myIntent.putExtras(b); //Put your id to your next Intent
                startActivity(myIntent);
            }
        });

        buttonVolunteer = (Button) findViewById(R.id.volunteering);
        buttonVolunteer.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                Intent myIntent = new Intent(HomeActivity.this,OpportunityActivity.class);
                Bundle b = new Bundle();
                b.putString("filter", "volunteering"); //Your id
                b.putString("userLevel", userLevel);
                myIntent.putExtras(b); //Put your id to your next Intent
                startActivity(myIntent);
            }
        });

        buttonClubMem = (Button) findViewById(R.id.club_membership);
        buttonClubMem.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                Intent myIntent = new Intent(HomeActivity.this, OpportunityActivity.class);
                Bundle b = new Bundle();
                b.putString("filter", "club_membership"); //Your id
                b.putString("userLevel", userLevel);
                myIntent.putExtras(b); //Put your id to your next Intent
                startActivity(myIntent);
            }
        });

        buttonHackAThon = (Button) findViewById(R.id.hack_a_thon);
        buttonHackAThon.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                Intent myIntent = new Intent(HomeActivity.this, OpportunityActivity.class);
                Bundle b = new Bundle();
                b.putString("filter", "hack-a-thon"); //Your id
                b.putString("userLevel", userLevel);
                myIntent.putExtras(b); //Put your id to your next Intent
                startActivity(myIntent);
            }
        });
    }

    private void setupSearch(){
        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setQueryHint("Search Opportunities");

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
            }
        });

        //*** setOnQueryTextListener ***
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    searchView = (SearchView) findViewById(R.id.searchView);
                    searchView.setIconified(true);
                }
                Intent myIntent = new Intent(HomeActivity.this, OpportunityActivity.class);
                Bundle b = new Bundle();
                b.putString("filter", query); //Your id
                b.putString("userLevel", userLevel);
                b.putBoolean("isComplete", false);
                myIntent.putExtras(b); //Put your id to your next Intent
                startActivity(myIntent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void setupTablayout() {

        Drive myapp = (Drive) getApplication();
        String userId = myapp.getUserId();
        getUserFromDb(userId);
        Intent intent = getIntent();

        Bundle b = new Bundle();
        b.putString("filter", ""); //Your id
        b.putString("userLevel", userLevel);
        intent.putExtras(b);

        // TODO Auto-generated method stub
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.addTab(tabLayout.newTab().setText("ALL"));
        tabLayout.addTab(tabLayout.newTab().setText("THIS WEEK"));
        tabLayout.addTab(tabLayout.newTab().setText("RECOMMENDED"));
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount(), intent.getExtras());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void getUserFromDb(String userId){
        String user = "user/"+userId;
        //"user/001722744"
        myFirebaseRef.child(user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer lev = Integer.parseInt(dataSnapshot.child("level").getValue().toString());
                userLevel = lev.toString();
                Integer[] dimScore = new Integer[5];
                int sumScore=0;
                for (int j = 1; j < 6; j++) {
                    dimScore[j - 1] = Integer.parseInt((String) dataSnapshot.child("dimensions").child("d" + j).getValue());
                    sumScore += dimScore[j - 1];
                }
                TextView userName = (TextView) findViewById(R.id.username);
                Button star = (Button) findViewById(R.id.star);

                userName.setText(dataSnapshot.child("fname").getValue().toString().toUpperCase());
                star.setText(((Integer) lev).toString());

                star.setOnClickListener(new OnClickListener() {
                    public void onClick(View arg0) {
                        Intent myIntent = new Intent(HomeActivity.this, MainActivity.class);
                        startActivity(myIntent);
                    }
                });


                Button trans = (Button)findViewById(R.id.buttonTransparent);
                trans.setOnClickListener(new OnClickListener() {
                    public void onClick(View arg0) {
                        Intent myIntent = new Intent(HomeActivity.this, MyOppsActivity.class);
                        startActivity(myIntent);
                    }
                });

                Button trans1 = (Button)findViewById(R.id.buttonTransparent);
                trans1.setOnClickListener(new OnClickListener() {
                    public void onClick(View arg0) {
                        Intent myIntent = new Intent(HomeActivity.this, MainActivity.class);
                        startActivity(myIntent);
                    }
                });


                Button trans2 = (Button)findViewById(R.id.buttonTrans2);
                trans2.setOnClickListener(new OnClickListener() {
                    public void onClick(View arg0) {
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });

                final int nextScore = sumScore;
                Firebase getLevelScore = new Firebase(new Drive().getFirebaseURL()+"level/" + lev.toString());
                getLevelScore.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String levScore = dataSnapshot.getValue().toString();
                        TextView progressScore = (TextView) findViewById(R.id.points);
                        progressBar = (ProgressBar) findViewById(R.id.progressBarHome);
                        progressBar.setMax(Integer.parseInt(levScore));
                        progressBar.setProgress(nextScore);
                        progressScore.setText(nextScore + "/" + levScore + " Points");
                    }
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {}
                });
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
    }

}
