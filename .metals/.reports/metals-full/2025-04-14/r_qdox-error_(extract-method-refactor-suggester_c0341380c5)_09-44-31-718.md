error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1709.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1709.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1709.java
text:
```scala
L@@istView view = (ListView) inflater.inflate(layout.dialog_list_view, null);

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
import static android.content.DialogInterface.BUTTON_NEUTRAL;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.github.mobile.R.string;
import com.github.mobile.ui.DialogFragmentActivity;
import com.github.mobile.ui.ItemListAdapter;
import com.github.mobile.ui.ItemView;
import com.github.mobile.ui.SingleChoiceDialogFragment;
import com.viewpagerindicator.R.id;
import com.viewpagerindicator.R.layout;

import java.util.ArrayList;

import org.eclipse.egit.github.core.Milestone;

/**
 * Dialog fragment to select an issue milestone
 */
public class MilestoneDialogFragment extends SingleChoiceDialogFragment {

    private static class MilestoneItemView extends ItemView {

        public final RadioButton selected;

        public final TextView title;

        public final TextView description;

        public MilestoneItemView(final View view) {
            super(view);

            selected = (RadioButton) view.findViewById(id.rb_selected);
            title = (TextView) view.findViewById(id.tv_milestone_title);
            description = (TextView) view.findViewById(id.tv_milestone_description);
        }
    }

    private static class MilestoneListAdapter extends ItemListAdapter<Milestone, MilestoneItemView> {

        private final int selected;

        public MilestoneListAdapter(LayoutInflater inflater, Milestone[] milestones, int selected) {
            super(layout.milestone_item, inflater, milestones);

            this.selected = selected;
        }

        @Override
        protected void update(final int position, final MilestoneItemView view, final Milestone item) {
            view.title.setText(item.getTitle());
            String description = item.getDescription();
            if (!TextUtils.isEmpty(description)) {
                view.description.setText(description);
                view.description.setVisibility(VISIBLE);
            } else
                view.description.setVisibility(GONE);
            view.selected.setChecked(selected == position);
        }

        @Override
        protected MilestoneItemView createView(View view) {
            return new MilestoneItemView(view);
        }
    }

    /**
     * Get selected milestone from results bundle
     *
     * @param arguments
     * @return milestone
     */
    public static Milestone getSelected(Bundle arguments) {
        return (Milestone) arguments.getSerializable(ARG_SELECTED);
    }

    /**
     * Confirm message and deliver callback to given activity
     *
     * @param activity
     * @param requestCode
     * @param title
     * @param message
     * @param choices
     * @param selectedChoice
     */
    public static void show(final DialogFragmentActivity activity, final int requestCode, final String title,
            final String message, ArrayList<Milestone> choices, final int selectedChoice) {
        show(activity, requestCode, title, message, choices, selectedChoice, new MilestoneDialogFragment());
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        Activity activity = getActivity();
        Bundle arguments = getArguments();

        final AlertDialog dialog = createDialog();
        dialog.setButton(BUTTON_NEUTRAL, activity.getString(string.clear), this);

        LayoutInflater inflater = activity.getLayoutInflater();

        ListView view = (ListView) inflater.inflate(layout.list_view, null);
        view.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onClick(dialog, position);
            }
        });

        ArrayList<Milestone> choices = getChoices();
        int selected = arguments.getInt(ARG_SELECTED_CHOICE);
        MilestoneListAdapter adapter = new MilestoneListAdapter(inflater,
                choices.toArray(new Milestone[choices.size()]), selected);
        view.setAdapter(adapter);
        if (selected >= 0)
            view.setSelection(selected);
        dialog.setView(view);

        return dialog;
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Milestone> getChoices() {
        return (ArrayList<Milestone>) getArguments().getSerializable(ARG_CHOICES);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        super.onClick(dialog, which);

        switch (which) {
        case BUTTON_NEUTRAL:
            onResult(RESULT_OK);
            break;
        default:
            getArguments().putSerializable(ARG_SELECTED, getChoices().get(which));
            onResult(RESULT_OK);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1709.java