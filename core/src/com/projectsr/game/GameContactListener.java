package com.projectsr.game;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;


public class GameContactListener implements ContactListener {

    public static final short ENEMY_CATEGORY = 0x0002;
    public static final short ENEMY_ATTACK_CATEGORY = 0x0004;
    public static final short ESSENCE_CATEGORY = 0x0001;

    public static final short PLAYER_CATEGORY = 0x0006;
    public static final short PLAYER_ATTACK_CATEGORY = 0x00010;

    public static final short MAP_CATEGORY = 0x0008;

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        // Check if both fixtures are enemies
        if (isEnemyFixture(fixtureA) && isEnemyFixture(fixtureB)) {
            contact.setEnabled(false);  // Disable the collision response
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        // Handle post-solve
    }

    /**
     * Check if the fixture is part of the enemy category.
     *
     * @param fixture The fixture that is being checked for collision.
     * @return The fixture category.
     */
    public static boolean isEnemyFixture(Fixture fixture) {
        return fixture.getFilterData().categoryBits == ENEMY_CATEGORY;
    }

    /**
     * Check if the fixture is part of the player attack category.
     *
     * @param fixture The fixture that is being checked for collision.
     * @return The fixture category.
     */
    public static boolean isPlayerAttackFixture(Fixture fixture) {
        return fixture.getFilterData().categoryBits == PLAYER_ATTACK_CATEGORY;
    }

    /**
     * Check if the fixture is part of the player category.
     *
     * @param fixture The fixture that is being checked for collision.
     * @return The fixture category.
     */
    public static boolean isPlayerFixture(Fixture fixture) {
        return fixture.getFilterData().categoryBits == PLAYER_CATEGORY;
    }

    /**
     * Check if the fixture is part of the enemy attack category.
     *
     * @param fixture The fixture that is being checked for collision.
     * @return The fixture category.
     */
    public static boolean isEnemyAttackFixture(Fixture fixture) {
        return fixture.getFilterData().categoryBits == ENEMY_ATTACK_CATEGORY;
    }

    /**
     * Check if the fixture is part of the essence category.
     *
     * @param fixture The fixture that is being checked for collision.
     * @return The fixture category.
     */
    public static boolean isEssenceFixture(Fixture fixture) {
        return fixture.getFilterData().categoryBits == ESSENCE_CATEGORY;
    }

    /**
     * Check if the fixture is part of the map category.
     *
     * @param fixture The fixture that is being checked for collision.
     * @return The fixture category.
     */
    public static boolean isMapFixture(Fixture fixture) {
        return fixture.getFilterData().categoryBits == MAP_CATEGORY;
    }
}

