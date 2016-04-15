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
public class CategoryTesterVolunteering extends InstrumentationTestCase {

    private UiDevice device;


    @Override
    public void setUp() throws Exception {
        super.setUp();
     //   Runtime.getRuntime().exec(new String[]{"am", "force-stop", "MSD"});
        device = UiDevice.getInstance(getInstrumentation());

        device.pressHome();
        device.pressRecentApps();
        try {
            UiObject close = device.findObject(new UiSelector().description("Dismiss CATLR."));
            close.click();
        }catch (Exception e){
           Log.d("exception",e.getMessage());
        }
        device.pressHome();
        device.wait(Until.hasObject(By.desc("All apps")), 3000);
        UiObject2 appsButton = device.findObject(By.desc("All apps"));
        appsButton.click();

        //wait till app is on screen
        device.wait(Until.hasObject(By.text("CATLR")), 5000);
        UiObject2 msdApp = device.findObject(By.text("CATLR"));
        msdApp.click();


        // Wait till the app opens, login page should be on screen
        device.wait(Until.hasObject(By.text("Login")), 5000);

        // Select the button for 9
        UiObject2 loginButton = device.findObject(By.text("Login"));
        device.findObject(new UiSelector().resourceId("com.example.chinmayee.mainactivity:id/edit_username")).setText("demo@husky.neu.edu");
        device.findObject(new UiSelector().resourceId("com.example.chinmayee.mainactivity:id/edit_password")).setText("demo1");
        loginButton.click();

        device.wait(Until.hasObject(By.text("HOME")), 5000);

        UiObject menuBanner = device.findObject(new UiSelector().resourceId("com.example.chinmayee.mainactivity:id/volunteering"));
        menuBanner.click();


        device.wait(Until.hasObject(By.text("search result for : volunteering")), 5000);
        UiObject2 banner = device.findObject(By.text("search result for : volunteering"));
        device.waitForIdle(3000);
        //  UiObject2 resultText = device.findObject(By.clazz("android.widget.EditText"));
        String result = banner.getText();
        assertTrue(result.equals("search result for : volunteering"));
    }
    public void testAdd() throws Exception {


    }

}


