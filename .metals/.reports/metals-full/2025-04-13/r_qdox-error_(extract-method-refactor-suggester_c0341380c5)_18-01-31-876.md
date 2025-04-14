error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/583.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/583.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/583.java
text:
```scala
r@@eturn new FullGist(gist, service.isStarred(id), comments);

package com.github.mobile.android.gist;

import static com.madgag.android.listviews.ReflectiveHolderFactory.reflectiveFactoryFor;
import static com.madgag.android.listviews.ViewInflator.viewInflatorFor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.github.mobile.android.AsyncLoader;
import com.github.mobile.android.R.layout;
import com.github.mobile.android.comment.CommentViewHolder;
import com.github.mobile.android.ui.fragments.ListLoadingFragment;
import com.github.mobile.android.util.AvatarHelper;
import com.google.inject.Inject;
import com.madgag.android.listviews.ViewHoldingListAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.GistFile;
import org.eclipse.egit.github.core.service.GistService;

/**
 * Fragment to display a Gist's files and comments
 */
public class GistFragment extends ListLoadingFragment<Comment> {

    private String id;

    private LoaderCallbacks<List<Comment>> loadListener;

    @Inject
    private GistService service;

    @Inject
    private GistStore store;

    private Gist gist;

    private List<View> fileHeaders = new ArrayList<View>();

    @Inject
    private AvatarHelper avatarHelper;

    public void onListItemClick(ListView l, View v, int position, long id) {
        Object item = l.getItemAtPosition(position);
        if (item instanceof GistFile)
            startActivity(ViewGistFileActivity.createIntent(gist, (GistFile) item));
    }

    /**
     * @param loadListener
     * @return this fragment
     */
    public GistFragment setLoadListener(LoaderCallbacks<List<Comment>> loadListener) {
        this.loadListener = loadListener;
        return this;
    }

    /**
     * @param id
     * @return this fragment
     */
    public GistFragment setId(String id) {
        this.id = id;
        return this;
    }

    @Override
    public Loader<List<Comment>> onCreateLoader(int i, Bundle bundle) {
        return new AsyncLoader<List<Comment>>(getActivity()) {
            @Override
            public List<Comment> loadInBackground() {
                try {
                    Gist gist = store.addGist(service.getGist(id));
                    List<Comment> comments;
                    if (gist.getComments() > 0)
                        comments = service.getComments(id);
                    else
                        comments = Collections.emptyList();
                    return new FullGist(gist, comments);
                } catch (IOException e) {
                    return new FullGist();
                }
            }
        };
    }

    @Override
    protected ViewHoldingListAdapter<Comment> adapterFor(List<Comment> items) {
        return new ViewHoldingListAdapter<Comment>(items, viewInflatorFor(getActivity(), layout.comment_view_item),
                reflectiveFactoryFor(CommentViewHolder.class, avatarHelper));
    }

    public void onLoadFinished(Loader<List<Comment>> loader, List<Comment> items) {
        FullGist gist = (FullGist) items;
        this.gist = gist.getGist();
        ListView view = getListView();
        for (View header : fileHeaders)
            view.removeHeaderView(header);
        fileHeaders.clear();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        for (GistFile file : gist.getGist().getFiles().values()) {
            View fileView = inflater.inflate(layout.gist_view_file_item, null);
            new GistFileViewHolder(fileView).updateViewFor(file);
            view.addHeaderView(fileView, file, true);
            fileHeaders.add(fileView);
        }
        super.onLoadFinished(loader, items);
        if (loadListener != null)
            loadListener.onLoadFinished(loader, items);
    }

    public void onLoaderReset(Loader<List<Comment>> listLoader) {
        super.onLoaderReset(listLoader);
        if (loadListener != null)
            loadListener.onLoaderReset(listLoader);
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/583.java