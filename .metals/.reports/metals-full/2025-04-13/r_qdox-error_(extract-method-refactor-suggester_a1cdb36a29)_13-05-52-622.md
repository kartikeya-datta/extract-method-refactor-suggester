error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4649.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4649.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4649.java
text:
```scala
c@@ase id.m_search:

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

import static com.actionbarsherlock.app.ActionBar.NAVIGATION_MODE_LIST;
import static com.github.mobile.Intents.EXTRA_USER;
import static com.github.mobile.ui.user.HomeDropdownListAdapter.ACTION_BOOKMARKS;
import static com.github.mobile.ui.user.HomeDropdownListAdapter.ACTION_DASHBOARD;
import static com.github.mobile.ui.user.HomeDropdownListAdapter.ACTION_GISTS;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.github.mobile.R.id;
import com.github.mobile.R.layout;
import com.github.mobile.R.menu;
import com.github.mobile.accounts.AccountUtils;
import com.github.mobile.core.user.UserComparator;
import com.github.mobile.persistence.AccountDataManager;
import com.github.mobile.ui.gist.GistsActivity;
import com.github.mobile.ui.issue.IssueDashboardActivity;
import com.github.mobile.ui.issue.FiltersViewActivity;
import com.github.mobile.ui.repo.OrganizationLoader;
import com.github.mobile.util.AvatarLoader;
import com.github.mobile.util.PreferenceUtils;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.viewpagerindicator.TitlePageIndicator;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.egit.github.core.User;

import roboguice.inject.InjectView;

/**
 * Home screen activity
 */
public class HomeActivity extends RoboSherlockFragmentActivity implements OnNavigationListener,
        OrganizationSelectionProvider, LoaderCallbacks<List<User>> {

    private static final String TAG = "HomeActivity";

    private static final String PREF_ORG_ID = "orgId";

    @Inject
    private AccountDataManager accountDataManager;

    @Inject
    private Provider<UserComparator> userComparatorProvider;

    private boolean isDefaultUser;

    private List<User> orgs = Collections.emptyList();

    private HomeDropdownListAdapter homeAdapter;

    private Set<OrganizationSelectionListener> orgSelectionListeners = new LinkedHashSet<OrganizationSelectionListener>();

    private User org;

    @InjectView(id.tpi_header)
    private TitlePageIndicator indicator;

    @InjectView(id.vp_pages)
    private ViewPager pager;

    @Inject
    private AvatarLoader avatarHelper;

    @Inject
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(layout.pager_with_title);

        getSupportLoaderManager().initLoader(0, null, this);

        User org = (User) getIntent().getSerializableExtra(EXTRA_USER);
        if (org == null && savedInstanceState != null)
            org = (User) savedInstanceState.getSerializable(EXTRA_USER);
        if (org != null)
            setOrg(org);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (org != null)
            outState.putSerializable(EXTRA_USER, org);
    }

    private void configureActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(NAVIGATION_MODE_LIST);

        homeAdapter = new HomeDropdownListAdapter(this, orgs, avatarHelper);
        actionBar.setListNavigationCallbacks(homeAdapter, this);
    }

    private void setOrg(User org) {
        Log.d(TAG, "setOrg : " + org.getLogin());

        PreferenceUtils.save(sharedPreferences.edit().putInt(PREF_ORG_ID, org.getId()));

        // Don't notify listeners or change pager if org hasn't changed
        if (this.org != null && this.org.getId() == org.getId())
            return;

        this.org = org;

        boolean isDefaultUser = isDefaultUser(org);
        PagerAdapter pagerAdater = pager.getAdapter();
        if (pagerAdater == null) {
            pager.setAdapter(new HomePagerAdapter(getSupportFragmentManager(), getResources(), isDefaultUser));
            indicator.setViewPager(pager);
        } else if (this.isDefaultUser != isDefaultUser) {
            int item = pager.getCurrentItem();
            ((HomePagerAdapter) pagerAdater).clearAdapter(isDefaultUser);
            pagerAdater.notifyDataSetChanged();
            if (item >= pagerAdater.getCount())
                item = pagerAdater.getCount() - 1;
            indicator.invalidate();
            indicator.setCurrentItem(item);
            pager.setCurrentItem(item, false);
        }
        this.isDefaultUser = isDefaultUser;

        for (OrganizationSelectionListener listener : orgSelectionListeners)
            listener.onOrganizationSelected(org);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu optionMenu) {
        getSupportMenuInflater().inflate(menu.home, optionMenu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case id.search:
            onSearchRequested();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        if (homeAdapter.isOrgPosition(itemPosition)) {
            homeAdapter.setSelected(itemPosition);
            setOrg(orgs.get(itemPosition));
        } else {
            switch (homeAdapter.getAction(itemPosition)) {
            case ACTION_GISTS:
                startActivity(new Intent(this, GistsActivity.class));
                break;
            case ACTION_DASHBOARD:
                startActivity(new Intent(this, IssueDashboardActivity.class));
                break;
            case ACTION_BOOKMARKS:
                startActivity(FiltersViewActivity.createIntent());
                break;
            }
            int orgSelected = homeAdapter.getSelected();
            ActionBar actionBar = getSupportActionBar();
            if (orgSelected < actionBar.getNavigationItemCount())
                actionBar.setSelectedNavigationItem(orgSelected);
        }
        return true;
    }

    @Override
    public Loader<List<User>> onCreateLoader(int i, Bundle bundle) {
        return new OrganizationLoader(this, accountDataManager, userComparatorProvider);
    }

    @Override
    public void onLoadFinished(Loader<List<User>> listLoader, List<User> orgs) {
        this.orgs = orgs;

        if (homeAdapter != null)
            homeAdapter.setOrgs(orgs);
        else
            configureActionBar();

        int sharedPreferencesOrgId = sharedPreferences.getInt(PREF_ORG_ID, -1);
        int targetOrgId = org == null ? sharedPreferencesOrgId : org.getId();

        ActionBar actionBar = getSupportActionBar();
        for (int i = 0; i < orgs.size(); i++)
            if (orgs.get(i).getId() == targetOrgId) {
                actionBar.setSelectedNavigationItem(i);
                break;
            }
    }

    @Override
    public void onLoaderReset(Loader<List<User>> listLoader) {
    }

    private boolean isDefaultUser(final User org) {
        final String accountLogin = AccountUtils.getLogin(this);
        return org != null && accountLogin != null && accountLogin.equals(org.getLogin());
    }

    @Override
    public User addListener(OrganizationSelectionListener listener) {
        if (listener != null)
            orgSelectionListeners.add(listener);
        return org;
    }

    @Override
    public OrganizationSelectionProvider removeListener(OrganizationSelectionListener listener) {
        if (listener != null)
            orgSelectionListeners.remove(listener);
        return this;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4649.java