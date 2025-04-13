error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7481.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7481.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7481.java
text:
```scala
v@@oid appendText(ChatLine text) {

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

package org.eclipse.ecf.example.collab.ui;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ecf.example.collab.ClientPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;

class TeamChat extends Composite {
	ChatComposite chat = null;
	SashForm sash = null;
	ChatTreeViewer treeView = null;
	ViewContentProvider vc;
	ToolBar bar;
	LineChatClientView view;
	ChatWindow chatWindow;

    static final int DEFAULT_TREE_WIDGET_PERCENT = 40;

	TeamChat(LineChatClientView view,Composite parent, int options, String initText) {
		super(parent, options);

		this.view = view;
		setLayout(new FillLayout());
		boolean useChatWindow =
			ClientPlugin.getDefault().getPluginPreferences().getBoolean(ClientPlugin.PREF_USE_CHAT_WINDOW);
		int[] w = null;
		if (!useChatWindow) {
			sash = new SashForm(this, SWT.NORMAL);
			sash.setLayout(new FillLayout());
			sash.setOrientation(SWT.HORIZONTAL);
			w = new int[2];
			w[0] = DEFAULT_TREE_WIDGET_PERCENT;
			w[1] = 100 - w[0];
		}

		treeView = 
			new ChatTreeViewer(
				useChatWindow ? (Composite) this : (Composite) sash, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		treeView.setAutoExpandLevel(LineChatClientView.TREE_EXPANSION_LEVELS);
		vc = new ViewContentProvider(view);
		
		treeView.setContentProvider(vc);
		treeView.setLabelProvider(new ViewLabelProvider());
		treeView.setInput(ResourcesPlugin.getWorkspace());
		

		if (useChatWindow) {
			chatWindow = new ChatWindow(view, this, treeView, initText);
			chatWindow.create();
			chat = chatWindow.getChat();
		} else {
			chat = new ChatComposite(view, sash, treeView, SWT.NORMAL, initText);
			sash.setWeights(w);
		}
	}

	void appendText(String text) {
		if (chatWindow != null 
				&& chatWindow.getShell() != null 
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

	void enableProxyMessage(boolean val) {
		chat.enableProxyMessage(val);
	}

	ChatTreeViewer getTree() {
		return treeView;
	}

	Control getTreeControl() {
		return treeView.getControl();
	}

	Control getTextControl() {
		return chat.getTextControl();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Widget#dispose()
	 */
	public void dispose() {
		if (chatWindow != null) {
			chatWindow.close();
			chatWindow = null;
		}
		
		super.dispose();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7481.java