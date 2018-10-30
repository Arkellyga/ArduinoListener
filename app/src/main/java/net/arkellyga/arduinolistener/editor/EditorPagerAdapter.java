package net.arkellyga.arduinolistener.editor;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import net.arkellyga.arduinolistener.R;

public class EditorPagerAdapter extends FragmentPagerAdapter {
    private static final int PAGE_COUNT = 4;
    private Context mContext;
    private String[] mTabTitles;
    private String mTileName;

    public EditorPagerAdapter(FragmentManager fm, Context ctx, String name) {
        super(fm);
        mContext = ctx;
        Resources res = ctx.getResources();
        mTabTitles = new String[] { res.getString(R.string.editor_common),
                                    res.getString(R.string.editor_text),
                                    res.getString(R.string.editor_colors),
                                    res.getString(R.string.editor_action)};
        mTileName = name;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return LocationEditorFragment.createInstance(mTileName);
            case 1:
                return TextEditorFragment.createInstance(mTileName);
            case 2:
                return ColorEditorFragment.createInstance(mTileName);
            case 3:
                return ActionEditorFragment.createInstance(mTileName);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabTitles[position];
    }
}
