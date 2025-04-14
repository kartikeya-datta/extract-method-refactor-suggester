error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9342.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9342.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9342.java
text:
```scala
h@@eaderView.findViewById(id.v_labels).setOnClickListener(new OnClickListener() {

package com.github.mobile.android.issue;

import static android.widget.Toast.LENGTH_LONG;
import static com.github.mobile.android.util.GitHubIntents.EXTRA_REPOSITORY;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.github.mobile.android.DialogFragmentActivity;
import com.github.mobile.android.MultiChoiceDialogFragment;
import com.github.mobile.android.R.id;
import com.github.mobile.android.R.layout;
import com.github.mobile.android.R.menu;
import com.github.mobile.android.R.string;
import com.github.mobile.android.SingleChoiceDialogFragment;
import com.github.mobile.android.TextWatcherAdapter;
import com.github.mobile.android.async.AuthenticatedUserTask;
import com.github.mobile.android.ui.issue.ViewIssuesActivity;
import com.github.mobile.android.util.AvatarHelper;
import com.github.mobile.android.util.GitHubIntents.Builder;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Label;
import org.eclipse.egit.github.core.Milestone;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.service.CollaboratorService;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.LabelService;
import org.eclipse.egit.github.core.service.MilestoneService;

import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

/**
 * Activity to create a new issue
 */
public class CreateIssueActivity extends DialogFragmentActivity {

    /**
     * Create intent to create an issue in the given {@link Repository}
     *
     * @param repo
     * @return intent
     */
    public static Intent createIntent(Repository repo) {
        return new Builder("repo.issues.create.VIEW").repo(repo).toIntent();
    }

    private static final String TAG = "CIA";

    private static final int REQUEST_CODE_LABELS = 1;

    private static final int REQUEST_CODE_MILESTONE = 2;

    private static final int REQUEST_CODE_ASSIGNEE = 3;

    @Inject
    private AvatarHelper avatarHelper;

    @Inject
    private IssueService service;

    @Inject
    private IssueStore store;

    @Inject
    private LabelService labelService;

    @Inject
    private MilestoneService milestoneService;

    @Inject
    private CollaboratorService collaboratorService;

    @InjectExtra(EXTRA_REPOSITORY)
    private Repository repo;

    private LabelsDialog labelsDialog;

    private MilestoneDialog milestoneDialog;

    private AssigneeDialog assigneeDialog;

    private CreateIssueHeaderViewHolder header;

    @InjectView(id.et_issue_title)
    private EditText titleText;

    @InjectView(id.et_issue_body)
    private EditText bodyText;

    private final Issue newIssue = new Issue();

    @Override
    public boolean onCreateOptionsMenu(Menu options) {
        getSupportMenuInflater().inflate(menu.issue_create, options);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.issue_create);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(string.new_issue);
        actionBar.setSubtitle(repo.generateId());
        avatarHelper.bind(actionBar, repo.getOwner());

        labelsDialog = new LabelsDialog(this, REQUEST_CODE_LABELS, repo, labelService);
        milestoneDialog = new MilestoneDialog(this, REQUEST_CODE_MILESTONE, repo, milestoneService);
        assigneeDialog = new AssigneeDialog(this, REQUEST_CODE_ASSIGNEE, repo, collaboratorService);

        View headerView = findViewById(id.ll_issue_header);
        headerView.findViewById(id.ll_milestone).setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                milestoneDialog.show(newIssue.getMilestone());
            }
        });
        headerView.findViewById(id.ll_assignee).setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                User assignee = newIssue.getAssignee();
                assigneeDialog.show(assignee != null ? assignee.getLogin() : null);
            }
        });
        headerView.findViewById(id.ll_labels).setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                labelsDialog.show(newIssue.getLabels());
            }
        });

        titleText.addTextChangedListener(new TextWatcherAdapter() {

            public void afterTextChanged(Editable s) {
                invalidateOptionsMenu();
            }
        });

        header = new CreateIssueHeaderViewHolder(headerView, avatarHelper, getResources());
        header.updateViewFor(newIssue);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(id.issue_create).setEnabled(titleText.getText().toString().length() > 0);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onDialogResult(int requestCode, int resultCode, Bundle arguments) {
        if (RESULT_OK != resultCode)
            return;

        switch (requestCode) {
        case REQUEST_CODE_LABELS:
            String[] labelNames = arguments.getStringArray(MultiChoiceDialogFragment.ARG_SELECTED);
            if (labelNames != null && labelNames.length > 0) {
                List<Label> labels = new ArrayList<Label>(labelNames.length);
                for (String name : labelNames)
                    labels.add(labelsDialog.getLabel(name));
                newIssue.setLabels(labels);
            } else
                newIssue.setLabels(null);
            header.updateViewFor(newIssue);
            break;
        case REQUEST_CODE_MILESTONE:
            String title = arguments.getString(SingleChoiceDialogFragment.ARG_SELECTED);
            if (title != null)
                newIssue.setMilestone(new Milestone().setTitle(title).setNumber(
                        milestoneDialog.getMilestoneNumber(title)));
            else
                newIssue.setMilestone(null);
            header.updateViewFor(newIssue);
            break;
        case REQUEST_CODE_ASSIGNEE:
            String login = arguments.getString(SingleChoiceDialogFragment.ARG_SELECTED);

            if (login != null) {
                User assignee = assigneeDialog.getCollaborator(login);
                if (assignee == null)
                    assignee = new User().setLogin(login);
                newIssue.setAssignee(assignee);
            } else
                newIssue.setAssignee(null);
            header.updateViewFor(newIssue);
            break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case id.issue_create:
            createIssue();
            return true;
        case id.issue_labels:
            labelsDialog.show(newIssue.getLabels());
            return true;
        case id.issue_milestone:
            milestoneDialog.show(newIssue.getMilestone());
            return true;
        case id.issue_assignee:
            User assignee = newIssue.getAssignee();
            assigneeDialog.show(assignee != null ? assignee.getLogin() : null);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void createIssue() {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Creating Issue...");
        progress.show();
        newIssue.setTitle(titleText.getText().toString());
        newIssue.setBody(bodyText.getText().toString());
        new AuthenticatedUserTask<Issue>(this) {

            public Issue run() throws Exception {
                return store.addIssue(service.createIssue(repo, newIssue));
            }

            protected void onSuccess(Issue issue) throws Exception {
                progress.cancel();
                startActivity(ViewIssuesActivity.createIntent(issue));
                setResult(RESULT_OK);
                finish();
            }

            protected void onException(Exception e) throws RuntimeException {
                progress.cancel();
                Log.e(TAG, e.getMessage(), e);
                Toast.makeText(CreateIssueActivity.this, e.getMessage(), LENGTH_LONG).show();
            }
        }.execute();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9342.java