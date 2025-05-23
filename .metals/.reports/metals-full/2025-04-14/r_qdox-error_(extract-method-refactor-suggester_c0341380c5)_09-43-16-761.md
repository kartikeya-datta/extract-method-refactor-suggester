error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1983.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1983.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1983.java
text:
```scala
s@@tartActivity(ViewGistFilesActivity.createIntent(gist, position - 1));

package com.github.mobile.android.gist;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;
import static android.widget.Toast.LENGTH_LONG;
import static com.github.mobile.android.util.GitHubIntents.EXTRA_COMMENTS;
import static com.github.mobile.android.util.GitHubIntents.EXTRA_COMMENT_BODY;
import static com.github.mobile.android.util.GitHubIntents.EXTRA_GIST_ID;
import static com.google.common.collect.Lists.newArrayList;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.github.mobile.android.ConfirmDialogFragment;
import com.github.mobile.android.DialogFragmentActivity;
import com.github.mobile.android.R.id;
import com.github.mobile.android.R.layout;
import com.github.mobile.android.R.menu;
import com.github.mobile.android.R.string;
import com.github.mobile.android.RefreshAnimation;
import com.github.mobile.android.comment.CommentViewHolder;
import com.github.mobile.android.comment.CreateCommentActivity;
import com.github.mobile.android.util.AvatarHelper;
import com.github.mobile.android.util.ErrorHelper;
import com.github.mobile.android.util.GitHubIntents.Builder;
import com.google.inject.Inject;
import com.madgag.android.listviews.ReflectiveHolderFactory;
import com.madgag.android.listviews.ViewHoldingListAdapter;
import com.madgag.android.listviews.ViewInflator;

import java.io.Serializable;
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
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;

/**
 * Activity to display an existing Gist
 */
public class ViewGistActivity extends DialogFragmentActivity implements OnItemClickListener {

    /**
     * Result if the Gist was deleted
     */
    public static final int RESULT_DELETED = RESULT_FIRST_USER;

    private static final int REQUEST_CODE_COMMENT = 1;

    private static final int REQUEST_CONFIRM_DELETE = REQUEST_CODE_COMMENT + 1;

    /**
     * Create intent to view Gist
     *
     * @param gist
     * @return intent
     */
    public static final Intent createIntent(Gist gist) {
        return createIntent(gist.getId());
    }

    /**
     * Create intent to view Gist
     *
     * @param gistId
     * @return intent
     */
    public static final Intent createIntent(String gistId) {
        return new Builder("gist.VIEW").add(EXTRA_GIST_ID, gistId).toIntent();
    }

    @InjectExtra(EXTRA_GIST_ID)
    private String gistId;

    @InjectExtra(value = EXTRA_COMMENTS, optional = true)
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.gist_view);
        setTitle(getString(string.gist) + " " + gistId);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list.setOnItemClickListener(this);
        list.setFastScrollEnabled(true);

        gist = store.getGist(gistId);

        headerView = getLayoutInflater().inflate(layout.gist_header, null);
        headerHolder = new GistHeaderViewHolder(headerView, avatarHelper);

        loadingView = getLayoutInflater().inflate(layout.comment_load_item, null);

        if (gist != null && comments != null)
            updateList(gist, comments);
        else {
            if (gist != null) {
                list.addHeaderView(headerView, null, false);
                headerHolder.updateViewFor(gist);
                updateFiles(gist);
            } else
                ((TextView) loadingView.findViewById(id.tv_loading)).setText(string.loading_gist);
            refreshGist();
        }
    }

    private boolean isOwner() {
        if (gist == null)
            return false;
        User user = gist.getUser();
        if (user == null)
            return false;
        return gistServiceProvider.get(this).getClient().getUser().equals(user.getLogin());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu options) {
        getSupportMenuInflater().inflate(menu.gist_view, options);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        boolean owner = isOwner();
        menu.findItem(id.gist_delete).setEnabled(owner);
        MenuItem starItem = menu.findItem(id.gist_star);
        starItem.setEnabled(loadFinished && !owner);
        if (starred)
            starItem.setTitle(string.unstar_gist);
        else
            starItem.setTitle(string.star_gist);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case id.gist_comment:
            startActivityForResult(CreateCommentActivity.createIntent(), REQUEST_CODE_COMMENT);
            return true;
        case id.gist_delete:
            ConfirmDialogFragment.show(this, REQUEST_CONFIRM_DELETE, getString(string.confirm_gist_delete_title),
                    getString(string.confirm_gist_delete_message));
            return true;
        case id.gist_star:
            if (starred)
                unstarGist();
            else
                starGist();
            return true;
        case id.refresh:
            refreshAnimation.setRefreshItem(item).start(this);
            refreshGist();
            return true;
        case android.R.id.home:
            Intent intent = new Intent(this, GistsActivity.class);
            intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void starGist() {
        Toast.makeText(getApplication(), getString(string.starring_gist), LENGTH_LONG).show();
        new RoboAsyncTask<Gist>(ViewGistActivity.this) {

            public Gist call() throws Exception {
                gistServiceProvider.get(getContext()).starGist(gistId);
                starred = true;
                return null;
            }

            protected void onException(Exception e) throws RuntimeException {
                Toast.makeText(getApplication(), e.getMessage(), LENGTH_LONG).show();
            }
        }.execute();
    }

    private void unstarGist() {
        Toast.makeText(getApplication(), getString(string.unstarring_gist), LENGTH_LONG).show();
        new RoboAsyncTask<Gist>(ViewGistActivity.this) {

            public Gist call() throws Exception {
                gistServiceProvider.get(getContext()).unstarGist(gistId);
                starred = false;
                return null;
            }

            protected void onException(Exception e) throws RuntimeException {
                Toast.makeText(getApplication(), e.getMessage(), LENGTH_LONG).show();
            }
        }.execute();
    }

    @Override
    public void onDialogResult(int requestCode, int resultCode, Bundle arguments) {
        if (REQUEST_CONFIRM_DELETE == requestCode && RESULT_OK == resultCode) {
            final ProgressDialog progress = new ProgressDialog(ViewGistActivity.this);
            progress.setIndeterminate(true);
            progress.setMessage(getString(string.deleting_gist));
            progress.show();
            new RoboAsyncTask<Gist>(ViewGistActivity.this) {

                public Gist call() throws Exception {
                    gistServiceProvider.get(getContext()).deleteGist(gistId);
                    return null;
                }

                protected void onSuccess(Gist gist) throws Exception {
                    progress.dismiss();
                    setResult(RESULT_DELETED);
                    finish();
                }

                protected void onException(Exception e) throws RuntimeException {
                    progress.dismiss();
                    Toast.makeText(getContext(), e.getMessage(), LENGTH_LONG).show();
                }
            }.execute();
            return;
        }

        super.onDialogResult(requestCode, resultCode, arguments);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK == resultCode && REQUEST_CODE_COMMENT == requestCode && data != null) {
            String comment = data.getStringExtra(EXTRA_COMMENT_BODY);
            if (comment != null && comment.length() > 0) {
                createComment(comment);
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void createComment(final String comment) {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage(getString(string.creating_comment));
        progress.setIndeterminate(true);
        progress.show();
        new RoboAsyncTask<Comment>(this) {

            public Comment call() throws Exception {
                return gistServiceProvider.get(ViewGistActivity.this).createComment(gistId, comment);
            }

            protected void onSuccess(Comment comment) throws Exception {
                progress.dismiss();
                refreshGist();
            }

            protected void onException(Exception e) throws RuntimeException {
                progress.dismiss();
                Toast.makeText(ViewGistActivity.this, e.getMessage(), LENGTH_LONG).show();
            }
        }.execute();

    }

    private void updateFiles(Gist gist) {
        for (View header : fileHeaders)
            list.removeHeaderView(header);
        fileHeaders.clear();
        LayoutInflater inflater = getLayoutInflater();
        for (GistFile file : gist.getFiles().values()) {
            View fileView = inflater.inflate(layout.gist_view_file_item, null);
            new GistFileViewHolder(fileView).updateViewFor(file);
            list.addHeaderView(fileView, file, true);
            fileHeaders.add(fileView);
        }
    }

    private void updateList(Gist gist, List<Comment> comments) {
        list.removeHeaderView(loadingView);
        if (list.getHeaderViewsCount() - fileHeaders.size() == 0)
            list.addHeaderView(headerView);
        headerHolder.updateViewFor(gist);

        updateFiles(gist);

        ViewHoldingListAdapter<Comment> adapter = getRootAdapter();
        if (adapter != null)
            adapter.setList(comments);
        else
            list.setAdapter(new ViewHoldingListAdapter<Comment>(comments, ViewInflator.viewInflatorFor(this,
                    layout.comment_view_item), ReflectiveHolderFactory.reflectiveFactoryFor(CommentViewHolder.class,
                    avatarHelper)));
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
        if (list.getAdapter() == null) {
            list.addHeaderView(loadingView, null, false);
            list.setAdapter(new ArrayAdapter<Comment>(this, layout.comment_view_item));
        }

        new RoboAsyncTask<FullGist>(this, executor) {

            public FullGist call() throws Exception {
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
                ErrorHelper.show(getApplication(), e, string.error_gist_load);
            }

            protected void onSuccess(FullGist fullGist) throws Exception {
                starred = fullGist.isStarred();
                loadFinished = true;
                gist = fullGist.getGist();
                comments = fullGist;
                getIntent().putExtra(EXTRA_COMMENTS, (Serializable) fullGist);
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
            startActivity(ViewGistFileActivity.createIntent(gist, (GistFile) item));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1983.java