package trithe.modelproject.fragment;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class HomePager extends FragmentPagerAdapter {
    public HomePager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                return new ListTheLoaiFragment();
            case 2:
                return new ListUserFragment();
            default:
                return new ListBookFragment();

        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 1:
                return "Type Book";
            case 2:
                return "User";
            default:
                return "Book";
        }

    }
}
