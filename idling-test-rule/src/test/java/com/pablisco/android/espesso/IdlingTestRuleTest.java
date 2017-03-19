package com.pablisco.android.espesso;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.LinkedList;
import java.util.List;

import static com.pablisco.mockito.MoreMockito.stub;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class IdlingTestRuleTest {

    @After
    public void tearDown() throws Exception {
        Espresso.clear();
    }

    @Test
    public void shouldRegisterIdlingResource() throws Throwable {
        IdlingResourcesStatementCaptor idlingResourcesCaptor = new IdlingResourcesStatementCaptor();
        IdlingResource expectedIdlingResource = stub(IdlingResource.class);
        Statement statement = statementWith(idlingResourcesCaptor, expectedIdlingResource);

        statement.evaluate();

        assertThat(idlingResourcesCaptor.getIdlingResources()).containsExactly(expectedIdlingResource);
    }

    @Test
    public void shouldUnregisterIdlingResource() throws Throwable {
        Statement statement = statementWith(stub(Statement.class), stub(IdlingResource.class));

        statement.evaluate();

        assertThat(Espresso.getIdlingResources()).isEmpty();
    }

    @Test
    public void shouldRegisterMultipleIdlingResources() throws Throwable {
        IdlingResourcesStatementCaptor idlingResourcesCaptor = new IdlingResourcesStatementCaptor();
        IdlingResource idlingResourceA = stub(IdlingResource.class);
        IdlingResource idlingResourceB = stub(IdlingResource.class);
        Statement statement = statementWith(idlingResourcesCaptor, idlingResourceA, idlingResourceB);

        statement.evaluate();

        assertThat(idlingResourcesCaptor.getIdlingResources()).containsExactly(idlingResourceA, idlingResourceB);
    }

    private Statement statementWith(Statement statement, IdlingResource... idlingResource) {
        IdlingTestRule idlingTestRule = IdlingTestRule.idlingTestRule().idleWith(idlingResource);
        return idlingTestRule.apply(statement, stub(Description.class));
    }

    private static class IdlingResourcesStatementCaptor extends Statement {
        private final List<IdlingResource> idlingResources = new LinkedList<>();

        private IdlingResourcesStatementCaptor() {
        }

        @Override
        public void evaluate() throws Throwable {
            idlingResources.addAll(Espresso.getIdlingResources());
        }

        private List<IdlingResource> getIdlingResources() {
            return idlingResources;
        }
    }
}