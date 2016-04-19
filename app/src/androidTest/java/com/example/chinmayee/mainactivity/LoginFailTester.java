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

public class LoginFailTester extends InstrumentationTestCase {

    private UiDevice device;


    @Override
    public void setUp() throws Exception {
        super.setUp();
        device = UiDevice.getInstance(getInstrumentation());
        device.pressHome();
        device.pressRecentApps();
        try {
            UiObject close = device.findObject(new UiSelector().description("Dismiss H.O.L.A.."));
            close.click();
        }catch (Exception e){
            Log.d("exception", e.getMessage());
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
        device.findObject(new UiSelector().resourceId("com.example.chinmayee.mainactivity:id/edit_username")).setText("demo.neu@husky.edu");
        device.findObject(new UiSelector().resourceId("com.example.chinmayee.mainactivity:id/edit_password")).setText("demo1");
        loginButton.click();

        device.wait(Until.hasObject(By.text("Login")), 5000);
        UiObject2 homeBanner = device.findObject(By.text("Login"));
        device.waitForIdle(3000);
        //  UiObject2 resultText = device.findObject(By.clazz("android.widget.EditText"));
        String result = homeBanner.getText();
        assertTrue(result.equals("Login"));
    }
    public void testAdd() throws Exception {


    }

}


