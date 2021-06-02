package android.bignerdranch.mapboxtest;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

public class ViewPageAdapter extends FragmentPagerAdapter {

    private final List<Fragment> songFragment = new ArrayList<>();
    private final List<String> titleList = new ArrayList<>();


    public ViewPageAdapter(FragmentManager fm){
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return songFragment.get(position);
    }

    @Override
    public int getCount() {
        return songFragment.size();
    }

    @Override
    public CharSequence getPageTitle(int position){
        return titleList.get(position) ;
    }


    public void addFragment(Fragment fragment, String title){
        songFragment.add(fragment);
        titleList.add(title);
    }
}
