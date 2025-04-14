error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7744.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7744.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7744.java
text:
```scala
i@@nt digits = Math.max(TypefaceUtils.getMaxDigits(numbers), 4);

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

import static android.graphics.Paint.STRIKE_THRU_TEXT_FLAG;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.github.mobile.R.id;
import com.github.mobile.R.layout;
import com.github.mobile.ui.ItemListAdapter;
import com.github.mobile.util.AvatarLoader;
import com.github.mobile.util.TimeUtils;
import com.github.mobile.util.TypefaceUtils;

import java.util.List;

import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Label;
import org.eclipse.egit.github.core.RepositoryIssue;

/**
 * Adapter to display a list of dashboard issues
 */
public class DashboardIssueListAdapter extends ItemListAdapter<RepositoryIssue, DashboardIssueView> {

    private final AvatarLoader avatarHelper;

    private int numberWidth;

    private final TextView numberView;

    /**
     * Create adapter
     *
     * @param avatarHelper
     * @param inflater
     */
    public DashboardIssueListAdapter(AvatarLoader avatarHelper, LayoutInflater inflater) {
        this(avatarHelper, inflater, null);
    }

    /**
     * Create adapter
     *
     * @param avatarHelper
     * @param inflater
     * @param elements
     */
    public DashboardIssueListAdapter(AvatarLoader avatarHelper, LayoutInflater inflater, RepositoryIssue[] elements) {
        super(layout.dashboard_issue_item, inflater);

        this.numberView = (TextView) inflater.inflate(layout.dashboard_issue_item, null).findViewById(
                id.tv_issue_number);
        this.avatarHelper = avatarHelper;

        if (elements != null)
            computeNumberWidth(elements);
    }

    private void computeNumberWidth(final Object[] items) {
        int[] numbers = new int[items.length];
        for (int i = 0; i < numbers.length; i++)
            numbers[i] = ((Issue) items[i]).getNumber();
        int digits = TypefaceUtils.getMaxDigits(numbers);
        numberWidth = TypefaceUtils.getWidth(numberView, digits) + numberView.getPaddingLeft()
                + numberView.getPaddingRight();
    }

    @Override
    public ItemListAdapter<RepositoryIssue, DashboardIssueView> setItems(final Object[] items) {
        computeNumberWidth(items);

        return super.setItems(items);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    protected void update(final DashboardIssueView view, final RepositoryIssue issue) {
        view.number.setText(Integer.toString(issue.getNumber()));
        if (issue.getClosedAt() != null)
            view.number.setPaintFlags(view.numberPaintFlags | STRIKE_THRU_TEXT_FLAG);
        else
            view.number.setPaintFlags(view.numberPaintFlags);
        view.number.getLayoutParams().width = numberWidth;

        avatarHelper.bind(view.avatar, issue.getUser());

        String[] segments = issue.getUrl().split("/");
        int length = segments.length;
        if (length >= 4)
            view.repoText.setText(segments[length - 4] + "/" + segments[length - 3]);
        else
            view.repoText.setText("");

        view.pullRequestIcon.setVisibility(issue.getPullRequest().getHtmlUrl() == null ? GONE : VISIBLE);

        view.title.setText(issue.getTitle());
        view.user.setText(issue.getUser().getLogin());
        view.creation.setText(TimeUtils.getRelativeTime(issue.getCreatedAt()));
        view.comments.setText(Integer.toString(issue.getComments()));

        List<Label> labels = issue.getLabels();
        if (labels != null && !labels.isEmpty()) {
            int size = Math.min(labels.size(), view.labels.length);
            for (int i = 0; i < size; i++) {
                String color = labels.get(i).getColor();
                if (!TextUtils.isEmpty(color)) {
                    view.labels[i].setBackgroundColor(Color.parseColor('#' + color));
                    view.labels[i].setVisibility(VISIBLE);
                } else
                    view.labels[i].setVisibility(GONE);
            }
            for (int i = size; i < view.labels.length; i++)
                view.labels[i].setVisibility(GONE);
        } else
            for (View label : view.labels)
                label.setVisibility(GONE);
    }

    @Override
    protected DashboardIssueView createView(final View view) {
        return new DashboardIssueView(view);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7744.java