package glivion.y2k.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.chyrta.onboarder.OnboarderPage;

import java.util.ArrayList;
import java.util.List;

import glivion.y2k.ui.ui.OnBoardFragment;

/**
 * Created by saeedissah on 5/16/16.
 */
public class OnBoarderAdapter extends FragmentStatePagerAdapter {

    List<OnboarderPage> pages = new ArrayList<OnboarderPage>();

    public OnBoarderAdapter(List<OnboarderPage> pages, FragmentManager fm) {
        super(fm);
        this.pages = pages;
    }

    @Override
    public Fragment getItem(int position) {
        return OnBoardFragment.newInstance(pages.get(position));
    }

    @Override
    public int getCount() {
        return pages.size();
    }
}
