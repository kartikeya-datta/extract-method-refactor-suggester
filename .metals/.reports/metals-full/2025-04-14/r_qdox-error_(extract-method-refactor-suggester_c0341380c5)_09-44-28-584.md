error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7158.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7158.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7158.java
text:
```scala
private static final S@@tring PREF_ORG_ID = "orgId";

package com.github.mobile.android;

import static com.actionbarsherlock.app.ActionBar.NAVIGATION_MODE_LIST;
import static com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import static com.github.mobile.android.R.menu;
import static com.github.mobile.android.util.GitHubIntents.EXTRA_USER;
import static com.google.common.collect.Lists.newArrayList;
import static com.madgag.android.listviews.ReflectiveHolderFactory.reflectiveFactoryFor;
import static com.madgag.android.listviews.ViewInflator.viewInflatorFor;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.github.mobile.android.R.id;
import com.github.mobile.android.R.layout;
import com.github.mobile.android.gist.GistsActivity;
import com.github.mobile.android.issue.FilterBrowseActivity;
import com.github.mobile.android.issue.IssueDashboardActivity;
import com.github.mobile.android.persistence.AccountDataManager;
import com.github.mobile.android.repo.OrgLoader;
import com.github.mobile.android.repo.OrgViewHolder;
import com.github.mobile.android.repo.UserComparator;
import com.github.mobile.android.ui.user.UserPagerAdapter;
import com.github.mobile.android.util.AvatarHelper;
import com.github.mobile.android.util.GitHubIntents.Builder;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.madgag.android.listviews.ViewFactory;
import com.madgag.android.listviews.ViewHolderFactory;
import com.madgag.android.listviews.ViewHoldingListAdapter;
import com.viewpagerindicator.TitlePageIndicator;

import java.util.Collections;
import java.util.List;

import org.eclipse.egit.github.core.User;

import roboguice.inject.InjectView;

/**
 * Home screen activity
 */
public class HomeActivity extends RoboSherlockFragmentActivity implements OnNavigationListener,
    LoaderCallbacks<List<User>> {

    private static final String TAG = "GH.UVA";

    public static final String PREF_ORG_ID = "orgId";

    /**
     * Create intent for this activity
     *
     * @param org
     * @return intent
     */
    public static Intent createIntent(User org) {
        return new Builder("org.VIEW").add(EXTRA_USER, org).toIntent();
    }

    @Inject
    private AccountDataManager accountDataManager;

    @Inject
    private Provider<UserComparator> userComparatorProvider;

    private List<User> orgs = Collections.emptyList();
    private ViewHoldingListAdapter<User> orgListAdapter;
    private List<OrgSelectionListener> orgSelectionListeners = newArrayList();


    private User org;

    @InjectView(id.tpi_header)
    private TitlePageIndicator indicator;

    @InjectView(id.vp_pages)
    private ViewPager pager;

    @Inject
    private AvatarHelper avatarHelper;

    @Inject
    private SharedPreferences sharedPreferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(layout.pager_with_title);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(NAVIGATION_MODE_LIST);

        getSupportLoaderManager().initLoader(0, null, this);
        ViewHolderFactory<User> userViewHolderFactory = reflectiveFactoryFor(OrgViewHolder.class, avatarHelper);
        ViewFactory<User> selectedUserViewFactory = new ViewFactory<User>(viewInflatorFor(this, layout.org_item),
            userViewHolderFactory);
        ViewFactory<User> dropDownViewFactory = new ViewFactory<User>(viewInflatorFor(this,
            layout.org_item_dropdown), userViewHolderFactory);
        orgListAdapter = new ViewHoldingListAdapter<User>(orgs, selectedUserViewFactory, dropDownViewFactory);
        getSupportActionBar().setListNavigationCallbacks(orgListAdapter, this);

        User org = (User) getIntent().getSerializableExtra(EXTRA_USER);
        if (org != null)
            setOrg(org);
    }

    private void setOrg(User org) {
        Log.d(TAG, "setOrg : " + org.getLogin());

        this.org = org;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PREF_ORG_ID, org.getId());
        editor.commit();

        if (pager.getAdapter() == null) {
            pager.setAdapter(new UserPagerAdapter(getSupportFragmentManager()));
            indicator.setViewPager(pager);
        }

        for (OrgSelectionListener listener : orgSelectionListeners)
            listener.onOrgSelected(org);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu optionMenu) {
        getSupportMenuInflater().inflate(menu.welcome, optionMenu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case id.dashboard_issues:
                startActivity(new Intent(this, IssueDashboardActivity.class));
                return true;
            case id.gists:
                startActivity(new Intent(this, GistsActivity.class));
                return true;
            case id.search:
                onSearchRequested();
                return true;
            case id.bookmarks:
                startActivity(FilterBrowseActivity.createIntent());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        setOrg(orgs.get(itemPosition));
        return true;
    }

    @Override
    public Loader<List<User>> onCreateLoader(int i, Bundle bundle) {
        return new OrgLoader(this, accountDataManager, userComparatorProvider);
    }

    @Override
    public void onLoadFinished(Loader<List<User>> listLoader, List<User> orgs) {
        this.orgs = orgs;

        orgListAdapter.setList(orgs);
        int sharedPreferencesOrgId = sharedPreferences.getInt(PREF_ORG_ID, -1);
        int targetOrgId = org == null ? sharedPreferencesOrgId : org.getId();

        for (int i = 0; i < orgs.size(); ++i) {
            User availableOrg = orgs.get(i);
            if (availableOrg.getId() == targetOrgId) {
                getSupportActionBar().setSelectedNavigationItem(i);
                break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<List<User>> listLoader) {
    }

    public void registerOrgSelectionListener(OrgSelectionListener listener) {
        orgSelectionListeners.add(listener);
    }

    public static interface OrgSelectionListener {
        public void onOrgSelected(User org);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7158.java