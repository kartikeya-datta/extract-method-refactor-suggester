error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6940.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6940.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6940.java
text:
```scala
c@@lasspathChanged(classpathChange, true/*refresh if external linked folder already exists*/);

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

import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.util.Util;

public class SetVariablesOperation extends ChangeClasspathOperation {

	String[] variableNames;
	IPath[] variablePaths;
	boolean updatePreferences;

	/*
	 * Creates a new SetVariablesOperation for the given variable values (null path meaning removal), allowing to change multiple variable values at once.
	 */
	public SetVariablesOperation(String[] variableNames, IPath[] variablePaths, boolean updatePreferences) {
		super(new IJavaElement[] {JavaModelManager.getJavaModelManager().getJavaModel()}, !ResourcesPlugin.getWorkspace().isTreeLocked());
		this.variableNames = variableNames;
		this.variablePaths = variablePaths;
		this.updatePreferences = updatePreferences;
	}

	protected void executeOperation() throws JavaModelException {
		checkCanceled();
		try {
			beginTask("", 1); //$NON-NLS-1$
			if (JavaModelManager.CP_RESOLVE_VERBOSE)
				verbose_set_variables();

			JavaModelManager manager = JavaModelManager.getJavaModelManager();
			if (manager.variablePutIfInitializingWithSameValue(this.variableNames, this.variablePaths))
				return;

			int varLength = this.variableNames.length;

			// gather classpath information for updating
			final HashMap affectedProjectClasspaths = new HashMap(5);
			IJavaModel model = getJavaModel();

			// filter out unmodified variables
			int discardCount = 0;
			for (int i = 0; i < varLength; i++){
				String variableName = this.variableNames[i];
				IPath oldPath = manager.variableGet(variableName); // if reentering will provide previous session value
				if (oldPath == JavaModelManager.VARIABLE_INITIALIZATION_IN_PROGRESS) {
					oldPath = null;  //33695 - cannot filter out restored variable, must update affected project to reset cached CP
				}
				if (oldPath != null && oldPath.equals(this.variablePaths[i])){
					this.variableNames[i] = null;
					discardCount++;
				}
			}
			if (discardCount > 0){
				if (discardCount == varLength) return;
				int changedLength = varLength - discardCount;
				String[] changedVariableNames = new String[changedLength];
				IPath[] changedVariablePaths = new IPath[changedLength];
				for (int i = 0, index = 0; i < varLength; i++){
					if (this.variableNames[i] != null){
						changedVariableNames[index] = this.variableNames[i];
						changedVariablePaths[index] = this.variablePaths[i];
						index++;
					}
				}
				this.variableNames = changedVariableNames;
				this.variablePaths = changedVariablePaths;
				varLength = changedLength;
			}

			if (isCanceled())
				return;

			IJavaProject[] projects = model.getJavaProjects();
			nextProject : for (int i = 0, projectLength = projects.length; i < projectLength; i++){
				JavaProject project = (JavaProject) projects[i];

				// check to see if any of the modified variables is present on the classpath
				IClasspathEntry[] classpath = project.getRawClasspath();
				for (int j = 0, cpLength = classpath.length; j < cpLength; j++){

					IClasspathEntry entry = classpath[j];
					for (int k = 0; k < varLength; k++){

						String variableName = this.variableNames[k];
						if (entry.getEntryKind() ==  IClasspathEntry.CPE_VARIABLE){

							if (variableName.equals(entry.getPath().segment(0))){
								affectedProjectClasspaths.put(project, project.getResolvedClasspath());
								continue nextProject;
							}
							IPath sourcePath, sourceRootPath;
							if (((sourcePath = entry.getSourceAttachmentPath()) != null	&& variableName.equals(sourcePath.segment(0)))
 ((sourceRootPath = entry.getSourceAttachmentRootPath()) != null	&& variableName.equals(sourceRootPath.segment(0)))) {

								affectedProjectClasspaths.put(project, project.getResolvedClasspath());
								continue nextProject;
							}
						}
					}
				}
			}

			// update variables
			for (int i = 0; i < varLength; i++){
				manager.variablePut(this.variableNames[i], this.variablePaths[i]);
				if (this.updatePreferences)
					manager.variablePreferencesPut(this.variableNames[i], this.variablePaths[i]);
			}

			// update affected project classpaths
			if (!affectedProjectClasspaths.isEmpty()) {
				String[] dbgVariableNames = this.variableNames;
				try {
					// propagate classpath change
					Iterator projectsToUpdate = affectedProjectClasspaths.keySet().iterator();
					while (projectsToUpdate.hasNext()) {

						if (this.progressMonitor != null && this.progressMonitor.isCanceled()) return;

						JavaProject affectedProject = (JavaProject) projectsToUpdate.next();

						// force resolved classpath to be recomputed
						if (JavaModelManager.CP_RESOLVE_VERBOSE_ADVANCED)
							verbose_update_project(dbgVariableNames, affectedProject);
						ClasspathChange classpathChange = affectedProject.getPerProjectInfo().resetResolvedClasspath();

						// if needed, generate delta, update project ref, create markers, ...
						classpathChanged(classpathChange);

						if (this.canChangeResources) {
							// touch project to force a build if needed
							affectedProject.getProject().touch(this.progressMonitor);
						}
					}
				} catch (CoreException e) {
					if (JavaModelManager.CP_RESOLVE_VERBOSE || JavaModelManager.CP_RESOLVE_VERBOSE_FAILURE){
						verbose_failure(dbgVariableNames);
						e.printStackTrace();
					}
					if (e instanceof JavaModelException) {
						throw (JavaModelException)e;
					} else {
						throw new JavaModelException(e);
					}
				}
			}
		} finally {
			done();
		}
	}

	private void verbose_failure(String[] dbgVariableNames) {
		Util.verbose(
			"CPVariable SET  - FAILED DUE TO EXCEPTION\n" + //$NON-NLS-1$
			"	variables: " + org.eclipse.jdt.internal.compiler.util.Util.toString(dbgVariableNames), //$NON-NLS-1$
			System.err);
	}

	private void verbose_update_project(String[] dbgVariableNames,
			JavaProject affectedProject) {
		Util.verbose(
			"CPVariable SET  - updating affected project due to setting variables\n" + //$NON-NLS-1$
			"	project: " + affectedProject.getElementName() + '\n' + //$NON-NLS-1$
			"	variables: " + org.eclipse.jdt.internal.compiler.util.Util.toString(dbgVariableNames)); //$NON-NLS-1$
	}

	private void verbose_set_variables() {
		Util.verbose(
			"CPVariable SET  - setting variables\n" + //$NON-NLS-1$
			"	variables: " + org.eclipse.jdt.internal.compiler.util.Util.toString(this.variableNames) + '\n' +//$NON-NLS-1$
			"	values: " + org.eclipse.jdt.internal.compiler.util.Util.toString(this.variablePaths)); //$NON-NLS-1$
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6940.java