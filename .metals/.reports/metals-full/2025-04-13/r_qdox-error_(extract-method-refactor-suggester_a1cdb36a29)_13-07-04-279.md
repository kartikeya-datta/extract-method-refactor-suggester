error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12281.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12281.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12281.java
text:
```scala
A@@jde.getDefault().getEditorAdapter().showSourceLine(

/* *******************************************************************
 * Copyright (c) 1999-2001 Xerox Corporation, 
 *               2002 Palo Alto Research Center, Incorporated (PARC).
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     Xerox/PARC     initial implementation 
 * ******************************************************************/


package org.aspectj.ajde.ui.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import org.aspectj.ajde.Ajde;
import org.aspectj.ajde.ui.FileStructureView;
import org.aspectj.ajde.ui.StructureView;
import org.aspectj.ajde.ui.StructureViewNode;
import org.aspectj.ajde.ui.StructureViewRenderer;
import org.aspectj.asm.ProgramElementNode;
import org.aspectj.asm.StructureNode;

/**
 * Represents the configuration of a structure view of the system, rendered
 * by the <CODE>StructureTreeManager</CODE>.
 *
 * @author Mik Kersten
 */
public class StructureViewPanel extends JPanel implements StructureViewRenderer {

    protected StructureTreeManager treeManager = new StructureTreeManager();
    protected StructureView currentView = null;
	private java.util.List structureViews = null;

    protected Border border1;
    protected Border border2;
    JScrollPane tree_ScrollPane = new JScrollPane();
    JPanel structureToolBar_panel = null;
    BorderLayout borderLayout1 = new BorderLayout();

	public StructureViewPanel(FileStructureView structureView) {
    	currentView = structureView;
		initView(structureView);
		structureToolBar_panel = new SimpleStructureViewToolPanel(currentView);
		init();
	}

	public StructureViewPanel(java.util.List structureViews) {
		this.structureViews = structureViews;

		for (Iterator it = structureViews.iterator(); it.hasNext(); ) {
			initView((StructureView)it.next());
		}
		currentView = (StructureView)structureViews.get(0);
		structureToolBar_panel = new BrowserStructureViewToolPanel(structureViews, currentView, this);
		init();
	}
	
	private void init() {
		try {
			jbInit();
		} catch (Exception e) {
			Ajde.getDefault().getErrorHandler().handleError("Could not initialize view panel.", e);
		}
		updateView(currentView);
	}

	public void setCurrentView(StructureView view) {
		currentView = view;
		treeManager.updateTree(view);
	}

    public void updateView(StructureView structureView) {
    	if (structureView == currentView) {
	    	treeManager.updateTree(structureView); 
    	}  
    }

	private void initView(StructureView view) {
		view.setRenderer(this);
	}

 	public void setActiveNode(StructureViewNode node) {
 		setActiveNode(node, 0);
 	}

	public void setActiveNode(StructureViewNode node, int lineOffset) {
		if (node == null) return;
 		if (!(node.getStructureNode() instanceof ProgramElementNode)) return;
		ProgramElementNode pNode = (ProgramElementNode)node.getStructureNode();
 		treeManager.highlightNode(pNode);
 		if (pNode.getSourceLocation() != null) {
	 		Ajde.getDefault().getEditorManager().showSourceLine(
	 			pNode.getSourceLocation().getSourceFile().getAbsolutePath(),
	 			pNode.getSourceLocation().getLine() + lineOffset,
	 			true
	 		);
 		}
	}

 	public void highlightActiveNode() {
 		if (currentView.getActiveNode() == null) return;
 		StructureNode node = currentView.getActiveNode().getStructureNode();
 		if (node instanceof ProgramElementNode) {
 			treeManager.highlightNode((ProgramElementNode)node);
 		}
 	}

	protected void jbInit() {
        border1 = BorderFactory.createBevelBorder(BevelBorder.LOWERED,Color.white,Color.white,new Color(156, 156, 158),new Color(109, 109, 110));
        border2 = BorderFactory.createEmptyBorder(0,1,0,0);

        this.setLayout(borderLayout1);
        this.add(tree_ScrollPane, BorderLayout.CENTER);
        this.add(structureToolBar_panel, BorderLayout.NORTH);

        tree_ScrollPane.getViewport().add(treeManager.getStructureTree(), null);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12281.java