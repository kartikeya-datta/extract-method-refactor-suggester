error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1075.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1075.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[103,2]

error in qdox parser
file content:
```java
offset: 3577
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1075.java
text:
```scala
package org.eclipse.wst.xquery.debug.ui.interpreters;

/*******************************************************************************
 * Copyright (c) 2008, 2009 28msec Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Gabriel Petrovay (28msec) - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xquery.internal.debug.ui.interpreters;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.internal.debug.ui.interpreters.IAddInterpreterDialogRequestor;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.ui.util.IStatusChangeListener;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public abstract class AbstractAddInterpreterDialogBlock implements IInterpreterNameProvider {

    protected IAddInterpreterDialogRequestor fRequestor;

    protected IInterpreterInstall fEditedInterpreter;

    protected GenericAddInterpreterDialog fAddInterpreterDialog;

    private Composite fContainer;

    public final void createControls(Composite comp) {
        fContainer = new Composite(comp, SWT.NONE);
        GridLayout layout = new GridLayout(3, false);
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        layout.verticalSpacing = fAddInterpreterDialog.convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
        layout.horizontalSpacing = fAddInterpreterDialog
                .convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
        fContainer.setLayout(layout);
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 3;
        fContainer.setLayoutData(gd);

        addControlsTo(fContainer);
    }

    public void dispose() {
        if (fContainer != null) {
            fContainer.dispose();
        }
    }

    abstract protected void addControlsTo(Composite comp);

    public void initialize(GenericAddInterpreterDialog dialog, IAddInterpreterDialogRequestor requestor,
            IInterpreterInstall editedInterpreter) {
        fAddInterpreterDialog = dialog;
        fRequestor = requestor;
        fEditedInterpreter = editedInterpreter;
    }

    protected IEnvironment getEnvironemnt() {
        return fAddInterpreterDialog.getEnvironment();
    }

    protected Shell getShell() {
        return fAddInterpreterDialog.getShell();
    }

    protected IStatusChangeListener getStatusListener() {
        return fAddInterpreterDialog;
    }

    abstract public void initializeFields(IInterpreterInstall install);

    abstract protected IStatus validateInterpreterLocation();

    abstract protected IStatus validateInterpreterName();

    abstract public void setFieldValuesToInterpreter(IInterpreterInstall install);

    abstract protected String getInterpreterName();

    abstract protected String getInterpreterLocation();

    public void setFocus() {
    }

    public void createFieldListeners() {
    }

    protected int convertWidthInCharsToPixels(int chars) {
        return fAddInterpreterDialog.convertWidthInCharsToPixels(chars);
    }

}
 N@@o newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1075.java