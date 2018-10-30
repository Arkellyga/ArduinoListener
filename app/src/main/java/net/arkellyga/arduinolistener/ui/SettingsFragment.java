package net.arkellyga.arduinolistener.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import net.arkellyga.arduinolistener.R;
import net.arkellyga.arduinolistener.services.UpdateService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import static net.arkellyga.arduinolistener.utils.Consts.*;

public class SettingsFragment extends PreferenceFragment {

    PreferenceScreen mPreferenceScreenUpdate;
    Preference mPreferenceUpdater, mPreferenceVersion;
    SharedPreferences mSharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mPreferenceScreenUpdate = (PreferenceScreen) findPreference("update_screen");
        mPreferenceUpdater = findPreference("update_app");
        mPreferenceVersion = findPreference("app_version");
        mPreferenceScreenUpdate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Calendar calendar = Calendar.getInstance();
                String updateTime = "никогда";
                long time = mSharedPreferences.getLong(PREF_LAST_UPDATE, 0);
                if (time != 0) {
                    calendar.setTimeInMillis(time);
                    updateTime = calendar.getTime().toString();
                }
                mPreferenceUpdater.setSummary(String.format(getResources().getString(R.string.update_app_summary), updateTime));

                try {
                    PackageInfo info = getActivity().getPackageManager()
                            .getPackageInfo(getActivity().getPackageName(), 0);
                    String version = String.format(getResources().getString(R.string.version), info.versionCode);
                    mPreferenceVersion.setTitle(version);
                } catch (PackageManager.NameNotFoundException ex) {
                    ex.printStackTrace();
                }
                return false;
            }
        });

        mPreferenceUpdater.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                getActivity().startService(new Intent(getActivity(), UpdateService.class));
                return true;
            }
        });

        findPreference("export_settings").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                try {
                    String outputpath = Environment.getExternalStorageDirectory() + "/CarManager";
                    Log.d("Settings", "onPreferenceClick: " + outputpath);
                    File dir = new File(outputpath);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    FileInputStream in = new FileInputStream(getActivity().getFileStreamPath("tiles.xml"));
                    FileOutputStream out = new FileOutputStream(outputpath + "/tiles.xml");

                    byte[] buffer = new byte[1024];
                    int read;
                    while ((read = in.read(buffer)) != -1) {
                        out.write(buffer, 0, read);
                    }
                    in.close();
                    in = null;

                    out.flush();
                    out.close();
                    out = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getActivity(), getResources().getString(R.string.notify_done), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
}
