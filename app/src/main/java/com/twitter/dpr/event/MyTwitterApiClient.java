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

import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.User;

import retrofit2.Call;
import retrofit2.http.Query;
import retrofit2.http.POST;

class MyTwitterApiClient extends TwitterApiClient {

    MyTwitterApiClient(Session session) {
        super((TwitterSession) session);
    }

    FriendshipsService getFriendshipsService() {
        return getService(FriendshipsService.class);
    }
}

interface FriendshipsService {
    @POST("/1.1/friendships/create.json")
    Call<User> create(@Query("screen_name") String screenName, @Query("user_id") String id, @Query("follow") boolean follow);
}
