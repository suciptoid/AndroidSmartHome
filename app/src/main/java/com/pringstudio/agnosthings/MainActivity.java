package com.pringstudio.agnosthings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    // Keperluan Navigation Drawer
    // ---------------------------------------------------------------------------------------------
    private Toolbar toolbar;
    RecyclerView navRecyclerView;
    NavAdapter navAdapter;
    RecyclerView.LayoutManager navLayoutManager;
    DrawerLayout navDrawerLayout;
    ActionBarDrawerToggle navDrawerToogle;
    List<FragmentMenu> navMenuList = new ArrayList<>();

    // Fragment content index
    int currentFragment = 1;

    /**
     * *********************************************************************************************
     * Overrided Method
     * Letakkan Overrided Method di bawah line ini
     * *********************************************************************************************
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Material Toolbar / actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Show Menu on toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Init Drawer navigation menu
        setupNavigationDrawer();

        // Set default content for startup activity
        setFragmentContent(currentFragment); // 0 is Home fragment

        //  Launch app intro
        showIntro();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /**
    * **********************************************************************************************
    * Custom Method
    * Letakkan Custom Method di bawah line ini
    * **********************************************************************************************
    */

    /**
     * Setup Menu Item
     */
    private void setupMenuItem(){
        // Create Home Menu
        FragmentMenu home = new FragmentMenu(
                new FragmentHome(),
                "Dashboard",
                R.drawable.ic_dashboard_grey
        );
        navMenuList.add(home);

        // Recycerlview
        FragmentMenu recycler = new FragmentMenu(
                new FragmentSaklar(),
                "Saklar Lampu",
                R.drawable.ic_cached_grey,
                "Semua Saklar Lampu"
        );

        navMenuList.add(recycler);

        // Create About menu
        FragmentMenu about = new FragmentMenu(
                new FragmentAbout(),
                "About",
                R.drawable.ic_info_grey,
                "About Us"
        );
        navMenuList.add(about);
    }

    /**
     * Setup Navigation Drawer
     **/
    private void setupNavigationDrawer() {

        // Get Menu Item
        setupMenuItem();

        // Extract Menu title and Icons for adapter
        ArrayList<String> menuTitles = new ArrayList<>();
        ArrayList<Integer> menuIcons = new ArrayList<>();
        for(FragmentMenu menu : navMenuList){
            menuTitles.add(menu.getMenuName());
            menuIcons.add(menu.getMenuIcon());
        }

        // Get the recyclerview
        navRecyclerView = (RecyclerView) findViewById(R.id.nav_drawer_recycler);
        navRecyclerView.setHasFixedSize(true);

        // Navigation Recycler Adapter
        navAdapter = new NavAdapter(
                menuTitles,
                menuIcons,
                "",
                "",
                this
        );

        // Apply the adapter to recyclerview
        navRecyclerView.setAdapter(navAdapter);

        // Navigation Layout manager
        navLayoutManager = new LinearLayoutManager(this);
        navRecyclerView.setLayoutManager(navLayoutManager);

        // Navigation DrawerLayout
        navDrawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer_layout);

        // Drawer Toogle
        navDrawerToogle = new ActionBarDrawerToggle(this, navDrawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // Ketika drawer dibuka
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Ketika drawer ditutup
            }
        };

        // Menu item click Listener
        navAdapter.setOnItemClickListener(new NavAdapter.NavItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {

                // Tutup drawer jika yang di klik bukan menu
                if(position != 0){
                    // Set Fragment content
                    setFragmentContent(position);
                    // Notify selected menu changed
                    navAdapter.notifyDataSetChanged();
                    // Close when menu item clicked
                    navDrawerLayout.closeDrawers();
                }

            }
        });

        // Set Drawer Listenner
        navDrawerLayout.addDrawerListener(navDrawerToogle);

        // SyncState drawer with Home menu on actionbar
        navDrawerToogle.syncState();

    }

    /**
     * Fragment View Manager
     * Untuk melakukan update view di halaman depan tanpa membuat activity baru
     * dengan menggunakan fragment.
     * @param: view this is the index number of view
     * @param: title Android toolbar Title (default is app name)
     * @param: subtitle Android toolbar subtitle (default null)
     **/
    private void setFragmentContent(int position){

        // Get Fragment Transaction
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        FragmentMenu menuItem = navMenuList.get(position-1);

        // Create fragment object
        Fragment fragment = menuItem.getFragment();
        String title = menuItem.getTitle();
        String subtitle = menuItem.getSubtitle();

        // Update index
        currentFragment = position;



        // Apply Fragment to Main Frame
        if(fragment != null){
            transaction.replace(R.id.frame_main, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }

        // Change the Toolbar title
        try {
            if(title != null){
                getSupportActionBar()
                        .setTitle(title);
            }else{
                getSupportActionBar()
                        .setTitle(getText(R.string.app_name).toString());
            }

            // Change Toolbar subtitle
            if(subtitle != null){
                getSupportActionBar()
                        .setSubtitle(subtitle);
            }else {
                getSupportActionBar().setSubtitle("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Show intro for first usage
    private void showIntro(){
        //  Declare a new thread to do a preference check
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

                //  If the activity has never started before...
                if (isFirstStart) {

                    //  Launch app intro
                    Intent i = new Intent(MainActivity.this, PringIntro.class);
                    startActivity(i);

                    //  Make a new preferences editor
                    SharedPreferences.Editor e = getPrefs.edit();

                    //  Edit preference to make it false because we don't want this to run again
                    e.putBoolean("firstStart", false);

                    //  Apply changes
                    e.apply();
                }
            }
        });

        // Start the thread
        t.start();
    }

    /**
     * Inner Class Menu Fragment Object
     * Digunakan untuk membuat menu item di Drawer menu
     */
    class FragmentMenu{

        String menuName, title, subtitle;
        int menuIcon;
        Fragment fragment;

        // Empty Constructor
        public FragmentMenu(){
            //
        }

        // Full Constructor
        public FragmentMenu(Fragment fragment, String menuName, int icon, String title, String subtitle){
            this.fragment = fragment;
            this.menuName = menuName;
            this.title = title;
            this.menuIcon = icon;
            this.subtitle = subtitle;
        }

        // Without Subtitle
        public FragmentMenu(Fragment fragment, String menuName, int icon, String title){
            this.fragment = fragment;
            this.title = title;
            this.menuIcon = icon;
            this.menuName = menuName;
        }

        // Without Title & Subtitle
        public FragmentMenu(Fragment fragment, String menuName, int icon){
            this.fragment = fragment;
            this.menuIcon = icon;
            this.menuName = menuName;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public int getMenuIcon() {
            return menuIcon;
        }

        public void setMenuIcon(int menuIcon) {
            this.menuIcon = menuIcon;
        }

        public Fragment getFragment() {
            return fragment;
        }

        public void setFragment(Fragment fragment) {
            this.fragment = fragment;
        }

        public String getMenuName() {
            return menuName;
        }

        public void setMenuName(String menuName) {
            this.menuName = menuName;
        }
    }

}
