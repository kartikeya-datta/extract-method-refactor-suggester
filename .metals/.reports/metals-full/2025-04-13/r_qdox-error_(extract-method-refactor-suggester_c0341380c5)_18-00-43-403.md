error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/515.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/515.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/515.java
text:
```scala
public static I@@ntent createIntent(Collection<? extends Issue> issues, int position) {

package com.github.mobile.android.ui.issue;

import static com.github.mobile.android.util.GitHubIntents.EXTRA_ISSUE_NUMBERS;
import static com.github.mobile.android.util.GitHubIntents.EXTRA_POSITION;
import static com.github.mobile.android.util.GitHubIntents.EXTRA_REPOSITORIES;
import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.actionbarsherlock.app.ActionBar;
import com.github.mobile.android.DialogFragmentActivity;
import com.github.mobile.android.R.id;
import com.github.mobile.android.R.layout;
import com.github.mobile.android.R.string;
import com.github.mobile.android.ui.UrlLauncher;
import com.github.mobile.android.util.GitHubIntents.Builder;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.RepositoryId;

import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

/**
 * Activity display a collection of issues in a pager
 */
public class ViewIssuesActivity extends DialogFragmentActivity implements OnPageChangeListener {

    /**
     * Create an intent to show a single issue
     *
     * @param issue
     * @return intent
     */
    public static Intent createIntent(Issue issue) {
        ArrayList<Issue> list = new ArrayList<Issue>(1);
        list.add(issue);
        return createIntent(list, 0);
    }

    /**
     * Create an intent to show issues with an initial selected issue
     *
     * @param issues
     * @param position
     * @return intent
     */
    public static Intent createIntent(Collection<Issue> issues, int position) {
        ArrayList<Integer> numbers = new ArrayList<Integer>(issues.size());
        ArrayList<RepositoryId> repos = new ArrayList<RepositoryId>(issues.size());
        for (Issue issue : issues) {
            numbers.add(issue.getNumber());
            repos.add(RepositoryId.createFromUrl(issue.getHtmlUrl()));
        }
        return new Builder("issues.VIEW").add(EXTRA_ISSUE_NUMBERS, numbers).add(EXTRA_REPOSITORIES, repos)
                .add(EXTRA_POSITION, position).toIntent();
    }

    @InjectView(id.vp_pages)
    private ViewPager pager;

    @InjectExtra(EXTRA_ISSUE_NUMBERS)
    private ArrayList<integer> issueIds;

    @InjectExtra(EXTRA_REPOSITORIES)
    private ArrayList<RepositoryId> repoIds;

    @InjectExtra(EXTRA_POSITION)
    private int initialPosition;

    private IssuesPagerAdapter adapter;

    private final UrlLauncher urlLauncher = new UrlLauncher();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(layout.pager);

        adapter = new IssuesPagerAdapter(getSupportFragmentManager(),
                repoIds.toArray(new RepositoryId[repoIds.size()]), issueIds.toArray(new Integer[issueIds.size()]));
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(this);
        pager.setCurrentItem(initialPosition);
        onPageSelected(initialPosition);
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // Intentionally left blank
    }

    public void onPageSelected(int position) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getString(string.issue_title) + issueIds.get(position));
        actionBar.setSubtitle(repoIds.get(position).generateId());
    }

    public void onPageScrollStateChanged(int state) {
        // Intentionally left blank
    }

    @Override
    public void onDialogResult(int requestCode, int resultCode, Bundle arguments) {
        adapter.onDialogResult(pager.getCurrentItem(), requestCode, resultCode, arguments);
    }

    @Override
    public void startActivity(Intent intent) {
        Intent converted = urlLauncher.convert(intent);
        if (converted != null)
            super.startActivity(converted);
        else
            super.startActivity(intent);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/515.java