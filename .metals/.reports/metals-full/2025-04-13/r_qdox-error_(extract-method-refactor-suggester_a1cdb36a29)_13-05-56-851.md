error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8284.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8284.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8284.java
text:
```scala
i@@f (JavaModelManager.CP_RESOLVE_VERBOSE || JavaModelManager.CP_RESOLVE_VERBOSE_FAILURE)

/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.core;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.util.Util;

public class SetContainerOperation extends ChangeClasspathOperation {
	
	IPath containerPath;
	IJavaProject[] affectedProjects;
	IClasspathContainer[] respectiveContainers;
	
	/*
	 * Creates a new SetContainerOperation.
	 */
	public SetContainerOperation(IPath containerPath, IJavaProject[] affectedProjects, IClasspathContainer[] respectiveContainers) {
		super(new IJavaElement[] {JavaModelManager.getJavaModelManager().getJavaModel()}, !ResourcesPlugin.getWorkspace().isTreeLocked());
		this.containerPath = containerPath;
		this.affectedProjects = affectedProjects;
		this.respectiveContainers = respectiveContainers;
	}

	protected void executeOperation() throws JavaModelException {
		checkCanceled();
		try {
			beginTask("", 1); //$NON-NLS-1$
			if (JavaModelManager.CP_RESOLVE_VERBOSE)
				verbose_set_container();
			if (JavaModelManager.CP_RESOLVE_VERBOSE_ADVANCED)
				verbose_set_container_invocation_trace();
			
			JavaModelManager manager = JavaModelManager.getJavaModelManager();
			if (manager.containerPutIfInitializingWithSameEntries(this.containerPath, this.affectedProjects, this.respectiveContainers))
				return;
	
			final int projectLength = this.affectedProjects.length;	
			final IJavaProject[] modifiedProjects;
			System.arraycopy(this.affectedProjects, 0, modifiedProjects = new IJavaProject[projectLength], 0, projectLength);
			final IClasspathEntry[][] oldResolvedPaths = new IClasspathEntry[projectLength][];
				
			// filter out unmodified project containers
			int remaining = 0;
			for (int i = 0; i < projectLength; i++){
				if (isCanceled()) 
					return;
				JavaProject affectedProject = (JavaProject) this.affectedProjects[i];
				IClasspathContainer newContainer = this.respectiveContainers[i];
				if (newContainer == null) newContainer = JavaModelManager.CONTAINER_INITIALIZATION_IN_PROGRESS; // 30920 - prevent infinite loop
				boolean found = false;
				if (JavaProject.hasJavaNature(affectedProject.getProject())){
					IClasspathEntry[] rawClasspath = affectedProject.getRawClasspath();
					for (int j = 0, cpLength = rawClasspath.length; j <cpLength; j++) {
						IClasspathEntry entry = rawClasspath[j];
						if (entry.getEntryKind() == IClasspathEntry.CPE_CONTAINER && entry.getPath().equals(this.containerPath)){
							found = true;
							break;
						}
					}
				}
				if (!found) {
					modifiedProjects[i] = null; // filter out this project - does not reference the container path, or isnt't yet Java project
					manager.containerPut(affectedProject, this.containerPath, newContainer);
					continue;
				}
				IClasspathContainer oldContainer = manager.containerGet(affectedProject, this.containerPath);
				if (oldContainer == JavaModelManager.CONTAINER_INITIALIZATION_IN_PROGRESS) {
					oldContainer = null;
				}
				if ((oldContainer != null && oldContainer.equals(this.respectiveContainers[i]))
 (oldContainer == this.respectiveContainers[i])/*handle case where old and new containers are null (see bug 149043*/) {
					modifiedProjects[i] = null; // filter out this project - container did not change
					continue;
				}
				remaining++; 
				oldResolvedPaths[i] = affectedProject.getResolvedClasspath();
				manager.containerPut(affectedProject, this.containerPath, newContainer);
			}
			
			if (remaining == 0) return;
			
			// trigger model refresh
			try {
				for(int i = 0; i < projectLength; i++){
					if (isCanceled()) 
						return;
					
					JavaProject affectedProject = (JavaProject)modifiedProjects[i];
					if (affectedProject == null) continue; // was filtered out
					if (JavaModelManager.CP_RESOLVE_VERBOSE_ADVANCED)
						verbose_update_project(affectedProject);
		
					// force resolved classpath to be recomputed
					ClasspathChange classpathChange = affectedProject.getPerProjectInfo().resetResolvedClasspath();
					
					// if needed, generate delta, update project ref, create markers, ...
					classpathChanged(classpathChange);
					
					if (this.canChangeResources) {
						// touch project to force a build if needed
						try {
							affectedProject.getProject().touch(this.progressMonitor);
						} catch (CoreException e) {
							// see https://bugs.eclipse.org/bugs/show_bug.cgi?id=148970
							if (!ExternalJavaProject.EXTERNAL_PROJECT_NAME.equals(affectedProject.getElementName()))
								throw e;
						}
					}
				}
			} catch(CoreException e) {
				if (JavaModelManager.CP_RESOLVE_VERBOSE)
					verbose_failure(e);
				if (e instanceof JavaModelException) {
					throw (JavaModelException)e;
				} else {
					throw new JavaModelException(e);
				}
			} finally {
				for (int i = 0; i < projectLength; i++) {
					if (this.respectiveContainers[i] == null) {
						manager.containerPut(this.affectedProjects[i], this.containerPath, null); // reset init in progress marker
					}
				}
			}
		} finally {		
			done();
		}
	}

	private void verbose_failure(CoreException e) {
		Util.verbose(
			"CPContainer SET  - FAILED DUE TO EXCEPTION\n" + //$NON-NLS-1$
			"	container path: " + this.containerPath, //$NON-NLS-1$
			System.err);
		e.printStackTrace();
	}

	private void verbose_update_project(JavaProject affectedProject) {
		Util.verbose(
			"CPContainer SET  - updating affected project due to setting container\n" + //$NON-NLS-1$
			"	project: " + affectedProject.getElementName() + '\n' + //$NON-NLS-1$
			"	container path: " + this.containerPath); //$NON-NLS-1$
	}

	private void verbose_set_container() {
		Util.verbose(
			"CPContainer SET  - setting container\n" + //$NON-NLS-1$
			"	container path: " + this.containerPath + '\n' + //$NON-NLS-1$
			"	projects: {" +//$NON-NLS-1$
			org.eclipse.jdt.internal.compiler.util.Util.toString(
				this.affectedProjects, 
				new org.eclipse.jdt.internal.compiler.util.Util.Displayable(){ 
					public String displayString(Object o) { return ((IJavaProject) o).getElementName(); }
				}) +
			"}\n	values: {\n"  +//$NON-NLS-1$
			org.eclipse.jdt.internal.compiler.util.Util.toString(
				this.respectiveContainers, 
				new org.eclipse.jdt.internal.compiler.util.Util.Displayable(){ 
					public String displayString(Object o) { 
						StringBuffer buffer = new StringBuffer("		"); //$NON-NLS-1$
						if (o == null) {
							buffer.append("<null>"); //$NON-NLS-1$
							return buffer.toString();
						}
						IClasspathContainer container = (IClasspathContainer) o;
						buffer.append(container.getDescription());
						buffer.append(" {\n"); //$NON-NLS-1$
						IClasspathEntry[] entries = container.getClasspathEntries();
						if (entries != null){
							for (int i = 0; i < entries.length; i++){
								buffer.append(" 			"); //$NON-NLS-1$
								buffer.append(entries[i]); 
								buffer.append('\n'); 
							}
						}
						buffer.append(" 		}"); //$NON-NLS-1$
						return buffer.toString();
					}
				}) +
			"\n	}");//$NON-NLS-1$
	}
	
	private void verbose_set_container_invocation_trace() {
		Util.verbose(
			"CPContainer SET  - setting container\n" + //$NON-NLS-1$
			"	invocation stack trace:"); //$NON-NLS-1$
			new Exception("<Fake exception>").printStackTrace(System.out); //$NON-NLS-1$
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8284.java