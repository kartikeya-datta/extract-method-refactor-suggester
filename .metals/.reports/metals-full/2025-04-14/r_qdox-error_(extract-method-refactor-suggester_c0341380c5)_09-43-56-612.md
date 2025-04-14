error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5570.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5570.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5570.java
text:
```scala
I@@ChatRoomInfo[] infos = managers[i].getChatRoomInfos();

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

package org.eclipse.ecf.ui.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ecf.presence.chatroom.IChatRoomInfo;
import org.eclipse.ecf.presence.chatroom.IChatRoomManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class ChatRoomSelectionDialog extends TitleAreaDialog {
	IChatRoomManager[] managers = null;

	private Room selectedRoom = null;

	public class Room {
		IChatRoomInfo info;

		IChatRoomManager manager;

		public Room(IChatRoomInfo info, IChatRoomManager man) {
			this.info = info;
			this.manager = man;
		}

		public IChatRoomInfo getRoomInfo() {
			return info;
		}

		public IChatRoomManager getManager() {
			return manager;
		}
	}

	public ChatRoomSelectionDialog(Shell parentShell,
			IChatRoomManager[] managers) {
		super(parentShell);
		this.managers = managers;
	}

	protected Control createDialogArea(Composite parent) {
		Composite main = new Composite(parent, SWT.NONE);
		main.setLayout(new GridLayout());
		main.setLayoutData(new GridData(GridData.FILL_BOTH));

		TableViewer viewer = new TableViewer(main, SWT.BORDER
 SWT.FULL_SELECTION);
		Table table = viewer.getTable();

		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		GridData tableGridData = new GridData(GridData.FILL_BOTH);
		tableGridData.grabExcessHorizontalSpace = true;

		table.setLayoutData(tableGridData);
		TableColumn tc = new TableColumn(table, SWT.NONE);
		tc.setText("Room Name");
		tc.pack();
		int width = tc.getWidth();
		tc.setWidth(width + (width / 4));
		tc = new TableColumn(table, SWT.NONE);
		tc.setText("Subject");
		tc.pack();
		width = tc.getWidth();
		tc.setWidth(width + (width / 4));
		tc = new TableColumn(table, SWT.NONE);
		tc.setText("Description");
		tc.pack();
		width = tc.getWidth();
		tc.setWidth(width + (width / 3));
		tc = new TableColumn(table, SWT.NONE);
		tc.setText("Members");
		tc.pack();
		tc = new TableColumn(table, SWT.NONE);
		tc.setText("Moderated");
		tc.pack();
		tc = new TableColumn(table, SWT.NONE);
		tc.setText("Persistent");
		tc.pack();
		tc = new TableColumn(table, SWT.NONE);
		tc.setText("Account");
		tc.pack();
		width = tc.getWidth();
		tc.setWidth(width * 3);

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				if (!event.getSelection().isEmpty()) {
					ChatRoomSelectionDialog.this.getButton(Window.OK)
							.setEnabled(true);
				}
			}

		});

		viewer.setContentProvider(new ChatRoomContentProvider());
		viewer.setLabelProvider(new ChatRoomLabelProvider());

		List all = new ArrayList();
		for (int i = 0; i < managers.length; i++) {
			IChatRoomInfo[] infos = managers[i].getChatRoomsInfo();
			if (infos != null) {
				for (int j = 0; j < infos.length; j++) {
					if (infos[j] != null && managers[i] != null) {
						all.add(new Room(infos[j], managers[i]));
					}
				}
			}
		}
		Room[] rooms = (Room[]) all.toArray(new Room[] {});
		viewer.setInput(rooms);

		this.setTitle("Chat Room Selection");
		this.setMessage("Select a Chat Room to Enter");

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				StructuredSelection s = (StructuredSelection) event
						.getSelection();
				if (s.getFirstElement() instanceof Room) {
					selectedRoom = (Room) s.getFirstElement();
				}
			}

		});

		viewer.addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(DoubleClickEvent event) {
				if (selectedRoom != null) {
					ChatRoomSelectionDialog.this.okPressed();
				}
			}

		});

		return parent;
	}

	private class ChatRoomContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {

			return (Room[]) inputElement;
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

	}

	private class ChatRoomLabelProvider implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			Room room = (Room) element;

			IChatRoomInfo info = room.getRoomInfo();
			switch (columnIndex) {
			case 0:
				return info.getName();
			case 1:
				return info.getSubject();
			case 2:
				return info.getDescription();
			case 3:
				return String.valueOf(info.getParticipantsCount());
			case 4:
				return String.valueOf(info.isModerated());
			case 5:
				return String.valueOf(info.isPersistent());
			case 6:
				return info.getConnectedID().getName();
			default:
				return "";

			}

		}

		public void addListener(ILabelProviderListener listener) {
		}

		public void dispose() {
		}

		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		public void removeListener(ILabelProviderListener listener) {
		}

	}

	protected Control createButtonBar(Composite parent) {

		Control bar = super.createButtonBar(parent);
		this.getButton(Window.OK).setText("Enter Chat");
		this.getButton(Window.OK).setEnabled(false);
		return bar;
	}

	public Room getSelectedRoom() {
		return selectedRoom;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5570.java