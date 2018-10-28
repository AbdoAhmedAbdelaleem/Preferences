package android.example.com.visualizerpreferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

public class PrefernceFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    PreferenceScreen preferenceScreen;
    SharedPreferences sharedPreferences;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preference_screen_layout);
        preferenceScreen = getPreferenceScreen();
        sharedPreferences = preferenceScreen.getSharedPreferences();
        int preferenceCount = preferenceScreen.getPreferenceCount();
        for (int i = 0; i < preferenceCount; i++)
        {
            Preference preference = preferenceScreen.getPreference(i);
            if(!( preference instanceof CheckBoxPreference)) {
                String s = sharedPreferences.getString(preference.getKey(), "");
                setSummary(preference, s);
            }
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    public void setSummary(Preference preference, String value)
    {
          if(preference instanceof ListPreference)
          {
              ListPreference listPreference=(ListPreference)preference;
              int indexOfValue = listPreference.findIndexOfValue(value);
              if(indexOfValue!=-1)
              {
                  listPreference.setSummary(listPreference.getEntries()[indexOfValue]);
              }
          }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String k) {
        Preference preference = preferenceScreen.findPreference(k);
        if(!(preference instanceof CheckBoxPreference))
        {
            String value = sharedPreferences.getString(k, "");
            setSummary(preference,value);
        }
    }
}
