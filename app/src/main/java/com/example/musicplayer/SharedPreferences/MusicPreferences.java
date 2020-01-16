package com.example.musicplayer.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.musicplayer.R;

public class MusicPreferences {

    private static final String LAST_MUSIC = "last_music";
    private static final String MUSIC_DOMINANT_COLOR = "music_dominant_color";
    private static final String MUSIC_IS_SHUFFLE_ON = "music_is_shuffle_on";
    private static final String MUSIC_IS_LIST_LOOP = "music_is_list_loop";

    public static Long getLastMusic(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        return prefs.getLong(LAST_MUSIC, 0);
    }

    public static void setLastMusic(Context context, Long songId) {
        SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        prefs.edit().putLong(LAST_MUSIC, songId).apply();
    }

    public static Boolean getIsShuffle(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        return prefs.getBoolean(MUSIC_IS_SHUFFLE_ON, false);
    }

    public static void setMusicIsShuffleOn(Context context, Boolean isShuffle) {
        SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        prefs.edit().putBoolean(MUSIC_IS_SHUFFLE_ON, isShuffle).apply();
    }

    public static Boolean getIsListLoop(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        return prefs.getBoolean(MUSIC_IS_LIST_LOOP, false);
    }

    public static void setMusicIsListLoop(Context context, Boolean isLoop) {
        SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        prefs.edit().putBoolean(MUSIC_IS_LIST_LOOP, isLoop).apply();
    }

    public static int getMusicDominantColor(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        return prefs.getInt(MUSIC_DOMINANT_COLOR, context.getResources().getColor(R.color.default_background));
    }

    public static void setMusicDominantColor(Context context, int dominantColor) {
        SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        prefs.edit().putInt(MUSIC_DOMINANT_COLOR, dominantColor).apply();
    }
}
