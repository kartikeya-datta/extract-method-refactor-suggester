error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9500.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9500.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9500.java
text:
```scala
L@@abelsDrawable drawable = new LabelsDrawable(createdText.getTextSize(), ServiceHelper.getDisplayWidth(labelsArea),

package com.github.mobile.android.issue;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import android.content.res.Resources;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.github.mobile.android.R.id;
import com.github.mobile.android.R.string;
import com.github.mobile.android.util.AvatarHelper;
import com.github.mobile.android.util.HtmlViewer;
import com.github.mobile.android.util.ServiceHelper;
import com.github.mobile.android.util.Time;
import com.madgag.android.listviews.ViewHolder;

import java.util.Locale;

import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.User;

/**
 * Holder for a issue minus the comments
 */
public class IssueHeaderViewHolder implements ViewHolder<Issue> {

    private final AvatarHelper avatarHelper;

    private final Resources resources;

    private final TextView titleText;

    private final HtmlViewer bodyViewer;

    private final TextView createdText;

    private final ImageView creatorAvatar;

    private final TextView assigneeText;

    private final ImageView assigneeAvatar;

    private final LinearLayout labelsArea;

    private final TextView milestoneText;

    private final TextView stateText;

    /**
     * @return bodyViewer
     */
    public HtmlViewer getBodyViewer() {
        return bodyViewer;
    }

    /**
     * Create issue header view holder
     *
     * @param view
     * @param avatarHelper
     * @param resources
     */
    public IssueHeaderViewHolder(final View view, final AvatarHelper avatarHelper, final Resources resources) {
        this.avatarHelper = avatarHelper;
        this.resources = resources;
        titleText = (TextView) view.findViewById(id.tv_issue_title);
        createdText = (TextView) view.findViewById(id.tv_issue_creation);
        creatorAvatar = (ImageView) view.findViewById(id.iv_gravatar);
        assigneeText = (TextView) view.findViewById(id.tv_assignee_name);
        assigneeAvatar = (ImageView) view.findViewById(id.iv_assignee_gravatar);
        labelsArea = (LinearLayout) view.findViewById(id.ll_labels);
        milestoneText = (TextView) view.findViewById(id.tv_milestone);
        stateText = (TextView) view.findViewById(id.tv_state);
        bodyViewer = new HtmlViewer((WebView) view.findViewById(id.wv_issue_body));
    }

    public void updateViewFor(Issue issue) {
        titleText.setText(issue.getTitle());
        String body = issue.getBodyHtml();
        if (body != null && body.length() > 0)
            bodyViewer.setHtml(body).getView().setVisibility(VISIBLE);
        else
            bodyViewer.getView().setVisibility(GONE);

        String reported = "<b>" + issue.getUser().getLogin() + "</b> opened "
                + Time.relativeTimeFor(issue.getCreatedAt());

        createdText.setText(Html.fromHtml(reported));
        avatarHelper.bind(creatorAvatar, issue.getUser());

        User assignee = issue.getAssignee();
        if (assignee != null) {
            assigneeText.setText(assignee.getLogin());
            assigneeAvatar.setVisibility(VISIBLE);
            avatarHelper.bind(assigneeAvatar, assignee);
        } else {
            assigneeAvatar.setVisibility(GONE);
            assigneeText.setText(assigneeText.getContext().getString(string.unassigned));
        }

        if (!issue.getLabels().isEmpty()) {
            labelsArea.setVisibility(VISIBLE);
            LabelsDrawable drawable = new LabelsDrawable(createdText.getTextSize(), ServiceHelper.getWidth(labelsArea),
                    issue.getLabels());
            drawable.getPaint().setColor(resources.getColor(android.R.color.transparent));
            labelsArea.setBackgroundDrawable(drawable);
            LayoutParams params = new LayoutParams(drawable.getBounds().width(), drawable.getBounds().height());
            labelsArea.setLayoutParams(params);
        } else
            labelsArea.setVisibility(GONE);

        if (issue.getMilestone() != null)
            milestoneText.setText(issue.getMilestone().getTitle());
        else
            milestoneText.setText(milestoneText.getContext().getString(string.no_milestone));

        String state = issue.getState();
        if (state != null && state.length() > 0)
            state = state.substring(0, 1).toUpperCase(Locale.US) + state.substring(1);
        else
            state = "";
        stateText.setText(state);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9500.java