error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11165.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11165.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11165.java
text:
```scala
final i@@nt severity = issue.isError() ? IMarker.SEVERITY_ERROR : IMarker.SEVERITY_WARNING;

/*******************************************************************************
 * Copyright (c) 2005, 2007 committers of openArchitectureWare and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     committers of openArchitectureWare - initial API and implementation
 *******************************************************************************/

package org.eclipse.xtend.shared.ui.core.builder;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.filebuffers.ITextFileBufferManager;
import org.eclipse.core.filebuffers.LocationKind;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.xtend.expression.AnalysationIssue;
import org.eclipse.xtend.shared.ui.Activator;
import org.eclipse.xtend.shared.ui.Messages;
import org.eclipse.xtend.shared.ui.internal.XtendLog;

public class XtendXpandMarkerManager {

    public static final String getMARKER_TYPE() {
        return Activator.getId() + ".problem";
    }

    public static void addMarker(final IFile file, final AnalysationIssue issue) {
        try {
            final IMarker marker = file.createMarker(getMARKER_TYPE());
            final int severity = IMarker.SEVERITY_ERROR;
            int start = -1, end = -1;
            if (issue.getElement() != null) {
                start = issue.getElement().getStart();
                end = issue.getElement().getEnd();
            }
            internalAddMarker(file, marker, issue.getMessage(), severity, start, end);
        } catch (final CoreException e) {
        }
    }

    public static void addErrorMarker(final IFile file, final String message, final int severity, final int start,
            final int end) {
        try {
            final IMarker marker = file.createMarker(getMARKER_TYPE());
            internalAddMarker(file, marker, message, severity, start, end);
        } catch (final CoreException e) {
            XtendLog.logError(e);
        }
    }

    public static void addWarningMarker(final IFile file, final String message, final int severity, final int start,
            final int end) {
        try {
            final IMarker marker = file.createMarker(getMARKER_TYPE());
            internalAddMarker(file, marker, message, severity, start, end);
        } catch (final CoreException e) {
        }
    }

    private final static void internalAddMarker(final IFile file, final IMarker marker, final String message,
            final int severity, final int start, final int end) {
        try {
            new WorkspaceModifyOperation() {

                @Override
                protected void execute(final IProgressMonitor monitor) throws CoreException, InvocationTargetException,
                        InterruptedException {

                    try {
                        marker.setAttribute(IMarker.MESSAGE, message);
                        marker.setAttribute(IMarker.SEVERITY, severity);
                        int s = start;
                        if (start == -1) {
                            s = 1;
                        }
                        int e = end;
                        if (end <= start) {
                            e = start + 1;
                        }
                        marker.setAttribute(IMarker.CHAR_START, s);
                        marker.setAttribute(IMarker.CHAR_END, e);
                        final ITextFileBufferManager mgr = FileBuffers.getTextFileBufferManager();
                        if (mgr != null) {
                            final IPath location = file.getFullPath();
                            try {
                                mgr.connect(location,LocationKind.NORMALIZE, new NullProgressMonitor());
                                final ITextFileBuffer buff = mgr.getTextFileBuffer(file.getFullPath(), LocationKind.NORMALIZE);
                                if (buff != null) {
                                    final IDocument doc = buff.getDocument();
                                    final int line = doc.getLineOfOffset(start);
                                    if (line > 0) {
                                        marker.setAttribute(IMarker.LINE_NUMBER, doc.getLineOfOffset(start));
                                        marker.setAttribute(IMarker.LOCATION, Messages.XtendXpandMarkerManager_Line + line);
                                    }
                                }
                            } finally {
                                mgr.disconnect(location,LocationKind.NORMALIZE, new NullProgressMonitor());
                            }
                        }
                    } catch (final CoreException e) {
                    	XtendLog.logError(e);
                    } catch (final BadLocationException e) {
                    	XtendLog.logError(e);
                    }
                }
            }.run(new NullProgressMonitor());
        } catch (final Exception e) {
            XtendLog.logError(e);
        }
    }

    public static void deleteMarkers(final IResource file) {
        try {
        	if (file.exists()) {
                new WorkspaceModifyOperation() {

                    @Override
                    protected void execute(final IProgressMonitor monitor) throws CoreException,
                            InvocationTargetException, InterruptedException {
                        file.deleteMarkers(getMARKER_TYPE(), true, IResource.DEPTH_INFINITE);
                    }

                }.run(new NullProgressMonitor());
            }
        } catch (final Exception ce) {
            XtendLog.logError(ce);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11165.java