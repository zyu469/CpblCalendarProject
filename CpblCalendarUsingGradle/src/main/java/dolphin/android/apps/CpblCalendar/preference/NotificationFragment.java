package dolphin.android.apps.CpblCalendar.preference;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.preference.PreferenceScreen;
import android.util.Log;

import dolphin.android.apps.CpblCalendar.NotifyReceiver;
import dolphin.android.apps.CpblCalendar.R;

/**
 * Created by dolphin on 2013/8/31.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class NotificationFragment extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener {
    private boolean mNotifyTeams;
    private String[] mPendingActions;
    private String[] mNotifyAlarms;
    private String[] mNotifyAlarmValues;//[54]dolphin++
    private AlarmHelper mHelper;

    private String KEY_DEBUG_LIST = "debug_alarm_list";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs_notification);

        final Activity activity = getActivity();
        final Resources resources = activity.getResources();
        mHelper = new AlarmHelper(activity);
        //[50]dolphin++ currently only support manual register alarm
        mNotifyTeams = resources.getBoolean(R.bool.feature_notify_teams);
        if (!mNotifyTeams) {
            PreferenceGroup group =
                    (PreferenceGroup) findPreference(PreferenceUtils.KEY_NOTIFICATION_GROUP);
            if (group != null) {
                Preference pref = findPreference(PreferenceUtils.KEY_NOTIFY_TEAMS);
                if (pref != null)
                    group.removePreference(pref);
                pref = findPreference(PreferenceUtils.KEY_MANUAL_GAME_NOTIFY);
                if (pref != null)
                    group.removePreference(pref);
            }
        }

        mPendingActions = resources.getStringArray(R.array.notify_pending_action);
        mNotifyAlarms = resources.getStringArray(R.array.notify_time);
        mNotifyAlarmValues = resources.getStringArray(R.array.notify_time_value);//[54]dolphin++
        Preference p1 = findPreference(PreferenceUtils.KEY_NOTIFY_PENDING_ACTION);
        if (p1 != null) {
            p1.setSummary(mPendingActions[PreferenceUtils.getNotifyPendingAction(activity)]);
            p1.setOnPreferenceChangeListener(this);
        }
        Preference p2 = findPreference(PreferenceUtils.KEY_NOTIFY_ALARM);
        if (p2 != null) {
            int i = getAlarmTimeIndex(PreferenceUtils.getAlarmNotifyTime(activity));
            p2.setSummary(mNotifyAlarms[i]);
            p2.setOnPreferenceChangeListener(this);
        }

        Preference p3 = findPreference(PreferenceUtils.KEY_ENABLE_NOTIFICATION);
        if (p3 != null)
            p3.setOnPreferenceChangeListener(this);

        //for debug version, add show existing alarms
        Preference pList = findPreference(KEY_DEBUG_LIST);
        if (pList != null && !PreferenceUtils.isEngineerMode(getActivity()))
            getPreferenceScreen().removePreference(pList);

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        String key = preference.getKey();
        if (key == null)
            return false;
        else if (key.equalsIgnoreCase(PreferenceUtils.KEY_NOTIFY_PENDING_ACTION)) {
            preference.setSummary(mPendingActions[Integer.parseInt(o.toString())]);
        } else if (key.equalsIgnoreCase(PreferenceUtils.KEY_NOTIFY_ALARM)) {
            int i = getAlarmTimeIndex(Integer.parseInt(o.toString()));
            preference.setSummary(mNotifyAlarms[i]);
        } else if (key.equalsIgnoreCase(PreferenceUtils.KEY_ENABLE_NOTIFICATION)) {
            //delete all pending alarms and cancel the notifications
            if (!Boolean.parseBoolean(o.toString())) {//[57]dolphin++
                Log.w(AlarmHelper.TAG, "clear all alarms");
                mHelper.clear();
                NotifyReceiver.cancelAlarm(getActivity(), null);
            }
        }
        return true;
    }

    private int getAlarmTimeIndex(int value) {
        for (int i = 0; i < mNotifyAlarmValues.length; i++) {
            if (Integer.parseInt(mNotifyAlarmValues[i]) == value)
                return i;
        }
        return 0;
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
                                         Preference preference) {
        if (preference.getKey().equalsIgnoreCase(KEY_DEBUG_LIST)) {
            return true;
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
}
