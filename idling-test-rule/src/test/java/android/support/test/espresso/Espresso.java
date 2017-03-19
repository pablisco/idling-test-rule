package android.support.test.espresso;

import java.util.LinkedList;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Test double from library: `com.android.support.test.espresso:espresso-core`
 */
public class Espresso {

    private static List<IdlingResource> idlingResources = new LinkedList<>();

    public static boolean registerIdlingResources(IdlingResource... resources) {
        idlingResources.addAll(asList(resources));
        return true;
    }

    public static boolean unregisterIdlingResources(IdlingResource... resources) {
        idlingResources.removeAll(asList(resources));
        return true;
    }

    public static List<IdlingResource> getIdlingResources() {
        return idlingResources;
    }

    public static void clear() {
        idlingResources.clear();
    }

}
