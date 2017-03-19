package com.pablisco.android.espesso;

import android.support.test.espresso.IdlingResource;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class FluentIdlingResource implements IdlingResource {

    private final IsIdle isIdle;
    private final String name;
    private final List<ResourceCallback> callbacks;

    private FluentIdlingResource(IsIdle isIdle) {
        this(isIdle, UUID.randomUUID().toString());
    }

    private FluentIdlingResource(IsIdle isIdle, String name) {
        this(isIdle, name, new ArrayList<ResourceCallback>());
    }

    private FluentIdlingResource(IsIdle isIdle, String name, List<ResourceCallback> callbacks) {
        this.isIdle = isIdle;
        this.name = name;
        this.callbacks = callbacks;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isIdleNow() {
        boolean idle = isIdle.isIdle();
        if (!idle) {
            for (ResourceCallback callback : callbacks) {
                callback.onTransitionToIdle();
            }
        }
        return idle;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.callbacks.add(callback);
    }

    public static FluentIdlingResource idle(IsIdle isIdle) {
        return new FluentIdlingResource(isIdle);
    }

    public static <T> FluentIdlingResource idle(final T value, final IsIdleWithType<T> isIdleWithType) {
        return new FluentIdlingResource(new IsIdleWithTypeAdapter<>(isIdleWithType, value));
    }

    public FluentIdlingResource withName(String name) {
        return new FluentIdlingResource(isIdle, name);
    }

    public FluentIdlingResource withCallbacks(ResourceCallback callback) {
        List<ResourceCallback> newCallbacks = new ArrayList<>(this.callbacks);
        newCallbacks.add(callback);
        return new FluentIdlingResource(isIdle, name, newCallbacks);
    }

    public interface IsIdle {
        boolean isIdle();
    }

    public interface IsIdleWithType<T> {
        boolean isIdle(T value);
    }

    private static class IsIdleWithTypeAdapter<T> implements IsIdle {
        private final IsIdleWithType<T> isIdleWithType;
        private final T value;

        private IsIdleWithTypeAdapter(IsIdleWithType<T> isIdleWithType, T value) {
            this.isIdleWithType = isIdleWithType;
            this.value = value;
        }

        @Override
        public boolean isIdle() {
            return isIdleWithType.isIdle(value);
        }
    }
}
