package android.example.com.visualizerpreferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.widget.Toast;

public class PrefernceFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener,Preference.OnPreferenceChangeListener {
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
        Preference minSizeEditTextPreference = findPreference(getString(R.string.ChangeSizeScaleDefaultKey));
        minSizeEditTextPreference.setOnPreferenceChangeListener(this);
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
          else if(preference instanceof EditTextPreference)
          {
              preference.setSummary(value);
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

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String key = preference.getKey();
        String value=(String)newValue;
        if(key.equals(getString(R.string.ChangeSizeScaleDefaultKey)))
        {
            Toast error = Toast.makeText(getContext(), "Erro on saving Min Size Scale Value", Toast.LENGTH_SHORT);
            try
            {
                int num = Integer.parseInt(value);
                if(num>3 || num<=0) {
                    error.show();
                    return  false;
                }
            }
            catch (Exception ex)
            {
                error.show();
                return false;
            }
        }
        return true;
    }
}
