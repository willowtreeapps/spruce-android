package com.willowtreeapps.spruce.dynamics;

/**
 * Hide this for now, in case we want to change the API.
 */
interface Force {
    // Acceleration based on position.
    float getAcceleration(float position, float velocity);

    boolean isAtEquilibrium(float value, float velocity);
}
