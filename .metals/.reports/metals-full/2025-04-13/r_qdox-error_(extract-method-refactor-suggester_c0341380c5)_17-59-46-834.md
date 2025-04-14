error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2993.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2993.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2993.java
text:
```scala
r@@eturn projects.get(ele);

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

package org.eclipse.xtend.shared.ui.core.internal;

import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.internal.xtend.util.Cache;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.xtend.shared.ui.Activator;
import org.eclipse.xtend.shared.ui.Messages;
import org.eclipse.xtend.shared.ui.core.IModelManager;
import org.eclipse.xtend.shared.ui.core.IXtendXpandProject;
import org.eclipse.xtend.shared.ui.core.IXtendXpandResource;
import org.eclipse.xtend.shared.ui.core.builder.XtendXpandNature;
import org.eclipse.xtend.shared.ui.internal.XtendLog;

public class XtendXpandModelManager implements IModelManager {

    public XtendXpandModelManager() {

    }

    public final Cache<IJavaProject, XtendXpandProject> projects = new Cache<IJavaProject, XtendXpandProject>() {
        @Override
        protected XtendXpandProject createNew(IJavaProject ele) {
            return new XtendXpandProject(ele);
        }
    };

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.internal.xtend.core.IXpandModelManager#findProject(org.eclipse.core.runtime.IPath)
     */
    public IXtendXpandProject findProject(final IPath path) {
        return findProject(ResourcesPlugin.getWorkspace().getRoot().findMember(path));
    }

    public IXtendXpandProject findProject(final IResource res) {
        if (res == null)
            return null;
        final IJavaProject ele = JavaCore.create(res.getProject());
        try {
            if (ele != null && res.getProject().isAccessible() && res.getProject().isNatureEnabled(XtendXpandNature.NATURE_ID))
                return (IXtendXpandProject) projects.get(ele);
        } catch (final CoreException e) {
            XtendLog.logError(e);
        }
        return null;
    }

    public void analyze(final IProgressMonitor monitor) {
        monitor.beginTask(Messages.XtendXpandModelManager_AnalyzingPrompt, computeAmoutOfWork());
        for (final Iterator<?> iter = projects.getValues().iterator(); iter.hasNext();) {
            if (monitor.isCanceled())
                return;
            IXtendXpandProject project = (IXtendXpandProject) iter.next();
			project.analyze(monitor, Activator.getExecutionContext(project.getProject()));
        }
        monitor.done();
    }

    /**
     * Computes the amount of work that has to be done during a build.
     * @return the number of resources registered within all Xtend projects.
     */
    private int computeAmoutOfWork() {
        int i = 0;
        for (final Iterator<?> iter = projects.getValues().iterator(); iter.hasNext();) {
            final IXtendXpandProject element = (IXtendXpandProject) iter.next();
            i += element.getRegisteredResources().length;
        }
        return i;
    }

    /**
     * Tries to locate an Xtend resource by its underlying file.
     * @param underlying IStorage
     */
    public IXtendXpandResource findExtXptResource(IStorage file) {
    	// it can be that the resource is located within a jar, than scan the projects for the resource
    	if (!(file instanceof IFile)) {
    		for (Iterator<?> it=projects.getValues().iterator(); it.hasNext(); ) {
    			IXtendXpandProject p = (IXtendXpandProject) it.next();
    			IXtendXpandResource res = p.findXtendXpandResource(file);
    			if (res!=null) {
    				return res;
    			}
    		}
    	} else {
	        final IXtendXpandProject project = findProject((IFile)file);
	        if (project != null) {
	            return project.findXtendXpandResource(file);
	        }
        }
        return null;
    }

	public IXtendXpandResource findXtendXpandResource(String oawNamespace, String extension) {
		for (IXtendXpandProject p : projects.getValues()) {
			IXtendXpandResource res = p.findExtXptResource(oawNamespace, extension);
			if (res!=null)
				return res;
		}
		return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2993.java