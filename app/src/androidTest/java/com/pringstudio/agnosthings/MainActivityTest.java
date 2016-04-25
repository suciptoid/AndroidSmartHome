package com.pringstudio.agnosthings;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by sucipto on 4/14/16.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity>{

    public MainActivityTest(){
        super(MainActivity.class);
    }

    // First Test
    public void testActivityExists(){
        MainActivity activity = getActivity();
        assertNotNull(activity);
    }

}
