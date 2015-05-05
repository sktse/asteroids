package com.stephentse.asteroids.api.newrelic;

import android.content.Context;

import com.newrelic.agent.android.NewRelic;

/**
 * A free New Relic account does not allow any event or attribute tracking.
 * Only general performance, network, and activity time tracking, which is still really nice.
 * Plus, they also will send me a free t-shirt! Swag :)
 */
public class NewRelicWrapper {

    private static final String APP_TOKEN = "AA89b0c0142a827025ae1ae0dbd30a20b3fdb7fa8c";

    public static void initialize(Context context) {
        NewRelic.withApplicationToken(APP_TOKEN).start(context);
    }

    /* Do not add any metrics here. Free New Relic accounts do not support it */
}
