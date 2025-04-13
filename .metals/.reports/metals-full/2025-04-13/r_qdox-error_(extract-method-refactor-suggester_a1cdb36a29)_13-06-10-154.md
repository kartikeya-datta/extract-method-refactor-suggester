error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6394.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6394.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,51]

error in qdox parser
file content:
```java
offset: 51
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6394.java
text:
```scala
@InjectExtra(GitHubIntents.EXTRA_REPOSITORY_NAME) S@@tring repository;

package com.github.mobile.android.issue;

import android.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mobile.android.R.id;
import com.github.mobile.android.R.layout;
import com.github.mobile.android.comment.CommentViewHolder;
import com.github.mobile.android.util.Avatar;
import com.github.mobile.android.util.GitHubIntents;
import com.github.mobile.android.util.Html;
import com.github.mobile.android.util.HttpImageGetter;
import com.github.mobile.android.util.Time;
import com.google.inject.Inject;
import com.madgag.android.listviews.ViewHoldingListAdapter;
import com.madgag.android.listviews.ViewInflator;

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

    public static Intent viewIssueIntentFor(Issue issue) {
        return new GitHubIntents.Builder("repo.issue.VIEW").issue(issue).toIntent();
    }

    @Inject
    private ContextScopedProvider<IssueService> service;

    @InjectView(R.id.list)
    private ListView comments;

    private IssueBodyViewHolder body;

    @InjectExtra(GitHubIntents.EXTRA_REPOSITORY) String repository;
    @InjectExtra(GitHubIntents.EXTRA_REPOSITORY_OWNER) String repositoryOwner;
    @InjectExtra(GitHubIntents.EXTRA_ISSUE_NUMBER) int issueNumber;


    private HttpImageGetter imageGetter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.issue_view);

        imageGetter = new HttpImageGetter(this);

        ((TextView) findViewById(id.tv_issue_number)).setText("Issue #" + issueNumber);

        loadIssue();
    }

    private void loadIssue() {
        new RoboAsyncTask<Issue>(this) {

            public Issue call() throws Exception {
                return service.get(ViewIssueActivity.this).getIssue(repositoryOwner,
                        repository, issueNumber);
            }

            protected void onSuccess(Issue issue) throws Exception {
                ((TextView) findViewById(id.tv_issue_title)).setText(issue.getTitle());
                String reported = "<b>" + issue.getUser().getLogin() + "</b> "
                        + Time.relativeTimeFor(issue.getCreatedAt());
                ((TextView) findViewById(id.tv_issue_creation)).setText(Html.encode(reported));
                Avatar.bind(ViewIssueActivity.this, (ImageView) findViewById(id.iv_gravatar), issue.getUser());
                View view = getLayoutInflater().inflate(layout.issue_view_body, null);
                body = new IssueBodyViewHolder(ViewIssueActivity.this, imageGetter, view);
                body.updateViewFor(issue);
                comments.addHeaderView(view);
                loadComments();
            }
        }.execute();
    }

    private void loadImages(final TextView view, final String html) {
        view.setText(Html.encode(html));
        new RoboAsyncTask<CharSequence>(this) {

            public CharSequence call() throws Exception {
                return Html.encode(html, imageGetter);
            }

            protected void onSuccess(CharSequence html) throws Exception {
                view.setText(html);
            }
        }.execute();
    }

    private void loadComments() {
        new RoboAsyncTask<List<Comment>>(this) {

            public List<Comment> call() throws Exception {
                return service.get(ViewIssueActivity.this).getComments(repositoryOwner,
                                        repository, issueNumber);
            }

            protected void onSuccess(List<Comment> issueComments) throws Exception {
                comments.setAdapter(new ViewHoldingListAdapter<Comment>(issueComments, ViewInflator.viewInflatorFor(
                        ViewIssueActivity.this, layout.comment_view_item), CommentViewHolder.createFactory(
                        ViewIssueActivity.this, imageGetter)));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6394.java