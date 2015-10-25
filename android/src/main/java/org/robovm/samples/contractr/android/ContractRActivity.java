package org.robovm.samples.contractr.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import org.robovm.samples.contractr.android.fragment.*;

import java.util.Locale;

public class ContractRActivity extends RoboAppCompatActivity
        implements AbstractClientFragment.ClientFragmentListener,
        AbstractTaskFragment.TaskFragmentListener,
        FragmentDrawer.FragmentDrawerListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory.
     */
    SectionsPagerAdapter sectionsPagerAdapter;

    WorkFragment workFragment;
    ClientsFragment clientsFragment;
    TasksFragment tasksFragment;

    Toolbar toolbar;
    FragmentDrawer drawerFragment;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Set up the toolbar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        // When swiping between different sections, change the toolbar title.
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                changeActionBarTitle(position);
            }
        });

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        drawerFragment.setDrawerListener(this);

        changeActionBarTitle(0);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        viewPager.setCurrentItem(position);
    }

    public void changeActionBarTitle(int position) {
        getSupportActionBar().setTitle(sectionsPagerAdapter.getPageTitle(position));
    }

    /**
     * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 0) {
                workFragment = WorkFragment.newInstance();
                return workFragment;
            }
            if (position == 1) {
                clientsFragment = ClientsFragment.newInstance();
                return clientsFragment;
            }
            tasksFragment = TasksFragment.newInstance();
            return tasksFragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
            case 0:
                return getString(R.string.title_section_work).toUpperCase(l);
            case 1:
                return getString(R.string.title_section_clients).toUpperCase(l);
            case 2:
                return getString(R.string.title_section_tasks).toUpperCase(l);
            }
            return null;
        }
    }

    @Override
    public void clientSaved() {
        clientsFragment.clientSaved();
        tasksFragment.taskSaved();
    }

    @Override
    public void taskSaved() {
        tasksFragment.taskSaved();
    }
}
