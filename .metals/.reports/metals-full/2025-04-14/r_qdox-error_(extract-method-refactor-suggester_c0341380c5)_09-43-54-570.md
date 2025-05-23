error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2552.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2552.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2552.java
text:
```scala
private v@@oid add(IFileTransfer transfer) {

/****************************************************************************
 * Copyright (c) 2007 Remy Suen and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Remy Suen <remy.suen@gmail.com> - initial API and implementation
 *****************************************************************************/
package org.eclipse.ecf.filetransfer.ui;

import java.util.Iterator;
import java.util.Vector;

import org.eclipse.ecf.filetransfer.IFileTransfer;
import org.eclipse.ecf.filetransfer.IFileTransferPausable;
import org.eclipse.ecf.filetransfer.IIncomingFileTransfer;
import org.eclipse.ecf.filetransfer.IOutgoingFileTransfer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

public class FileTransfersView extends ViewPart {

	public static final Vector transfers = new Vector();

	public static final String ID = "org.eclipse.ecf.filetransfer.ui.FileTransfersView"; //$NON-NLS-1$

	private static final String[] COLUMNS = { "Name", "Downloaded", "Uploaded",
			"Done" };

	private static final int[] WIDTHS = { 250, 100, 100, 40 };

	private static final Object[] EMPTY_ARRAY = new Object[0];

	private static final double GIGABYTE = Math.pow(2, 30);

	private static final double MEGABYTE = Math.pow(2, 20);

	private static final double KILOBYTE = Math.pow(2, 10);

	private static final int NAME = 0;

	private static final int DOWNLOADED = NAME + 1;

	private static final int UPLOADED = DOWNLOADED + 1;

	private static final int DONE = UPLOADED + 1;

	public static void addTransfer(IFileTransfer transfer) {
		transfers.add(transfer);
		if (instance != null) {
			instance.add(transfer);
		}
	}

	private TableViewer viewer;

	private Table table;

	private Action resumeAction;

	private Action pauseAction;

	private Action removeAction;

	private static String getTwoDigitNumber(long value) {
		if (value > GIGABYTE) {
			double num = value / GIGABYTE;
			return Double.toString(Math.floor(num * 100) / 100) + " GB";
		} else if (value > MEGABYTE) {
			double num = value / MEGABYTE;
			return Double.toString(Math.floor(num * 100) / 100) + " MB";
		} else if (value > KILOBYTE) {
			double num = value / KILOBYTE;
			return Double.toString(Math.floor(num * 100) / 100) + " KB";
		}
		return value + " bytes";
	}

	private static FileTransfersView instance;

	public FileTransfersView() {
		instance = this;
	}

	public void dispose() {
		instance = null;
		super.dispose();
	}

	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
 SWT.V_SCROLL | SWT.VIRTUAL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new ViewerSorter());
		viewer.setInput(getViewSite());
		table = viewer.getTable();

		for (int i = 0; i < WIDTHS.length; i++) {
			TableColumn col = new TableColumn(table, SWT.LEFT);
			col.setText(COLUMNS[i]);
			col.setWidth(WIDTHS[i]);
		}

		Iterator iterator = transfers.iterator();
		while (iterator.hasNext()) {
			add((IFileTransfer) iterator.next());
		}

		makeActions();
		hookContextMenu();

		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.setSize(1000, 1000);
	}

	public void add(IFileTransfer transfer) {
		if (table != null && !table.isDisposed()) {
			viewer.add(transfer);
		}
	}

	public void update(IFileTransfer transfer) {
		if (table != null && !table.isDisposed()) {
			viewer.update(transfer, COLUMNS);
		}
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager();
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				fillContextMenu(manager);
				enableActions();
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		table.setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(resumeAction);
		manager.add(pauseAction);
		manager.add(removeAction);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void makeActions() {
		resumeAction = new Action() {
			public void run() {
				ISelection sel = viewer.getSelection();
				if (!(sel instanceof IStructuredSelection)) {
					return;
				}
				IStructuredSelection ssel = (IStructuredSelection) sel;
				Iterator iterator = ssel.iterator();
				while (iterator.hasNext()) {
					IFileTransfer transfer = (IFileTransfer) iterator.next();
					IFileTransferPausable pausable = (IFileTransferPausable) transfer
							.getAdapter(IFileTransferPausable.class);
					if (pausable != null) {
						pausable.resume();
					}
				}
			}
		};
		resumeAction.setText("&Resume");

		pauseAction = new Action() {
			public void run() {
				ISelection sel = viewer.getSelection();
				if (!(sel instanceof IStructuredSelection)) {
					return;
				}
				IStructuredSelection ssel = (IStructuredSelection) sel;
				Iterator iterator = ssel.iterator();
				while (iterator.hasNext()) {
					IFileTransfer transfer = (IFileTransfer) iterator.next();
					IFileTransferPausable pausable = (IFileTransferPausable) transfer
							.getAdapter(IFileTransferPausable.class);
					if (pausable != null) {
						pausable.pause();
					}
				}
			}
		};
		pauseAction.setText("&Pause");

		removeAction = new Action() {
			public void run() {
				ISelection sel = viewer.getSelection();
				if (!(sel instanceof IStructuredSelection)) {
					return;
				}
				IStructuredSelection ssel = (IStructuredSelection) sel;
				Iterator iterator = ssel.iterator();
				while (iterator.hasNext()) {
					IFileTransfer transfer = (IFileTransfer) iterator.next();
					transfer.cancel();
					viewer.remove(transfer);
				}
			}
		};
		removeAction.setText("&Remove");
		removeAction.setImageDescriptor(PlatformUI.getWorkbench()
				.getSharedImages().getImageDescriptor(
						ISharedImages.IMG_TOOL_DELETE));
	}

	private void enableActions() {
		ISelection is = viewer.getSelection();
		resumeAction.setEnabled(false);
		pauseAction.setEnabled(false);
		if (is instanceof IStructuredSelection) {
			IStructuredSelection iss = (IStructuredSelection) is;
			removeAction.setEnabled(!iss.isEmpty());
			Iterator iterator = iss.iterator();
			while (iterator.hasNext()) {
				IFileTransfer transfer = (IFileTransfer) iterator.next();
				IFileTransferPausable pausable = (IFileTransferPausable) transfer
						.getAdapter(IFileTransferPausable.class);
				if (pausable != null) {
					resumeAction.setEnabled(true);
					pauseAction.setEnabled(true);
					return;
				}
			}
		}
	}

	public void setFocus() {
		table.setFocus();
	}

	private class ViewLabelProvider extends LabelProvider implements
			ITableLabelProvider {

		public String getColumnText(Object obj, int index) {
			if (!(obj instanceof IFileTransfer)) {
				return getText(obj);
			}
			IFileTransfer transfer = (IFileTransfer) obj;
			switch (index) {
			case NAME:
				return transfer.getID().getName();
			case DOWNLOADED:
				if (transfer instanceof IIncomingFileTransfer) {
					return getTwoDigitNumber(((IIncomingFileTransfer) transfer)
							.getBytesReceived());
				} else {
					return "N/A"; //$NON-NLS-1$
				}
			case UPLOADED:
				if (transfer instanceof IOutgoingFileTransfer) {
					return getTwoDigitNumber(((IOutgoingFileTransfer) transfer)
							.getBytesSent());
				} else {
					return "N/A"; //$NON-NLS-1$
				}
			case DONE:
				return Double.toString(transfer.getPercentComplete()) + '%';
			}
			return getText(obj);
		}

		public Image getColumnImage(Object obj, int index) {
			return null;
		}
	}

	private class ViewContentProvider implements IStructuredContentProvider {

		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}

		public Object[] getElements(Object inputElement) {
			return EMPTY_ARRAY;
		}
	}
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2552.java