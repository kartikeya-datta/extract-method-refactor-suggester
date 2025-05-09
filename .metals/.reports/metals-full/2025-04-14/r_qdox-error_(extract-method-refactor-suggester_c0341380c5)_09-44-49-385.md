error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2928.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2928.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2928.java
text:
```scala
I@@nteger.toString(payload.getPullRequest().getNumber()), repoName);

package com.github.mobile.android.ui.user;

import static org.eclipse.egit.github.core.event.Event.TYPE_COMMIT_COMMENT;
import static org.eclipse.egit.github.core.event.Event.TYPE_PULL_REQUEST_REVIEW_COMMENT;
import static org.eclipse.egit.github.core.event.Event.TYPE_CREATE;
import static org.eclipse.egit.github.core.event.Event.TYPE_DELETE;
import static org.eclipse.egit.github.core.event.Event.TYPE_DOWNLOAD;
import static org.eclipse.egit.github.core.event.Event.TYPE_FOLLOW;
import static org.eclipse.egit.github.core.event.Event.TYPE_FORK;
import static org.eclipse.egit.github.core.event.Event.TYPE_FORK_APPLY;
import static org.eclipse.egit.github.core.event.Event.TYPE_GIST;
import static org.eclipse.egit.github.core.event.Event.TYPE_GOLLUM;
import static org.eclipse.egit.github.core.event.Event.TYPE_ISSUES;
import static org.eclipse.egit.github.core.event.Event.TYPE_ISSUE_COMMENT;
import static org.eclipse.egit.github.core.event.Event.TYPE_MEMBER;
import static org.eclipse.egit.github.core.event.Event.TYPE_PUBLIC;
import static org.eclipse.egit.github.core.event.Event.TYPE_PULL_REQUEST;
import static org.eclipse.egit.github.core.event.Event.TYPE_PUSH;
import static org.eclipse.egit.github.core.event.Event.TYPE_TEAM_ADD;
import static org.eclipse.egit.github.core.event.Event.TYPE_WATCH;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.github.mobile.android.R.id;
import com.github.mobile.android.util.Time;
import com.madgag.android.listviews.ViewHolder;

import java.text.MessageFormat;

import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Team;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.event.CreatePayload;
import org.eclipse.egit.github.core.event.DeletePayload;
import org.eclipse.egit.github.core.event.Event;
import org.eclipse.egit.github.core.event.EventPayload;
import org.eclipse.egit.github.core.event.FollowPayload;
import org.eclipse.egit.github.core.event.GistPayload;
import org.eclipse.egit.github.core.event.IssueCommentPayload;
import org.eclipse.egit.github.core.event.IssuesPayload;
import org.eclipse.egit.github.core.event.PullRequestPayload;
import org.eclipse.egit.github.core.event.PushPayload;
import org.eclipse.egit.github.core.event.TeamAddPayload;

/**
 * View holder for a rendered news event
 */
public class NewsEventViewHolder implements ViewHolder<Event> {

    /**
     * Can the given event be rendered by this view holder?
     *
     * @param event
     * @return true if renderable, false otherwise
     */
    public static boolean isValid(final Event event) {
        if (event == null)
            return false;

        final EventPayload payload = event.getPayload();
        if (payload == null || EventPayload.class.equals(payload.getClass()))
            return false;

        final String type = event.getType();
        if (TextUtils.isEmpty(type))
            return false;

        return TYPE_COMMIT_COMMENT.equals(type) //
 TYPE_CREATE.equals(type) //
 TYPE_DELETE.equals(type) //
 TYPE_DOWNLOAD.equals(type) //
 TYPE_FOLLOW.equals(type) //
 TYPE_FORK.equals(type) //
 TYPE_FORK_APPLY.equals(type) //
 TYPE_GIST.equals(type) //
 TYPE_GOLLUM.equals(type) //
 TYPE_ISSUE_COMMENT.equals(type) //
 TYPE_ISSUES.equals(type) //
 TYPE_MEMBER.equals(type) //
 TYPE_PUBLIC.equals(type) //
 TYPE_PULL_REQUEST.equals(type) //
 TYPE_PULL_REQUEST_REVIEW_COMMENT.equals(type) //
 TYPE_PUSH.equals(type) //
 TYPE_TEAM_ADD.equals(type) //
 TYPE_WATCH.equals(type);
    }

    private TextView eventText;

    /**
     * Create view holder
     *
     * @param view
     */
    public NewsEventViewHolder(final View view) {
        eventText = (TextView) view.findViewById(id.tv_event);
    }

    public void updateViewFor(Event event) {
        String relativeTime = Time.relativeTimeFor(event.getCreatedAt()).toString();
        String actor = "<b>" + event.getActor().getLogin() + "</b>";
        String repoName = event.getRepo().getName();
        String type = event.getType();
        String text = null;

        if (TYPE_COMMIT_COMMENT.equals(type))
            text = MessageFormat.format("{0} commented on commit on {1}", actor, repoName);
        else if (TYPE_CREATE.equals(type)) {
            CreatePayload payload = (CreatePayload) event.getPayload();
            String refType = payload.getRefType();
            String created;
            if (!"repository".equals(refType))
                created = payload.getRef() + " at " + repoName;
            else
                created = repoName.substring(repoName.indexOf('/') + 1);

            text = MessageFormat.format("{0} created {1} {2}", actor, refType, created);
        } else if (TYPE_DELETE.equals(type)) {
            DeletePayload payload = (DeletePayload) event.getPayload();
            String refType = payload.getRefType();
            text = MessageFormat.format("{0} deleted {1} {2} at {3}", actor, refType, payload.getRef(), repoName);
        } else if (TYPE_DOWNLOAD.equals(type))
            text = MessageFormat.format("{0} uploaded a file to {1}", actor, repoName);
        else if (TYPE_FOLLOW.equals(type))
            text = MessageFormat.format("{0} started following {1}", actor, ((FollowPayload) event.getPayload())
                    .getTarget().getLogin());
        else if (TYPE_FORK.equals(type))
            text = MessageFormat.format("{0} forked repository {1}", actor, repoName);
        else if (TYPE_GIST.equals(type)) {
            GistPayload payload = (GistPayload) event.getPayload();
            String action;
            if ("create".equals(payload.getAction()))
                action = "created";
            else if ("update".equals(payload.getAction()))
                action = "updated";
            else
                action = payload.getAction();
            text = MessageFormat.format("{0} {1} Gist {2}", actor, action, payload.getGist().getId());
        } else if (TYPE_GOLLUM.equals(type))
            text = MessageFormat.format("{0} updated the wiki in {1}", actor, repoName);
        else if (TYPE_ISSUE_COMMENT.equals(type)) {
            Issue issue = ((IssueCommentPayload) event.getPayload()).getIssue();
            text = MessageFormat.format("{0} commented on issue {1} on {2}", actor,
                    Integer.toString(issue.getNumber()), repoName);
        } else if (TYPE_ISSUES.equals(type)) {
            IssuesPayload payload = (IssuesPayload) event.getPayload();
            text = MessageFormat.format("{0} {1} issue {2} on {3}", actor, payload.getAction(),
                    Integer.toString(payload.getIssue().getNumber()), repoName);
        } else if (TYPE_MEMBER.equals(type))
            text = MessageFormat.format("{0} was added as a collaborator to {1}", actor, repoName);
        else if (TYPE_PUBLIC.equals(type))
            text = MessageFormat.format("{0} open sourced repository {1}", actor, repoName);
        else if (TYPE_PULL_REQUEST.equals(type)) {
            PullRequestPayload payload = (PullRequestPayload) event.getPayload();
            String action = payload.getAction();
            if ("synchronize".equals(action))
                action = "updated";
            text = MessageFormat.format("{0} {1} pull request {2} on {3}", actor, action,
                    Integer.toBinaryString(payload.getPullRequest().getNumber()), repoName);
        } else if (TYPE_PULL_REQUEST_REVIEW_COMMENT.equals(type))
            text = MessageFormat.format("{0} commented on {1}", actor, repoName);
        else if (TYPE_PUSH.equals(type)) {
            PushPayload payload = (PushPayload) event.getPayload();
            String ref = payload.getRef();
            if (ref.startsWith("refs/heads/"))
                ref = ref.substring(11);
            text = MessageFormat.format("{0} pushed to {1} at {2}", actor, ref, repoName);
        } else if (TYPE_TEAM_ADD.equals(type)) {
            TeamAddPayload payload = (TeamAddPayload) event.getPayload();
            Team team = payload.getTeam();
            String teamName;
            if (team != null)
                teamName = " " + team.getName();
            else
                teamName = "";
            String value;
            User user = payload.getUser();
            if (user != null)
                value = user.getLogin();
            else
                value = payload.getRepo().getName();
            text = MessageFormat.format("{0} added {1} to team{2}", actor, value, teamName);
        } else if (TYPE_WATCH.equals(type))
            text = MessageFormat.format("{0} started watching {1}", actor, repoName);

        eventText.setText(Html.fromHtml(text + "  " + relativeTime));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2928.java