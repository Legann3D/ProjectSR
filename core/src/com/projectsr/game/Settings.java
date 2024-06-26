package com.projectsr.game;

public class Settings {
    private static float volume = 1.0f;

    public static float getVolume() {
        return volume;
    }

    public static void setVolume(float volume) {
        Settings.volume = volume;
    }
}
