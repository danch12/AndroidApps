package android.bignerdranch.mapboxtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.google.android.material.tabs.TabLayout;
import com.mapbox.geojson.Point;

public class ExistingPlaylistTabActivity extends AppCompatActivity {

    private static final String TAG = "ExistingPlaylistTab";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPageAdapter adapter;

    public static Intent createIntent(Context context, String accessToken, double journeyDuration,
                                      String id, Point destination, Point origin){
        Intent intent =  new Intent(context, SongListActivity.class);
        intent.putExtra(SongListActivity.EXTRA_TOKEN, accessToken);
        intent.putExtra(ExistingPlaylistActivity.EXTRA_ID, id);
        intent.putExtra(SongListActivity.EXTRA_JOURNEY, journeyDuration);
        intent.putExtra(SongListActivity.EXTRA_DESTINATION,destination);
        intent.putExtra(SongListActivity.EXTRA_ORIGIN,origin);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: "+"start");
        setContentView(R.layout.activity_existing_playlist_tab);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewpager_id);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(),
                ExistingPlaylistTabActivity.this);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout_id);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getTabView(i));
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);
        Log.d(TAG, "onCreate: "+"hello");
    }




    private Fragment createSongFragment(){
        Fragment fragment = new ExistingSongFragment();
        Bundle bundle = new Bundle();
        bundle.putCharSequence(SongListActivity.EXTRA_TOKEN,getIntent().getStringExtra(SongListActivity.EXTRA_TOKEN));
        bundle.putCharSequence(SongListActivity.GENRE_TOKEN,"rock");
        bundle.putSerializable(SongListActivity.EXTRA_DESTINATION,getIntent().getSerializableExtra(SongListActivity.EXTRA_DESTINATION));
        bundle.putSerializable(SongListActivity.EXTRA_ORIGIN,getIntent().getSerializableExtra(SongListActivity.EXTRA_ORIGIN));
        bundle.putDouble(SongListActivity.EXTRA_JOURNEY,getIntent().getDoubleExtra(SongListActivity.EXTRA_JOURNEY,0));
        fragment.setArguments(bundle);
        return fragment;
    }

    private class PagerAdapter extends FragmentPagerAdapter {

        String tabTitles[] = new String[] { "Songs", "Comments" };
        Context context;

        public PagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return createSongFragment();
                case 1:
                    return createSongFragment();
            }

            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }

        public View getTabView(int position) {
            View tab = LayoutInflater.from(ExistingPlaylistTabActivity.this).inflate(R.layout.activity_existing_playlist_tab, null);
            TextView tv = (TextView) tab.findViewById(R.id.tabLayout_id);
            tv.setText(tabTitles[position]);
            return tab;
        }

        protected Fragment createSongFragment(){
            Fragment fragment = new ExistingSongFragment();
            Bundle bundle = new Bundle();
            bundle.putCharSequence(SongListActivity.EXTRA_TOKEN,getIntent().getStringExtra(SongListActivity.EXTRA_TOKEN));
            bundle.putCharSequence(SongListActivity.GENRE_TOKEN,"rock");
            bundle.putSerializable(SongListActivity.EXTRA_DESTINATION,getIntent().getSerializableExtra(SongListActivity.EXTRA_DESTINATION));
            bundle.putSerializable(SongListActivity.EXTRA_ORIGIN,getIntent().getSerializableExtra(SongListActivity.EXTRA_ORIGIN));
            bundle.putDouble(SongListActivity.EXTRA_JOURNEY,getIntent().getDoubleExtra(SongListActivity.EXTRA_JOURNEY,0));
            fragment.setArguments(bundle);
            return fragment;
        }

    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dev_menu_logs_menu, menu);
        return true;
    }



}