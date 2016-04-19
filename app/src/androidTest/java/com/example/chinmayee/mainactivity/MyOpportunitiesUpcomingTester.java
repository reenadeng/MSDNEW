package com.example.chinmayee.mainactivity; /**
 * Created by bhumi on 3/24/2016.
 */


import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.test.InstrumentationTestCase;
import android.util.Log;


//Click an opportynity on home page to go opportunity details page
public class MyOpportunitiesUpcomingTester extends InstrumentationTestCase {

    private UiDevice device;


    @Override
    public void setUp() throws Exception {
        super.setUp();
     //   Runtime.getRuntime().exec(new String[]{"am", "force-stop", "MSD"});
        device = UiDevice.getInstance(getInstrumentation());

        device.pressHome();
        device.pressRecentApps();
        try {
            UiObject close = device.findObject(new UiSelector().description("Dismiss H.O.L.A.."));
            close.click();
        }catch (Exception e){
           Log.d("exception",e.getMessage());
        }
        device.pressHome();
        device.wait(Until.hasObject(By.desc("Apps")), 3000);
        UiObject2 appsButton = device.findObject(By.desc("Apps"));
        appsButton.click();

        //wait till app is on screen
        device.wait(Until.hasObject(By.text("H.O.L.A.")), 5000);
        UiObject2 msdApp = device.findObject(By.text("H.O.L.A."));
        msdApp.click();


        // Wait till the app opens, login page should be on screen
        device.wait(Until.hasObject(By.text("Login")), 5000);

        // Select the button for 9
        UiObject2 loginButton = device.findObject(By.text("Login"));
        device.findObject(new UiSelector().resourceId("com.example.chinmayee.mainactivity:id/edit_username")).setText("demo@husky.neu.edu");
        device.findObject(new UiSelector().resourceId("com.example.chinmayee.mainactivity:id/edit_password")).setText("demo1");
        loginButton.click();

        device.wait(Until.hasObject(By.text("HOME")), 5000);
        //UiObject2 homeBanner = device.findObject(By.text("HOME"));

        UiObject menuButton = device.findObject(new UiSelector().descriptionContains("Open navigation drawer"));
        menuButton.click();
       // menuButton.click();

       //device.click(1000,1000);
      //  UiScrollable appViews =new UiScrollable(new UiSelector().className("android.view.View")
      //          .scrollable(true));
       // appViews.swipeLeft(1000);

        device.wait(Until.hasObject(By.text("MY OPPORTUNITIES")), 3000);
        UiObject menuBanner = device.findObject(new UiSelector().resourceId("com.example.chinmayee.mainactivity:id/buttonTransparent"));
        menuBanner.click();


        device.wait(Until.hasObject(By.text(" UPCOMING ")), 10000);
        UiObject2 opp = device.findObject(By.text(" UPCOMING "));
        opp.click();

        device.waitForIdle(5000);
        //  UiObject2 resultText = device.findObject(By.clazz("android.widget.EditText"));
        String result = opp.getText();
        assertTrue(result.equals(" UPCOMING "));
    }
    public void testAdd() throws Exception {


    }

}


