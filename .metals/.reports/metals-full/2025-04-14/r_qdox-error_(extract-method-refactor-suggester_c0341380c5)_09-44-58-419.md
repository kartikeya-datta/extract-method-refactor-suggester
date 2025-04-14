error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3999.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3999.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3999.java
text:
```scala
h@@elpMessageLabel.setText(""); //$NON-NLS-1$

/*******************************************************************************
 * Copyright (c) 2008 Marcelo Mayworm. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 	Marcelo Mayworm - initial API and implementation
 *
 ******************************************************************************/
package org.eclipse.ecf.presence.ui;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.Assert;
import org.eclipse.ecf.core.IContainerListener;
import org.eclipse.ecf.core.events.IContainerDisconnectedEvent;
import org.eclipse.ecf.core.events.IContainerEvent;
import org.eclipse.ecf.core.util.ECFException;
import org.eclipse.ecf.internal.presence.ui.Activator;
import org.eclipse.ecf.internal.presence.ui.Messages;
import org.eclipse.ecf.presence.IPresenceContainerAdapter;
import org.eclipse.ecf.presence.roster.IRosterSubscriptionSender;
import org.eclipse.ecf.presence.search.IResult;
import org.eclipse.ecf.presence.search.IResultList;
import org.eclipse.ecf.ui.SharedImages;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.*;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.ViewPart;

/**
 * View class for displaying multiple users that match a search in a tree viewer. This view part
 * provides the ability to display multiple users in a single tree viewer. This class may be subclassed as
 * desired to add or customize behavior.
 * @since 2.0
 */
public class UserSearchView extends ViewPart {

	public static final String VIEW_ID = "org.eclipse.ecf.presence.ui.UserSearchView"; //$NON-NLS-1$

	protected static final int DEFAULT_EXPAND_LEVEL = 3;

	protected TreeViewer treeViewer;

	protected List users = new ArrayList();

	private IAction addContactAction;

	private MultiRosterAccount account;

	private PageBook pageBook;

	private Label helpMessageLabel;

	private IContainerListener listener;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(Composite parent) {
		pageBook = new PageBook(parent, SWT.NONE);

		createHelpMessage(pageBook);
		setupTreeViewer(pageBook);

		if (users.size() == 0)
			pageBook.showPage(helpMessageLabel);
	}

	private void createHelpMessage(Composite parent) {
		if (!parent.isDisposed()) {
			helpMessageLabel = new Label(parent, SWT.TOP + SWT.LEFT + SWT.WRAP);
		}
	}

	protected void setupTreeViewer(Composite parent) {
		treeViewer = new TreeViewer(parent, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
		getSite().setSelectionProvider(treeViewer);
		treeViewer.setContentProvider(new UserSearchContentProvider());
		treeViewer.setLabelProvider(new UserSearchLabelProvider());
		makeActions();
		hookContextMenu();

	}

	private void makeActions() {

		addContactAction = new Action(Messages.MultiRosterView_AddContact, SharedImages.getImageDescriptor(SharedImages.IMG_ADD_BUDDY)) {
			public void run() {
				ITreeSelection selection = (ITreeSelection) treeViewer.getSelection();
				IResult contact = (IResult) selection.getFirstElement();
				if (MessageDialog.openConfirm(null, Messages.AddContactDialog_DialogTitle, NLS.bind(Messages.SearchContactDialog_AddContactMessage, contact.getUser().getName()))) {
					IPresenceContainerAdapter ipca = account.getPresenceContainerAdapter();
					IRosterSubscriptionSender sender = ipca.getRosterManager().getRosterSubscriptionSender();
					try {
						sender.sendRosterAdd(contact.getUser().getID().getName(), contact.getUser().getName(), null);
					} catch (ECFException e) {
						Activator.getDefault().getLog().log(e.getStatus());
					}
				}

			}
		};

	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager();
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(treeViewer.getControl());
		treeViewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, treeViewer);
	}

	private void fillContextMenu(IMenuManager manager) {
		IStructuredSelection iss = (IStructuredSelection) treeViewer.getSelection();
		Object element = iss.getFirstElement();
		if (element instanceof IResult) {
			manager.add(addContactAction);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#dispose()
	 */
	public void dispose() {
		treeViewer = null;
		users.clear();
		if (account != null)
			account.container.removeListener(listener);
		super.dispose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	public void setFocus() {
		if (treeViewer != null)
			treeViewer.getControl().setFocus();
	}

	/**
	 * Add the list of {@link IResult} that will be display on a tree
	 * @param list
	 * @return boolean
	 */
	public boolean addResult(IResultList list) {
		users.clear();
		users.add(list);
		treeViewer.setInput(users);
		pageBook.showPage(treeViewer.getControl());
		treeViewer.expandToLevel(DEFAULT_EXPAND_LEVEL);
		helpMessageLabel.setText("");
		return true;
	}

	/**
	 * Add the MultiRosterAccount for add user contact
	 * @param multiRosterAccount
	 * @return boolean
	 */
	public boolean addMultiRosterAccount(MultiRosterAccount multiRosterAccount) {
		Assert.isNotNull(multiRosterAccount);
		this.account = multiRosterAccount;

		listener = new IContainerListener() {
			public void handleEvent(IContainerEvent event) {
				if (event instanceof IContainerDisconnectedEvent) {
					users.clear();
					showMessage(Messages.MultiRosterView_HELP_MESSAGE);
				}
			}
		};
		account.container.addListener(listener);

		return true;
	}

	/**
	 * Show a message into the view before show the result in a tree view
	 * @param message
	 */
	public void showMessage(String message) {
		helpMessageLabel.setText(message);
		pageBook.showPage(helpMessageLabel);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3999.java