package com.projectsr.game;

public class Settings {
    private static float volume = 1.0f;

    public static float getVolume() {
        return volume;
    }

    /**
     * Set the volume for the music in the game.
     *
     * @param volume The given volume to set.
     */
    public static void setVolume(float volume) {
        Settings.volume = volume;
    }
}
