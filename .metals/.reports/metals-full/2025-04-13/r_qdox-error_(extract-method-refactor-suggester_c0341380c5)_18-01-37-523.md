error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8916.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8916.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8916.java
text:
```scala
D@@isplay.getDefault().syncExec(new Runnable() {

/****************************************************************************
 * Copyright (c) 2004 Composent, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Composent, Inc. - initial API and implementation
 *****************************************************************************/

package org.eclipse.ecf.presence.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.presence.IPresenceContainerAdapter;
import org.eclipse.ecf.presence.roster.IRoster;
import org.eclipse.ecf.presence.roster.IRosterItem;
import org.eclipse.ecf.presence.roster.IRosterManager;
import org.eclipse.ecf.presence.roster.IRosterUpdateListener;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;

/**
 * View class for displaying multiple rosters in a tree viewer
 *
 */
public class MultiRosterView extends ViewPart implements IMultiRosterViewPart {

	protected TreeViewer treeViewer;

	protected MultiRosterLabelProvider rosterLabelProvider;

	protected MultiRosterContentProvider rosterContentProvider;

	protected List rosterAccounts = new ArrayList();

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(Composite parent) {
		treeViewer = new TreeViewer(parent, SWT.BORDER | SWT.MULTI
 SWT.V_SCROLL);
		getSite().setSelectionProvider(treeViewer);
		rosterContentProvider = new MultiRosterContentProvider();
		rosterLabelProvider = new MultiRosterLabelProvider();
		treeViewer.setLabelProvider(rosterLabelProvider);
		treeViewer.setContentProvider(rosterContentProvider);
		treeViewer.setInput(new Object());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#dispose()
	 */
	public void dispose() {
		super.dispose();
		treeViewer = null;
		rosterLabelProvider = null;
		rosterContentProvider = null;
		rosterAccounts.clear();
	}

	protected void addRosterAccountsToProviders() {
		for (Iterator i = rosterAccounts.iterator(); i.hasNext();) {
			RosterAccount account = (RosterAccount) i.next();
			rosterContentProvider.add(account.getRoster());
		}
	}

	protected boolean addRosterAccount(RosterAccount account) {
		if (account == null)
			return false;
		if (rosterAccounts.add(account)) {
			if (rosterContentProvider != null) {
				rosterContentProvider.add(account.getRoster());
			}
			return true;
		} else
			return false;
	}

	protected boolean removeRosterAccount(RosterAccount account) {
		if (account == null)
			return false;
		if (rosterAccounts.remove(account)) {
			if (rosterContentProvider != null) {
				rosterContentProvider.remove(account.getRoster());
			}
			account.dispose();
			return true;
		} else
			return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	public void setFocus() {
		treeViewer.getControl().setFocus();
	}

	class RosterAccount {
		IContainer container;

		IPresenceContainerAdapter adapter;

		IRosterUpdateListener updateListener = new IRosterUpdateListener() {
			public void handleRosterUpdate(final IRoster roster,
					final IRosterItem changedValue) {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						refreshTreeViewer(changedValue,true);
					}
				});
			}
		};

		public RosterAccount(IContainer container,
				IPresenceContainerAdapter adapter) {
			Assert.isNotNull(container);
			Assert.isNotNull(adapter);
			this.container = container;
			this.adapter = adapter;
			getRosterManager().addRosterUpdateListener(updateListener);
		}

		public IContainer getContainer() {
			return container;
		}

		public IPresenceContainerAdapter getPresenceAdapter() {
			return adapter;
		}

		public IRosterManager getRosterManager() {
			return getPresenceAdapter().getRosterManager();
		}

		public IRoster getRoster() {
			return getRosterManager().getRoster();
		}
		
		public void dispose() {
			getRosterManager().removeRosterUpdateListener(updateListener);
			container = null;
			adapter = null;
		}
	}

	protected void refreshTreeViewer(Object val, boolean labels) {
		if (treeViewer != null) {
			if (val != null) treeViewer.refresh(val,labels);
			else treeViewer.refresh(labels);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.presence.ui.IMultiRosterViewPart#addContainer(org.eclipse.ecf.core.IContainer)
	 */
	public boolean addContainer(IContainer container) {
		if (container == null)
			return false;
		IPresenceContainerAdapter containerAdapter = (IPresenceContainerAdapter) container
				.getAdapter(IPresenceContainerAdapter.class);
		if (containerAdapter == null)
			return false;
		if (addRosterAccount(new RosterAccount(container, containerAdapter))) {
			refreshTreeViewer(null,true);
			return true;
		} else
			return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8916.java