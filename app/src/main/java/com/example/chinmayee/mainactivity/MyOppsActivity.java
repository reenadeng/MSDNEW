package com.example.chinmayee.mainactivity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.client.Firebase;



import android.content.Intent;
import android.content.res.Configuration;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;


public class MyOppsActivity extends AppCompatActivity {
//new
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;

    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opportunities);
        Firebase.setAndroidContext(this);
        setupToolbar();
        setupTablayout();

        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = "MY OPPORTUNITIES";

        addDrawerItems();
        setupDrawer();

    }


    //NEW METHODS

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
                //  startActivity(new Intent(this, Item3Activity.class));
                break;
            case 4:
                // startActivity(new Intent(this, Item4Activity.class));
                break;
            case 5:
                // startActivity(new Intent(this, Item5Activity.class));
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
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    //END NEW METHODS

    private void setupToolbar() {
        // TODO Auto-generated method stub
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarsdfs);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);}
    }

    private void setupTablayout() {
        // TODO Auto-generated method stub

        Intent intent = getIntent();
        Drive myapp = (Drive) getApplication();

        Bundle b = new Bundle();
        b.putString("nuId", myapp.getUserId());
        b.putString("filter", ""); //Your id
        b.putString("userLevel", myapp.getLevel());
        intent.putExtras(b);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.addTab(tabLayout.newTab().setText("INPROGRESS"));
        tabLayout.addTab(tabLayout.newTab().setText(" UPCOMING "));
        tabLayout.addTab(tabLayout.newTab().setText("COMPLETED "));
        tabLayout.addTab(tabLayout.newTab().setText("  SAVED"));
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final MyOppPagerAdapter adapter = new MyOppPagerAdapter(getSupportFragmentManager(),
                tabLayout.getTabCount(), intent.getExtras());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}