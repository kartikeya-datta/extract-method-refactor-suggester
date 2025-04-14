error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13192.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13192.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,14]

error in qdox parser
file content:
```java
offset: 14
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13192.java
text:
```scala
static final i@@nt DEFAULT_TREE_WIDGET_PERCENT = 15;

/*******************************************************************************
 * Copyright (c) 2004, 2007 Composent, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Composent, Inc. - initial API and implementation
 ******************************************************************************/

package org.eclipse.ecf.internal.example.collab.ui;

import java.util.List;

import org.eclipse.ecf.example.collab.share.User;
import org.eclipse.ecf.internal.example.collab.ClientPlugin;
import org.eclipse.ecf.ui.SharedImages;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;

class TeamChat extends Composite {
	ChatComposite chat = null;
	SashForm sash = null;
	TableViewer tableView = null;
	ToolBar bar;
	LineChatClientView view;
	ChatWindow chatWindow;

	static final int DEFAULT_TREE_WIDGET_PERCENT = 10;

	TeamChat(LineChatClientView view, Composite parent, int options,
			String initText) {
		super(parent, options);

		this.view = view;
		setLayout(new FillLayout());
		boolean useChatWindow = ClientPlugin.getDefault()
				.getPluginPreferences().getBoolean(
						ClientPlugin.PREF_USE_CHAT_WINDOW);
		int[] w = null;
		if (!useChatWindow) {
			sash = new SashForm(this, SWT.NORMAL);
			sash.setLayout(new FillLayout());
			sash.setOrientation(SWT.HORIZONTAL);
			w = new int[2];
			w[0] = DEFAULT_TREE_WIDGET_PERCENT;
			w[1] = 100 - w[0];
		}

		tableView = new TableViewer(useChatWindow ? (Composite) this
				: (Composite) sash, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL
 SWT.BORDER);
		tableView.setContentProvider(new ViewContentProvider());
		tableView.setLabelProvider(new ViewLabelProvider());

		if (useChatWindow) {
			chatWindow = new ChatWindow(view, tableView, initText);
			chatWindow.create();
			chat = chatWindow.getChat();
		} else {
			chat = new ChatComposite(view, sash, tableView, initText);
			sash.setWeights(w);
		}
	}

	void appendText(ChatLine text) {
		if (chatWindow != null && chatWindow.getShell() != null
				&& !chatWindow.getShell().isDisposed()
				&& !chatWindow.hasFocus()) {

			if (chatWindow.getShell().isVisible())
				chatWindow.flash();
			else
				chatWindow.open();
		}

		chat.appendText(text);
		setStatus(null);
	}

	void setStatus(String status) {
		if (chatWindow != null)
			chatWindow.setStatus(status);
	}

	void clearInput() {
		chat.clearInput();
	}

	TableViewer getTableViewer() {
		return tableView;
	}

	Control getTreeControl() {
		return tableView.getControl();
	}

	Control getTextControl() {
		return chat.getTextControl();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Widget#dispose()
	 */
	public void dispose() {
		if (chatWindow != null) {
			chatWindow.close();
			chatWindow = null;
		}

		super.dispose();
	}

	private class ViewContentProvider implements IStructuredContentProvider {

		public void dispose() {
		}

		public Object[] getElements(Object parent) {
			return ((List) parent).toArray();
		}

		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
	}

	private class ViewLabelProvider extends LabelProvider {
		public Image getImage(Object obj) {
			return obj instanceof User ? SharedImages
					.getImage(SharedImages.IMG_USER_AVAILABLE) : null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13192.java