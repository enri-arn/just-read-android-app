package it.unito.di.justread.fragments;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.RingtonePreference;
import android.preference.SwitchPreference;
import android.util.Log;

import it.unito.di.justread.R;

public class SettingScreenFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

    SwitchPreference switchPreference;
    RingtonePreference ringtonePreference;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_setting);
        SwitchPreference switchPreference=(SwitchPreference) findPreference("switch_notify");
        switchPreference.setChecked(true);
        RingtonePreference ringtonePreference=(RingtonePreference) findPreference("ring_sound");
        switchPreference.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        Log.i("TAG", "Ehi, funziona!!");
        if(switchPreference.getDisableDependentsState())
            ringtonePreference.setEnabled(true);
        else
            ringtonePreference.setEnabled(false);
        return true;
    }
}
