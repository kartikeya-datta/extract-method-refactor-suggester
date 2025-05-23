error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10191.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10191.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10191.java
text:
```scala
b@@ody = new IssueBodyViewHolder(imageGetter, view);

package com.github.mobile.android.issue;

import static com.github.mobile.android.util.GitHubIntents.EXTRA_COMMENTS;
import static com.github.mobile.android.util.GitHubIntents.EXTRA_COMMENT_BODY;
import static com.github.mobile.android.util.GitHubIntents.EXTRA_ISSUE;
import static com.github.mobile.android.util.GitHubIntents.EXTRA_ISSUE_NUMBER;
import static com.github.mobile.android.util.GitHubIntents.EXTRA_REPOSITORY_NAME;
import static com.github.mobile.android.util.GitHubIntents.EXTRA_REPOSITORY_OWNER;
import static com.madgag.android.listviews.ReflectiveHolderFactory.reflectiveFactoryFor;
import static com.madgag.android.listviews.ViewInflator.viewInflatorFor;
import android.R;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mobile.android.R.color;
import com.github.mobile.android.R.id;
import com.github.mobile.android.R.layout;
import com.github.mobile.android.R.menu;
import com.github.mobile.android.comment.CommentViewHolder;
import com.github.mobile.android.comment.CreateCommentActivity;
import com.github.mobile.android.util.Avatar;
import com.github.mobile.android.util.GitHubIntents.Builder;
import com.github.mobile.android.util.Html;
import com.github.mobile.android.util.HttpImageGetter;
import com.github.mobile.android.util.Time;
import com.google.inject.Inject;
import com.madgag.android.listviews.ViewHoldingListAdapter;

import java.io.Serializable;
import java.util.List;

import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.service.IssueService;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContextScopedProvider;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;

/**
 * Activity to view a specific issue
 */
public class ViewIssueActivity extends RoboFragmentActivity {

    private static final int REQUEST_CODE_COMMENT = 1;

    /**
     * Create intent to view issue
     *
     * @param issue
     * @return intent
     */
    public static Intent viewIssueIntentFor(Issue issue) {
        return new Builder("repo.issue.VIEW").issue(issue).toIntent();
    }

    @Inject
    private ContextScopedProvider<IssueService> service;

    @InjectView(R.id.list)
    private ListView comments;

    private IssueBodyViewHolder body;

    @InjectExtra(EXTRA_REPOSITORY_NAME)
    private String repository;

    @InjectExtra(EXTRA_REPOSITORY_OWNER)
    private String repositoryOwner;

    @InjectExtra(EXTRA_ISSUE_NUMBER)
    private int issueNumber;

    private HttpImageGetter imageGetter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.issue_view);

        imageGetter = new HttpImageGetter(this);

        ((TextView) findViewById(id.tv_issue_number)).setText("Issue #" + issueNumber);

        loadIssue(false);
    }

    private Issue getIssue() {
        return (Issue) getIntent().getSerializableExtra(EXTRA_ISSUE);
    }

    @SuppressWarnings({ "unchecked" })
    private List<Comment> getComments() {
        return (List<Comment>) getIntent().getSerializableExtra(EXTRA_COMMENTS);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu options) {
        getMenuInflater().inflate(menu.issue_view, options);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case id.issue_comment:
            // Don't allow commenting before issue loads
            if (getIssue() != null)
                startActivityForResult(CreateCommentActivity.createIntent(), REQUEST_CODE_COMMENT);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
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
        progress.setMessage("Creating comment...");
        progress.setIndeterminate(true);
        progress.show();
        new RoboAsyncTask<Comment>(this) {

            public Comment call() throws Exception {
                return service.get(ViewIssueActivity.this).createComment(repositoryOwner, repository, issueNumber,
                        comment);
            }

            protected void onSuccess(Comment comment) throws Exception {
                loadComments(true);
            }

            protected void onException(Exception e) throws RuntimeException {
                Toast.makeText(ViewIssueActivity.this, e.getMessage(), 5000).show();
            }

            protected void onFinally() throws RuntimeException {
                progress.dismiss();
            };
        }.execute();

    }

    private void loadIssue(boolean force) {
        Issue current = getIssue();
        if (force || current == null)
            new RoboAsyncTask<Issue>(this) {

                public Issue call() throws Exception {
                    return service.get(ViewIssueActivity.this).getIssue(repositoryOwner, repository, issueNumber);
                }

                protected void onSuccess(Issue issue) throws Exception {
                    getIntent().putExtra(EXTRA_ISSUE, issue);
                    displayIssue(issue);
                }
            }.execute();
        else
            displayIssue(current);
    }

    private void loadComments(boolean force) {
        List<Comment> current = getComments();
        if (force || current == null)
            new RoboAsyncTask<List<Comment>>(this) {

                public List<Comment> call() throws Exception {
                    return service.get(ViewIssueActivity.this).getComments(repositoryOwner, repository, issueNumber);
                }

                protected void onSuccess(List<Comment> issueComments) throws Exception {
                    getIntent().putExtra(EXTRA_COMMENTS, (Serializable) issueComments);
                    displayComments(issueComments);
                }
            }.execute();
        else
            displayComments(current);
    }

    private void displayComments(List<Comment> issueComments) {
        comments.setAdapter(new ViewHoldingListAdapter<Comment>(issueComments, viewInflatorFor(ViewIssueActivity.this,
                layout.comment_view_item), reflectiveFactoryFor(CommentViewHolder.class, ViewIssueActivity.this,
                imageGetter)));
    }

    private void displayIssue(Issue issue) {
        ((TextView) findViewById(id.tv_issue_title)).setText(issue.getTitle());
        String reported = "<b>" + issue.getUser().getLogin() + "</b> " + Time.relativeTimeFor(issue.getCreatedAt());

        TextView creation = (TextView) findViewById(id.tv_issue_creation);
        creation.setText(Html.encode(reported));
        Avatar.bind(ViewIssueActivity.this, (ImageView) findViewById(id.iv_gravatar), issue.getUser());
        View view = getLayoutInflater().inflate(layout.issue_view_body, null);
        body = new IssueBodyViewHolder(ViewIssueActivity.this, imageGetter, view);
        body.updateViewFor(issue);

        LinearLayout labels = (LinearLayout) findViewById(id.ll_labels);
        if (!issue.getLabels().isEmpty()) {
            LabelsDrawable drawable = new LabelsDrawable(creation.getTextSize(), issue.getLabels());
            drawable.getPaint().setColor(getResources().getColor(color.item_background));
            labels.setBackgroundDrawable(drawable);
            LayoutParams params = new LayoutParams(drawable.getBounds().width(), drawable.getBounds().height());
            labels.setLayoutParams(params);
        }
        comments.addHeaderView(view);
        loadComments(false);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10191.java