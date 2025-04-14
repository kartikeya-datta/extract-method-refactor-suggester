error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5390.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5390.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5390.java
text:
```scala
public R@@epoIssueViewHolder(View v, int maxNumberCount) {

package com.github.mobile.android.issue;

import static android.graphics.Paint.STRIKE_THRU_TEXT_FLAG;
import static android.text.Html.fromHtml;
import static com.github.mobile.android.util.Time.relativeTimeFor;
import android.graphics.Paint;
import android.view.View;
import android.widget.TextView;

import com.github.mobile.android.R.id;
import com.madgag.android.listviews.ViewHolder;

import java.util.Arrays;

import org.eclipse.egit.github.core.Issue;

/**
 * View holder for an issue in a repository
 */
public class RepoIssueViewHolder implements ViewHolder<Issue> {

    /**
     * Find the maximum number of digits in the given issue numbers
     *
     * @param issues
     * @return max digits
     */
    public static int computeMaxDigits(Iterable<Issue> issues) {
        int max = 1;
        for (Issue issue : issues)
            max = Math.max(max, (int) Math.log10(issue.getNumber()) + 1);
        return max;
    }

    private final TextView number;

    private final TextView title;

    private final TextView creation;

    private final TextView comments;

    private final int flags;

    /**
     * Create view holder
     *
     * @param v
     * @param maxNumberCount
     */
    public RepoIssueViewHolder(View v, Integer maxNumberCount) {
        number = (TextView) v.findViewById(id.tv_issue_number);
        flags = number.getPaintFlags();
        title = (TextView) v.findViewById(id.tv_issue_title);
        creation = (TextView) v.findViewById(id.tv_issue_creation);
        comments = (TextView) v.findViewById(id.tv_issue_comments);

        // Set number field to max number size
        Paint paint = new Paint();
        paint.setTypeface(number.getTypeface());
        paint.setTextSize(number.getTextSize());
        char[] text = new char[maxNumberCount + 1];
        Arrays.fill(text, '0');
        text[0] = '#';
        number.getLayoutParams().width = Math.round(paint.measureText(text, 0, text.length));
    }

    @Override
    public void updateViewFor(Issue i) {
        number.setText("#" + i.getNumber());
        if (i.getClosedAt() != null)
            number.setPaintFlags(flags | STRIKE_THRU_TEXT_FLAG);
        else
            number.setPaintFlags(flags);
        title.setText(i.getTitle());
        creation.setText(fromHtml("by <b>" + i.getUser().getLogin() + "</b> " + relativeTimeFor(i.getCreatedAt())));
        comments.setText(Integer.toString(i.getComments()));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5390.java