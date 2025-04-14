error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7496.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7496.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7496.java
text:
```scala
r@@eturn part.isCloseable();

/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.presentations.IPresentablePart;
import org.eclipse.ui.presentations.IStackPresentationSite;
import org.eclipse.ui.presentations.StackPresentation;

/**
 * @since 3.0
 */
public abstract class DefaultStackPresentationSite implements
        IStackPresentationSite {

    private StackPresentation presentation;

    private int state = IStackPresentationSite.STATE_RESTORED;

    private int activeState = StackPresentation.AS_INACTIVE;

    public DefaultStackPresentationSite() {

    }

    public void setPresentation(StackPresentation newPresentation) {
        presentation = newPresentation;
        if (presentation != null) {
            presentation.setState(state);
            presentation.setActive(activeState);
        }
    }

    public StackPresentation getPresentation() {
        return presentation;
    }

    public int getState() {
        return state;
    }

    public void setActive(int activeState) {
        if (activeState != this.activeState) {
            this.activeState = activeState;
            if (presentation != null) {
                presentation.setActive(activeState);
            }
        }
    }

    public int getActive() {
        return activeState;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.skins.IStackPresentationSite#selectPart(org.eclipse.ui.internal.skins.IPresentablePart)
     */
    public void selectPart(IPresentablePart toSelect) {

        if (presentation != null) {
            presentation.selectPart(toSelect);
        }
    }

    public void dispose() {
        if (presentation != null) {
            presentation.dispose();
        }
        setPresentation(null);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.skins.IPresentationSite#setState(int)
     */
    public void setState(int newState) {
        setPresentationState(newState);
    }

    public void setPresentationState(int newState) {
        state = newState;
        if (presentation != null) {
            presentation.setState(newState);
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.skins.IPresentablePart#isClosable()
     */
    public boolean isCloseable(IPresentablePart part) {
        return true;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.skins.IPresentationSite#dragStart(org.eclipse.ui.internal.skins.IPresentablePart, boolean)
     */
    public void dragStart(IPresentablePart beingDragged, Point initialPosition,
            boolean keyboard) {
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.skins.IPresentationSite#close(org.eclipse.ui.internal.skins.IPresentablePart)
     */
    public void close(IPresentablePart toClose) {
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.skins.IPresentationSite#dragStart(boolean)
     */
    public void dragStart(Point initialPosition, boolean keyboard) {
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.presentations.IStackPresentationSite#supportsState(int)
     */
    public boolean supportsState(int state) {
        return true;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.presentations.IStackPresentationSite#getSelectedPart()
     */
    public abstract IPresentablePart getSelectedPart();

    public void addSystemActions(IMenuManager menuManager) {

    }

    public abstract boolean isPartMoveable(IPresentablePart toMove);

    public abstract boolean isStackMoveable();

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7496.java