error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3478.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3478.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3478.java
text:
```scala
n@@ewPart = new ViewPane((IViewReference)ref, page);

/*******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal;

import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.IPluginContribution;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.activities.IIdentifier;
import org.eclipse.ui.activities.IIdentifierListener;
import org.eclipse.ui.activities.IWorkbenchActivitySupport;
import org.eclipse.ui.activities.IdentifierEvent;
import org.eclipse.ui.activities.WorkbenchActivityHelper;

import org.eclipse.ui.internal.registry.IViewDescriptor;

/**
 * Helper methods that the internal layout classes (<code>PageLayout</code> and 
 * <code>FolderLayout</code>) utilize for activities support and view creation.
 * 
 * @since 3.0
 */
class LayoutHelper {
	
	/**
	 * Not intended to be instantiated.
	 */
	private LayoutHelper() {
		//no-op
	}

	/**
	 * Creates a series of listeners that will activate the provided view on the
	 * provided page layout when <code>IIdenfier</code> enablement changes. The 
	 * rules for this activation are as follows: <p>
	 * <ul>
	 * <li> if the identifier becomes enabled and the perspective of the page 
	 * layout is the currently active perspective in its window, then activate 
	 * the views immediately.
	 * <li> if the identifier becomes enabled and the perspective of the page 
	 * layout is not the currently active perspecitve in its window, then add an
	 * <code>IPerspectiveListener</code> to the window and activate the views 
	 * when the perspective becomes active. 
	 * 
	 * @param pageLayout <code>PageLayout</code>.
	 * @param viewId the view id to activate upon <code>IIdentifier</code> enablement.
	 */
	public static final void addViewActivator(
		PageLayout pageLayout,
		final String viewId) {
		if (viewId == null)
			return;

		ViewFactory viewFactory = pageLayout.getViewFactory();

		final IWorkbenchPage partPage = viewFactory.getWorkbenchPage();
		if (partPage == null)
			return;

		final IPerspectiveDescriptor partPerspective =
			pageLayout.getDescriptor();

		IWorkbenchActivitySupport support = PlatformUI.getWorkbench().getActivitySupport();

		IViewDescriptor descriptor = viewFactory.getViewRegistry().find(viewId);
		if (!(descriptor instanceof IPluginContribution))
			return;

		IIdentifier identifier =
			support.getActivityManager().getIdentifier(
				WorkbenchActivityHelper.createUnifiedId(
					(IPluginContribution) descriptor));

		identifier.addIdentifierListener(new IIdentifierListener() {
			
			/* (non-Javadoc)
			 * @see org.eclipse.ui.activities.IIdentifierListener#identifierChanged(org.eclipse.ui.activities.IdentifierEvent)
			 */
			public void identifierChanged(IdentifierEvent identifierEvent) {
				if (identifierEvent.hasEnabledChanged()) {
					IIdentifier thisIdentifier =
						identifierEvent.getIdentifier();
					if (thisIdentifier.isEnabled()) {
						// show view
						thisIdentifier.removeIdentifierListener(this);
						IWorkbenchPage activePage =
							partPage.getWorkbenchWindow().getActivePage();
						if (partPage == activePage
							&& partPerspective == activePage.getPerspective()) {
							// show immediately.
							try {
								partPage.showView(viewId);
							} catch (PartInitException e) {
								WorkbenchPlugin.log(e.getMessage());
							}
						} else { // show when the perspective becomes active							
							partPage
								.getWorkbenchWindow()
								.addPerspectiveListener(
									new IPerspectiveListener() {

								/* (non-Javadoc)
								 * @see org.eclipse.ui.IPerspectiveListener#perspectiveActivated(org.eclipse.ui.IWorkbenchPage, org.eclipse.ui.IPerspectiveDescriptor)
								 */
								public void perspectiveActivated(
									IWorkbenchPage page,
									IPerspectiveDescriptor newPerspective) {
									if (partPerspective == newPerspective) {
										partPage
											.getWorkbenchWindow()
											.removePerspectiveListener(
											this);
										try {
											page.showView(viewId);
										} catch (PartInitException e) {
											WorkbenchPlugin.log(e.getMessage());
										}
									}
								}

								/* (non-Javadoc)
								 * @see org.eclipse.ui.IPerspectiveListener#perspectiveChanged(org.eclipse.ui.IWorkbenchPage, org.eclipse.ui.IPerspectiveDescriptor, java.lang.String)
								 */
								public void perspectiveChanged(
									IWorkbenchPage page,
									IPerspectiveDescriptor perspective,
									String changeId) {
									// no-op
								}
							});
						}
					} 
				}
			}
		});
	}

	/**
	 * Create the view.  If it's already been been created in the provided 
	 * factory, return the shared instance.
	 * 
	 * @param factory the <code>ViewFactory</code> to use.
	 * @param viewID the view id to use.
	 * @return the new <code>ViewPane</code>.
	 * @throws PartInitException thrown if there is a problem creating the view.
	 */
	public static final ViewPane createView(
		ViewFactory factory,
		String viewId)
		throws PartInitException {
		WorkbenchPartReference ref =
			(WorkbenchPartReference) factory.createView(
				viewId);
		ViewPane newPart = (ViewPane) ref.getPane();
		if (newPart == null) {
			WorkbenchPage page = (WorkbenchPage) ref.getPage();
			newPart = new ViewPane((IViewReference)ref, page);
			ref.setPane(newPart);
		}
		return newPart;
	}
	
	/**
	 * Create the view with a specified theme.  
	 * If it's already been been created in the provided 
	 * factory, return the shared instance.
	 * 
	 * @param factory the <code>ViewFactory</code> to use.
	 * @param viewID the view id to use.
	 * @return the new <code>ViewPane</code>.
	 * @throws PartInitException thrown if there is a problem creating the view.
	 *
	 * @issue view should refer to current perspective for theme setting
	 */
	public static final ViewPane createView(
			ViewFactory factory,
			String viewId, 
			String theme)
	throws PartInitException {
		WorkbenchPartReference ref =
		(WorkbenchPartReference) factory.createView(
				viewId);
		ViewPane newPart = (ViewPane) ref.getPane();
		if (newPart == null) {
			WorkbenchPage page = (WorkbenchPage) ref.getPage();
			newPart = new ViewPane((IViewReference)ref, page, theme);
			ref.setPane(newPart);
		}
		return newPart;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3478.java