error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8296.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8296.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8296.java
text:
```scala
public static I@@PreferenceStore getPreferenceStore() {

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.internal.util.PrefUtil;
import org.eclipse.ui.testing.TestableObject;

/**
 * The central class for access to the Eclipse Platform User Interface. 
 * This class cannot be instantiated; all functionality is provided by 
 * static methods.
 * 
 * Features provided:
 * <ul>
 * <li>creation of the workbench.</li>
 * <li>access to the workbench.</li>
 * </ul>
 * <p>
 *
 * @see IWorkbench
 */
public final class PlatformUI {
	/**
	 * Identifies the workbench plug-in.
	 */
	public static final String PLUGIN_ID = "org.eclipse.ui"; //$NON-NLS-1$
	
	/**
	 * Return code (value 0) indicating that the workbench terminated normally.
	 * 
	 * @see #createAndRunWorkbench
	 * @since 3.0
	 */
	public static final int RETURN_OK = 0;

	/**
	 * Return code (value 1) indicating that the workbench was terminated with
	 * a call to <code>IWorkbench.restart</code>.
	 * 
	 * @see #createAndRunWorkbench
	 * @see IWorkbench#restart
	 * @since 3.0
	 */
	public static final int RETURN_RESTART = 1;

	/**
	 * Return code (value 2) indicating that the workbench failed to start.
	 * 
	 * @see #createAndRunWorkbench
	 * @see IWorkbench#restart
	 * @since 3.0
	 */
	public static final int RETURN_UNSTARTABLE = 2;

	/**
	 * Return code (value 3) indicating that the workbench was terminated with
	 * a call to {@link org.eclipse.ui.application.IWorkbenchConfigurer.emergencyClose}.
	 * 
	 * @see #createAndRunWorkbench
	 * @since 3.0
	 */
	public static final int RETURN_EMERGENCY_CLOSE = 3;

	/**
	 * Block instantiation.
	 */
	private PlatformUI() {
	    // do nothing
	}
	
	/**
	 * Returns the workbench. Fails if the workbench has not been created yet.
	 * 
	 * @return the workbench
	 */
	public static IWorkbench getWorkbench() {
		if (Workbench.getInstance() == null) {
			// app forgot to call createAndRunWorkbench beforehand
			throw new IllegalStateException(WorkbenchMessages.getString("PlatformUI.NoWorkbench")); //$NON-NLS-1$
		}
		return Workbench.getInstance();
	}
	
	/**
	 * Returns whether {@link #createAndRunWorkbench createAndRunWorkbench} has been
	 * called to create the workbench, and the workbench has yet to terminate.
	 * 
	 * @return <code>true</code> if the workbench has been created and is still
	 * running, and <code>false</code> if the workbench has not yet been created
	 * or has completed
	 * @since 3.0
	 */
	public static boolean isWorkbenchRunning() {
		return Workbench.getInstance() != null  && Workbench.getInstance().isRunning();
	}
	
	/**
	 * Creates the workbench and associates it with the given display and workbench
	 * advisor, and runs the workbench UI. This entails processing and dispatching
	 * events until the workbench is closed or restarted.
	 * <p>
	 * This method is intended to be called by the main class (the "application").
	 * Fails if the workbench UI has already been created.
	 * </p>
	 * <p>
	 * Use {@link #createDisplay createDisplay} to create the display to pass in.
	 * </p>
	 * <p>
	 * Note that this method is intended to be called by the application
	 * (<code>org.eclipse.core.boot.IPlatformRunnable</code>). It must be
	 * called exactly once, and early on before anyone else asks
	 * <code>getWorkbench()</code> for the workbench.
	 * </p>
	 * 
	 * @param display the display to be used for all UI interactions with the workbench
	 * @param advisor the application-specific advisor that configures and
	 * specializes the workbench
	 * @return return code {@link #RETURN_OK RETURN_OK} for normal exit; 
	 * {@link #RETURN_RESTART RETURN_RESTART} if the workbench was terminated
	 * with a call to {@link IWorkbench#restart IWorkbench.restart}; 
	 * {@link #RETURN_UNSTARTABLE RETURN_UNSTARTABLE} if the workbench could
	 * not be started; 
	 * {@link #RETURN_EMERGENCY_CLOSE RETURN_EMERGENCY_CLOSE} if the UI quit
	 * because of an emergency; other values reserved for future use
	 * @since 3.0
	 */
	public static int createAndRunWorkbench(Display display, WorkbenchAdvisor advisor) {
		return Workbench.createAndRunWorkbench(display, advisor);
	}
	
	/**
	 * Creates the <code>Display</code> to be used by the workbench.
	 * It is the caller's responsibility to dispose the resulting <code>Display</code>, 
	 * not the workbench's.
	 * 
	 * @return the display
	 * @since 3.0
	 */
	public static Display createDisplay() {
		return Workbench.createDisplay();
	}

	/**
	 * Returns the testable object facade, for use by the test harness.
	 * <p>
	 * IMPORTANT: This method is only for use by the test harness.
	 * Applications and regular plug-ins should not call this method.
	 * </p> 
	 * 
	 * @return the testable object facade
	 * @since 3.0
	 */
	public static TestableObject getTestableObject() {
		return Workbench.getWorkbenchTestable();
	}
	
	/**
	 * Returns the preference store used for publicly settable workbench preferences.
	 * Constants for these preferences are defined on 
	 * {@link org.eclipse.ui.IWorkbenchPreferenceConstants}.
	 * 
	 * @return the workbench public preference store
	 * @since 3.0
	 */
	public IPreferenceStore getPreferenceStore() {
	    return PrefUtil.getAPIPreferenceStore();
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8296.java