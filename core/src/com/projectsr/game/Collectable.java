package com.projectsr.game;

public class Collectable {
    mainGame game;
    private Hub hub;
    private int greenEssences;
    private int redEssences;

    /**
     * Set up and initialise the Collectable class attributes and type.
     *
     * @param hub The hub instance of the Hub class.
     */
    public Collectable(Hub hub) {
        this.hub = hub;
    }

    /**
     * If type of essence = Red is being picked up, increase the redEssence amount.
     * Additionally increment the collectedRedEssence variable inside the Hub.
     * If type of essence = Green is being picked up, increase the greenEssence amount.
     * Additionally increment the collectedGreenEssence variable inside the Hub.
     *
     * @param amount variable that stores the amount of essences collected
     * @param essenceType The type of essence that exist inside the Type enum.
     */
    public void addEssence(int amount, Essence.Type essenceType) {
        switch (essenceType) {
            case RED:
                redEssences += amount;
                hub.collectedRedEssence++;
                break;

            case GREEN:
                greenEssences += amount;
                hub.collectedGreenEssence++;
                break;
        }
    }

    /**
     * If type of essence = Red is being used or the player dies, decrease the redEssence amount.
     * If type of essence = Green is being used or the player dies,  decrease the greenEssence amount.
     *
     * @param amount variable that stores the amount of essences collected
     * @param essenceType The type of essence that exist inside the Type enum.
     */
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
    /**
     * Getter for redEssence variable
     * Getter for greenEssence variable
     *
     * @param essenceType The type of essence that exist inside the Type enum.
     */
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
