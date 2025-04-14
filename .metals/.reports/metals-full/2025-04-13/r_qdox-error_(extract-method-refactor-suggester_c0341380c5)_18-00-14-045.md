error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6982.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6982.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6982.java
text:
```scala
a@@ctionBar.setTitle(getString(string.gist_title) + gistId);

package com.github.mobile.android.gist;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;
import static com.github.mobile.android.util.GitHubIntents.EXTRA_GIST;
import static com.github.mobile.android.util.GitHubIntents.EXTRA_GIST_ID;
import static com.github.mobile.android.util.GitHubIntents.EXTRA_GIST_IDS;
import static com.github.mobile.android.util.GitHubIntents.EXTRA_POSITION;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.github.mobile.android.ConfirmDialogFragment;
import com.github.mobile.android.DialogFragmentActivity;
import com.github.mobile.android.R.drawable;
import com.github.mobile.android.R.id;
import com.github.mobile.android.R.layout;
import com.github.mobile.android.R.string;
import com.github.mobile.android.util.AvatarHelper;
import com.github.mobile.android.util.GitHubIntents.Builder;
import com.google.inject.Inject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.egit.github.core.Gist;

import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

/**
 * Activity to display a collection of Gists in a pager
 */
public class ViewGistsActivity extends DialogFragmentActivity implements OnPageChangeListener {

    private static final int REQUEST_CONFIRM_DELETE = 1;

    /**
     * Create an intent to show a single gist
     *
     * @param gist
     * @return intent
     */
    public static Intent createIntent(Gist gist) {
        return new Builder("gists.VIEW").add(EXTRA_GIST, gist).add(EXTRA_POSITION, 0).toIntent();
    }

    /**
     * Create an intent to show gists with an initial selected Gist
     *
     * @param gists
     * @param position
     * @return intent
     */
    public static Intent createIntent(List<Gist> gists, int position) {
        List<String> ids = new ArrayList<String>(gists.size());
        for (Gist gist : gists)
            ids.add(gist.getId());
        return new Builder("gists.VIEW").add(EXTRA_GIST_IDS, (Serializable) ids).add(EXTRA_POSITION, position)
                .toIntent();
    }

    @InjectView(id.vp_pages)
    private ViewPager pager;

    @InjectExtra(value = EXTRA_GIST_IDS, optional = true)
    private List<String> gists;

    @InjectExtra(value = EXTRA_GIST, optional = true)
    private Gist gist;

    @InjectExtra(EXTRA_POSITION)
    private int initialPosition;

    @Inject
    private GistStore store;

    @Inject
    private AvatarHelper avatarHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(layout.pager);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Support opening this activity with a single Gist that may be present in the intent but not currently present
        // in the store
        if (gists == null && gist != null) {
            String id = gist.getId();
            if (gist.getCreatedAt() != null) {
                Gist stored = store.getGist(id);
                if (stored == null)
                    store.addGist(gist);
            }
            gists = Collections.singletonList(id);
        }

        pager.setAdapter(new GistsPagerAdapter(getSupportFragmentManager(), gists.toArray(new String[gists.size()])));
        pager.setOnPageChangeListener(this);
        pager.setCurrentItem(initialPosition);
        onPageSelected(initialPosition);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            Intent intent = new Intent(this, GistsActivity.class);
            intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            return true;
        case id.gist_delete:
            String gistId = gists.get(pager.getCurrentItem());
            Bundle args = new Bundle();
            args.putString(EXTRA_GIST_ID, gistId);
            ConfirmDialogFragment.show(this, REQUEST_CONFIRM_DELETE, getString(string.confirm_gist_delete_title),
                    getString(string.confirm_gist_delete_message), args);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDialogResult(int requestCode, int resultCode, Bundle arguments) {
        if (REQUEST_CONFIRM_DELETE == requestCode && RESULT_OK == resultCode) {
            final String gistId = arguments.getString(EXTRA_GIST_ID);
            new DeleteGistTask(this, gistId).start();
            return;
        }

        super.onDialogResult(requestCode, resultCode, arguments);
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // Intentionally left blank
    }

    public void onPageSelected(int position) {
        ActionBar actionBar = getSupportActionBar();
        String gistId = gists.get(position);
        Gist gist = store.getGist(gistId);
        if (gist == null) {
            actionBar.setSubtitle(null);
            actionBar.setLogo(null);
            actionBar.setIcon(drawable.github_app_icon);
        } else if (gist.getUser() != null) {
            avatarHelper.bind(actionBar, gist.getUser());
            actionBar.setSubtitle(gist.getUser().getLogin());
        } else {
            actionBar.setSubtitle(string.anonymous);
            actionBar.setLogo(null);
            actionBar.setIcon(drawable.github_app_icon);
        }
        actionBar.setTitle(getString(string.gist) + ' ' + gistId);
    }

    public void onPageScrollStateChanged(int state) {
        // Intentionally left blank
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6982.java