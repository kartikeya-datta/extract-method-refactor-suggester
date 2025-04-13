error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7195.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7195.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7195.java
text:
```scala
r@@eflectiveFactoryFor(RepoIssueViewHolder.class, RepoIssueViewHolder.computeMaxDigits(items)));

package com.github.mobile.android.issue;

import static android.view.ViewGroup.LayoutParams.FILL_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.madgag.android.listviews.ReflectiveHolderFactory.reflectiveFactoryFor;
import static com.madgag.android.listviews.ViewInflator.viewInflatorFor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.github.mobile.android.AsyncLoader;
import com.github.mobile.android.R.layout;
import com.github.mobile.android.ui.fragments.ListLoadingFragment;
import com.google.inject.Inject;
import com.madgag.android.listviews.ViewHoldingListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.client.PageIterator;
import org.eclipse.egit.github.core.service.IssueService;

/**
 * Fragment to display a list of issues
 */
public class IssuesFragment extends ListLoadingFragment<Issue> {

    private static final int DEFAULT_SIZE = 20;

    private OnItemClickListener clickListener;

    private LoaderCallbacks<List<Issue>> loadListener;

    @Inject
    private IssueService service;

    private IRepositoryIdProvider repository;

    private IssueFilter filter;

    private Issue lastIssue;

    private boolean hasMore = true;

    private Button moreButton;

    private int pages = 1;

    /**
     * @param repository
     * @return this fragment
     */
    public IssuesFragment setRepository(IRepositoryIdProvider repository) {
        this.repository = repository;
        return this;
    }

    /**
     * @param filter
     * @return this fragment
     */
    public IssuesFragment setFilter(IssueFilter filter) {
        this.filter = filter;
        return this;
    }

    /**
     * @param clickListener
     * @return this fragment
     */
    public IssuesFragment setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
        return this;
    }

    /**
     * @param loadListener
     * @return this fragment
     */
    public IssuesFragment setLoadListener(LoaderCallbacks<List<Issue>> loadListener) {
        this.loadListener = loadListener;
        return this;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setEmptyText("No Issues");
    }

    public void onLoaderReset(Loader<List<Issue>> listLoader) {
        super.onLoaderReset(listLoader);

        if (loadListener != null)
            loadListener.onLoaderReset(listLoader);
    }

    public void onLoadFinished(Loader<List<Issue>> loader, final List<Issue> items) {
        if (hasMore) {
            if (moreButton == null) {
                moreButton = new Button(getActivity());
                moreButton.setLayoutParams(new LayoutParams(FILL_PARENT, WRAP_CONTENT));
                moreButton.setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        moreButton.setText("Loading More Issues...");
                        pages++;
                        lastIssue = (Issue) getListView().getItemAtPosition(
                                getListView().getCount() - getListView().getFooterViewsCount() - 1);
                        refresh();
                    }
                });
                getListView().addFooterView(moreButton);
            }
            moreButton.setText("Show More...");
        } else {
            getListView().removeFooterView(moreButton);
            moreButton = null;
        }

        super.onLoadFinished(loader, items);

        if (lastIssue != null) {
            final int target = lastIssue.getNumber();
            getListView().post(new Runnable() {

                public void run() {
                    ListView view = getListView();
                    for (int i = 0; i < view.getCount() - view.getFooterViewsCount(); i++) {
                        Issue issue = (Issue) view.getItemAtPosition(i);
                        if (target == issue.getNumber()) {
                            view.setSelection(i);
                            return;
                        }
                    }
                }
            });
        }

        if (loadListener != null)
            loadListener.onLoadFinished(loader, items);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (clickListener != null)
            clickListener.onItemClick(l, v, position, id);
    }

    @Override
    public Loader<List<Issue>> onCreateLoader(int i, Bundle bundle) {
        return new AsyncLoader<List<Issue>>(getActivity()) {

            @Override
            public List<Issue> loadInBackground() {
                hasMore = false;
                final List<Issue> all = new ArrayList<Issue>();
                for (Map<String, String> query : filter) {
                    PageIterator<Issue> issues = service.pageIssues(repository, query, 1, DEFAULT_SIZE);
                    for (int i = 0; i < pages && issues.hasNext(); i++)
                        all.addAll(issues.next());
                    hasMore |= issues.hasNext();
                }
                Collections.sort(all, new CreatedAtComparator());
                return all;
            }
        };
    }

    @Override
    protected ListAdapter adapterFor(List<Issue> items) {
        return new ViewHoldingListAdapter<Issue>(items, viewInflatorFor(getActivity(), layout.repo_issue_list_item),
                reflectiveFactoryFor(RepoIssueViewHolder.class));
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7195.java