error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/761.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/761.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/761.java
text:
```scala
V@@iew root = inflater.inflate(layout.comment_list_view, null);

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
package com.github.mobile.ui.gist;

import static android.app.Activity.RESULT_OK;
import static android.graphics.Typeface.ITALIC;
import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.github.mobile.Intents.EXTRA_COMMENT_BODY;
import static com.github.mobile.Intents.EXTRA_GIST_ID;
import static com.github.mobile.RequestCodes.COMMENT_CREATE;
import static java.lang.String.CASE_INSENSITIVE_ORDER;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.github.mobile.R.id;
import com.github.mobile.R.layout;
import com.github.mobile.R.menu;
import com.github.mobile.R.string;
import com.github.mobile.accounts.AccountUtils;
import com.github.mobile.core.gist.FullGist;
import com.github.mobile.core.gist.GistStore;
import com.github.mobile.core.gist.RefreshGistTask;
import com.github.mobile.core.gist.StarGistTask;
import com.github.mobile.core.gist.UnstarGistTask;
import com.github.mobile.ui.comment.CommentListAdapter;
import com.github.mobile.ui.comment.CreateCommentActivity;
import com.github.mobile.util.AvatarLoader;
import com.github.mobile.util.HttpImageGetter;
import com.github.mobile.util.TimeUtils;
import com.github.mobile.util.ToastUtils;
import com.github.mobile.util.TypefaceUtils;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.GistFile;
import org.eclipse.egit.github.core.User;

import roboguice.inject.InjectView;

/**
 * Activity to display an existing Gist
 */
public class GistFragment extends RoboSherlockFragment implements OnItemClickListener {

    private static final SpannableStringBuilder NO_DESCRIPTION;

    static {
        NO_DESCRIPTION = new SpannableStringBuilder("No description");
        NO_DESCRIPTION.setSpan(new StyleSpan(ITALIC), 0, NO_DESCRIPTION.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private String gistId;

    private List<Comment> comments;

    private Gist gist;

    @InjectView(android.R.id.list)
    private ListView list;

    @Inject
    private GistStore store;

    @Inject
    private HttpImageGetter imageGetter;

    private View headerView;

    private TextView created;

    private TextView updated;

    private TextView description;

    private View loadingView;

    private boolean starred;

    private boolean loadFinished;

    @Inject
    private AvatarLoader avatarHelper;

    private List<View> fileHeaders = new ArrayList<View>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        gistId = getArguments().getString(EXTRA_GIST_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(layout.gist_view, null);

        headerView = inflater.inflate(layout.gist_header, null);
        created = (TextView) headerView.findViewById(id.tv_gist_creation);
        updated = (TextView) headerView.findViewById(id.tv_gist_updated);
        description = (TextView) headerView.findViewById(id.tv_gist_description);

        loadingView = inflater.inflate(layout.loading_item, null);
        ((TextView) loadingView.findViewById(id.tv_loading)).setText(string.loading_comments);

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        list.setOnItemClickListener(this);
        list.addHeaderView(headerView, null, false);

        gist = store.getGist(gistId);

        if (gist != null) {
            updateHeader(gist);
            updateFiles(gist);
        } else {
            ((TextView) loadingView.findViewById(id.tv_loading)).setText(string.loading_gist);
            headerView.setVisibility(GONE);
        }

        if (gist == null || (gist.getComments() > 0 && comments == null)) {
            if (gist == null || gist.getFiles() == null || gist.getFiles().isEmpty())
                loadingView.findViewById(id.v_separator).setVisibility(GONE);
            list.addHeaderView(loadingView, null, false);
        }

        List<Comment> initialComments = comments;
        if (initialComments == null)
            initialComments = Collections.emptyList();

        Activity activity = getActivity();
        list.setAdapter(new CommentListAdapter(activity.getLayoutInflater(), initialComments
                .toArray(new Comment[initialComments.size()]), avatarHelper, new HttpImageGetter(activity)));

        if (gist != null && comments != null)
            updateList(gist, comments);
        else
            refreshGist();
    }

    private boolean isOwner() {
        if (gist == null)
            return false;
        User user = gist.getUser();
        if (user == null)
            return false;
        String login = AccountUtils.getLogin(getActivity());
        return login != null && login.equals(user.getLogin());
    }

    private void updateHeader(Gist gist) {
        Date createdAt = gist.getCreatedAt();
        if (createdAt != null) {
            created.setText(getString(string.prefix_created) + TimeUtils.getRelativeTime(createdAt));
            created.setVisibility(VISIBLE);
        } else
            created.setVisibility(GONE);

        Date updatedAt = gist.getUpdatedAt();
        if (updatedAt != null && !updatedAt.equals(createdAt)) {
            updated.setText(getString(string.updated_prefix) + TimeUtils.getRelativeTime(updatedAt));
            updated.setVisibility(VISIBLE);
        } else
            updated.setVisibility(GONE);

        String desc = gist.getDescription();
        if (!TextUtils.isEmpty(desc))
            description.setText(desc);
        else
            description.setText(NO_DESCRIPTION);
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
        if (gist == null)
            return super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
        case id.gist_comment:
            String title = getString(string.gist_title) + gistId;
            User user = gist.getUser();
            String subtitle = user != null ? user.getLogin() : null;
            startActivityForResult(CreateCommentActivity.createIntent(title, subtitle, user), COMMENT_CREATE);
            return true;
        case id.gist_star:
            if (starred)
                unstarGist();
            else
                starGist();
            return true;
        case id.refresh:
            refreshGist();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void starGist() {
        ToastUtils.show(getActivity(), string.starring_gist);

        new StarGistTask(getActivity(), gistId) {

            @Override
            protected void onSuccess(Gist gist) throws Exception {
                super.onSuccess(gist);

                starred = true;
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                super.onException(e);

                ToastUtils.show((Activity) getContext(), e.getMessage());
            }

        }.execute();
    }

    private void unstarGist() {
        ToastUtils.show(getActivity(), string.unstarring_gist);

        new UnstarGistTask(getActivity(), gistId) {

            @Override
            protected void onSuccess(Gist gist) throws Exception {
                super.onSuccess(gist);

                starred = false;
            }

            protected void onException(Exception e) throws RuntimeException {
                super.onException(e);

                ToastUtils.show((Activity) getContext(), e.getMessage());
            }

        }.execute();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK == resultCode && COMMENT_CREATE == requestCode && data != null) {
            String comment = data.getStringExtra(EXTRA_COMMENT_BODY);
            if (comment != null && comment.length() > 0) {
                createComment(comment);
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void createComment(final String comment) {
        new CreateCommentTask(getActivity(), gistId, comment) {

            @Override
            protected void onSuccess(Comment comment) throws Exception {
                super.onSuccess(comment);

                refreshGist();
            }

        }.start();
    }

    private void updateFiles(Gist gist) {
        final Activity activity = getActivity();
        if (activity == null)
            return;

        for (View header : fileHeaders)
            list.removeHeaderView(header);
        fileHeaders.clear();

        Map<String, GistFile> files = gist.getFiles();
        if (files == null || files.isEmpty())
            return;

        final GistFile[] sortedFiles = files.values().toArray(new GistFile[files.size()]);
        Arrays.sort(sortedFiles, new Comparator<GistFile>() {

            public int compare(final GistFile lhs, final GistFile rhs) {
                return CASE_INSENSITIVE_ORDER.compare(lhs.getFilename(), rhs.getFilename());
            }
        });

        final LayoutInflater inflater = activity.getLayoutInflater();
        final Typeface octicons = TypefaceUtils.getOcticons(activity);
        for (GistFile file : sortedFiles) {
            View fileView = inflater.inflate(layout.gist_file_item, null);
            ((TextView) fileView.findViewById(id.tv_file)).setText(file.getFilename());
            ((TextView) fileView.findViewById(id.tv_file_icon)).setTypeface(octicons);
            list.addHeaderView(fileView, file, true);
            fileHeaders.add(fileView);
        }
    }

    private void updateList(Gist gist, List<Comment> comments) {
        list.removeHeaderView(loadingView);

        headerView.setVisibility(VISIBLE);
        updateHeader(gist);

        updateFiles(gist);

        CommentListAdapter adapter = getRootAdapter();
        if (adapter != null)
            adapter.setItems(comments.toArray(new Comment[comments.size()]));
    }

    private CommentListAdapter getRootAdapter() {
        ListAdapter adapter = list.getAdapter();
        if (adapter == null)
            return null;
        adapter = ((HeaderViewListAdapter) adapter).getWrappedAdapter();
        if (adapter instanceof CommentListAdapter)
            return (CommentListAdapter) adapter;
        else
            return null;
    }

    private void refreshGist() {
        new RefreshGistTask(getActivity(), gistId, imageGetter) {

            @Override
            protected void onException(Exception e) throws RuntimeException {
                super.onException(e);

                ToastUtils.show(getActivity(), e, string.error_gist_load);
            }

            @Override
            protected void onSuccess(FullGist fullGist) throws Exception {
                super.onSuccess(fullGist);

                if (getActivity() == null)
                    return;

                starred = fullGist.isStarred();
                loadFinished = true;
                gist = fullGist.getGist();
                comments = fullGist;
                updateList(fullGist.getGist(), fullGist);
            }

        }.execute();
    }

    @Override
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/761.java