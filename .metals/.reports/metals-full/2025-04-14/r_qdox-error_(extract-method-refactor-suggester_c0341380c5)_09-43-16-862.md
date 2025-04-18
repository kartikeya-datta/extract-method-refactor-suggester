error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5860.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5860.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5860.java
text:
```scala
m@@ilestoneProgressArea = headerView.findViewById(id.v_closed);

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
package com.github.mobile.ui.issue;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.github.mobile.Intents.EXTRA_COMMENT;
import static com.github.mobile.Intents.EXTRA_ISSUE;
import static com.github.mobile.Intents.EXTRA_ISSUE_NUMBER;
import static com.github.mobile.Intents.EXTRA_REPOSITORY_NAME;
import static com.github.mobile.Intents.EXTRA_REPOSITORY_OWNER;
import static com.github.mobile.Intents.EXTRA_USER;
import static com.github.mobile.RequestCodes.COMMENT_CREATE;
import static com.github.mobile.RequestCodes.ISSUE_ASSIGNEE_UPDATE;
import static com.github.mobile.RequestCodes.ISSUE_CLOSE;
import static com.github.mobile.RequestCodes.ISSUE_EDIT;
import static com.github.mobile.RequestCodes.ISSUE_LABELS_UPDATE;
import static com.github.mobile.RequestCodes.ISSUE_MILESTONE_UPDATE;
import static com.github.mobile.RequestCodes.ISSUE_REOPEN;
import static org.eclipse.egit.github.core.service.IssueService.STATE_OPEN;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.github.kevinsawicki.wishlist.ViewUtils;
import com.github.mobile.R.drawable;
import com.github.mobile.R.id;
import com.github.mobile.R.layout;
import com.github.mobile.R.menu;
import com.github.mobile.R.string;
import com.github.mobile.core.issue.FullIssue;
import com.github.mobile.core.issue.IssueStore;
import com.github.mobile.core.issue.IssueUtils;
import com.github.mobile.core.issue.RefreshIssueTask;
import com.github.mobile.ui.DialogFragment;
import com.github.mobile.ui.DialogFragmentActivity;
import com.github.mobile.ui.HeaderFooterListAdapter;
import com.github.mobile.ui.StyledText;
import com.github.mobile.ui.comment.CommentListAdapter;
import com.github.mobile.util.AvatarLoader;
import com.github.mobile.util.HttpImageGetter;
import com.github.mobile.util.ShareUtils;
import com.github.mobile.util.ToastUtils;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Label;
import org.eclipse.egit.github.core.Milestone;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.User;

/**
 * Fragment to display an issue
 */
public class IssueFragment extends DialogFragment {

    private int issueNumber;

    private List<Comment> comments;

    private RepositoryId repositoryId;

    private Issue issue;

    private User user;

    @Inject
    private AvatarLoader avatars;

    @Inject
    private IssueStore store;

    private ListView list;

    private ProgressBar progress;

    private View headerView;

    private View loadingView;

    private View footerView;

    private HeaderFooterListAdapter<CommentListAdapter> adapter;

    private EditMilestoneTask milestoneTask;

    private EditAssigneeTask assigneeTask;

    private EditLabelsTask labelsTask;

    private EditStateTask stateTask;

    private View headerSeparator;

    private TextView stateText;

    private TextView titleText;

    private TextView bodyText;

    private TextView authorText;

    private TextView createdDateText;

    private ImageView creatorAvatar;

    private TextView assigneeText;

    private ImageView assigneeAvatar;

    private TextView labelsArea;

    private View milestoneArea;

    private View milestoneProgressArea;

    private TextView milestoneText;

    private MenuItem stateItem;

    @Inject
    private HttpImageGetter bodyImageGetter;

    @Inject
    private HttpImageGetter commentImageGetter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        repositoryId = RepositoryId.create(
                args.getString(EXTRA_REPOSITORY_OWNER),
                args.getString(EXTRA_REPOSITORY_NAME));
        issueNumber = args.getInt(EXTRA_ISSUE_NUMBER);
        user = (User) args.getSerializable(EXTRA_USER);

        DialogFragmentActivity dialogActivity = (DialogFragmentActivity) getActivity();

        milestoneTask = new EditMilestoneTask(dialogActivity, repositoryId,
                issueNumber) {

            @Override
            protected void onSuccess(Issue editedIssue) throws Exception {
                super.onSuccess(editedIssue);

                updateHeader(editedIssue);
            }
        };

        assigneeTask = new EditAssigneeTask(dialogActivity, repositoryId,
                issueNumber) {

            @Override
            protected void onSuccess(Issue editedIssue) throws Exception {
                super.onSuccess(editedIssue);

                updateHeader(editedIssue);
            }
        };

        labelsTask = new EditLabelsTask(dialogActivity, repositoryId,
                issueNumber) {

            @Override
            protected void onSuccess(Issue editedIssue) throws Exception {
                super.onSuccess(editedIssue);

                updateHeader(editedIssue);
            }
        };

        stateTask = new EditStateTask(dialogActivity, repositoryId, issueNumber) {

            @Override
            protected void onSuccess(Issue editedIssue) throws Exception {
                super.onSuccess(editedIssue);

                updateHeader(editedIssue);
            }
        };
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter.addHeader(headerView);
        adapter.addFooter(footerView);

        issue = store.getIssue(repositoryId, issueNumber);

        TextView loadingText = (TextView) loadingView
                .findViewById(id.tv_loading);
        loadingText.setText(string.loading_comments);

        if (issue == null || (issue.getComments() > 0 && comments == null))
            adapter.addHeader(loadingView);

        if (issue != null && comments != null)
            updateList(issue, comments);
        else {
            if (issue != null)
                updateHeader(issue);
            refreshIssue();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(layout.comment_list, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        list = finder.find(android.R.id.list);
        progress = finder.find(id.pb_loading);

        LayoutInflater inflater = getLayoutInflater(savedInstanceState);

        headerView = inflater.inflate(layout.issue_header, null);

        headerSeparator = view.findViewById(id.v_header_separator);
        stateText = (TextView) headerView.findViewById(id.tv_state);
        titleText = (TextView) headerView.findViewById(id.tv_issue_title);
        authorText = (TextView) headerView.findViewById(id.tv_issue_author);
        createdDateText = (TextView) headerView
                .findViewById(id.tv_issue_creation_date);
        creatorAvatar = (ImageView) headerView.findViewById(id.iv_avatar);
        assigneeText = (TextView) headerView.findViewById(id.tv_assignee_name);
        assigneeAvatar = (ImageView) headerView
                .findViewById(id.iv_assignee_avatar);
        labelsArea = (TextView) headerView.findViewById(id.tv_labels);
        milestoneArea = headerView.findViewById(id.ll_milestone);
        milestoneText = (TextView) headerView.findViewById(id.tv_milestone);
        milestoneProgressArea = (View) headerView.findViewById(id.v_closed);
        bodyText = (TextView) headerView.findViewById(id.tv_issue_body);
        bodyText.setMovementMethod(LinkMovementMethod.getInstance());

        loadingView = inflater.inflate(layout.loading_item, null);

        footerView = inflater.inflate(layout.footer_separator, null);

        stateText.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (issue != null)
                    stateTask.confirm(STATE_OPEN.equals(issue.getState()));
            }
        });

        milestoneArea.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                if (issue != null)
                    milestoneTask.prompt(issue.getMilestone());
            }
        });

        headerView.findViewById(id.ll_assignee).setOnClickListener(
                new OnClickListener() {

                    public void onClick(View v) {
                        if (issue != null)
                            assigneeTask.prompt(issue.getAssignee());
                    }
                });

        labelsArea.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                if (issue != null)
                    labelsTask.prompt(issue.getLabels());
            }
        });

        Activity activity = getActivity();
        adapter = new HeaderFooterListAdapter<CommentListAdapter>(list,
                new CommentListAdapter(activity.getLayoutInflater(), avatars,
                        commentImageGetter));
        list.setAdapter(adapter);
    }

    private void updateHeader(final Issue issue) {
        if (!isUsable())
            return;

        titleText.setText(issue.getTitle());

        String body = issue.getBodyHtml();
        if (!TextUtils.isEmpty(body))
            bodyImageGetter.bind(bodyText, body, issue.getId());
        else
            bodyText.setText(string.no_description_given);

        authorText.setText(issue.getUser().getLogin());
        createdDateText.setText(new StyledText().append(
                getString(string.prefix_opened)).append(issue.getCreatedAt()));
        avatars.bind(creatorAvatar, issue.getUser());

        boolean open = STATE_OPEN.equals(issue.getState());
        if (!open) {
            StyledText text = new StyledText();
            text.bold(getString(string.closed));
            Date closedAt = issue.getClosedAt();
            if (closedAt != null)
                text.append(' ').append(closedAt);
            stateText.setText(text);
        }
        ViewUtils.setGone(stateText, open);
        ViewUtils.setGone(headerSeparator, !open);

        User assignee = issue.getAssignee();
        if (assignee != null) {
            StyledText name = new StyledText();
            name.bold(assignee.getLogin());
            name.append(' ').append(getString(string.assigned));
            assigneeText.setText(name);
            assigneeAvatar.setVisibility(VISIBLE);
            avatars.bind(assigneeAvatar, assignee);
        } else {
            assigneeAvatar.setVisibility(GONE);
            assigneeText.setText(string.unassigned);
        }

        List<Label> labels = issue.getLabels();
        if (labels != null && !labels.isEmpty()) {
            LabelDrawableSpan.setText(labelsArea, labels);
            labelsArea.setVisibility(VISIBLE);
        } else
            labelsArea.setVisibility(GONE);

        if (issue.getMilestone() != null) {
            Milestone milestone = issue.getMilestone();
            StyledText milestoneLabel = new StyledText();
            milestoneLabel.append(getString(string.milestone_prefix));
            milestoneLabel.append(' ');
            milestoneLabel.bold(milestone.getTitle());
            milestoneText.setText(milestoneLabel);
            float closed = milestone.getClosedIssues();
            float total = closed + milestone.getOpenIssues();
            if (total > 0) {
                ((LayoutParams) milestoneProgressArea.getLayoutParams()).weight = closed
                        / total;
                milestoneProgressArea.setVisibility(VISIBLE);
            } else
                milestoneProgressArea.setVisibility(GONE);
            milestoneArea.setVisibility(VISIBLE);
        } else
            milestoneArea.setVisibility(GONE);

        String state = issue.getState();
        if (state != null && state.length() > 0)
            state = state.substring(0, 1).toUpperCase(Locale.US)
                    + state.substring(1);
        else
            state = "";

        ViewUtils.setGone(progress, true);
        ViewUtils.setGone(list, false);
        updateStateItem(issue);
    }

    private void refreshIssue() {
        getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);

        new RefreshIssueTask(getActivity(), repositoryId, issueNumber,
                bodyImageGetter, commentImageGetter) {

            @Override
            protected void onException(Exception e) throws RuntimeException {
                super.onException(e);

                ToastUtils.show(getActivity(), e, string.error_issue_load);
                ViewUtils.setGone(progress, true);

                getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
            }

            @Override
            protected void onSuccess(FullIssue fullIssue) throws Exception {
                super.onSuccess(fullIssue);

                if (!isUsable())
                    return;

                issue = fullIssue.getIssue();
                comments = fullIssue;
                updateList(fullIssue.getIssue(), fullIssue);

                getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
            }
        }.execute();

    }

    private void updateList(Issue issue, List<Comment> comments) {
        adapter.getWrappedAdapter().setItems(comments);
        adapter.removeHeader(loadingView);

        headerView.setVisibility(VISIBLE);
        updateHeader(issue);
    }

    @Override
    public void onDialogResult(int requestCode, int resultCode, Bundle arguments) {
        if (RESULT_OK != resultCode)
            return;

        switch (requestCode) {
        case ISSUE_MILESTONE_UPDATE:
            milestoneTask.edit(MilestoneDialogFragment.getSelected(arguments));
            break;
        case ISSUE_ASSIGNEE_UPDATE:
            assigneeTask.edit(AssigneeDialogFragment.getSelected(arguments));
            break;
        case ISSUE_LABELS_UPDATE:
            ArrayList<Label> labels = LabelsDialogFragment
                    .getSelected(arguments);
            if (labels != null && !labels.isEmpty())
                labelsTask.edit(labels.toArray(new Label[labels.size()]));
            else
                labelsTask.edit(null);
            break;
        case ISSUE_CLOSE:
            stateTask.edit(true);
            break;
        case ISSUE_REOPEN:
            stateTask.edit(false);
            break;
        }
    }

    private void updateStateItem(Issue issue) {
        if (issue != null && stateItem != null)
            if (STATE_OPEN.equals(issue.getState()))
                stateItem.setTitle(string.close).setIcon(
                        drawable.menu_issue_close);
            else
                stateItem.setTitle(string.reopen).setIcon(
                        drawable.menu_issue_open);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        updateStateItem(issue);
    }

    @Override
    public void onCreateOptionsMenu(Menu optionsMenu, MenuInflater inflater) {
        inflater.inflate(menu.issue_view, optionsMenu);
        stateItem = optionsMenu.findItem(id.m_state);
        updateStateItem(issue);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK != resultCode || data == null)
            return;

        switch (requestCode) {
        case ISSUE_EDIT:
            updateHeader((Issue) data.getSerializableExtra(EXTRA_ISSUE));
            return;
        case COMMENT_CREATE:
            Comment comment = (Comment) data
                    .getSerializableExtra(EXTRA_COMMENT);
            if (comments != null) {
                comments.add(comment);
                issue.setComments(issue.getComments() + 1);
                updateList(issue, comments);
            } else
                refreshIssue();
            return;
        }
    }

    private void shareIssue() {
        String id = repositoryId.generateId();
        if (IssueUtils.isPullRequest(issue))
            startActivity(ShareUtils.create("Pull Request " + issueNumber
                    + " on " + id, "https://github.com/" + id + "/pull/"
                    + issueNumber));
        else
            startActivity(ShareUtils
                    .create("Issue " + issueNumber + " on " + id,
                            "https://github.com/" + id + "/issues/"
                                    + issueNumber));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case id.m_edit:
            if (issue != null)
                startActivityForResult(EditIssueActivity.createIntent(issue,
                        repositoryId.getOwner(), repositoryId.getName(), user),
                        ISSUE_EDIT);
            return true;
        case id.m_comment:
            if (issue != null)
                startActivityForResult(CreateCommentActivity.createIntent(
                        repositoryId, issueNumber, user), COMMENT_CREATE);
            return true;
        case id.m_refresh:
            refreshIssue();
            return true;
        case id.m_share:
            if (issue != null)
                shareIssue();
            return true;
        case id.m_state:
            if (issue != null)
                stateTask.confirm(STATE_OPEN.equals(issue.getState()));
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5860.java