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

public class RegisterTester extends InstrumentationTestCase {

    private UiDevice device;


    @Override
    public void setUp() throws Exception {
        super.setUp();
        device = UiDevice.getInstance(getInstrumentation());
        device.pressHome();
        device.pressRecentApps();
        try {
            UiObject close = device.findObject(new UiSelector().description("Dismiss CATLR."));
            close.click();
        }catch (Exception e){
            Log.d("exception", e.getMessage());
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
        device.wait(Until.hasObject(By.text("New User? REGISTER")), 5000);

        UiObject2 regButton = device.findObject(By.text("New User? REGISTER"));
        regButton.click();

        device.wait(Until.hasObject(By.text("REGISTER")), 5000);
        UiObject2 homeBanner = device.findObject(By.text("REGISTER"));

        String result = homeBanner.getText();
        assertTrue(result.equals("REGISTER"));
    }
    public void testAdd() throws Exception {


    }

}


