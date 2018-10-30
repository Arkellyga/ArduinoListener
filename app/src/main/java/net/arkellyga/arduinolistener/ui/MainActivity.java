package net.arkellyga.arduinolistener.ui;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import static net.arkellyga.arduinolistener.utils.Consts.*;

import net.arkellyga.arduinolistener.R;
import net.arkellyga.arduinolistener.utils.Tile;
import net.arkellyga.arduinolistener.utils.TilesFactory;
import net.arkellyga.arduinolistener.editor.ActionEditorFragment;
import net.arkellyga.arduinolistener.editor.ColorEditorFragment;
import net.arkellyga.arduinolistener.editor.EditorPagerAdapter;
import net.arkellyga.arduinolistener.editor.LocationEditorFragment;
import net.arkellyga.arduinolistener.editor.TextEditorFragment;

public class MainActivity extends AppCompatActivity implements LocationEditorFragment.onLocationUpdateListener,
                                                                TextEditorFragment.onTextUpdateListener,
                                                                ColorEditorFragment.onColorUpdateListener,
                                                                ActionEditorFragment.onActionUpdateListener,
                                                                TilesFragment.OnOpenEditorTile {
    private SharedPreferences mSharedPreferences;
    private static final String TAG = "MainActivity";
    private TabLayout mTabLayout;
    private ViewPager mPager;
    private LinearLayout mEditorLayout;
    private Tile mTile;
    private String mOldTileName;
    private Button mBtnSave, mBtnCancel, mBtnDelete;
    private TilesFactory mFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        setupUI();
        requestWriteReadPermissions();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Check app for update
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            int version = info.versionCode;
            if (mSharedPreferences.getInt(PREF_APP_VERSION, 1) < version) {
                mSharedPreferences.edit()
                        .putInt(PREF_APP_VERSION, version)
                        .putLong(PREF_LAST_UPDATE, System.currentTimeMillis())
                        .apply();
            }
        } catch (PackageManager.NameNotFoundException ex) {
            ex.printStackTrace();
        }
        mFactory = new TilesFactory(this);
    }

   /* @Override
    public void updateTile(Tile tile) {
        TilesFragment fragment = (TilesFragment) getFragmentManager()
                .findFragmentById(R.id.tiles_fragment_main);
        if (fragment != null && fragment.isInLayout()) {
            fragment.updateView(tile);
        }
    }

    @Override
    public void addNewTile(Tile tile) {
        TilesFragment fragment = (TilesFragment) getFragmentManager()
                .findFragmentById(R.id.tiles_fragment_main);
        if (fragment != null && fragment.isInLayout()) {
            fragment.addNewTile(tile);
        }
    }
    */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_menu_item:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.about_menu_item:
                Toast.makeText(this, "Created by Arkell", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tiles_activity_menu_item:
                startActivity(new Intent(this, TilesActivity.class));
                break;
            case R.id.add_tile_menu_item:
                mTile = new Tile();
                mOldTileName = mTile.getName();
                mEditorLayout.setVisibility(View.VISIBLE);
                mPager.setAdapter(new EditorPagerAdapter(getSupportFragmentManager(), this, null));
                mTabLayout.setupWithViewPager(mPager);
                TilesFragment fragment = (TilesFragment) getFragmentManager()
                        .findFragmentById(R.id.tiles_fragment_main);
                if (fragment != null && fragment.isInLayout())
                    fragment.addNewTile(mTile);
                break;
        }
        return true;
    }

    private void setupUI() {
        getSupportActionBar().setIcon(R.mipmap.ic_launcher_round);
        mTabLayout = findViewById(R.id.tab_layout_main);
        mPager = findViewById(R.id.view_pager_editor_main);
        mPager.setOffscreenPageLimit(4);
        mEditorLayout = findViewById(R.id.editor_layout_main);
        mBtnSave = findViewById(R.id.save_tile_button_main);
        mBtnDelete = findViewById(R.id.delete_tile_button_main);
        mBtnCancel = findViewById(R.id.cancel_tile_button_main);
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TilesFragment fragment = (TilesFragment) getFragmentManager()
                        .findFragmentById(R.id.tiles_fragment_main);
                if (fragment != null && fragment.isInLayout()) {
//                    fragment.updateView(mTile);
                    if (!mTile.getName().equals(mOldTileName)) {
                        mFactory.removeTileByName(mOldTileName);
                    }
                    mFactory.addTile(mTile);
                    mFactory.save();
                    fragment.removeView(mOldTileName);
                    fragment.addNewTile(mTile);
                    mEditorLayout.setVisibility(View.GONE);
                }
            }
        });
        mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TilesFragment fragment = (TilesFragment) getFragmentManager()
                        .findFragmentById(R.id.tiles_fragment_main);
                if (fragment != null && fragment.isInLayout()) {
                    // TODO: 21.08.18 Add remove view from child
                    mFactory.removeTileByName(mOldTileName);
                    mFactory.save();

                    mEditorLayout.setVisibility(View.GONE);
                }
            }
        });
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TilesFragment fragment = (TilesFragment) getFragmentManager()
                        .findFragmentById(R.id.tiles_fragment_main);
                if (fragment != null && fragment.isInLayout()) {
                    // TODO: 21.08.18 Add cancel for child
                    mEditorLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    private void requestWriteReadPermissions() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            //
                        } else {
                            requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void editTile(Tile tile) {
        mTile = tile;
        mOldTileName = tile.getName();
        mEditorLayout.setVisibility(View.VISIBLE);
        mPager.setAdapter(new EditorPagerAdapter(getSupportFragmentManager(), this, tile.getName()));
        mTabLayout.setupWithViewPager(mPager);
    }

    @Override
    public void updateLocation(Tile tile) {
        TilesFragment fragment = (TilesFragment) getFragmentManager()
                .findFragmentById(R.id.tiles_fragment_main);
        if (fragment != null && fragment.isInLayout()) {
            mTile.setName(tile.getName());
            mTile.setX(tile.getX());
            mTile.setY(tile.getY());
            mTile.setWidth(tile.getWidth());
            mTile.setHeight(tile.getHeight());
            fragment.updateView(mTile, mOldTileName);
        }
    }

    @Override
    public void updateText(Tile tile) {
        TilesFragment fragment = (TilesFragment) getFragmentManager()
                .findFragmentById(R.id.tiles_fragment_main);
        if (fragment != null && fragment.isInLayout()) {
            mTile.setTextX(tile.getTextX());
            mTile.setTextY(tile.getTextY());
            mTile.setTextSize(tile.getTextSize());
            mTile.setData(tile.getData());
            fragment.updateView(mTile, mOldTileName);
        }
    }

    @Override
    public void updateColor(Tile tile) {
        TilesFragment fragment = (TilesFragment) getFragmentManager()
                .findFragmentById(R.id.tiles_fragment_main);
        if (fragment != null && fragment.isInLayout()) {
            mTile.setColor(tile.getColor());
            mTile.setBorderColor(tile.getBorderColor());
            mTile.setTextColor(tile.getTextColor());
            mTile.setBorderSize(tile.getBorderSize());
            fragment.updateView(mTile, mOldTileName);
        }
    }

    @Override
    public void updateAction(Tile tile) {
        TilesFragment fragment = (TilesFragment) getFragmentManager()
                .findFragmentById(R.id.tiles_fragment_main);
        if (fragment != null && fragment.isInLayout()) {
            mTile.setSwitch(tile.isSwitch());
            mTile.setOnFirstClick(tile.getOnFirstClick());
            mTile.setOnSecondClick(tile.getOnSecondClick());
            fragment.updateView(mTile, mOldTileName);
        }
    }

    @Override
    public void onBackPressed() {
        if (mEditorLayout.getVisibility() == View.VISIBLE)
            mEditorLayout.setVisibility(View.GONE);
        else
            super.onBackPressed();
    }
}