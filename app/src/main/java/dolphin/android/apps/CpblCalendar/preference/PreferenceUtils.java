package dolphin.android.apps.CpblCalendar.preference;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.SparseArray;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import dolphin.android.apps.CpblCalendar.R;
import dolphin.android.apps.CpblCalendar.Utils;
import dolphin.android.apps.CpblCalendar.provider.Team;

/**
 * Created by dolphin on 2013/6/9.
 * <p/>
 * Helper class for accessing preference value
 */
public class PreferenceUtils {
    public static final String TAG = "Preference";

    //GeneralFragment
    public static final String KEY_APP_VERSION = "app_version";
    public static final String KEY_CPBL_WEB = "data_from_cpbl";
    public static final String KEY_TWBALL_WIKI = "data_from_twbsball";
    public static final String KEY_ZXC22 = "data_from_zxc22";
    public static final String KEY_LIB_ABS_WEB = "lib_actionbarsherlock";
    public static final String KEY_LIB_ABDT_GIT = "lib_npombourcq";
    public static final String KEY_RES_AAS = "res_asset_studio";//[13]++
    public static final String KEY_RES_ICONIC = "res_iconic_set";//[13]++
    public static final String KEY_LIB_SHOWCASE = "lib_showcaseview";//[18]++
    public static final String KEY_LIB_NINEOLD = "lib_nineold_android";//[18]++
    public static final String KEY_LIB_FAB = "lib_floating_action_button";//[129]++
    public static final String KEY_LIB_CIRCLE_IMAGE_VIEW = "lib_circle_image_view";//[168]++
    public static final String KEY_LIB_EVERNOTE_JOB = "lib_evernote_android_job";//[168]++

    //DisplayFragment
    public final static String KEY_DISPLAY_GROUP = "display_group";//[29]++
    public final static String KEY_UPCOMING_ON = "upcoming_game";
    public final static String KEY_TEAM_LOGO_ON = "team_logo";
    public final static String KEY_HIGHLIGHT_WIN = "highlight_win";
    public final static String KEY_HIGHLIGHT_TODAY = "highlight_today";
    public final static String KEY_INCLUDE_LEADER = "include_lead_board";//[29]++
    public final static String KEY_DISPLAY_EXTRA_GROUP = "display_extra_group";//[29]++
    public final static String KEY_FAVORITE_TEAMS = "favorite_teams";

    @Deprecated //ShowcaseView
    public final static String KEY_SHOWCASE_PHONE = "showcase_phone";
    //public final static String KEY_SHOWCASE_TABLET = "showcase_tablet";

    //ActionBar
    public final static String KEY_CACHE_MODE = "cache_mode";

    //[50]dolphin++ Notifications
    public final static String KEY_ENABLE_NOTIFICATION = "enable_notification";
    public final static String KEY_NOTIFICATION_GROUP = "notification_group";
    public final static String KEY_NOTIFY_TEAMS = "notify_teams";
    public final static String KEY_MANUAL_GAME_NOTIFY = "manual_game_notify";
    public final static String KEY_NOTIFY_ALARM = "notify_alarm";
    public final static String KEY_NOTIFY_RINGTONE = "notify_ringtone";//[183]++
    public final static String KEY_NOTIFY_PENDING_ACTION = "notify_pending_action";
    public final static String KEY_NOTIFY_DIALOG = "enable_notify_dialog";//[51]++
    public final static String KEY_NOTIFY_SONG = "enable_notify_song";//[122]++
    public final static String KEY_NOTIFY_SONG_LIST = "notify_song_list";//[139]++
    public final static String KEY_NOTIFY_VIBRATE = "enable_notify_vibrate";//[62]++

    //to start some key website
    public final static String URL_CPBL_OFFICAL_WEBSITE = "http://www.cpbl.com.tw/";
    public final static String URL_TW_BASEBALL_WIKI = "http://twbsball.dils.tku.edu.tw/";
    public final static String URL_ZXC22 = "http://zxc22.idv.tw/";
    public final static String URL_ACTIONBAR_SHERLOCK =
            "http://actionbarsherlock.com/index.html";
    public final static String URL_ACTIONBAR_DRAWER_TOGGLE =
            "https://gist.github.com/npombourcq/5636121";
    public final static String URL_ANDROID_ASSET_STUDIO = //[13]dolphin++
            "http://android-ui-utils.googlecode.com/hg/asset-studio/dist/index.html";
    public final static String URL_ICONIC_ICON_SET = //[13]dolphin++
            "http://somerandomdude.com/work/iconic/";
    //[18]dolphin++ ShowcaseView
    public final static String URL_SHOWCASE_VIEW =
            "http://espiandev.github.io/ShowcaseView/";
    //[18]dolphin++ NineOldAndroids
    public final static String URL_NINEOLD_ANDROID = "http://nineoldandroids.com/";

    //[129]dolphin++ FloatingActionButton
    public final static String URL_FLOATING_ACTION_BUTTON =
            "https://github.com/makovkastar/FloatingActionButton";

    //[129]dolphin++ FloatingActionButton
    public final static String URL_CIRCLE_IMAGE_VIEW =
            "https://github.com/hdodenhof/CircleImageView";

    //[188]dolphin++ Evernote Android Job library
    public final static String URL_EVERNOTE_ANDROID_JOB =
            "https://blog.evernote.com/tech/2015/10/26/unified-job-library-android/";

    /**
     * english build
     *
     * @param context Context
     * @return true if we are in engineer mode
     */
    public static boolean isEngineerMode(Context context) {
        return context.getResources().getBoolean(R.bool.pref_engineer_mode);
    }

    /**
     * show team logo in matchup
     *
     * @param context Context
     * @return true if we should show the team logo
     */
    public static boolean isTeamLogoShown(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getBoolean(KEY_TEAM_LOGO_ON,
                context.getResources().getBoolean(R.bool.def_team_logo));
    }

    /**
     * auto scroll to upcoming games
     *
     * @param context Context
     * @return true if we should scroll to upcoming games
     */
    public static boolean isUpComingOn(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getBoolean(KEY_UPCOMING_ON,
                context.getResources().getBoolean(R.bool.def_upcoming_game));
    }

    /**
     * highlight the winner
     *
     * @param context Context
     * @return tru if we should highlight the winner team
     */
    public static boolean isHighlightWin(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getBoolean(KEY_HIGHLIGHT_WIN,
                context.getResources().getBoolean(R.bool.def_highlight_win));
    }

    /**
     * get favorite teams
     *
     * @param context Context
     * @return favorite teams
     */
    public static SparseArray<Team> getFavoriteTeams(Context context) {
        SparseArray<Team> teams = new SparseArray<>();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {//SDK > 11
                dolphin.android.preference.PreferenceUtils utils =
                        new dolphin.android.preference.PreferenceUtils(context);
                Set<String> teamSet = getFavoriteTeams_pre(utils);
                if (teamSet != null) {
                    for (String team : teamSet) {
                        int id = Integer.parseInt(team);
                        //Log.d("PreferenceUtils", "id = " + id);
                        if (id == Team.ID_ELEPHANTS) {
                            id = Team.ID_ELEPHANTS;
                        } else if (id == Team.ID_UNI_LIONS) {//[184]++
                            id = Team.ID_UNI_711_LIONS;
                        }
                        teams.put(id, new Team(context, id));
                    }
                } else
                    throw new Exception("pre HONEYCOMB");
            } else
                throw new Exception("pre HONEYCOMB");
        } catch (Exception e) {
            //pre-Honeycomb devices, no favorite team options
            String[] teamSet = context.getResources().getStringArray(R.array.cpbl_team_id);
            for (String team : teamSet) {
                int id = Integer.parseInt(team);
                //Log.d("PreferenceUtils", "id = " + id);
                teams.put(id, new Team(context, id));
            }
        }

        return teams;
    }

    /***
     * pre-processing of favorite team list
     *
     * @param utils accessing to shared preference
     * @return updated list
     */
    public static Set<String> getFavoriteTeams_pre(dolphin.android.preference.PreferenceUtils utils) {
        Set<String> newSet = new HashSet<String>();
        Set<String> teamSet = utils.getStringSet(KEY_FAVORITE_TEAMS, null);
        if (teamSet != null) {
            for (String team : teamSet) {
                int id = Integer.parseInt(team);
                //2014: ID_ELEPHANTS -> ID_CT_ELEPHANTS
                if (id == Team.ID_ELEPHANTS) {
                    //http://goo.gl/sfsrWc http://goo.gl/KTDRdB
                    //teamSet.remove(String.valueOf(Team.ID_ELEPHANTS));
                    //iterator.remove();
                    //id = Team.ID_CT_ELEPHANTS;
                    newSet.add(String.valueOf(Team.ID_CT_ELEPHANTS));
                } else if (id == Team.ID_UNI_LIONS) {//[184]++
                    newSet.add(String.valueOf(Team.ID_UNI_711_LIONS));
                } else {
                    newSet.add(String.valueOf(id));
                }
            }
            utils.putStringSet(KEY_FAVORITE_TEAMS, newSet);
            return newSet;
        }
        return null;
    }

    /**
     * highlight today background
     *
     * @param context Context
     * @return true if we should highlight today
     */
    public static boolean isHighlightToday(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return isEngineerMode(context) || pref.getBoolean(KEY_HIGHLIGHT_TODAY,
                context.getResources().getBoolean(R.bool.def_highlight_today));
    }

    /**
     * show leader board layout in main screen
     *
     * @param context Context
     * @return true if we should include leader board on main UI
     */
    @Deprecated
    public static boolean isIncludeLeaderBoard(Context context) {
//        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
//        return isEngineerMode(context) ? true : pref.getBoolean(KEY_INCLUDE_LEADER,
//                context.getResources().getBoolean(R.bool.def_include_lead_board));
        return false;//[88]dolphin++ force to use menu item
    }

    /**
     * enable notification
     *
     * @param context Context
     * @return true if user enables notification
     */
    public static boolean isEnableNotification(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {//SDK < 11
            return false;
        }
        if (!context.getResources().getBoolean(R.bool.feature_notify))//[74]++
            return false;//not enabled function

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return isEngineerMode(context) || pref.getBoolean(KEY_ENABLE_NOTIFICATION,
                context.getResources().getBoolean(R.bool.def_enable_notification));
    }

    /**
     * set alarm time offset before a game
     *
     * @param context Context
     * @return time offset before a game for notification
     */
    public static int getAlarmNotifyTime(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return Integer.decode(pref.getString(KEY_NOTIFY_ALARM,
                context.getString(R.string.def_notify_alarm)));
    }

    /**
     * set notification auto dismiss
     */
    public final static int PENDING_ACTION_DISMISS = 0;
    /**
     * set notification to open activity
     */
    public final static int PENDING_ACTION_ACTIVITY = 1;

    /**
     * get notification pending action
     *
     * @param context Context
     * @return PENDING_ACTION_*
     */
    public static int getNotifyPendingAction(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return Integer.decode(pref.getString(KEY_NOTIFY_PENDING_ACTION,
                context.getString(R.string.def_notify_pending_action)));
    }

    /**
     * enable notification dialog
     *
     * @param context Context
     * @return true if user enable notification dialog
     */
    public static boolean isEnableNotifyDialog(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return isEngineerMode(context) || pref.getBoolean(KEY_NOTIFY_DIALOG,
                context.getResources().getBoolean(R.bool.def_enable_notify_dialog));
    }

    /**
     * enable notification song
     *
     * @param context Context
     * @return true if we should play the notification song
     */
    public static boolean isEnableNotifySong(Context context) {
        if (!context.getResources().getBoolean(R.bool.feature_enable_song)) {
            return false;
        }
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return isEngineerMode(context) || pref.getBoolean(KEY_NOTIFY_SONG,
                context.getResources().getBoolean(R.bool.def_enable_notify_song));
    }

    /**
     * get current notification song
     *
     * @param context Context
     * @return notification song file location
     */
    public static File getNotifySong(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return new File(context.getCacheDir(), pref.getString(PreferenceUtils.KEY_NOTIFY_SONG_LIST,
                context.getString(R.string.def_notify_song)));
    }

    /**
     * enable notification vibration
     *
     * @param context Context
     * @return true if user enables notification vibration
     */
    public static boolean isEnableNotifyVibrate(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return isEngineerMode(context) || pref.getBoolean(KEY_NOTIFY_VIBRATE,
                context.getResources().getBoolean(R.bool.def_notify_vibrate));
    }

    /**
     * enable cache mode
     *
     * @param context Context
     * @return true if we enable cache mode
     */
    public static boolean isCacheMode(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getBoolean(KEY_CACHE_MODE,
                context.getResources().getBoolean(R.bool.def_cache_mode));
    }

    /**
     * set cache mode
     *
     * @param context Context
     * @param enabled true if we enable cache mode
     */
    public static void setCacheMode(Context context, boolean enabled) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(KEY_CACHE_MODE, enabled);
        editor.apply();
    }

    /**
     * get notification Uri
     *
     * @param context Context
     * @return notification Uri
     */
    public static Uri getNotificationUri(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String path = pref.getString(KEY_NOTIFY_RINGTONE, null);
        return path != null ? Uri.parse(path) : null;
    }
}
