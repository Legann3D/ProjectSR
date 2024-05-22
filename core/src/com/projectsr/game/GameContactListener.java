package com.projectsr.game;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;


public class GameContactListener implements ContactListener {

    public static final short ENEMY_CATEGORY = 0x0002;

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        System.out.println("Contact began between: " + fixtureA + " and " + fixtureB);
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        System.out.println("Contact ended between: " + fixtureA + " and " + fixtureB);
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
     *
     * @param fixture The fixture that is being checked for collision.
     * @return The fixture category.
     */
    // TODO: Convert to generic method if planning to use for player
    public static boolean isEnemyFixture(Fixture fixture) {
        return fixture.getFilterData().categoryBits == ENEMY_CATEGORY; // TODO: Create getter in Enemy class
    }
}

