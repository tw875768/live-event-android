/**
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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

import retrofit2.Call;

public class MainActivity extends ActionBarActivity {

    public static final String USER_HANDLE_EXTRA = "user_handle_extra";
    private TwitterLoginButton loginButton;
    private String SEARCH_QUERY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        SEARCH_QUERY = getResources().getString(R.string.twitter_search);

        setUpLoginButton();
        setUpTimeline();
    }

    private void setUpLoginButton() {

        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                invalidateOptionsMenu();
            }

            @Override
            public void failure(TwitterException exception) {
                invalidateOptionsMenu();
            }
        });

    }

    private void setUpTimeline() {
        SearchTimeline searchTimeline = new SearchTimeline.Builder().query(SEARCH_QUERY).build();

        final TweetTimelineListAdapter timelineAdapter = new TweetTimelineListAdapter(this, searchTimeline);

        ListView timelineView = (ListView) findViewById(R.id.event_timeline);
        timelineView.setEmptyView(findViewById(R.id.empty_timeline));
        timelineView.setAdapter(timelineAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
            } else {
                follow(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            loginButton.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        updateFeatures(menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        updateFeatures(menu);
        return super.onPrepareOptionsMenu(menu);
    }

    private void updateFeatures(Menu menu) {
        if (TwitterCore.getInstance().getSessionManager().getActiveSession() != null) {
            MenuItem item = menu.findItem(R.id.action_follow);
            item.setVisible(true);
            item = menu.findItem(R.id.action_share_handle);
            item.setVisible(true);
            item = menu.findItem(R.id.action_account_to_follow);
            item.setTitle(getResources().getString(R.string.menu_account_to_follow) + " @" + getResources().getString(R.string.account_to_follow));
            item.setVisible(true);
            item = menu.findItem(R.id.action_sign_out);
            item.setVisible(true);
            loginButton.setVisibility(View.GONE);
        } else {
            MenuItem item = menu.findItem(R.id.action_follow);
            item.setVisible(false);
            item = menu.findItem(R.id.action_share_handle);
            item.setVisible(false);
            item = menu.findItem(R.id.action_account_to_follow);
            item.setVisible(false);
            item = menu.findItem(R.id.action_sign_out);
            item.setVisible(false);
            loginButton.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create_tweet:
                createTweet();
                return true;
            case R.id.action_follow:
                scanToFollow();
                return true;
            case R.id.action_share_handle:
                shareHandle();
                return true;
            case R.id.action_account_to_follow:
                follow(getResources().getString(R.string.account_to_follow));
                return true;
            case R.id.action_about:
                about();
                return true;
            case R.id.action_sign_out:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createTweet() {
        final TweetComposer.Builder builder =
                new TweetComposer.Builder(this).text(getApplicationContext().getResources()
                        .getString(R.string.hashtag));
        builder.show();
    }

    private void scanToFollow() {
        IntentIntegrator ii = new IntentIntegrator(this);
        ii.setPrompt(getResources().getString(R.string.scan_prompt));
        ii.initiateScan();
    }

    private void follow(String userHandle) {
        MyTwitterApiClient mtac = new MyTwitterApiClient(TwitterCore.getInstance().getSessionManager().getActiveSession());
        Call<User> call = mtac.getFriendshipsService().create(userHandle, null, false);
        call.enqueue(new Callback<User>() {
            @Override
            public void success(Result<User> result) {
                Toast.makeText(MainActivity.this,
                        getResources().getString(R.string.toast_follow_user_success) + " @" + result.data.screenName,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(TwitterException e) {
                Toast.makeText(MainActivity.this,
                        getResources().getString(R.string.toast_follow_user_error),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void shareHandle() {
        final Intent intent = new Intent(this, ShareHandleActivity.class);
        intent.putExtra(USER_HANDLE_EXTRA, TwitterCore.getInstance().getSessionManager().getActiveSession().getUserName());
        startActivity(intent);
    }

    private void about() {
        final Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    private void signOut() {
        TwitterCore.getInstance().getSessionManager().clearActiveSession();
        invalidateOptionsMenu();
        Toast.makeText(MainActivity.this,
                getResources().getString(R.string.toast_sign_out),
                Toast.LENGTH_SHORT).show();
    }
}
