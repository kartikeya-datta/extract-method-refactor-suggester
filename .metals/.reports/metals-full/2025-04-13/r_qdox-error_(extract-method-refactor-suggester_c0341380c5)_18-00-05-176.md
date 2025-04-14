error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2809.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2809.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2809.java
text:
```scala
s@@uper(window, forward);

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchWindow;

/**
 * This is the implementation for both NextEditorAction and PrevEditorAction.
 */
public class CycleEditorAction extends CyclePartAction {

    /**
     * Creates a CycleEditorAction.
     * @param window the window
     * @param forward whether the editors will be cycled forward
     */
    public CycleEditorAction(IWorkbenchWindow window, boolean forward) {
        super(window, forward); //$NON-NLS-1$
        updateState();
    }

    protected void setText() {
        // TBD: Remove text and tooltip when this becomes an invisible action.
        if (forward) {
            setText(WorkbenchMessages.CycleEditorAction_next_text); 
            setToolTipText(WorkbenchMessages.CycleEditorAction_next_toolTip); 
            // @issue missing action ids
            getWorkbenchWindow().getWorkbench().getHelpSystem().setHelp(this,
					IWorkbenchHelpContextIds.CYCLE_EDITOR_FORWARD_ACTION);
            setActionDefinitionId("org.eclipse.ui.window.nextEditor"); //$NON-NLS-1$
        } else {
            setText(WorkbenchMessages.CycleEditorAction_prev_text);
            setToolTipText(WorkbenchMessages.CycleEditorAction_prev_toolTip); 
            // @issue missing action ids
            getWorkbenchWindow().getWorkbench().getHelpSystem().setHelp(this,
					IWorkbenchHelpContextIds.CYCLE_EDITOR_BACKWARD_ACTION);
            setActionDefinitionId("org.eclipse.ui.window.previousEditor"); //$NON-NLS-1$
        }
    }

    /**
     * Updates the enabled state.
     */
    public void updateState() {
        WorkbenchPage page = (WorkbenchPage) getActivePage();
        if (page == null) {
            setEnabled(false);
            return;
        }
        // enable iff there is at least one other editor to switch to
        setEnabled(page.getSortedEditors().length >= 1);
    }

    /**
     * Add all views to the dialog in the activation order
     */
    protected void addItems(Table table, WorkbenchPage page) {
        IEditorReference refs[] = page.getSortedEditors();
        for (int i = refs.length - 1; i >= 0; i--) {
            TableItem item = null;
            item = new TableItem(table, SWT.NONE);
            if (refs[i].isDirty())
                item.setText("*" + refs[i].getTitle()); //$NON-NLS-1$
            else
                item.setText(refs[i].getTitle());
            item.setImage(refs[i].getTitleImage());
            item.setData(refs[i]);
        }
    }

    /**
     * Returns the string which will be shown in the table header.
     */
    protected String getTableHeader() {
        return WorkbenchMessages.CycleEditorAction_header;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2809.java