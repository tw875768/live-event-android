/*
 * Copyright (C) 2015 Twitter Inc and other contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.twitter.dpr.event;

import android.app.Application;

import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.GuestSession;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterAuthConfig;

public class App extends Application {

    private static final String TWITTER_KEY = BuildConfig.CONSUMER_KEY;
    private static final String TWITTER_SECRET = BuildConfig.CONSUMER_SECRET;
    public GuestSession guestAppSession = null;

    @Override
    public void onCreate() {
        super.onCreate();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        TwitterConfig config = new TwitterConfig.Builder(this).twitterAuthConfig(authConfig).build();
        Twitter.initialize(config);
    }

    public GuestSession getGuestAppSession() {
        return guestAppSession;
    }

    public void setGuestAppSession(GuestSession guestAppSession) {
        this.guestAppSession = guestAppSession;
    }
}
