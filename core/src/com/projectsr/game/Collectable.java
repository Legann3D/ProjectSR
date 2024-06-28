package com.projectsr.game;

public class Collectable {
    private Hub hub;

    private int greenEssences = 10;
    private int redEssences = 10;
    public void addEssence(int amount, Essence.Type essenceType) {
        switch (essenceType) {
            case RED:
                redEssences += amount;
                hub.collectedItems++;
                hub.counterLabel.setText(String.valueOf(hub.collectedItems));
                break;

            case GREEN:
                greenEssences += amount;
                break;
        }
    }
    public void removeEssence(int amount, Essence.Type essenceType) {
        switch (essenceType) {
            case RED:
                redEssences -= amount;
                if (redEssences < 0) {
                    redEssences = 0;
                }
                break;

            case GREEN:
                greenEssences -= amount;
                if (greenEssences < 0) {
                    greenEssences = 0;
                }
                break;
        }
    }
    public int getEssences(Essence.Type essenceType) {
        switch (essenceType) {
            case RED:
                return redEssences;

            case GREEN:
                return greenEssences;

            default:
                return 0;
        }
    }
}
