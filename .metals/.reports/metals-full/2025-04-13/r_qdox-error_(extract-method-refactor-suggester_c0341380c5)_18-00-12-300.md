error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1454.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1454.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,26]

error in qdox parser
file content:
```java
offset: 26
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1454.java
text:
```scala
transient private static L@@ogger log = Hierarchy.getDefaultHierarchy().getLoggerFor("jmeter.gui");

/*
 * ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the
 * distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 * if any, must include the following acknowledgment:
 * "This product includes software developed by the
 * Apache Software Foundation (http://www.apache.org/)."
 * Alternately, this acknowledgment may appear in the software itself,
 * if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" and
 * "Apache JMeter" must not be used to endorse or promote products
 * derived from this software without prior written permission. For
 * written permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 * "Apache JMeter", nor may "Apache" appear in their name, without
 * prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
package org.apache.jmeter.visualizers;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.samplers.Clearable;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.log.Hierarchy;
import org.apache.log.Logger;
/****************************************
 * Allows the tester to view the textual response from sampling an Entry. This
 * also allows to "single step through" the sampling process via a nice
 * "Continue" button.
 *
 *@author    Khor Soon Hin
 *@created   2001/08/30
 *@version   1.0
 ***************************************/
public abstract class TreeVisualizer extends JPanel
		 implements TreeSelectionListener, Clearable
{
	/****************************************
	 * !ToDo (Field description)
	 ***************************************/
	protected DefaultMutableTreeNode root;
	/****************************************
	 * !ToDo (Field description)
	 ***************************************/
	protected DefaultTreeModel treeModel;
	/****************************************
	 * !ToDo (Field description)
	 ***************************************/
	protected GridBagLayout gridBag;
	/****************************************
	 * !ToDo (Field description)
	 ***************************************/
	protected GridBagConstraints gbc;
	/****************************************
	 * !ToDo (Field description)
	 ***************************************/
	protected JPanel resultPanel;
	/****************************************
	 * !ToDo (Field description)
	 ***************************************/
	protected JScrollPane treePane;
	/****************************************
	 * !ToDo (Field description)
	 ***************************************/
	protected JScrollPane resultPane;
	/****************************************
	 * !ToDo (Field description)
	 ***************************************/
	protected JSplitPane treeSplitPane;
	/****************************************
	 * !ToDo (Field description)
	 ***************************************/
	protected JTree jTree;
	/****************************************
	 * !ToDo (Field description)
	 ***************************************/
	protected int childIndex;

	ResultCollector model;

	private static Logger log = Hierarchy.getDefaultHierarchy().getLoggerFor("jmeter.gui");

	//----- TreeSelectionListener interface : end -----

	/****************************************
	 * !ToDo (Constructor description)
	 ***************************************/
	public TreeVisualizer()
	{
		super();
		this.setLayout(new GridLayout(1, 1));
		log.debug("Start : TreeVisualizer1");
		log.debug("End : TreeVisualizer1");
	}

	//----- ModelSupported interface : start -----

	/****************************************
	 * Set the reporter type this visualizer will work with
	 *
	 *@param model  reporter instance
	 ***************************************/
	public void setModel(Object model)
	{
		log.debug("Start : setModel1");
		this.model = (ResultCollector)model;
		this.model.setListener(this);
		init();
		log.debug("End : setModel1");
	}

	/****************************************
	 * Update the visualizer with new data
	 ***************************************/
	public abstract void updateGui();

	//----- ModelSupported interface : end -----

	//----- TreeSelectionListener interface : start -----

	/****************************************
	 * Sets the bottom pane to correspond to the selected node of the top tree
	 *
	 *@param e  !ToDo (Parameter description)
	 ***************************************/
	public abstract void valueChanged(TreeSelectionEvent e);

	/****************************************
	 * Clears the visualizer
	 ***************************************/
	public void clear()
	{
		log.debug("Start : clear1");
		int totalChild = root.getChildCount();
		if(log.isDebugEnabled())
		{
			log.debug("clear1 : total child - " + totalChild);
		}
		for(int i = 0; i < totalChild; i++)
		{
			// the child to be removed will always be 0 'cos as the nodes are removed
			// the nth node will become (n-1)th
			treeModel.removeNodeFromParent(
					(DefaultMutableTreeNode)root.getChildAt(0));
		}
		resultPanel.removeAll();
		resultPanel.repaint();
		// reset the child index
		childIndex = 0;
		log.debug("End : clear1");
	}

	/****************************************
	 * Returns the description of this visualizer
	 *
	 *@return   description of this visualizer
	 ***************************************/
	public abstract String toString();

	/****************************************
	 * Initialize this visualizer
	 ***************************************/
	protected void init()
	{
		log.debug("Start : init1");
		SampleResult rootSampleResult = new SampleResult();
		rootSampleResult.setSampleLabel("Root");
		root = new DefaultMutableTreeNode(rootSampleResult);
		treeModel = new DefaultTreeModel(root);
		jTree = new JTree(treeModel);
		jTree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		jTree.addTreeSelectionListener(this);
		treePane = new JScrollPane(jTree);
		gridBag = new GridBagLayout();
		gbc = new GridBagConstraints();
		resultPanel = new JPanel(gridBag);
		resultPanel.setPreferredSize(new Dimension(2000, 800));
		resultPane = new JScrollPane(resultPanel);
		treeSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				treePane, resultPane);
		add(treeSplitPane);
		log.debug("End : init1");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1454.java