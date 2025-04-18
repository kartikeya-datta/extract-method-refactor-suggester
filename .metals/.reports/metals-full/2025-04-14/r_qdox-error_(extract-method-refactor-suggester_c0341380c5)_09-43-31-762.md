error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9970.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9970.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9970.java
text:
```scala
D@@ate commitCommitterDate = CommitUtils.getCommitterDate(commit);

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
package com.github.mobile.ui.commit;

import static android.app.Activity.RESULT_OK;
import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.graphics.Paint.UNDERLINE_TEXT_FLAG;
import static com.github.mobile.Intents.EXTRA_BASE;
import static com.github.mobile.Intents.EXTRA_COMMENT;
import static com.github.mobile.Intents.EXTRA_REPOSITORY;
import static com.github.mobile.RequestCodes.COMMENT_CREATE;
import android.accounts.Account;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.github.kevinsawicki.wishlist.ViewFinder;
import com.github.kevinsawicki.wishlist.ViewUtils;
import com.github.mobile.R.id;
import com.github.mobile.R.layout;
import com.github.mobile.R.menu;
import com.github.mobile.R.string;
import com.github.mobile.core.commit.CommitStore;
import com.github.mobile.core.commit.CommitUtils;
import com.github.mobile.core.commit.FullCommit;
import com.github.mobile.core.commit.FullCommitFile;
import com.github.mobile.core.commit.RefreshCommitTask;
import com.github.mobile.ui.DialogFragment;
import com.github.mobile.ui.HeaderFooterListAdapter;
import com.github.mobile.ui.LightAlertDialog;
import com.github.mobile.ui.StyledText;
import com.github.mobile.util.AvatarLoader;
import com.github.mobile.util.HttpImageGetter;
import com.github.mobile.util.ShareUtils;
import com.github.mobile.util.ToastUtils;
import com.google.inject.Inject;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.CommitComment;
import org.eclipse.egit.github.core.CommitFile;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;

/**
 * Fragment to display commit details with diff output
 */
public class CommitDiffListFragment extends DialogFragment implements
        OnItemClickListener {

    private DiffStyler diffStyler;

    private ListView list;

    private ProgressBar progress;

    private Repository repository;

    private String base;

    private RepositoryCommit commit;

    private List<CommitComment> comments;

    private List<FullCommitFile> files;

    @Inject
    private AvatarLoader avatars;

    @Inject
    private CommitStore store;

    private View loadingView;

    private View commitHeader;

    private TextView commitMessage;

    private View authorArea;

    private ImageView authorAvatar;

    private TextView authorName;

    private TextView authorDate;

    private View committerArea;

    private ImageView committerAvatar;

    private TextView committerName;

    private TextView committerDate;

    private HeaderFooterListAdapter<CommitFileListAdapter> adapter;

    @Inject
    private HttpImageGetter commentImageGetter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        base = args.getString(EXTRA_BASE);
        repository = (Repository) args.getSerializable(EXTRA_REPOSITORY);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        commit = store.getCommit(repository, base);

        ((TextView) loadingView.findViewById(id.tv_loading))
                .setText(string.loading_files_and_comments);

        if (files == null
 (commit != null && commit.getCommit().getCommentCount() > 0 && comments == null))
            adapter.addFooter(loadingView);

        if (commit != null && comments != null && files != null)
            updateList(commit, comments, files);
        else {
            if (commit != null)
                updateHeader(commit);
            refreshCommit();
        }
    }

    private void addComment(final CommitComment comment) {
        if (comments != null && files != null) {
            comments.add(comment);
            Commit rawCommit = commit.getCommit();
            if (rawCommit != null)
                rawCommit.setCommentCount(rawCommit.getCommentCount() + 1);
            commentImageGetter.encode(comment, comment.getBodyHtml());
            updateItems(comments, files);
        } else
            refreshCommit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK == resultCode && COMMENT_CREATE == requestCode
                && data != null) {
            CommitComment comment = (CommitComment) data
                    .getSerializableExtra(EXTRA_COMMENT);
            addComment(comment);
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreateOptionsMenu(final Menu optionsMenu,
            final MenuInflater inflater) {
        inflater.inflate(menu.commit_view, optionsMenu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (!isUsable())
            return false;

        switch (item.getItemId()) {
        case id.m_refresh:
            refreshCommit();
            return true;
        case id.m_comment:
            startActivityForResult(
                    CreateCommentActivity.createIntent(repository, base),
                    COMMENT_CREATE);
            return true;
        case id.m_share:
            shareCommit();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void shareCommit() {
        String id = repository.generateId();
        startActivity(ShareUtils.create(
                "Commit " + CommitUtils.abbreviate(base) + " on " + id,
                "https://github.com/" + id + "/commit/" + base));
    }

    private void refreshCommit() {
        getSherlockActivity()
                .setSupportProgressBarIndeterminateVisibility(true);

        new RefreshCommitTask(getActivity(), repository, base,
                commentImageGetter) {

            @Override
            protected FullCommit run(Account account) throws Exception {
                FullCommit full = super.run(account);

                List<CommitFile> files = full.getCommit().getFiles();
                diffStyler.setFiles(files);
                if (files != null)
                    Collections.sort(files, new CommitFileComparator());
                return full;
            }

            @Override
            protected void onSuccess(FullCommit commit) throws Exception {
                super.onSuccess(commit);

                updateList(commit.getCommit(), commit, commit.getFiles());
                getSherlockActivity()
                        .setSupportProgressBarIndeterminateVisibility(false);
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                super.onException(e);

                ToastUtils.show(getActivity(), e, string.error_commit_load);
                ViewUtils.setGone(progress, true);

                getSherlockActivity()
                        .setSupportProgressBarIndeterminateVisibility(false);
            }

        }.execute();
    }

    private boolean isDifferentCommitter(final String author,
            final String committer) {
        return committer != null && !committer.equals(author);
    }

    private void addCommitDetails(RepositoryCommit commit) {
        adapter.addHeader(commitHeader);

        commitMessage.setText(commit.getCommit().getMessage());

        String commitAuthor = CommitUtils.getAuthor(commit);
        String commitCommitter = CommitUtils.getCommitter(commit);

        if (commitAuthor != null) {
            CommitUtils.bindAuthor(commit, avatars, authorAvatar);
            authorName.setText(commitAuthor);
            StyledText styledAuthor = new StyledText();
            styledAuthor.append(getString(string.authored));

            Date commitAuthorDate = CommitUtils.getAuthorDate(commit);
            if (commitAuthorDate != null)
                styledAuthor.append(' ').append(commitAuthorDate);

            authorDate.setText(styledAuthor);
            ViewUtils.setGone(authorArea, false);
        } else
            ViewUtils.setGone(authorArea, true);

        if (isDifferentCommitter(commitAuthor, commitCommitter)) {
            CommitUtils.bindCommitter(commit, avatars, committerAvatar);
            committerName.setText(commitCommitter);
            StyledText styledCommitter = new StyledText();
            styledCommitter.append(getString(string.committed));

            Date commitCommitterDate = CommitUtils.getCommiterDate(commit);
            if (commitCommitterDate != null)
                styledCommitter.append(' ').append(commitCommitterDate);

            committerDate.setText(styledCommitter);
            ViewUtils.setGone(committerArea, false);
        } else
            ViewUtils.setGone(committerArea, true);
    }

    private void addDiffStats(RepositoryCommit commit, LayoutInflater inflater) {
        View fileHeader = inflater.inflate(layout.commit_file_details_header,
                null);
        ((TextView) fileHeader.findViewById(id.tv_commit_file_summary))
                .setText(CommitUtils.formatStats(commit.getFiles()));
        adapter.addHeader(fileHeader);
    }

    private void addCommitParents(RepositoryCommit commit,
            LayoutInflater inflater) {
        List<Commit> parents = commit.getParents();
        if (parents == null || parents.isEmpty())
            return;

        for (Commit parent : parents) {
            View parentView = inflater.inflate(layout.commit_parent_item, null);
            TextView parentIdText = (TextView) parentView
                    .findViewById(id.tv_commit_id);
            parentIdText.setPaintFlags(parentIdText.getPaintFlags()
 UNDERLINE_TEXT_FLAG);
            StyledText parentText = new StyledText();
            parentText.append(getString(string.parent_prefix));
            parentText.monospace(CommitUtils.abbreviate(parent));
            parentIdText.setText(parentText);
            adapter.addHeader(parentView, parent, true);
        }
    }

    private void updateHeader(RepositoryCommit commit) {
        ViewUtils.setGone(progress, true);
        ViewUtils.setGone(list, false);

        addCommitDetails(commit);
        addCommitParents(commit, getActivity().getLayoutInflater());
    }

    private void updateList(RepositoryCommit commit,
            List<CommitComment> comments, List<FullCommitFile> files) {
        if (!isUsable())
            return;

        this.commit = commit;
        this.comments = comments;
        this.files = files;

        adapter.clearHeaders();
        adapter.clearFooters();
        updateHeader(commit);
        addDiffStats(commit, getActivity().getLayoutInflater());
        updateItems(comments, files);
    }

    private void updateItems(List<CommitComment> comments,
            List<FullCommitFile> files) {
        CommitFileListAdapter rootAdapter = adapter.getWrappedAdapter();
        rootAdapter.clear();
        for (FullCommitFile file : files)
            rootAdapter.addItem(file);
        for (CommitComment comment : comments)
            rootAdapter.addComment(comment);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        list = finder.find(android.R.id.list);
        progress = finder.find(id.pb_loading);

        diffStyler = new DiffStyler(getResources());

        list.setOnItemClickListener(this);

        LayoutInflater inflater = getActivity().getLayoutInflater();

        adapter = new HeaderFooterListAdapter<CommitFileListAdapter>(list,
                new CommitFileListAdapter(inflater, diffStyler, avatars,
                        commentImageGetter));
        adapter.addFooter(inflater.inflate(layout.footer_separator, null));
        list.setAdapter(adapter);

        commitHeader = inflater.inflate(layout.commit_header, null);
        commitMessage = (TextView) commitHeader
                .findViewById(id.tv_commit_message);

        authorArea = commitHeader.findViewById(id.ll_author);
        authorAvatar = (ImageView) commitHeader.findViewById(id.iv_author);
        authorName = (TextView) commitHeader.findViewById(id.tv_author);
        authorDate = (TextView) commitHeader.findViewById(id.tv_author_date);

        committerArea = commitHeader.findViewById(id.ll_committer);
        committerAvatar = (ImageView) commitHeader
                .findViewById(id.iv_committer);
        committerName = (TextView) commitHeader.findViewById(id.tv_committer);
        committerDate = (TextView) commitHeader.findViewById(id.tv_commit_date);

        loadingView = inflater.inflate(layout.loading_item, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(layout.commit_diff_list, null);
    }

    private void showFileOptions(CharSequence line, final int position,
            final CommitFile file) {
        final AlertDialog dialog = LightAlertDialog.create(getActivity());
        dialog.setTitle(CommitUtils.getName(file));
        dialog.setCanceledOnTouchOutside(true);

        View view = getActivity().getLayoutInflater().inflate(
                layout.diff_line_dialog, null);
        ViewFinder finder = new ViewFinder(view);

        TextView diff = finder.textView(id.tv_diff);
        diff.setText(line);
        diffStyler.updateColors(line, diff);

        finder.setText(id.tv_commit, getString(string.commit_prefix)
                + CommitUtils.abbreviate(commit));

        finder.find(id.ll_view_area).setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                dialog.dismiss();

                openFile(file);
            }
        });

        finder.find(id.ll_comment_area).setOnClickListener(
                new OnClickListener() {

                    public void onClick(View v) {
                        dialog.dismiss();

                        startActivityForResult(CreateCommentActivity
                                .createIntent(repository, commit.getSha(),
                                        file.getFilename(), position),
                                COMMENT_CREATE);
                    }
                });

        dialog.setView(view);
        dialog.setButton(BUTTON_NEGATIVE, getString(string.cancel),
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }

    private void openFile(CommitFile file) {
        if (!TextUtils.isEmpty(file.getFilename())
                && !TextUtils.isEmpty(file.getSha()))
            startActivity(CommitFileViewActivity.createIntent(repository, base,
                    file));
    }

    /**
     * Select previous file by scanning backwards from the current position
     *
     * @param position
     * @param item
     * @param parent
     */
    private void selectPreviousFile(int position, Object item,
            AdapterView<?> parent) {
        CharSequence line;
        if (item instanceof CharSequence)
            line = (CharSequence) item;
        else
            line = null;

        int linePosition = 0;
        while (--position >= 0) {
            item = parent.getItemAtPosition(position);

            if (item instanceof CommitFile) {
                if (line != null)
                    showFileOptions(line, linePosition, (CommitFile) item);
                break;
            } else if (item instanceof CharSequence)
                if (line != null)
                    linePosition++;
                else
                    line = (CharSequence) item;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        Object item = parent.getItemAtPosition(position);
        if (item instanceof Commit)
            startActivity(CommitViewActivity.createIntent(repository,
                    ((Commit) item).getSha()));
        else if (item instanceof CommitFile)
            openFile((CommitFile) item);
        else if (item instanceof CharSequence)
            selectPreviousFile(position, item, parent);
        else if (item instanceof CommitComment)
            if (!TextUtils.isEmpty(((CommitComment) item).getPath()))
                selectPreviousFile(position, item, parent);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9970.java