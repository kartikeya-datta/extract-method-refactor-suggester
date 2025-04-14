error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3632.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3632.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3632.java
text:
```scala
E@@rrorHelper.show(getContext().getApplicationContext(), e, string.error_gist_load);

package com.github.mobile.android.gist;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.widget.Toast.LENGTH_LONG;
import static com.github.mobile.android.util.GitHubIntents.EXTRA_COMMENTS;
import static com.github.mobile.android.util.GitHubIntents.EXTRA_COMMENT_BODY;
import static com.github.mobile.android.util.GitHubIntents.EXTRA_GIST_ID;
import static com.google.common.collect.Lists.newArrayList;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.github.mobile.android.R.id;
import com.github.mobile.android.R.layout;
import com.github.mobile.android.R.menu;
import com.github.mobile.android.R.string;
import com.github.mobile.android.RefreshAnimation;
import com.github.mobile.android.async.AuthenticatedUserTask;
import com.github.mobile.android.comment.CommentViewHolder;
import com.github.mobile.android.comment.CreateCommentActivity;
import com.github.mobile.android.core.gist.FullGist;
import com.github.mobile.android.util.AccountHelper;
import com.github.mobile.android.util.AvatarHelper;
import com.github.mobile.android.util.ErrorHelper;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;
import com.google.inject.Inject;
import com.madgag.android.listviews.ReflectiveHolderFactory;
import com.madgag.android.listviews.ViewHoldingListAdapter;
import com.madgag.android.listviews.ViewInflator;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.GistFile;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.service.GistService;

import roboguice.inject.ContextScopedProvider;
import roboguice.inject.InjectView;

/**
 * Activity to display an existing Gist
 */
public class GistFragment extends RoboSherlockFragment implements OnItemClickListener {

    private static final int REQUEST_CODE_COMMENT = 1;

    private String gistId;

    private List<Comment> comments;

    private Gist gist;

    @InjectView(android.R.id.list)
    private ListView list;

    @Inject
    private GistStore store;

    @Inject
    private ContextScopedProvider<GistService> service;

    private View headerView;

    private GistHeaderViewHolder headerHolder;

    private View loadingView;

    private RefreshAnimation refreshAnimation = new RefreshAnimation();

    private boolean starred;

    private boolean loadFinished;

    @Inject
    private AvatarHelper avatarHelper;

    @Inject
    private ContextScopedProvider<GistService> gistServiceProvider;

    private Executor executor = Executors.newFixedThreadPool(1);

    private List<View> fileHeaders = newArrayList();

    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        gistId = getArguments().getString(EXTRA_GIST_ID);
        comments = (List<Comment>) getArguments().getSerializable(EXTRA_COMMENTS);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(layout.gist_view, null);

        headerView = inflater.inflate(layout.gist_header, null);
        headerHolder = new GistHeaderViewHolder(headerView);

        loadingView = inflater.inflate(layout.comment_load_item, null);

        return root;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        list.setOnItemClickListener(this);
        list.setFastScrollEnabled(true);
        list.addHeaderView(headerView, null, false);

        gist = store.getGist(gistId);

        if (gist != null) {
            headerHolder.updateViewFor(gist);
            updateFiles(gist);
        } else {
            ((TextView) loadingView.findViewById(id.tv_loading)).setText(string.loading_gist);
            headerView.setVisibility(GONE);
        }

        if (gist == null || (gist.getComments() > 0 && comments == null))
            list.addHeaderView(loadingView, null, false);

        List<Comment> initialComments = comments;
        if (initialComments == null)
            initialComments = Collections.emptyList();
        list.setAdapter(new ViewHoldingListAdapter<Comment>(initialComments, ViewInflator.viewInflatorFor(
                getActivity(), layout.comment_view_item), ReflectiveHolderFactory.reflectiveFactoryFor(
                CommentViewHolder.class, avatarHelper)));

        if (gist != null && comments != null)
            updateList(gist, comments);

        refreshGist();
    }

    private boolean isOwner() {
        if (gist == null)
            return false;
        User user = gist.getUser();
        if (user == null)
            return false;
        String login = AccountHelper.getLogin(getActivity());
        return login != null && login.equals(user.getLogin());
    }

    @Override
    public void onCreateOptionsMenu(Menu options, MenuInflater inflater) {
        inflater.inflate(menu.gist_view, options);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        boolean owner = isOwner();
        menu.findItem(id.gist_delete).setEnabled(owner);
        MenuItem starItem = menu.findItem(id.gist_star);
        starItem.setEnabled(loadFinished && !owner);
        if (starred)
            starItem.setTitle(string.unstar_gist);
        else
            starItem.setTitle(string.star_gist);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case id.gist_comment:
            startActivityForResult(CreateCommentActivity.createIntent(), REQUEST_CODE_COMMENT);
            return true;
        case id.gist_star:
            if (starred)
                unstarGist();
            else
                starGist();
            return true;
        case id.refresh:
            refreshAnimation.setRefreshItem(item).start(getActivity());
            refreshGist();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void starGist() {
        Toast.makeText(getActivity().getApplicationContext(), getString(string.starring_gist), LENGTH_LONG).show();
        new AuthenticatedUserTask<Gist>(getActivity()) {

            public Gist run() throws Exception {
                gistServiceProvider.get(getContext()).starGist(gistId);
                starred = true;
                return null;
            }

            protected void onException(Exception e) throws RuntimeException {
                Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), LENGTH_LONG).show();
            }
        }.execute();
    }

    private void unstarGist() {
        Toast.makeText(getActivity().getApplicationContext(), getString(string.unstarring_gist), LENGTH_LONG).show();
        new AuthenticatedUserTask<Gist>(getActivity()) {

            public Gist run() throws Exception {
                gistServiceProvider.get(getActivity()).unstarGist(gistId);
                starred = false;
                return null;
            }

            protected void onException(Exception e) throws RuntimeException {
                Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), LENGTH_LONG).show();
            }
        }.execute();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK == resultCode && REQUEST_CODE_COMMENT == requestCode && data != null) {
            String comment = data.getStringExtra(EXTRA_COMMENT_BODY);
            if (comment != null && comment.length() > 0) {
                createComment(comment);
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void createComment(final String comment) {
        final ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setMessage(getString(string.creating_comment));
        progress.setIndeterminate(true);
        progress.show();
        new AuthenticatedUserTask<Comment>(getActivity()) {

            public Comment run() throws Exception {
                return gistServiceProvider.get(getActivity()).createComment(gistId, comment);
            }

            protected void onSuccess(Comment comment) throws Exception {
                progress.dismiss();
                refreshGist();
            }

            protected void onException(Exception e) throws RuntimeException {
                progress.dismiss();
                Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), LENGTH_LONG).show();
            }
        }.execute();

    }

    private void updateFiles(Gist gist) {
        for (View header : fileHeaders)
            list.removeHeaderView(header);
        fileHeaders.clear();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        for (GistFile file : gist.getFiles().values()) {
            View fileView = inflater.inflate(layout.gist_view_file_item, null);
            new GistFileViewHolder(fileView).updateViewFor(file);
            list.addHeaderView(fileView, file, true);
            fileHeaders.add(fileView);
        }
    }

    private void updateList(Gist gist, List<Comment> comments) {
        list.removeHeaderView(loadingView);
        headerView.setVisibility(VISIBLE);
        headerHolder.updateViewFor(gist);

        updateFiles(gist);

        ViewHoldingListAdapter<Comment> adapter = getRootAdapter();
        if (adapter != null)
            adapter.setList(comments);
    }

    @SuppressWarnings("unchecked")
    private ViewHoldingListAdapter<Comment> getRootAdapter() {
        ListAdapter adapter = list.getAdapter();
        if (adapter == null)
            return null;
        adapter = ((HeaderViewListAdapter) adapter).getWrappedAdapter();
        if (adapter instanceof ViewHoldingListAdapter<?>)
            return (ViewHoldingListAdapter<Comment>) adapter;
        else
            return null;
    }

    private void refreshGist() {
        new AuthenticatedUserTask<FullGist>(getActivity(), executor) {

            public FullGist run() throws Exception {
                Gist gist = store.refreshGist(gistId);
                GistService gistService = service.get(getContext());
                List<Comment> comments;
                if (gist.getComments() > 0)
                    comments = gistService.getComments(gistId);
                else
                    comments = Collections.emptyList();
                return new FullGist(gist, gistService.isStarred(gistId), comments);
            }

            protected void onException(Exception e) throws RuntimeException {
                ErrorHelper.show(getActivity().getApplicationContext(), e, string.error_gist_load);
            }

            protected void onSuccess(FullGist fullGist) throws Exception {
                starred = fullGist.isStarred();
                loadFinished = true;
                gist = fullGist.getGist();
                comments = fullGist;
                getArguments().putSerializable(EXTRA_COMMENTS, fullGist);
                updateList(fullGist.getGist(), fullGist);
            }

            protected void onFinally() throws RuntimeException {
                refreshAnimation.stop();
            }
        }.execute();
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object item = parent.getItemAtPosition(position);
        if (item instanceof GistFile)
            startActivity(ViewGistFilesActivity.createIntent(gist, position - 1));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3632.java