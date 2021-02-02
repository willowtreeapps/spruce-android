package com.willowtreeapps.spruce.dynamics;

import android.util.FloatProperty;

import androidx.annotation.RequiresApi;

/**
 * <p>FloatPropertyCompat is an abstraction that can be used to represent a mutable float value that
 * is held in a host object. To access this float value, {@link #setValue(Object, float)} and getter
 * {@link #getValue(Object)} need to be implemented. Both the setter and the getter take the
 * primitive <code>float</code> type and avoids autoboxing and other overhead associated with the
 * <code>Float</code> class.
 *
 * <p>For API 24 and later, {@link FloatProperty} instances can be converted to
 * {@link FloatPropertyCompat} through
 * {@link FloatPropertyCompat#createFloatPropertyCompat(FloatProperty)}.
 *
 * @param <T> the class on which the Property is declared
 */
public abstract class FloatPropertyCompat<T> {
    final String mPropertyName;

    /**
     * A constructor that takes an identifying name.
     */
    public FloatPropertyCompat(String name) {
        mPropertyName = name;
    }

    /**
     * Create a {@link FloatPropertyCompat} wrapper for a {@link FloatProperty} object. The new
     * {@link FloatPropertyCompat} instance will access and modify the property value of
     * {@link FloatProperty} through the {@link FloatProperty} instance's setter and getter.
     *
     * @param property FloatProperty instance to be wrapped
     * @param <T> the class on which the Property is declared
     * @return a new {@link FloatPropertyCompat} wrapper for the given {@link FloatProperty} object
     */
    @RequiresApi(24)
    public static <T> FloatPropertyCompat<T> createFloatPropertyCompat(
            final FloatProperty<T> property) {
        return new FloatPropertyCompat<T>(property.getName()) {
            @Override
            public float getValue(T object) {
                return property.get(object);
            }

            @Override
            public void setValue(T object, float value) {
                property.setValue(object, value);
            }
        };
    }

    /**
     * Returns the current value that this property represents on the given <code>object</code>.
     *
     * @param object object which this property represents
     * @return the current property value of the given object
     */
    public abstract float getValue(T object);

    /**
     * Sets the value on <code>object</code> which this property represents.
     *
     * @param object object which this property represents
     * @param value new value of the property
     */
    public abstract void setValue(T object, float value);
}
