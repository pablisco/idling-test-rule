package com.pablisco.android.espesso;

import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.IdlingResource.ResourceCallback;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FluentIdlingResourceTest {

    public static final FluentIdlingResource.IsIdle IS_ALWAYS_IDLE = new FluentIdlingResource.IsIdle() {
        @Override
        public boolean isIdle() {
            return true;
        }
    };
    public static final FluentIdlingResource.IsIdle IS_NEVER_IDLE = new FluentIdlingResource.IsIdle() {
        @Override
        public boolean isIdle() {
            return false;
        }
    };

    @Test
    public void shouldNotBeIdle_whenFunctionIsNotIdle() throws Exception {
        IdlingResource idlingResource = FluentIdlingResource.idle(IS_NEVER_IDLE);

        boolean idleNow = idlingResource.isIdleNow();

        assertThat(idleNow).isFalse();
    }

    @Test
    public void shouldBeIdle_whenFunctionIsIdle() throws Exception {
        IdlingResource idlingResource = FluentIdlingResource.idle(IS_ALWAYS_IDLE);

        boolean idleNow = idlingResource.isIdleNow();

        assertThat(idleNow).isTrue();
    }

    @Test
    public void shouldTakeTypedIsIdleNowMethod() throws Exception {
        IdlingResource idlingResource = FluentIdlingResource.idle("", new FluentIdlingResource.IsIdleWithType<String>() {
            @Override
            public boolean isIdle(String value) {
                return value.isEmpty();
            }
        });

        boolean idleNow = idlingResource.isIdleNow();

        assertThat(idleNow).isTrue();
    }

    @Test
    public void shouldProvideUniqueName() throws Exception {
        IdlingResource idlingResourceA = FluentIdlingResource.idle(IS_ALWAYS_IDLE);
        IdlingResource idlingResourceB = FluentIdlingResource.idle(IS_ALWAYS_IDLE);

        String nameA = idlingResourceA.getName();
        String nameB = idlingResourceB.getName();

        assertThat(nameA).isNotEqualTo(nameB);
    }

    @Test
    public void shouldAllowToChangeName() throws Exception {
        String expectedName = "custom name";
        IdlingResource idlingResource = FluentIdlingResource.idle(IS_ALWAYS_IDLE)
                .withName(expectedName);

        String actualName = idlingResource.getName();

        assertThat(actualName).isEqualTo(expectedName);
    }

    @Test
    public void shouldReportToCallback_whenNotIdleAnymore() throws Exception {
        IdlingResource idlingResource = FluentIdlingResource.idle(IS_NEVER_IDLE);
        ResourceCallback callback = mock(ResourceCallback.class);
        idlingResource.registerIdleTransitionCallback(callback);

        idlingResource.isIdleNow();

        verify(callback).onTransitionToIdle();
    }

    @Test
    public void shouldAllowFluidCallback() throws Exception {
        ResourceCallback callback = mock(ResourceCallback.class);
        IdlingResource idlingResource = FluentIdlingResource.idle(IS_NEVER_IDLE)
                .withCallbacks(callback);

        idlingResource.isIdleNow();

        verify(callback).onTransitionToIdle();
    }
}