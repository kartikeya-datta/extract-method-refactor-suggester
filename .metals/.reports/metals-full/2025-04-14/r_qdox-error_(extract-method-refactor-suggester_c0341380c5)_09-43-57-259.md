error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7834.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7834.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7834.java
text:
```scala
private b@@oolean followingStatusChecked;

/*
 * Copyright 2012 GitHub Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.mobile.ui.user;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;
import static com.github.mobile.Intents.EXTRA_USER;
import static com.github.mobile.util.TypefaceUtils.ICON_FOLLOW;
import static com.github.mobile.util.TypefaceUtils.ICON_NEWS;
import static com.github.mobile.util.TypefaceUtils.ICON_PUBLIC;
import static com.github.mobile.util.TypefaceUtils.ICON_WATCH;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.github.kevinsawicki.wishlist.ViewUtils;
import com.github.mobile.Intents.Builder;
import com.github.mobile.R.id;
import com.github.mobile.R.layout;
import com.github.mobile.R.menu;
import com.github.mobile.R.string;
import com.github.mobile.core.user.FollowUserTask;
import com.github.mobile.core.user.FollowingUserTask;
import com.github.mobile.core.user.RefreshUserTask;
import com.github.mobile.core.user.UnfollowUserTask;
import com.github.mobile.ui.TabPagerActivity;
import com.github.mobile.util.AvatarLoader;
import com.github.mobile.util.ToastUtils;
import com.google.inject.Inject;

import org.eclipse.egit.github.core.User;

import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

/**
 * Activity to view a user's various pages
 */
public class UserViewActivity extends TabPagerActivity<UserPagerAdapter>
        implements OrganizationSelectionProvider {

    /**
     * Create intent for this activity
     *
     * @param user
     * @return intent
     */
    public static Intent createIntent(User user) {
        return new Builder("user.VIEW").user(user).toIntent();
    }

    @Inject
    private AvatarLoader avatars;

    @InjectExtra(EXTRA_USER)
    private User user;

    @InjectView(id.pb_loading)
    private ProgressBar loadingBar;

    private boolean isFollowing;

    private boolean followingStatusChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(user.getLogin());

        if (user.getAvatarUrl() != null)
            configurePager();
        else {
            ViewUtils.setGone(loadingBar, false);
            setGone(true);
            new RefreshUserTask(this, user.getLogin()) {

                @Override
                protected void onSuccess(User fullUser) throws Exception {
                    super.onSuccess(fullUser);

                    user = fullUser;
                    configurePager();
                }

                @Override
                protected void onException(Exception e) throws RuntimeException {
                    super.onException(e);

                    ToastUtils.show(UserViewActivity.this,
                            string.error_person_load);
                    ViewUtils.setGone(loadingBar, true);
                }
            }.execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu optionsMenu) {
        getSupportMenuInflater().inflate(menu.user_follow, optionsMenu);

        return super.onCreateOptionsMenu(optionsMenu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem followItem = menu.findItem(id.m_follow);

        followItem.setVisible(followingStatusChecked);
        followItem.setTitle(isFollowing ? string.unfollow : string.follow);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case id.m_follow:
            followUser();
            return true;
        case android.R.id.home:
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }

    }

    private void configurePager() {
        avatars.bind(getSupportActionBar(), user);
        configureTabPager();
        ViewUtils.setGone(loadingBar, true);
        setGone(false);
        checkFollowingUserStatus();
    }

    @Override
    public User addListener(OrganizationSelectionListener listener) {
        return user;
    }

    @Override
    public OrganizationSelectionProvider removeListener(
            OrganizationSelectionListener listener) {
        return this;
    }

    @Override
    protected UserPagerAdapter createAdapter() {
        return new UserPagerAdapter(this);
    }

    @Override
    protected int getContentView() {
        return layout.tabbed_progress_pager;
    }

    @Override
    protected String getIcon(int position) {
        switch (position) {
        case 0:
            return ICON_NEWS;
        case 1:
            return ICON_PUBLIC;
        case 2:
            return ICON_WATCH;
        case 3:
            return ICON_FOLLOW;
        default:
            return super.getIcon(position);
        }
    }

    private void followUser() {
        if (isFollowing)
            new UnfollowUserTask(this, user.getLogin()) {

                @Override
                protected void onSuccess(User user) throws Exception {
                    super.onSuccess(user);

                    isFollowing = !isFollowing;
                }

                @Override
                protected void onException(Exception e) throws RuntimeException {
                    super.onException(e);

                    ToastUtils.show(UserViewActivity.this,
                            string.error_unfollowing_person);
                }
            }.start();
        else
            new FollowUserTask(this, user.getLogin()) {

                @Override
                protected void onSuccess(User user) throws Exception {
                    super.onSuccess(user);

                    isFollowing = !isFollowing;
                }

                @Override
                protected void onException(Exception e) throws RuntimeException {
                    super.onException(e);

                    ToastUtils.show(UserViewActivity.this,
                            string.error_following_person);
                }
            }.start();
    }

    private void checkFollowingUserStatus() {
        followingStatusChecked = false;
        new FollowingUserTask(this, user.getLogin()) {

            @Override
            protected void onSuccess(Boolean following) throws Exception {
                super.onSuccess(following);

                isFollowing = following;
                followingStatusChecked = true;
                invalidateOptionsMenu();
            }
        }.execute();
    }
}
```

```



#### Error stacktrace:

```
com.thoughtworks.qdox.parser.impl.Parser.yyerror(Parser.java:2025)
	com.thoughtworks.qdox.parser.impl.Parser.yyparse(Parser.java:2147)
	com.thoughtworks.qdox.parser.impl.Parser.parse(Parser.java:2006)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:232)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:190)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:94)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:89)
	com.thoughtworks.qdox.library.SortedClassLibraryBuilder.addSource(SortedClassLibraryBuilder.java:162)
	com.thoughtworks.qdox.JavaProjectBuilder.addSource(JavaProjectBuilder.java:174)
	scala.meta.internal.mtags.JavaMtags.indexRoot(JavaMtags.scala:48)
	scala.meta.internal.metals.SemanticdbDefinition$.foreachWithReturnMtags(SemanticdbDefinition.scala:97)
	scala.meta.internal.metals.Indexer.indexSourceFile(Indexer.scala:489)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7(Indexer.scala:361)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7$adapted(Indexer.scala:356)
	scala.collection.IterableOnceOps.foreach(IterableOnce.scala:619)
	scala.collection.IterableOnceOps.foreach$(IterableOnce.scala:617)
	scala.collection.AbstractIterator.foreach(Iterator.scala:1306)
	scala.collection.parallel.ParIterableLike$Foreach.leaf(ParIterableLike.scala:938)
	scala.collection.parallel.Task.$anonfun$tryLeaf$1(Tasks.scala:52)
	scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
	scala.util.control.Breaks$$anon$1.catchBreak(Breaks.scala:97)
	scala.collection.parallel.Task.tryLeaf(Tasks.scala:55)
	scala.collection.parallel.Task.tryLeaf$(Tasks.scala:49)
	scala.collection.parallel.ParIterableLike$Foreach.tryLeaf(ParIterableLike.scala:935)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:169)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7834.java