package dolphin.android.apps.CpblCalendar.preference;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import dolphin.android.apps.CpblCalendar.provider.Game;

/**
 * Created by dolphin on 2013/8/31.
 */
public class AlarmHelper {
    public final static String TAG = "AlarmHelper";
    private Context mContext;
    private dolphin.android.preference.PreferenceUtils pHelper;
    //private SparseArray<Game> mAlarmArray = null;

    private final static String KEY_ALARM_LIST = "_alarm_list";

    /**
     * get key for Alarm ID
     *
     * @param game
     * @return
     */
    public static String getAlarmIdKey(Game game) {
        return String.format("%d-%d", game.StartTime.getTimeInMillis(), game.Id);
    }

    /**
     * get key for Alarm data
     *
     * @param game_key
     * @return
     */
    private static String getAlarmDataKey(String game_key) {
        return "_alarm_" + game_key;
    }

    public AlarmHelper(Context context) {
        mContext = context;
        pHelper = new dolphin.android.preference.PreferenceUtils(mContext);
        //mAlarmArray = new SparseArray<Game>();
    }

    /**
     * add a game to alarm list
     *
     * @param game
     */
    public void addGame(Game game) {
        if (game == null) {
            throw new NullPointerException("no game");
        }

        String key = getAlarmIdKey(game);
        Log.v(TAG, "addGame key: " + key);
        Set<String> keySet = pHelper.getStringSet(KEY_ALARM_LIST);
        if (keySet != null) {
            if (keySet.contains(key)) {
                Log.e(TAG, "already in alarm list");
                return;
            }
        } else {
            keySet = new HashSet<String>();
        }
        keySet.add(key);

        pHelper.putStringSet(KEY_ALARM_LIST, keySet);
        pHelper.putString(getAlarmDataKey(key), Game.toPrefString(game));
        //mAlarmArray.put(game.Id, game);//put to memory
    }

    /**
     * remove a game from alarm list
     *
     * @param game
     */
    public void removeGame(Game game) {
        removeGame(getAlarmIdKey(game));
    }

    /**
     * remove a game from alarm list
     *
     * @param key
     */
    private void removeGame(String key) {
        Log.v(TAG, "removeGame key: " + key);
        Set<String> keySet = pHelper.getStringSet(KEY_ALARM_LIST);
        if (keySet != null && keySet.contains(key)) {
            keySet.remove(key);
            pHelper.putStringSet(KEY_ALARM_LIST, keySet);
            pHelper.remove(getAlarmDataKey(key));
            //mAlarmArray.remove(Integer.decode(key.split("-")[1]));
        }
    }

    /**
     * update a game data in preference
     *
     * @param game
     */
    public void updateGame(Game game) {
        String key = getAlarmIdKey(game);
        //Log.d(TAG, "updateGame key: " + key);
        Set<String> keySet = pHelper.getStringSet(KEY_ALARM_LIST);
        if (keySet != null) {
            if (keySet.contains(key))//StartTime not change
                pHelper.putString(getAlarmDataKey(key), Game.toPrefString(game));
            else {//check game id, for some delayed games
                Iterator<String> iterator = keySet.iterator();
                while (iterator.hasNext()) {
                    key = iterator.next().toString();//StartTime-Id
                    if (Integer.decode(key.split("-")[1]) == game.Id) {
                        removeGame(key);//remove old entry
                        addGame(game);//put as new entry
                        break;
                    }
                }
            }
        }
    }

    /**
     * get entire alarm list
     *
     * @return
     */
    public ArrayList<Game> getAlarmList() {
        Set<String> keySet = pHelper.getStringSet(KEY_ALARM_LIST);
        if (keySet == null) {
            return null;
        }

        List<String> keys = new LinkedList<String>(keySet);
        Collections.sort(keys, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                //Log.d(TAG, s1 + " " + s2);
                String[] str1 = s1.split("-");
                String[] str2 = s2.split("-");
                if (str1[0].compareTo(str2[0]) == 0) {
                    return Integer.decode(str1[1]).compareTo(Integer.decode(str2[1]));
                }
                return Long.decode(str1[0]).compareTo(Long.decode(str2[0]));
            }
        });

        //mAlarmArray.clear();
        ArrayList<Game> list = new ArrayList<Game>();
        Calendar now = Calendar.getInstance();
        for (String key : keys) {
            Game g = Game.fromPrefString(mContext, pHelper.getString(getAlarmDataKey(key)));
            //Log.d(TAG, " " + g.Id + " " + g.StartTime.getTime().toString());
            //mAlarmArray.put(g.Id, g);//put to memory
            if (g.StartTime.after(now))
                list.add(g);
            else {//should remove from the list
                Log.w(TAG, String.format("game %d StartTime is passed.", g.Id));
                if (g.StartTime.get(Calendar.YEAR) < now.get(Calendar.YEAR))
                    removeGame(g);//remove last year's game
            }
        }

        return list;
    }

    /**
     * check if this game has set alarm
     *
     * @param game
     * @return
     */
    public boolean hasAlarm(Game game) {
        String key = getAlarmIdKey(game);
        //Log.d(TAG, "hasAlarm key: " + key);
        Set<String> keySet = pHelper.getStringSet(KEY_ALARM_LIST);
        if (keySet != null) {
            if (keySet.contains(key))//StartTime not change
                return true;
            else {//check game id, for some delayed games
                Iterator<String> iterator = keySet.iterator();
                while (iterator.hasNext()) {
                    key = iterator.next().toString();//StartTime-Id
                    if (Integer.decode(key.split("-")[1]) == game.Id) {
                        updateGame(game);//update the delayed info
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * get next alarm from the list
     *
     * @return
     */
    public Game getNextAlarm() {
        ArrayList<Game> list = getAlarmList();
        if (list != null) {//not empty list
            Calendar now = Calendar.getInstance();
            for (Game game : list) {
                //Log.d(TAG, " " + game.Id + " " + game.StartTime.getTime().toString());
                if (game.StartTime.after(now)) {
                    return game;
                }
                removeGame(game);//remove the game from alarm list
            }
            //return list.get(0);
        }
        return null;
    }

    public void clear() {
        ArrayList<Game> list = getAlarmList();
        if (list != null) {//not empty list
            for (Game game : list)
                removeGame(game);//remove the game from alarm list
        }
    }
}
