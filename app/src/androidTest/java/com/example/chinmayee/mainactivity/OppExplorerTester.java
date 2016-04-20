package com.example.chinmayee.mainactivity;
/**
 * Chinmayee Nitin Vaidya, Bhumitra Nagar, Swapnil Mahajan, Xinyan Deng
 */


import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.test.InstrumentationTestCase;
import android.util.Log;

public class OppExplorerTester extends InstrumentationTestCase {
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

        //"Apps" should be changes to "All apps" if running tests on a real mobile device.
        // It varies from device to device. We used HTC one M8.
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

        device.wait(Until.hasObject(By.text("ALL")), 5000);
        UiObject2 homeBanner = device.findObject(By.text("What: A Talk on Robotics by Tim Cook"));
        homeBanner.click();
        device.waitForIdle(3000);
        device.wait(Until.hasObject(By.text("REGISTER")), 5000);
        UiObject2 registerButton = device.findObject(By.text("REGISTER"));

        String result = registerButton.getText();
        assertTrue(result.equals("REGISTER"));

        registerButton.click();

        device.wait(Until.hasObject(By.text("OK")), 5000);
        UiObject2 ok = device.findObject(By.text("OK"));
        ok.click();

        device.wait(Until.hasObject(By.text("MARK COMPLETE")), 5000);
        UiObject2 comp = device.findObject(By.text("MARK COMPLETE"));

        String complete = comp.getText();
        assertTrue(complete.equals("MARK COMPLETE"));
        comp.click();
        device.wait(Until.hasObject(By.text("OK")), 5000);
        UiObject2 ok1 = device.findObject(By.text("OK"));
        ok1.click();
    }
    public void testAdd() throws Exception {
    }
}