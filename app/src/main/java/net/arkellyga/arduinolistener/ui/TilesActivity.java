package net.arkellyga.arduinolistener.ui;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class TilesActivity extends AppCompatActivity {
    private static final String TAG = "TilesActivity";
    private static final String EXTRA_NAME = "name";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
            String tileName = intent.getStringExtra(EXTRA_NAME);
            if (tileName != null)
                setFragment(EditTileFragment.createInstance(tileName));
            else
                setFragment(new TilesList());
    }

    private void setFragment(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, fragment)
                .commit();
    }

    public static Intent startEditFragment(String name, Context context) {
        Intent intent = new Intent(context, TilesActivity.class);
        intent.putExtra(EXTRA_NAME, name);
        return intent;
    }
}
