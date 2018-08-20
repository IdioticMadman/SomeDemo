package com.robert.fragmenttabhostdemo;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTabHost = (FragmentTabHost) findViewById(R.id.fragment_tab_host);
        mTabHost.setup(this, getSupportFragmentManager(),android.R.id.tabcontent);
        mTabHost.addTab(mTabHost.newTabSpec("First").setIndicator("First"), FirstFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Second").setIndicator("Second"), SecondFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Third").setIndicator("Third"), ThirdFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Fourth").setIndicator("Fourth"), FourthFragment.class, null);
    }
}
