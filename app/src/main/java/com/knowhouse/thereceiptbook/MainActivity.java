//Author: Enoch Viewu
//Cooperation: KnowHouse
//Date Modified: 4th Feb, 2020
//Code: The MainActivity of the application

package com.knowhouse.thereceiptbook;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.knowhouse.thereceiptbook.FragmentActivities.InboxFragment;
import com.knowhouse.thereceiptbook.FragmentActivities.MainActivityFragment;
import com.knowhouse.thereceiptbook.FragmentActivities.SalesFragment;
import com.knowhouse.thereceiptbook.FragmentActivities.TransactionsFragment;
import com.knowhouse.thereceiptbook.LoginSingleton.SharedPrefManager;

import de.hdodenhof.circleimageview.CircleImageView;

/*
 *  public class which is a subclass of appcompat activity implementing the navigation view on navigation
 * item selected listener interface
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    //These variables our data going to be received from an intent
    public static final String USERID = "id";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String FULL_NAME = "fullName";
    public static final String COMPANY_NAME = "companyName";
    public static final String IMAGE_URL = "image";
    public static final String RECEIPTISSUED = "receipt_sent";

    private int userid;
    private int phoneNumber;
    private String  fullName;
    private String companyName;
    private boolean receiptSent;
    private String imageUrl;

    private  Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);         //call the superclass method
        setContentView(R.layout.activity_main);     //set layout to activity main
        Toolbar toolbar = findViewById(R.id.toolbar);   //capture the toolbar layout
        setSupportActionBar(toolbar);                   //set the action bar support for the toolbar
        getSupportActionBar().setDisplayShowTitleEnabled(false);    //Turn off the title of the action bar

        FloatingActionButton fab = findViewById(R.id.fab);  //capture the floating button layout
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {                    //set an onClickListener for the floating action button
                Intent intent = new Intent(MainActivity.this,ReceiptPageActivity.class);
                startActivity(intent);
            }
        });


        //Navigation drawer
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);   //Get a reference to the drawer layout
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout,toolbar,R.string.nav_open_drawer,R.string.nav_close_drawer);   //Create and action bar drawer toggle button
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //Register the activity with the navigation view as a listener
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        //Get information from intent
        Intent intent = getIntent();
        userid = intent.getExtras().getInt(USERID);
        phoneNumber = intent.getExtras().getInt(PHONE_NUMBER);
        fullName = intent.getExtras().getString(FULL_NAME).trim();
        String phoneNumberString = "0".concat(String.valueOf(phoneNumber));
        companyName = intent.getExtras().getString(COMPANY_NAME).trim();
        imageUrl = intent.getExtras().getString(IMAGE_URL);
        receiptSent = intent.getExtras().getBoolean(RECEIPTISSUED);

        if(receiptSent) {
            Fragment fragment = null;           //Fragment object to hold the fragment in fragment activities package
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();   //fragment transaction object to dynamically change fragment
            fragment = new TransactionsFragment();      //Stores a reference to transaction fragment object in fragment variable
            ft.replace(R.id.content_frame, fragment);    //replace the currently used fragment with home fragment
            ft.commit();    //commit the new fragment onto the application frameLayout
        }

        //Initialization of navigation widgets for drawer layout
        TextView navFullName = headerView.findViewById(R.id.nav_full_name);
        TextView navPhoneNumber = headerView.findViewById(R.id.nav_phone_number);
        CircleImageView navProfilePic = headerView.findViewById(R.id.nav_profile_pic);

        //Get user name and phone number from shared preferences
        navPhoneNumber.setText(phoneNumberString);
        navFullName.setText(fullName);

        if(!imageUrl.equals("null")){
            image = ImageConverter.convertFromStringToImg(imageUrl);
            navProfilePic.setImageBitmap(image);
        }

        Fragment fragment = new MainActivityFragment();      //Store a reference to mainActivityfragment object in fragment variable
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame,fragment);    //replace the currently used fragment with main activity fragment
        ft.commit();                                //commit the new fragment onto the application framelayout

    }

    //Populate the menu on the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);        //call the super method onCreateOptionsMenu to ensure menu loads properly
        getMenuInflater().inflate(R.menu.menu_main,menu);   //getMenuInflater to inflate the menu with the menu layout
        if(!imageUrl.equals("null")){
            RoundedBitmapDrawable myProfilePic = RoundedBitmapDrawableFactory.create(getResources(),image);
            myProfilePic.setCircular(true);
            MenuItem item = menu.findItem(R.id.user_profile_image);
            item.setIcon(myProfilePic);
        }
        return true;        //return true to display the menu items
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.logging_out:
                SharedPrefManager.getInstance(this).logout();
                startActivity(new Intent(this,LoginActivity.class));
                finish();
                break;
            case R.id.user_profile_image:
                Intent intent = new Intent(this,UserProfileActivity.class);
                intent.putExtra(UserProfileActivity.USERID,userid);
                intent.putExtra(UserProfileActivity.PHONE_NUMBER,phoneNumber);
                intent.putExtra(UserProfileActivity.FULL_NAME,fullName);
                intent.putExtra(UserProfileActivity.COMPANY_NAME,companyName);
                intent.putExtra(UserProfileActivity.IMAGE_URL,imageUrl);
                startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();      //integer to hold value for the selected item in the menu
        Fragment fragment = null;           //Fragment object to hold the fragment in fragment activities package
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();   //fragment transaction object to dynamically change fragment
        Intent intent = null;   //intent to launch help activity and feedback activity

        switch (id){

            case R.id.home:                                 //Switch case where home is selected in the navigation view
                fragment = new MainActivityFragment();      //Store a reference to mainActivityfragment object in fragment variable
                ft.replace(R.id.content_frame,fragment);    //replace the currently used fragment with main activity fragment
                ft.commit();                                //commit the new fragment onto the application framelayout
                break;                                      //break from switch statement
            case R.id.inbox:
                fragment = new InboxFragment();     //Switch case where inbox is selected in the navigation view
                ft.replace(R.id.content_frame,fragment);    //store a reference to inbox is selected in navigation view
                ft.commit();        //commit the new fragment onto the application frameLayout
                break;                          //break from switch statement


            case R.id.transactions:                 //switch case where transaction is selected in navigation view
                fragment = new TransactionsFragment();  //store a reference to transaction fragment object in fragment variable
                ft.replace(R.id.content_frame,fragment);    //replace the currently used fragment with transaction fragment
                ft.commit();    //commit the new fragment onto the application frameLayout
                break;  //break from switch statement

            case R.id.sales:            //switch case where sales is selected on in navigation view
                fragment = new SalesFragment();     //store a reference to sales fragment object in fragment variable
                ft.replace(R.id.content_frame,fragment);    //replace the currently used fragment with transaction fragment
                ft.commit();    //commit the new fragment onto the application frameLayout

                break;  //break from switch statement

            case R.id.nav_feedback: //switch case where sales is selected on in navigation view
                intent = new Intent(this,FeedbackActivity.class);     //Create an intent to launch the feed back activity class
                break;

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout); //Get a reference to the drawer layout
        drawer.closeDrawer(GravityCompat.START);        //Close drawer layout
        return true;            //return true to show that condition has been consumed or acted on
    }


    /*Set the back button so it closes the nav view when it is opened*/
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }
}
