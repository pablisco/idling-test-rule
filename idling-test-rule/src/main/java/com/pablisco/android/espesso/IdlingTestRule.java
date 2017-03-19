package com.pablisco.android.espesso;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class IdlingTestRule implements TestRule {

    private List<IdlingResource> idlingResources = new LinkedList<>();

    @Override
    public Statement apply(final Statement base, Description description) {
        IdlingResource[] idlingResources = this.idlingResources.toArray(new IdlingResource[this.idlingResources.size()]);
        return new IdlingResourcesStatement(base, idlingResources);
    }

    public static IdlingTestRule idlingTestRule() {
        return new IdlingTestRule();
    }

    public IdlingTestRule idleWith(IdlingResource... idlingResources) {
        this.idlingResources.addAll(Arrays.asList(idlingResources));
        return this;
    }

    private class IdlingResourcesStatement extends Statement {

        private final Statement base;
        private IdlingResource[] idlingResources;

        private IdlingResourcesStatement(Statement base, IdlingResource... idlingResources) {
            this.base = base;
            this.idlingResources = idlingResources;
        }

        @Override
        public void evaluate() throws Throwable {
            Espresso.registerIdlingResources(idlingResources);
            base.evaluate();
            Espresso.unregisterIdlingResources(idlingResources);
        }
    }

}
