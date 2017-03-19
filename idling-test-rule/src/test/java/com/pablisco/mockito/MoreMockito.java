package com.pablisco.mockito;

import org.mockito.MockSettings;
import org.mockito.Mockito;

import static org.mockito.Mockito.withSettings;

public class MoreMockito {

    private MoreMockito() {
        // hide constructor, utils type
    }

    public static <T> T stub(Class<T> type) {
        return stub(type, withSettings());
    }

    public static <T> T stub(Class<T> type, MockSettings mockSettings) {
        return Mockito.mock(type, mockSettings.stubOnly());
    }

}
