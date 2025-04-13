error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1993.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1993.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1993.java
text:
```scala
b@@uilder.append(" at ");

package com.github.mobile.android.ui.user;

import static android.graphics.Typeface.BOLD;
import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
import static com.github.mobile.android.util.TypefaceHelper.ICON_ADD_MEMBER;
import static com.github.mobile.android.util.TypefaceHelper.ICON_COMMENT;
import static com.github.mobile.android.util.TypefaceHelper.ICON_CREATE;
import static com.github.mobile.android.util.TypefaceHelper.ICON_DELETE;
import static com.github.mobile.android.util.TypefaceHelper.ICON_FOLLOW;
import static com.github.mobile.android.util.TypefaceHelper.ICON_FORK;
import static com.github.mobile.android.util.TypefaceHelper.ICON_GIST;
import static com.github.mobile.android.util.TypefaceHelper.ICON_ISSUE_CLOSE;
import static com.github.mobile.android.util.TypefaceHelper.ICON_ISSUE_COMMENT;
import static com.github.mobile.android.util.TypefaceHelper.ICON_ISSUE_OPEN;
import static com.github.mobile.android.util.TypefaceHelper.ICON_ISSUE_REOPEN;
import static com.github.mobile.android.util.TypefaceHelper.ICON_PULL_REQUEST;
import static com.github.mobile.android.util.TypefaceHelper.ICON_PUSH;
import static com.github.mobile.android.util.TypefaceHelper.ICON_UPLOAD;
import static com.github.mobile.android.util.TypefaceHelper.ICON_WATCH;
import static com.github.mobile.android.util.TypefaceHelper.ICON_WIKI;
import static org.eclipse.egit.github.core.event.Event.TYPE_COMMIT_COMMENT;
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
import static org.eclipse.egit.github.core.event.Event.TYPE_PULL_REQUEST_REVIEW_COMMENT;
import static org.eclipse.egit.github.core.event.Event.TYPE_PUSH;
import static org.eclipse.egit.github.core.event.Event.TYPE_TEAM_ADD;
import static org.eclipse.egit.github.core.event.Event.TYPE_WATCH;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mobile.android.R.id;
import com.github.mobile.android.util.AvatarHelper;
import com.github.mobile.android.util.Time;
import com.github.mobile.android.util.TypefaceHelper;
import com.madgag.android.listviews.ViewHolder;

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

    private final AvatarHelper avatarHelper;

    private final ImageView avatarView;

    private final TextView eventText;

    private final TextView iconText;

    private final TextView dateText;

    /**
     * Create view holder
     *
     * @param view
     * @param avatarHelper
     */
    public NewsEventViewHolder(final View view, final AvatarHelper avatarHelper) {
        this.avatarHelper = avatarHelper;
        avatarView = (ImageView) view.findViewById(id.iv_gravatar);
        eventText = (TextView) view.findViewById(id.tv_event);
        iconText = (TextView) view.findViewById(id.tv_event_icon);
        TypefaceHelper.setOctocons(iconText);
        dateText = (TextView) view.findViewById(id.tv_event_date);
    }

    private CharSequence formatCommitComment(Event event) {
        SpannableStringBuilder builder = new SpannableStringBuilder();

        builder.append(event.getActor().getLogin());
        builder.setSpan(new StyleSpan(BOLD), 0, builder.length(), SPAN_EXCLUSIVE_EXCLUSIVE);

        builder.append(" commented on commit on ");

        String repoName = event.getRepo().getName();
        builder.append(repoName);
        builder.setSpan(new StyleSpan(BOLD), builder.length() - repoName.length(), builder.length(),
                SPAN_EXCLUSIVE_EXCLUSIVE);

        return builder;
    }

    private CharSequence formatDownload(Event event) {
        SpannableStringBuilder builder = new SpannableStringBuilder();

        builder.append(event.getActor().getLogin());
        builder.setSpan(new StyleSpan(BOLD), 0, builder.length(), SPAN_EXCLUSIVE_EXCLUSIVE);

        builder.append(" uploaded a file to ");

        String repoName = event.getRepo().getName();
        builder.append(repoName);
        builder.setSpan(new StyleSpan(BOLD), builder.length() - repoName.length(), builder.length(),
                SPAN_EXCLUSIVE_EXCLUSIVE);

        return builder;
    }

    private CharSequence formatCreate(Event event) {
        SpannableStringBuilder builder = new SpannableStringBuilder();

        builder.append(event.getActor().getLogin());
        builder.setSpan(new StyleSpan(BOLD), 0, builder.length(), SPAN_EXCLUSIVE_EXCLUSIVE);

        builder.append(" created ");
        CreatePayload payload = (CreatePayload) event.getPayload();
        String refType = payload.getRefType();
        builder.append(refType);
        builder.append(' ');
        String repoName = event.getRepo().getName();
        if (!"repository".equals(refType)) {
            builder.append(payload.getRef());
            builder.append(" at ");
        } else
            repoName = repoName.substring(repoName.indexOf('/') + 1);

        builder.append(repoName);
        builder.setSpan(new StyleSpan(BOLD), builder.length() - repoName.length(), builder.length(),
                SPAN_EXCLUSIVE_EXCLUSIVE);

        return builder;
    }

    private CharSequence formatDelete(Event event) {
        SpannableStringBuilder builder = new SpannableStringBuilder();

        builder.append(event.getActor().getLogin());
        builder.setSpan(new StyleSpan(BOLD), 0, builder.length(), SPAN_EXCLUSIVE_EXCLUSIVE);

        DeletePayload payload = (DeletePayload) event.getPayload();
        builder.append(" deleted ");
        builder.append(payload.getRefType());
        builder.append(' ');
        builder.append(payload.getRef());
        builder.append(' ');

        String repoName = event.getRepo().getName();
        builder.append(repoName);
        builder.setSpan(new StyleSpan(BOLD), builder.length() - repoName.length(), builder.length(),
                SPAN_EXCLUSIVE_EXCLUSIVE);

        return builder;
    }

    private CharSequence formatFollow(Event event) {
        SpannableStringBuilder builder = new SpannableStringBuilder();

        builder.append(event.getActor().getLogin());
        builder.setSpan(new StyleSpan(BOLD), 0, builder.length(), SPAN_EXCLUSIVE_EXCLUSIVE);

        builder.append(" started following ");
        builder.append(((FollowPayload) event.getPayload()).getTarget().getLogin());

        return builder;
    }

    private CharSequence formatFork(Event event) {
        SpannableStringBuilder builder = new SpannableStringBuilder();

        builder.append(event.getActor().getLogin());
        builder.setSpan(new StyleSpan(BOLD), 0, builder.length(), SPAN_EXCLUSIVE_EXCLUSIVE);

        builder.append(" forked repository ");

        String repoName = event.getRepo().getName();
        builder.append(repoName);
        builder.setSpan(new StyleSpan(BOLD), builder.length() - repoName.length(), builder.length(),
                SPAN_EXCLUSIVE_EXCLUSIVE);

        return builder;
    }

    private CharSequence formatGist(Event event) {
        SpannableStringBuilder builder = new SpannableStringBuilder();

        builder.append(event.getActor().getLogin());
        builder.setSpan(new StyleSpan(BOLD), 0, builder.length(), SPAN_EXCLUSIVE_EXCLUSIVE);

        GistPayload payload = (GistPayload) event.getPayload();
        String action = payload.getAction();
        if ("create".equals(action))
            builder.append("created");
        else if ("update".equals(action))
            builder.append("updated");
        else
            builder.append(action);
        builder.append(" Gist ");
        builder.append(payload.getGist().getId());

        return builder;
    }

    private CharSequence formatWiki(Event event) {
        SpannableStringBuilder builder = new SpannableStringBuilder();

        builder.append(event.getActor().getLogin());
        builder.setSpan(new StyleSpan(BOLD), 0, builder.length(), SPAN_EXCLUSIVE_EXCLUSIVE);

        builder.append(" updated the wiki in ");

        String repoName = event.getRepo().getName();
        builder.append(repoName);
        builder.setSpan(new StyleSpan(BOLD), builder.length() - repoName.length(), builder.length(),
                SPAN_EXCLUSIVE_EXCLUSIVE);

        return builder;
    }

    private CharSequence formatIssueComment(Event event) {
        SpannableStringBuilder builder = new SpannableStringBuilder();

        builder.append(event.getActor().getLogin());
        builder.setSpan(new StyleSpan(BOLD), 0, builder.length(), SPAN_EXCLUSIVE_EXCLUSIVE);

        builder.append(" commented on ");
        Issue issue = ((IssueCommentPayload) event.getPayload()).getIssue();
        String issueNumber = "issue " + issue.getNumber();
        builder.append(issueNumber);
        builder.setSpan(new StyleSpan(BOLD), builder.length() - issueNumber.length(), builder.length(),
                SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(" on ");

        String repoName = event.getRepo().getName();
        builder.append(repoName);
        builder.setSpan(new StyleSpan(BOLD), builder.length() - repoName.length(), builder.length(),
                SPAN_EXCLUSIVE_EXCLUSIVE);

        return builder;
    }

    private CharSequence formatIssues(Event event) {
        SpannableStringBuilder builder = new SpannableStringBuilder();

        builder.append(event.getActor().getLogin());
        builder.setSpan(new StyleSpan(BOLD), 0, builder.length(), SPAN_EXCLUSIVE_EXCLUSIVE);

        IssuesPayload payload = (IssuesPayload) event.getPayload();
        String action = payload.getAction();
        builder.append(' ');
        builder.append(action);
        builder.append(' ');
        String issueNumber = "issue " + payload.getIssue().getNumber();
        builder.append(issueNumber);
        builder.setSpan(new StyleSpan(BOLD), builder.length() - issueNumber.length(), builder.length(),
                SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(" on ");

        String repoName = event.getRepo().getName();
        builder.append(repoName);
        builder.setSpan(new StyleSpan(BOLD), builder.length() - repoName.length(), builder.length(),
                SPAN_EXCLUSIVE_EXCLUSIVE);

        return builder;
    }

    private CharSequence formatAddMember(Event event) {
        SpannableStringBuilder builder = new SpannableStringBuilder();

        builder.append(event.getActor().getLogin());
        builder.setSpan(new StyleSpan(BOLD), 0, builder.length(), SPAN_EXCLUSIVE_EXCLUSIVE);

        builder.append(" was added as a collaborator to ");

        String repoName = event.getRepo().getName();
        builder.append(repoName);
        builder.setSpan(new StyleSpan(BOLD), builder.length() - repoName.length(), builder.length(),
                SPAN_EXCLUSIVE_EXCLUSIVE);

        return builder;
    }

    private CharSequence formatPublic(Event event) {
        SpannableStringBuilder builder = new SpannableStringBuilder();

        builder.append(event.getActor().getLogin());
        builder.setSpan(new StyleSpan(BOLD), 0, builder.length(), SPAN_EXCLUSIVE_EXCLUSIVE);

        builder.append(" open sourced repository ");

        String repoName = event.getRepo().getName();
        builder.append(repoName);
        builder.setSpan(new StyleSpan(BOLD), builder.length() - repoName.length(), builder.length(),
                SPAN_EXCLUSIVE_EXCLUSIVE);

        return builder;
    }

    private CharSequence formatWatch(Event event) {
        SpannableStringBuilder builder = new SpannableStringBuilder();

        builder.append(event.getActor().getLogin());
        builder.setSpan(new StyleSpan(BOLD), 0, builder.length(), SPAN_EXCLUSIVE_EXCLUSIVE);

        builder.append(" started watching ");

        String repoName = event.getRepo().getName();
        builder.append(repoName);
        builder.setSpan(new StyleSpan(BOLD), builder.length() - repoName.length(), builder.length(),
                SPAN_EXCLUSIVE_EXCLUSIVE);

        return builder;
    }

    private CharSequence formatReviewComment(Event event) {
        SpannableStringBuilder builder = new SpannableStringBuilder();

        builder.append(event.getActor().getLogin());
        builder.setSpan(new StyleSpan(BOLD), 0, builder.length(), SPAN_EXCLUSIVE_EXCLUSIVE);

        builder.append(" commented on ");

        String repoName = event.getRepo().getName();
        builder.append(repoName);
        builder.setSpan(new StyleSpan(BOLD), builder.length() - repoName.length(), builder.length(),
                SPAN_EXCLUSIVE_EXCLUSIVE);

        return builder;
    }

    private CharSequence formatPullRequest(Event event) {
        SpannableStringBuilder builder = new SpannableStringBuilder();

        String actor = event.getActor().getLogin();
        builder.append(actor);
        builder.setSpan(new StyleSpan(BOLD), 0, actor.length(), SPAN_EXCLUSIVE_EXCLUSIVE);

        PullRequestPayload payload = (PullRequestPayload) event.getPayload();
        String action = payload.getAction();
        if ("synchronize".equals(action))
            action = "updated";
        builder.append(' ');
        builder.append(action);
        builder.append(' ');
        String issueNumber = "pull request " + payload.getPullRequest().getNumber();
        builder.append(issueNumber);
        builder.setSpan(new StyleSpan(BOLD), builder.length() - issueNumber.length(), builder.length(),
                SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(" on ");

        String repoName = event.getRepo().getName();
        builder.append(repoName);
        builder.setSpan(new StyleSpan(BOLD), builder.length() - repoName.length(), builder.length(),
                SPAN_EXCLUSIVE_EXCLUSIVE);

        return builder;
    }

    private CharSequence formatPush(Event event) {
        SpannableStringBuilder builder = new SpannableStringBuilder();

        String actor = event.getActor().getLogin();
        builder.append(actor);
        builder.setSpan(new StyleSpan(BOLD), 0, actor.length(), SPAN_EXCLUSIVE_EXCLUSIVE);

        builder.append(" pushed to ");
        PushPayload payload = (PushPayload) event.getPayload();
        String ref = payload.getRef();
        if (ref.startsWith("refs/heads/"))
            ref = ref.substring(11);
        builder.append(ref);
        builder.setSpan(new StyleSpan(BOLD), builder.length() - ref.length(), builder.length(),
                SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(" at ");

        String repoName = event.getRepo().getName();
        builder.append(repoName);
        builder.setSpan(new StyleSpan(BOLD), builder.length() - repoName.length(), builder.length(),
                SPAN_EXCLUSIVE_EXCLUSIVE);

        return builder;
    }

    private CharSequence formatTeamAdd(Event event) {
        SpannableStringBuilder builder = new SpannableStringBuilder();

        builder.append(event.getActor().getLogin());
        builder.setSpan(new StyleSpan(BOLD), 0, builder.length(), SPAN_EXCLUSIVE_EXCLUSIVE);

        TeamAddPayload payload = (TeamAddPayload) event.getPayload();
        Team team = payload.getTeam();
        String value;
        User user = payload.getUser();
        if (user != null)
            value = user.getLogin();
        else
            value = payload.getRepo().getName();
        builder.append(" added ");
        builder.append(value);
        builder.append(" to team");
        String teamName = team != null ? team.getName() : null;
        if (teamName != null)
            builder.append(' ').append(teamName);

        return builder;
    }

    public void updateViewFor(Event event) {
        avatarHelper.bind(avatarView, event.getActor());

        CharSequence text = null;
        char icon = ' ';

        String type = event.getType();
        if (TYPE_COMMIT_COMMENT.equals(type)) {
            icon = ICON_COMMENT;
            text = formatCommitComment(event);
        } else if (TYPE_CREATE.equals(type)) {
            icon = ICON_CREATE;
            text = formatCreate(event);
        } else if (TYPE_DELETE.equals(type)) {
            icon = ICON_DELETE;
            text = formatDelete(event);
        } else if (TYPE_DOWNLOAD.equals(type)) {
            icon = ICON_UPLOAD;
            text = formatDownload(event);
        } else if (TYPE_FOLLOW.equals(type)) {
            icon = ICON_FOLLOW;
            text = formatFollow(event);
        } else if (TYPE_FORK.equals(type)) {
            icon = ICON_FORK;
            text = formatFork(event);
        } else if (TYPE_GIST.equals(type)) {
            icon = ICON_GIST;
            text = formatGist(event);
        } else if (TYPE_GOLLUM.equals(type)) {
            icon = ICON_WIKI;
            text = formatWiki(event);
        } else if (TYPE_ISSUE_COMMENT.equals(type)) {
            icon = ICON_ISSUE_COMMENT;
            text = formatIssueComment(event);
        } else if (TYPE_ISSUES.equals(type)) {
            String action = ((IssuesPayload) event.getPayload()).getAction();
            if ("opened".equals(action))
                icon = ICON_ISSUE_OPEN;
            else if ("reopened".equals(action))
                icon = ICON_ISSUE_REOPEN;
            else if ("closed".equals(action))
                icon = ICON_ISSUE_CLOSE;
            text = formatIssues(event);
        } else if (TYPE_MEMBER.equals(type)) {
            icon = ICON_ADD_MEMBER;
            text = formatAddMember(event);
        } else if (TYPE_PUBLIC.equals(type))
            text = formatPublic(event);
        else if (TYPE_PULL_REQUEST.equals(type)) {
            icon = ICON_PULL_REQUEST;
            text = formatPullRequest(event);
        } else if (TYPE_PULL_REQUEST_REVIEW_COMMENT.equals(type)) {
            icon = ICON_COMMENT;
            text = formatReviewComment(event);
        } else if (TYPE_PUSH.equals(type)) {
            icon = ICON_PUSH;
            text = formatPush(event);
        } else if (TYPE_TEAM_ADD.equals(type)) {
            icon = ICON_ADD_MEMBER;
            text = formatTeamAdd(event);
        } else if (TYPE_WATCH.equals(type)) {
            icon = ICON_WATCH;
            text = formatWatch(event);
        }

        iconText.setText(icon != ' ' ? Character.toString(icon) : null);
        eventText.setText(text);
        dateText.setText(Time.relativeTimeFor(event.getCreatedAt()).toString());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1993.java