error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8733.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8733.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8733.java
text:
```scala
s@@tringBuffer.append(",activeShell="); //$NON-NLS-1$

/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/
package org.eclipse.ui.commands;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.internal.util.Util;

/**
 * </p>
 * An instance of this class represents a request to handle a command. A handler
 * submission specifies a list of conditions under which it would be appropriate
 * for a particular command to have a particular handler. These conditions
 * include things like the active part or the active shell. So, it is possible
 * to say things like: "when my part is active, please consider calling these
 * classes when you want to perform a cut, copy or paste".
 * </p>
 * <p>
 * The workbench considers all of the submissions it has received and choses the
 * ones it views as the best possible match.
 * </p>
 * <p>
 * This class is not intended to be extended by clients.
 * </p>
 * 
 * @since 3.0
 * @see org.eclipse.ui.commands.IWorkbenchCommandSupport
 */
public final class HandlerSubmission implements Comparable {

    /**
     * A factor used in the hash function.
     */
    private final static int HASH_FACTOR = 89;

    /**
     * The seed for the hash function -- computed from this class' name's hash
     * code.
     */
    private final static int HASH_INITIAL = HandlerSubmission.class.getName()
            .hashCode();

    /**
     * The part identifier for the part that should be active before this
     * submission can be considered.  This value can be <code>null</code>, which
     * indicates that it should match any part.
     */
    private final String activePartId;

    /**
     * The shell that must be active before this submission can be considered.
     * This value can be <code>null</code>, which indicates that it should match
     * any shell.
     */
    private final Shell activeShell;

    /**
     * The workbench site that must be active before this submission can be
     * considered.  This value can be <code>null</code>, which indicates that it
     * should match an workbench part site.
     */
    private final IWorkbenchPartSite activeWorkbenchPartSite;

    /**
     * The identifier for the command which the submitted handler handles.  This
     * value cannot be <code>null</code>.
     */
    private final String commandId;

    /**
     * The handler being submitted.  This value cannot be <code>null</code>.  
     */
    private final IHandler handler;

    /**
     * A lazily computed cache of the hash code.  This value is computed once on
     * demand.
     */
    private transient int hashCode;

    /**
     * Whether the hash code has been computed yet.
     */
    private transient boolean hashCodeComputed;

    /**
     * The priority for this submission.  In the event of all other factors
     * being equal, the priority will be considered in an attempt to resolve
     * conflicts.  This value cannot be <code>null</code>.
     */
    private final Priority priority;

    /**
     * A lazily computed cache of the string representation of this submission.
     * This value is computed once; before it is computed, it is
     * <code>null</code>.
     */
    private transient String string;

    /**
     * Creates a new instance of this class.
     * 
     * @param activePartId
     *            the identifier of the part that must be active for this
     *            request to be considered. May be <code>null</code>.
     * @param activeShell
     *            the shell that must be active for this request to be
     *            considered. May be <code>null</code>.
     * @param activeWorkbenchPartSite
     *            the workbench part site of the part that must be active for
     *            this request to be considered. May be <code>null</code>.
     * @param commandId
     *            the identifier of the command to be handled. Must not be
     *            <code>null</code>.
     * @param handler
     *            the handler. Must not be <code>null</code>.
     * @param priority
     *            the priority. Must not be <code>null</code>.
     */
    public HandlerSubmission(String activePartId, Shell activeShell,
            IWorkbenchPartSite activeWorkbenchPartSite, String commandId,
            IHandler handler, Priority priority) {
        if (commandId == null || handler == null || priority == null)
                throw new NullPointerException();

        this.activePartId = activePartId;
        this.activeShell = activeShell;
        this.activeWorkbenchPartSite = activeWorkbenchPartSite;
        this.commandId = commandId;
        this.handler = handler;
        this.priority = priority;
    }

    /**
     * @see Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object object) {
        HandlerSubmission castedObject = (HandlerSubmission) object;
        int compareTo = Util.compare(activeWorkbenchPartSite,
                castedObject.activeWorkbenchPartSite);

        if (compareTo == 0) {
            compareTo = Util.compare(activePartId, castedObject.activePartId);

            if (compareTo == 0) {
                compareTo = Util.compare(activeShell, castedObject.activeShell);

                if (compareTo == 0) {
                    compareTo = Util.compare(priority, castedObject.priority);

                    if (compareTo == 0) {
                        compareTo = Util.compare(commandId,
                                castedObject.commandId);

                        if (compareTo == 0)
                                compareTo = Util.compare(handler,
                                        castedObject.handler);
                    }
                }
            }
        }

        return compareTo;
    }

    /**
     * Returns the identifier of the part that must be active for this request
     * to be considered.
     * 
     * @return the identifier of the part that must be active for this request
     *         to be considered. May be <code>null</code>.
     */
    public String getActivePartId() {
        return activePartId;
    }

    /**
     * Returns the shell that must be active for this request to be considered.
     * 
     * @return the shell that must be active for this request to be considered.
     *         May be <code>null</code>.
     */
    public Shell getActiveShell() {
        return activeShell;
    }

    /**
     * Returns the workbench part site of the part that must be active for this
     * request to be considered.
     * 
     * @return the workbench part site of the part that must be active for this
     *         request to be considered. May be <code>null</code>.
     */
    public IWorkbenchPartSite getActiveWorkbenchPartSite() {
        return activeWorkbenchPartSite;
    }

    /**
     * Returns the identifier of the command to be handled.
     * 
     * @return the identifier of the command to be handled. Guaranteed not to be
     *         <code>null</code>.
     */
    public String getCommandId() {
        return commandId;
    }

    /**
     * Returns the handler.
     * 
     * @return the handler. Guaranteed not to be <code>null</code>.
     */
    public IHandler getHandler() {
        return handler;
    }

    /**
     * Returns the priority.
     * 
     * @return the priority. Guaranteed not to be <code>null</code>.
     */
    public Priority getPriority() {
        return priority;
    }

    /**
     * @see Object#hashCode()
     */
    public int hashCode() {
        if (!hashCodeComputed) {
            hashCode = HASH_INITIAL;
            hashCode = hashCode * HASH_FACTOR + Util.hashCode(activePartId);
            hashCode = hashCode * HASH_FACTOR + Util.hashCode(activeShell);
            hashCode = hashCode * HASH_FACTOR
                    + Util.hashCode(activeWorkbenchPartSite);
            hashCode = hashCode * HASH_FACTOR + Util.hashCode(commandId);
            hashCode = hashCode * HASH_FACTOR + Util.hashCode(handler);
            hashCode = hashCode * HASH_FACTOR + Util.hashCode(priority);
            hashCodeComputed = true;
        }

        return hashCode;
    }

    /**
     * @see Object#toString()
     */
    public String toString() {
        if (string == null) {
            final StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("[activePartId="); //$NON-NLS-1$
            stringBuffer.append(activePartId);
            stringBuffer.append("activeShell="); //$NON-NLS-1$
            stringBuffer.append(activeShell);
            stringBuffer.append(",activeWorkbenchSite="); //$NON-NLS-1$
            stringBuffer.append(activeWorkbenchPartSite);
            stringBuffer.append(",commandId="); //$NON-NLS-1$
            stringBuffer.append(commandId);
            stringBuffer.append(",handler="); //$NON-NLS-1$
            stringBuffer.append(handler);
            stringBuffer.append(",priority="); //$NON-NLS-1$
            stringBuffer.append(priority);
            stringBuffer.append(']');
            string = stringBuffer.toString();
        }

        return string;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8733.java